
package main.scala

import org.apache.spark.sql.functions._
import com.databricks.spark.corenlp.functions._
import org.apache.spark.sql.SparkSession

object SparkCoreNLP {
  def main(args: Array[String]): Unit = {

    //Define Spark Session
    val spark=SparkSession.builder()
      .appName("Spark with CoreNLP Example")
      .config("spark.master","local")
      .getOrCreate()

    //Implicit methods available in Scala for converting common Scala objects into DataFrames
    import spark.implicits._

    //Create a DataFrame
    val input = Seq(
      (1, "<xml>This is an example of running CoreNLP with Apache Spark. " +
        "CoreNLP is developed by Standford university, one of the best university in the world.</xml>")
    ).toDF("id", "text")

    //Remove xml tag
    val cleanedText=input.select(cleanxml('text).as('doc))

    //Split into sentence
    val sents=cleanedText.select(explode(ssplit('doc)).as('sentences))
    sents.show(false)

    //Split sentence into words
    val words=sents.select('sentences, tokenize('sentences).as('words))
    words.show(false)

    //Get lemma
    val lem=sents.select('sentences, lemma('sentences).as('lemmas))
    lem.show(false)

    //Get Part of speech
    val pos1=sents.select('sentences, pos('sentences).as('posTags))
    pos1.show(false)

    //Get Name Entity
    val ner1=sents.select('sentences,ner('sentences).as('nerTags))
    ner1.show(false)

    //Get sentiment results
    val sen1=sents.select('sentences,sentiment('sentences).as('sentiment))
    sen1.show(false)
  }

}
