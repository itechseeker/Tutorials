import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.Arrays;

public class KafkaSparkUsingStructuredStreaming {
    public static void main(String[] args) throws StreamingQueryException {
        //Set log level to warn
        Logger.getLogger("org").setLevel(Level.OFF);

        // Define a Spark Session
        SparkSession spark = SparkSession
                .builder()
                .appName("Spark Kafka Integration using Structured Streaming")
                .master("local")
                .getOrCreate();

        //Subscribe to topic 'test'
        // Different with Scala API, we need to use Dataset<Row> to represent a DataFrame in Java API
        Dataset< Row > df = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "test")
                .load();

        // Start running the query that prints the data getting from Kafka 'test' topic
        StreamingQuery query = df.writeStream()
                .outputMode("append")
                .format("console")
                .start();

        //Getting the data value as String
        Dataset lines=df.selectExpr("CAST(value AS STRING)");

        // Split the lines into words
        Dataset<String> words = lines.as(Encoders.STRING())
                                .flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());

        // Count the occurrence of each word
        Dataset<Row> wordCounts = words.groupBy("value").count();

        // Start running the query that prints the word count
        StreamingQuery query1 = wordCounts.writeStream()
                .outputMode("complete")
                .format("console")
                .start();

        //Await the termination of each query
        query.awaitTermination();
        query1.awaitTermination();

    }

}
