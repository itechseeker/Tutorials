
import com.datastax.driver.core._

object BasicProgram {
  def main(args: Array[String]): Unit = {
    val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
    val session = cluster.connect("testkeyspace")


    val set = session.execute("SELECT * FROM emp;");
    System.out.println(set.all());

  }

}
