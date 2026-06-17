package create_structure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class HBaseCustomClient {

    private HBaseAdmin admin;
    private Connection connection = null;

    public HBaseCustomClient(Configuration conf) throws IOException {
        connection = ConnectionFactory.createConnection(conf);
        admin = (HBaseAdmin) connection.getAdmin();
    }

    public void createTable(TableName tableName, String[] CFs) {

        try {
            if (admin.tableExists(tableName)) {

                System.out.println(tableName + "Already Exists");

            } else {

                HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName.toString()));

                for (String CFName : CFs) {
                    tableDescriptor.addFamily(new HColumnDescriptor(CFName));
                }

                admin.createTable(tableDescriptor);

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void deleteTable(TableName tableName) {

        try {
            if (admin.tableExists(tableName)) {

                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            } else {
                System.out.println(tableName + " Doesn't exist");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void insertRecord(TableName tableName, String rowKey, String family, String qualifier, String value)
            throws IOException {
        try (Table table = connection.getTable(tableName)) { // try-with-resources
            Put p = new Put(Bytes.toBytes(rowKey));
            p.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteRecord(String tableName, String rowKey) {

        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Delete d = new Delete(Bytes.toBytes(rowKey));
            table.delete(d);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void truncateTable(TableName tableName, Configuration conf) throws IOException {

        try (Connection connection = ConnectionFactory.createConnection(conf);

                Admin admin = connection.getAdmin()) {

            admin.disableTable(tableName);

            admin.truncateTable(tableName, true);

        }

    }

    public <T> void showTable(String tablename, Map<String, String> columnMapping, Class<T> modelClass) {
        ResultScanner rsObj = null;

        try {
            Table table = connection.getTable(TableName.valueOf(tablename));
            byte[] columnFamily = Bytes.toBytes("main");

            Scan s = new Scan();
            s.addFamily(columnFamily);

            rsObj = table.getScanner(s);

            // Create a list to store the objects
            List<T> objects = new ArrayList<T>();
            ObjectMapper objectMapper = new ObjectMapper();

            for (Result result : rsObj) {
                // Do something with the result, e.g. print it to the console
                String json = objectMapper.writeValueAsString(result.rawCells());
                T object = modelClass.newInstance();
                for (Cell cell : result.rawCells()) {
                    // Get the column name
                    String columnName = Bytes.toString(CellUtil.cloneQualifier(cell));
                    // Get the variable name from the columnMapping
                    String variableName = columnMapping.get(columnName);
                    // Get the value of the cell as a string
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    // Set the value to the variable
                    Field field = object.getClass().getDeclaredField(variableName);
                    field.setAccessible(true);
                    field.set(object, value);
                }
                objects.add(object);
            }

            String json = objectMapper.writeValueAsString(objects);
            System.out.println(json);

            // Close the scanner and table objects
            rsObj.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            rsObj.close();
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

// Create a class for the objects that will be stored
class UserObject {
    private String id;
    private String email;
    private String password;
    private String role;

    public UserObject() {
    }

    public UserObject(String id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "email":
                this.email = value;
                break;
            case "password":
                this.password = value;
                break;
            case "role":
                this.role = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}