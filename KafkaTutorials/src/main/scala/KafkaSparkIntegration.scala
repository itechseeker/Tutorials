package main.scala


import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils


object KafkaSparkIntegration {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("KafkaWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    ssc.checkpoint("checkpoint")


    val zkQuorum= "localhost:9092"
    val group=""
    val topicMap="TwitterData"

    val dStreams=KafkaUtils.createStream(ssc, "localhost:9092", "consumerGroup", Set("TwitterData"))

    val lines = dStreams.map(_._2)
    val words = lines.flatMap(_.split(" "))

    lines.print()

    ssc.start()
    ssc.awaitTermination()

  }

}
