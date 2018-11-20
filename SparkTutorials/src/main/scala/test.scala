/* Apache Spark Test*/
import org.apache.spark.sql.SparkSession

object test {
  def main(args: Array[String]) {
    // Specific the path of the file
    val textFile = "src/main/resources/README.md"
    // Create a Spark session
    val spark = SparkSession
      .builder()
      .appName("Spark Test")
      .config("spark.master", "local")
      .getOrCreate();

    spark.sparkContext.setLogLevel("WARN")

    //Make a new Dataset from the text of README.md
    val textData = spark.read.textFile(textFile)
    println("Number of items in the Dataset: ",textData.count())}
}