package main.scala

/* Apache Spark Test*/
import org.apache.spark.sql.SparkSession

object test {
  def main(args: Array[String]) {

    //Define Spark Session
    val spark = SparkSession
      .builder()
      .appName("Spark Test")
      .config("spark.master", "local")
      .getOrCreate();

    //Set the Log file level
    spark.sparkContext.setLogLevel("WARN")

    // Specific the path of the file
    val textFile = "src/main/resources/README.md"

    //Make a new Dataset from the text of README.md
    val textData = spark.read.textFile(textFile)
    //Print some result
    println("Number of items in the Dataset: ",textData.count())}
}