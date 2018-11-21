package main.scala
import org.apache.spark.sql.SparkSession

object SparkSql {
  def main(args: Array[String]): Unit = {

    //Define Spark Session
    val spark=SparkSession.builder()
      .appName("Spark SQL Example")
      .config("spark.master","local")
      .getOrCreate()

    //Implicit methods available in Scala for converting common Scala objects into DataFrames
    import spark.implicits._

    //Set the Log file level
    spark.sparkContext.setLogLevel("WARN")

    //The file path
    val file="src/main/resources/people.json"

    //Create a Data Frame
    val df=spark.read.json(file)

    // Displays the content of the DataFrame
    println("The content of the DataFrame:")
    df.show()

    // Select only the "name" column
    println("The column Name:")
    df.select("name").show()

    // Select everybody, but increment the age by 1
    println("Increasing age by 1:")
    df.select($"name", $"age" + 1).show()

    //Find people older than 20
    println("People older than 20:")
    df.filter($"age" >20).show()

    // Register the DataFrame as a SQL temporary view
    df.createOrReplaceTempView("people")
    //Run SQL query
    val sqlDF=spark.sql("Select * from people")
    println("Running SQL queries using Spark ")
    sqlDF.show()
  }
}