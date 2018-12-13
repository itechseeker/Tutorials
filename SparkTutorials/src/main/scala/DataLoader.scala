package main.scala

import org.apache.spark.sql.{SaveMode, SparkSession}

object DataLoader {

  def main(args: Array[String]): Unit = {

    //Define a Spark Session
    val spark=SparkSession.builder().appName("Loading Data From Various Sources")
      .master("local")
      .getOrCreate()

    //Set log level
    spark.sparkContext.setLogLevel("WARN")

    //Read default data source (parquet file)
    val usersDF=spark.read.load(getFilePath("users.parquet"))
    println("Read default data source (users.parquet file):")
    usersDF.show()

    //Read data source using .format()
    val employeeDF=spark.read.format("json").load(getFilePath("employees.json"))
    println("Read data source using .format() (employees.json file): ")
    employeeDF.show()

    //Read data source using short name
    val employeeDF1=spark.read.json(getFilePath("employees.json"))
    println("Read data source using short name (employees.json file): ")
    employeeDF1.show()

    //Read the original .csv file
    val people_csvDF=spark.read.format("csv").load(getFilePath("people.csv"))
    println("Read the original .csv file (people.csv): ")
    people_csvDF.show()

    //Using .option() to reorganise .csv data
    val people_csvDF1=spark.read.format("csv")
      .option("sep", ";")
      .option("inferSchema", "true")
      .option("header", "true")
      .load(getFilePath("people.csv"))
    println("Using .option() to reorganise .csv data:")
    people_csvDF1.show()

    //Save DataFrame to a specified format
    people_csvDF1.select("name", "job").write.mode(SaveMode.ErrorIfExists).json(getFilePath("people_csvDF1"))

  }

  val dataSource="src/main/resources/data_source/"
  def getFilePath(fileName: String):String={
    return dataSource+fileName
  }

}
