import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;


import java.util.*;


import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class KafkaSpark {

    static Map<String, Object> kafkaParams = new HashMap<String, Object>();


    public static void main(String[] args) throws InterruptedException, StreamingQueryException {

        //Set log level to warn
        Logger.getLogger("org").setLevel(Level.OFF);

        // Create a local StreamingContext with two working thread and batch interval of 1 second
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Sampleapp");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(10));

        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "0");
        kafkaParams.put("auto.offset.reset", "earliest"); // get message from begining
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("test");

        final JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        jssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
                );

        /*stream.mapToPair(
                new PairFunction<ConsumerRecord<String, String>, String, String>() {
                    public Tuple2<String, String> call(ConsumerRecord<String, String> record) throws Exception {
                        System.out.println("record key : "+record.key()+" value is : "+record.value());
                        return new Tuple2<String, String>(record.key(), record.value());
                    }
                });*/

        /*

        JavaPairDStream<String, String> jPairDStream =  stream.mapToPair(
                (PairFunction<ConsumerRecord<String, String>, String, String>) record -> new Tuple2<>(record.key(), record.value()));

        jPairDStream.foreachRDD(jPairRDD -> {
            jPairRDD.foreach(rdd -> { System.out.println("key="+rdd._1()+" value="+rdd._2()); });
        });



        // Read value of each message from Kafka and return it
        JavaDStream<String> lines = stream.map(new Function<ConsumerRecord<String,String>, String>() {
            @Override
            public String call(ConsumerRecord<String, String> kafkaRecord) throws Exception {
                return kafkaRecord.value();
            }
        });

         // Break every message into words and return list of words
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" ")).iterator();
            }
        });

        // Take every word and return Tuple with (word,1)
        JavaPairDStream<String,Integer> wordMap = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<>(word,1);
            }
        });

        // Count occurance of each word
        JavaPairDStream<String,Integer> wordCount = wordMap.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer first, Integer second) throws Exception {
                return first+second;
            }
        });

        */

        // Read value of each message from Kafka and return it
        JavaDStream<String> lines = stream.map((Function<ConsumerRecord<String, String>, String>) kafkaRecord -> kafkaRecord.value());

        // Break every message into words and return list of words
        JavaDStream<String> words = lines.flatMap((FlatMapFunction<String, String>) line -> Arrays.asList(line.split(" ")).iterator());

        // Take every word and return Tuple with (word,1)
        JavaPairDStream<String,Integer> wordMap = words.mapToPair((PairFunction<String, String, Integer>) word -> new Tuple2<>(word,1));

        // Count occurance of each word
        JavaPairDStream<String,Integer> wordCount = wordMap.reduceByKey((Function2<Integer, Integer, Integer>) (first, second) -> first+second);

        //Print the word count
        wordCount.print();


        jssc.start();// Start the computation
        jssc.awaitTermination();
    }

}
