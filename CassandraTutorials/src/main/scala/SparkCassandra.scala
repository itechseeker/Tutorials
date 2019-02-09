import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.sql.cassandra._

object SparkCassandra {
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

    //Connect Spark to Cassandra and execute CQL statements from Spark applications
    val connector = CassandraConnector(sparkContext.getConf)
    connector.withSessionDo(session =>
    {
      session.execute("DROP KEYSPACE IF EXISTS testkeyspace")
      session.execute("CREATE KEYSPACE testkeyspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}")
      session.execute("USE testkeyspace")
      session.execute("CREATE TABLE emp(emp_id int PRIMARY KEY,emp_name text,emp_city text,emp_sal varint,emp_phone varint)")
      session.execute("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal) VALUES(1,'John', 'London', 0786022338, 65000);")
      session.execute("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal) VALUES(2,'David', 'Hanoi', 0986022576, 40000);")
      session.execute("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal) VALUES(3,'John Cass', 'Scotland', 0786022342, 75000);")
      session.execute("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal) VALUES(4,'Bob Cass', 'Bristol', 0786022258, 80950);")
    }
    )

    //Read Cassandra data using DataFrame
    val df = spark.read
                  .format("org.apache.spark.sql.cassandra")
                  .options(Map( "table" -> "emp", "keyspace" -> "testkeyspace"))
                  .load()

    //Display all row of the emp table
    println("Details of all employees: ")
    df.show()

    //Use Selection and Filtering to find all employees who have high salary (>50000)
    //(In spark, Where is an alias for Filter)
    var highSal=df.select("emp_name","emp_city").filter($"emp_sal">50000)

   //Create a Cassandra Table from a Dataset
    highSal.createCassandraTable("testkeyspace","highsalary")

    //Using a format helper to save data into a Cassandra table
    highSal.write.cassandraFormat("highsalary","testkeyspace").save()

    //Read Cassandra data using DataFrame
    val df_highSal = spark.read.cassandraFormat("highsalary", "testkeyspace").load()

    //Display all high salary employees
    println("All high salary employees: ")
    df_highSal.show()
  }
}

