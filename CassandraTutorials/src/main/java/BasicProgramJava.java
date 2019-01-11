
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class BasicProgramJava {

    public static void main(String args[]){

        //Declare a query string
        String query;

        //creating Cluster object
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();

        //Creating Session object
        Session session = cluster.connect();

        //Query to drop the keyspace
        query="DROP KEYSPACE testkeyspace;";

        //Executing the query
        session.execute(query);

        //Query to create a keyspace
        query= "CREATE KEYSPACE testkeyspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};";

        //Executing the query
        session.execute(query);

        //Connect to the testkeyspace keyspace
        session=cluster.connect("testkeyspace");

        //Create emp table
        query=  "CREATE TABLE emp(\n" +
                "emp_id int PRIMARY KEY,\n" +
                "emp_name text,\n" +
                "emp_city text,\n" +
                "emp_sal varint,\n" +
                "emp_phone varint\n" +
                ");";

        //Executing the query
        session.execute(query);

        //Insert data into the table
        query="INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal)\n" +
                "VALUES(1,'John', 'London', 0786022338, 65000);";

        String query2="INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal)\n" +
                      "VALUES(2,'David', 'Hanoi', 0986022576, 40000);";

        //Executing the query
        session.execute(query);
        session.execute(query2);

        //Retrieve data from emp table
        query2="SELECT * FROM emp;";
        ResultSet result= session.execute(query2);

        //Display column definition
        System.out.println("Column definition: "+result.getColumnDefinitions().toString());

        //Display all data
        System.out.println("\nALl data in the table: "+result.all());

        //update query
        query = "UPDATE emp SET emp_city='HoChiMinh',emp_sal=55000 WHERE emp_id=2";
        session.execute(query);
        //Display the data after updating
        System.out.println("\nThe data after updating: "+session.execute(query2).all());

        //Display a specific column
        result= session.execute("SELECT * FROM emp;");
        System.out.println("\nSelect only emp_name and emp_city in the ResultSet:");
        for(Row row:result.all())
        {
            System.out.println("Name: "+row.getString("emp_name") +", City: "+row.getString("emp_city"));
        }

        //Using batch to execute multiple modification statements
        StringBuilder sb = new StringBuilder("BEGIN BATCH ")
                .append("INSERT INTO emp (emp_id, emp_name, emp_city,emp_phone, emp_sal)\n" +
                        "VALUES(3,'Bob', 'NewYork', 0726022689, 58000);")
                .append("UPDATE emp SET emp_sal = 69000 WHERE emp_id =1;")
                .append("DELETE emp_city, emp_phone FROM emp WHERE emp_id = 2;")
                .append("APPLY BATCH");
        //Executing the query
        session.execute(sb.toString());
        //Display all data
        System.out.println("\nALl data in the table: "+session.execute(query2).all());
    }
}