import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.sql.cassandra._

object SparkCassandraTest {
  def main(args: Array[String]): Unit = {

    //Create a Spark session which connect to Cassandra
    val spark = org.apache.spark.sql.SparkSession
      .builder()
      .master("local[*]")
      .config("spark.cassandra.connection.host", "localhost")
      .appName("Spark Cassandra Connector Example")
      .getOrCreate()

    //Implicit methods available in Scala for converting common Scala objects into DataFrames
    import spark.implicits._

    //Get Spark Context from Spark session
    val sparkContext = spark.sparkContext

    //Set the Log file level
    sparkContext.setLogLevel("WARN")


    //Read Cassandra data using DataFrame
    val df = spark.read
                  .format("org.apache.spark.sql.cassandra")
                  .options(Map( "table" -> "master_dataset", "keyspace" -> "lambda_architecture"))
                  .load()

    //Display all row of the  table
    println("Details of the table: ")
    df.show()

  }
}

