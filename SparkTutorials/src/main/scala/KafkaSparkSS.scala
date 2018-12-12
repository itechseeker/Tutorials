package main.scala

import org.apache.spark.sql.SparkSession

object KafkaSparkSS {
  def main(args: Array[String]): Unit = {
    //Define a Spark session
    val spark=SparkSession.builder().appName("Spark Kafka Intergration using Strutured Streaming")
                                    .master("local")
                                    .getOrCreate()

    //Set the Log file level
    spark.sparkContext.setLogLevel("WARN")

    //Implicit methods available in Scala for converting common Scala objects into DataFrames
    import spark.implicits._

    //Subscribe Spark to topic 'test'
    val df=spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers","localhost:9092")
      .option("subscribe","test")
      .load()

    //Convert received data value from ASCII to String
    val lines=df.selectExpr( "CAST(value AS STRING)")

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
