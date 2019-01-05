import main.scala.DFProcessing
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object TestingObject {
  //Define Spark Session
  val spark=SparkSession.builder()
    .appName("Spark DataFrame Example")
    .master("local")
    .getOrCreate()
  //Implicit methods available in Scala for converting common Scala objects into DataFrames
  import spark.implicits._

  //Set the Log file level
  spark.sparkContext.setLogLevel("WARN")

  def main(args: Array[String]): Unit = {
   /* //The file path
    val file="E:/Workspace/BigData/Data/train.csv"

    //Create a Data Frame
    var df=spark.read.format("csv")
      .option("sep", ",")
      .option("inferSchema", "true")
      .option("header", "true")
      .load(file)

    df.select("ps_car_06_cat").show()
    println(df.agg(countDistinct("ps_car_06_cat")).first().get(0))
    println(df.select("ps_car_06_cat").distinct().count())*/
    /*val start_time=System.currentTimeMillis()
    DFProcessing.main(args)
    val end_time=System.currentTimeMillis()
    println("Running time: "+(end_time-start_time)/1000)*/

    val twitter=spark.read.json("src/main/resources/data_source/twitter.json").toDF()
    twitter.printSchema()
    println(twitter.show())

    val user=twitter.select("user.name");
    user.registerTempTable("User")
    user.printSchema()
    user.show()
    //println(user.select("name").show())

  }

}
