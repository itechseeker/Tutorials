package main.scala

import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession
object WordCount {
  def main(args: Array[String]): Unit = {
    // Create a Spark session
    val spark = SparkSession
      .builder()
      .appName("Word Count Example")
      .config("spark.master", "local")
      .getOrCreate();
    //Implicit methods available in Scala for converting common Scala objects into DataFrames
    import spark.implicits._
    //Set the Log file level
    spark.sparkContext.setLogLevel("WARN")
    // Create DataFrame from input str
    val lines = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()
    //Convert DataFrame to DataSet
    val linesDataset=lines.as[String]
    // Split the linesDataset into words
    val words = linesDataset.flatMap(_.split(" "))
    // Count each word
    val wordCounts = words.groupBy("value").count()
    // Display output
    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()
    query.awaitTermination()
  }
}