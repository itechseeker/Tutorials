package main.scala

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.from_json

object KafkaSparkSS {
  def main(args: Array[String]): Unit = {
    //Define a Spark session
    val spark=SparkSession.builder().appName("Spark Kafka Integration using Structured Streaming")
                                    .master("local")
                                    .getOrCreate()

    //Set the Log file level
    spark.sparkContext.setLogLevel("WARN")

    //Implicit methods available in Scala for converting common Scala objects into DataFrames
    import spark.implicits._

    //Subscribe Spark to topic 'TwitterStreaming'
    val df=spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers","localhost:9092")
      .option("subscribe","TwitterStreaming")
      .load()

    //Extract the schema from a sample of Twitter Data
    val twitterData=spark.read.json("src/main/resources/data_source/twitter.json").toDF()
    val twitterDataScheme=twitterData.schema


    //Reading the streaming json data with its schema
    val twitterStreamData=df.selectExpr( "CAST(value AS STRING) as jsonData")
      .select(from_json($"jsonData",schema = twitterDataScheme).as("data"))
      .select("data.*")

    // Display output (all columns)
    val query = twitterStreamData
      .writeStream
      .outputMode("append")
      .format("console")
      .start()

    // Display output (only few columns)
    val query2 = twitterStreamData.select("created_at","user.name","text","user.lang")
      .writeStream
      .outputMode("append")
      .format("console")
      .start()

    query.awaitTermination()
    query2.awaitTermination()
  }

}
