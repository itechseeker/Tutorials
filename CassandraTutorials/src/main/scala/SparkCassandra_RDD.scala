import org.apache.spark.{SparkConf, SparkContext}
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector


object SparkCassandra_RDD {
  def main(args: Array[String]): Unit = {

    //Define Spark Configuration
    val sparkConf = new SparkConf()
      .setAppName("Spark Cassandra Connection Example")
      .setMaster("local[*]")
      .set("spark.cassandra.connection.host", "localhost")

    //Create Spark Context
    val sparkContext = new SparkContext(sparkConf)

    //Set the Log file level
    sparkContext.setLogLevel("WARN")

    //Connect Spark to Cassandra and execute CQL statements from Spark applications
    val connector = CassandraConnector(sparkContext.getConf)
    connector.withSessionDo(session =>
    {
      session.execute("DROP KEYSPACE IF EXISTS testkeyspace")
      session.execute("CREATE KEYSPACE testkeyspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':3}")
      session.execute("USE testkeyspace")
      session.execute("CREATE TABLE emp(emp_id int PRIMARY KEY,emp_name text,emp_city text,emp_sal varint,emp_phone varint)")
      session.execute("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal) VALUES(1,'John', 'London', 0786022338, 65000);")
      session.execute("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal) VALUES(2,'David', 'Hanoi', 0986022576, 40000);")
      session.execute("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal) VALUES(3,'John Cass', 'Scotland', 0786022342, 75000);")
      session.execute("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal) VALUES(4,'Bob Cass', 'Bristol', 0786022258, 80950);")
    }
    )

    //Display all row of the emp table
    var table_rdd = sparkContext.cassandraTable("testkeyspace", "emp")
    println("All row in the emp table:")
    table_rdd.foreach(println)

    //Use Selection and Filtering to reduce the amount of data transferred from Cassandra
    //to Spark to speed up processing
    val table2_rdd = sparkContext.cassandraTable("testkeyspace", "emp").select("emp_name","emp_city").where("emp_sal > ?",50000)
    println("\nHigh salary employees: ")
    table2_rdd.foreach(println)

  }

}
