package main.scala

import java.util

import org.apache.spark.sql.types.{BooleanType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions._

object DFProcessing {

  //Define Spark Session
  val spark=SparkSession.builder()
    .appName("Spark DataFrame Example")
    .master("local[*]")
    .getOrCreate()
  //Implicit methods available in Scala for converting common Scala objects into DataFrames
  import spark.implicits._

  //Set the Log file level
  spark.sparkContext.setLogLevel("WARN")

  def main(args: Array[String]): Unit = {

    //The file path
    val file="E:/Workspace/BigData/Data/train.csv"

    //Create a Data Frame
    var df=spark.read.format("csv")
      .option("sep", ",")
      .option("inferSchema", "true")
      .option("header", "true")
      .load(file)

    //Print the fist five rows
    println("The first five rows: ")
    df.show(5)

    //Get the number of row and columns
    print("The number of row and columns: ")
    println(df.count()+","+df.columns.length)

    //Check if there are duplicate rows
    df=df.dropDuplicates();
    val totalRow=df.count()
    print("The number of row and columns after removing duplicate rows: ")
    println(totalRow+","+df.columns.length)

    println("\nThe type of each column variable")
    df.printSchema()

    //Initialize the value of role, level, keep and dtype
    var role = ""
    var level=""
    var keep=true
    var dtype=""
    val data=new util.ArrayList[Row]()
    for (col <- df.columns)
    {
      //Define the role
      if(col.contains("target"))
        role="target"
      else if(col.contains("id"))
        role="id"
      else
        role="input"

      //Define the level
      dtype=df.select(col).dtypes(0)._2
      if(col.contains("bin")|| col.equals("target"))
        level="binary"
      else if(col.contains("cat")||col.equals("id"))
        level="nominal"
      else if(dtype.equals("DoubleType"))
        level="interval"
      else if(dtype.equals("IntegerType"))
        level="ordinal"

      //Set True to all variables except id
      keep=true
      if(col.equals("id"))
        keep=false

      //Add Row to the Arraylist
      data.add(Row(col,role,level,keep,dtype))
    }

    //Define a DataFrame Schema
    val schema = StructType(
      List(
        StructField("varname", StringType, true),
        StructField("role", StringType, true),
        StructField("level", StringType, true),
        StructField("keep", BooleanType, true),
        StructField("dtype", StringType, true)
      )
    )

    //Create meta DataFrame
    val meta = spark.createDataFrame(data,schema )

    //Show the value of meta DataFrame
    println("The metadata of the dataset")
    meta.show(df.columns.length)

    //Extract all nominal variables that are not dropped
    println("All nominal variables that are not dropped: ")
    meta.filter($"level"==="nominal" && $"keep").select("varname").show()

    //Count the number of variables per role and level
    println("The number of variables per role and level: ")
    meta.groupBy("role","level").count().show()

    //Interval variables
    getVarDF("interval",meta,df)
    //Ordinal variables
    getVarDF("ordinal",meta,df)
    //Binary variables
    getVarDF("binary",meta,df)

   //Checking missing value
    var vars_with_missing=new util.ArrayList[String]()
    var missing=0.0
    for (column <- df.columns)
    {
      missing=df.filter(col(column) === -1).count()
      if(missing>0)
        {
          println(column+" has "+missing+"/"+totalRow+" record with missing values")
          vars_with_missing.add(column)
        }
    }
    println("Totally, there are "+vars_with_missing.size()+" variables with missing values")

    //Checking the cardinality of the categorical variables
    //Get the list of categorical Variables
    val catDF = getVarDF("nominal",meta,df,false)
    println("\nDistinct values of each categorical variable")
    for (column <- catDF.columns)
    {
      println(column+" has "+df.select(column).distinct().count()+" distinct values")
      //We can also use the following code to get the distinct value
      //println(df.agg(countDistinct(column)).first().get(0))
    }
  }
  def getVarDF(varName : String, metaDF: DataFrame, dataFrame: DataFrame, describe:Boolean =true) : DataFrame ={
    //Get the list of Variables
    val varCols = metaDF.filter($"level" === varName && $"keep").select("varname").map(r => r.getString(0)).collect.toList

    //Convert List of String to List of Column
    val colNames = varCols.map(name => col(name))

    // Create a new DataFrame with a specified columns from the original data frame
    val varDF = dataFrame.select(colNames: _*)

    //Print the descripion of the DataFrame if the boolean value is true
    if(describe==true)
      {
        println(varName+" variables: ")
        varDF.describe().show()
      }
    return varDF
  }
}
