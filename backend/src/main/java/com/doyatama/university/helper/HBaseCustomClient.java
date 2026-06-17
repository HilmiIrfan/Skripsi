package com.doyatama.university.helper;

import com.doyatama.university.model.Answer;
import com.doyatama.university.model.Question;
import com.doyatama.university.model.BankSoalUjian;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 * Created By: Doyatama
 */
public class HBaseCustomClient {

    private HBaseAdmin admin;
    private Connection connection = null;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    public void insertRecord(TableName tableName, String rowKey, String family, String qualifier, String value) {
        try {
            Table table = connection.getTable(tableName);
            Put p = new Put(Bytes.toBytes(rowKey));
            // Always insert a column, use empty string if value is null
            String valueToInsert = (value != null) ? value : "";
            p.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(valueToInsert));
            table.put(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void insertListRecord(TableName tableName, String rowKey, String family, String qualifier,
            List<String> values) {
        try {
            Table table = connection.getTable(tableName);
            Put p = new Put(Bytes.toBytes(rowKey));
            for (String value : values) {
                p.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            }
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

    public Table getTable(String tableName) throws IOException {
        return connection.getTable(TableName.valueOf(tableName));
    }

    public <T> List<T> showListTable(String tablename, Map<String, String> columnMapping, Class<T> modelClass,
            int sizeLimit) {
        ResultScanner rsObj = null;

        try {
            Table table = connection.getTable(TableName.valueOf(tablename));

            Scan s = new Scan();
            s.setCaching(100);
            if (sizeLimit > 0) {
                s.setLimit(sizeLimit);
            }

            TableDescriptor tableDescriptor = connection.getAdmin().getDescriptor(TableName.valueOf(tablename));
            ColumnFamilyDescriptor[] columnFamilies = tableDescriptor.getColumnFamilies();
            for (ColumnFamilyDescriptor columnFamily : columnFamilies) {
                byte[] family = columnFamily.getName();
                s.addFamily(family);
            }

            rsObj = table.getScanner(s);

            // Create a list to store the objects
            List<T> objects = new ArrayList<T>();

            for (Result result : rsObj) {
                // Do something with the result, e.g. print it to the console
                T object = modelClass.newInstance();
                for (Cell cell : result.listCells()) {
                    // Get the column name
                    String familyName = Bytes.toString(CellUtil.cloneFamily(cell));
                    String columnName = Bytes.toString(CellUtil.cloneQualifier(cell));

                    // Get the variable name from the columnMapping
                    String variableName = columnMapping.get(columnName);

                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    // Get the value of the cell as a string
                    // Check if the variableName contains "department"
                    if (columnMapping.containsKey(familyName)) {
                        // Get the subfield name
                        String subFieldName = columnName.substring(columnName.indexOf(".") + 1);
                        // Get the department object from the main object
                        Field familyField = object.getClass().getDeclaredField(familyName);
                        familyField.setAccessible(true);
                        Object familyObject = familyField.get(object);
                        if (familyObject == null) {
                            if (familyField.getType() == List.class) {
                                familyObject = new ArrayList<>();
                                familyField.set(object, familyObject);
                            } else {
                                familyObject = familyField.getType().newInstance();
                                familyField.set(object, familyObject);
                            }
                        }
                        // Set the value to the subfield
                        if (familyObject instanceof List) {
                            Object currentObject = familyObject;
                            ObjectMapper mapper = new ObjectMapper();

                            JsonNode jsonNode = null;
                            try {
                                jsonNode = mapper.readTree((String) value);
                            } catch (Exception e) {
                                // Tidak berformat JSON, lakukan konversi biasa
                            }

                            if (jsonNode != null
                                    && jsonNode.getNodeType() == JsonNodeFactory.instance.objectNode().getNodeType()) {
                                // Value berformat JSON, lakukan konversi ke Map
                                Map<String, Object> dataList = mapper.readValue((String) value,
                                        new TypeReference<Map<String, Object>>() {
                                        });
                                ((List) currentObject).add(dataList);
                            } else {
                                // Value tidak berformat JSON, lakukan konversi biasa
                                ((List) currentObject).add(value);
                            }
                        } else {
                            Field subField = familyObject.getClass().getDeclaredField(subFieldName);
                            subField.setAccessible(true);
                            setField(subField, familyObject, value);
                        }
                    } else {
                        if (variableName != null) {
                            // Set the value to the variable
                            Field field = object.getClass().getDeclaredField(variableName);
                            field.setAccessible(true);
                            setField(field, object, value);
                        }
                    }
                }
                objects.add(object);
            }

            // Close the scanner and table objects
            rsObj.close();
            return objects;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            if (rsObj != null) {

            }
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public <T> List<T> showListTableIndex(String tablename, Map<String, String> columnMapping, Class<T> modelClass,
            Map<String, String> indexedFields, int sizeLimit) {
        ResultScanner rsObj = null;

        try {
            Table table = connection.getTable(TableName.valueOf(tablename));

            Scan s = new Scan();
            s.setCaching(100);
            if (sizeLimit > 0) {
                s.setLimit(sizeLimit);
            }

            TableDescriptor tableDescriptor = connection.getAdmin().getDescriptor(TableName.valueOf(tablename));
            ColumnFamilyDescriptor[] columnFamilies = tableDescriptor.getColumnFamilies();
            for (ColumnFamilyDescriptor columnFamily : columnFamilies) {
                byte[] family = columnFamily.getName();
                s.addFamily(family);
            }

            rsObj = table.getScanner(s);

            // Create a list to store the objects
            List<T> objects = new ArrayList<T>();

            for (Result result : rsObj) {
                // Membuat objek baru dari class model
                T object = modelClass.newInstance();

                // Maps untuk menyimpan field terindeks sementara
                Map<String, Map<String, String>> mapFields = new HashMap<>();
                Map<String, List<String>> listFields = new HashMap<>();

                // Inisialisasi map untuk setiap field yang terindeks
                for (Map.Entry<String, String> indexedField : indexedFields.entrySet()) {
                    String fieldName = indexedField.getKey();
                    String fieldType = indexedField.getValue();

                    if (fieldType.equals("MAP")) {
                        mapFields.put(fieldName, new HashMap<>());
                    } else if (fieldType.equals("LIST")) {
                        listFields.put(fieldName, new ArrayList<>());
                    }
                }

                // Memproses semua sel untuk objek ini
                for (Cell cell : result.listCells()) {
                    String familyName = Bytes.toString(CellUtil.cloneFamily(cell));
                    String columnName = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));

                    // Cek apakah ini adalah field terindeks (misalnya opsi_0, opsi_1, dst)
                    boolean isIndexedField = false;
                    for (String fieldPrefix : indexedFields.keySet()) {
                        if (columnName.startsWith(fieldPrefix + "_")) {
                            isIndexedField = true;
                            String fieldType = indexedFields.get(fieldPrefix);

                            if (fieldType.equals("MAP")) {
                                // Format untuk MAP: "key:value"
                                int separatorIndex = value.indexOf(':');
                                if (separatorIndex > 0) {
                                    String key = value.substring(0, separatorIndex);
                                    String val = value.substring(separatorIndex + 1);
                                    mapFields.get(fieldPrefix).put(key, val);
                                }
                            } else if (fieldType.equals("LIST")) {
                                // Untuk LIST, tambahkan nilai langsung
                                listFields.get(fieldPrefix).add(value);
                            }
                            break;
                        }
                    }

                    // Jika bukan field terindeks, proses seperti biasa
                    if (!isIndexedField) {
                        // Get the variable name from the columnMapping
                        String variableName = columnMapping.get(columnName);

                        // Check if the variableName contains "department"
                        if (columnMapping.containsKey(familyName)) {
                            // Get the subfield name
                            String subFieldName = columnName.substring(columnName.indexOf(".") + 1);
                            // Get the department object from the main object
                            Field familyField = object.getClass().getDeclaredField(familyName);
                            familyField.setAccessible(true);
                            Object familyObject = familyField.get(object);
                            if (familyObject == null) {
                                if (familyField.getType() == List.class) {
                                    familyObject = new ArrayList<>();
                                    familyField.set(object, familyObject);
                                } else {
                                    familyObject = familyField.getType().newInstance();
                                    familyField.set(object, familyObject);
                                }
                            }
                            // Set the value to the subfield
                            if (familyObject instanceof List) {
                                Object currentObject = familyObject;
                                ObjectMapper mapper = new ObjectMapper();

                                JsonNode jsonNode = null;
                                try {
                                    jsonNode = mapper.readTree((String) value);
                                } catch (Exception e) {
                                    // Tidak berformat JSON, lakukan konversi biasa
                                }

                                if (jsonNode != null
                                        && jsonNode.getNodeType() == JsonNodeFactory.instance.objectNode()
                                                .getNodeType()) {
                                    // Value berformat JSON, lakukan konversi ke Map
                                    Map<String, Object> dataList = mapper.readValue((String) value,
                                            new TypeReference<Map<String, Object>>() {
                                            });
                                    ((List) currentObject).add(dataList);
                                } else {
                                    // Value tidak berformat JSON, lakukan konversi biasa
                                    ((List) currentObject).add(value);
                                }
                            } else {
                                Field subField = familyObject.getClass().getDeclaredField(subFieldName);
                                subField.setAccessible(true);
                                setField(subField, familyObject, value);
                            }
                        } else {
                            if (variableName != null) {
                                // Set the value to the variable
                                Field field = object.getClass().getDeclaredField(variableName);
                                field.setAccessible(true);
                                setField(field, object, value);
                            }
                        }
                    }
                }

                // Setelah memproses semua sel, atur field terindeks ke objek
                for (Map.Entry<String, Map<String, String>> entry : mapFields.entrySet()) {
                    String fieldName = entry.getKey();
                    Map<String, String> fieldValue = entry.getValue();

                    if (!fieldValue.isEmpty()) {
                        Field field = object.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(object, fieldValue);
                    }
                }

                for (Map.Entry<String, List<String>> entry : listFields.entrySet()) {
                    String fieldName = entry.getKey();
                    List<String> fieldValue = entry.getValue();

                    if (!fieldValue.isEmpty()) {
                        Field field = object.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(object, fieldValue);
                    }
                }

                objects.add(object);
            }

            // Close the scanner and table objects
            rsObj.close();
            return objects;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            if (rsObj != null) {
                rsObj.close();
            }
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public <T> List<T> showListTableFiltered(String tablename, Map<String, String> columnMapping, Class<T> modelClass,
            int sizeLimit, String familyName, String columnName, String columnValue) {
        ResultScanner rsObj = null;

        try {
            Table table = connection.getTable(TableName.valueOf(tablename));
            Scan s = new Scan();
            s.setCaching(100);

            if (sizeLimit > 0) {
                s.setLimit(sizeLimit);
            }

            // Menambahkan keluarga kolom
            TableDescriptor tableDescriptor = connection.getAdmin().getDescriptor(TableName.valueOf(tablename));
            ColumnFamilyDescriptor[] columnFamilies = tableDescriptor.getColumnFamilies();
            for (ColumnFamilyDescriptor columnFamily : columnFamilies) {
                byte[] family = columnFamily.getName();
                s.addFamily(family);
            }

            // Memastikan bahwa familyName, columnName, dan columnValue tidak null
            if (familyName != null && columnName != null && columnValue != null) {
                Filter filter = new SingleColumnValueFilter(
                        Bytes.toBytes(familyName),
                        Bytes.toBytes(columnName),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(columnValue));
                s.setFilter(filter);
            }

            // Mengambil scanner
            rsObj = table.getScanner(s);
            List<T> objects = new ArrayList<>();

            // Mengiterasi hasil
            for (Result result : rsObj) {
                T object = modelClass.getDeclaredConstructor().newInstance(); // Menggunakan Constructor

                for (Cell cell : result.listCells()) {
                    String familyNameCell = Bytes.toString(CellUtil.cloneFamily(cell));
                    String columnNameCell = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String variableName = columnMapping.get(columnNameCell);
                    String value = Bytes.toString(CellUtil.cloneValue(cell));

                    // Mengisi objek dengan data yang diambil
                    if (columnMapping.containsKey(familyNameCell)) {
                        String subFieldName = columnNameCell.substring(columnNameCell.indexOf(".") + 1);
                        Field familyField = object.getClass().getDeclaredField(familyNameCell);
                        familyField.setAccessible(true);
                        Object familyObject = familyField.get(object);

                        if (familyObject == null) {
                            if (familyField.getType() == List.class) {
                                familyObject = new ArrayList<>();
                                familyField.set(object, familyObject);
                            } else {
                                familyObject = familyField.getType().newInstance();
                                familyField.set(object, familyObject);
                            }
                        }

                        // Menangani pengisian field sub
                        if (familyObject instanceof List) {
                            ((List) familyObject).add(value);
                        } else {
                            Field subField = familyObject.getClass().getDeclaredField(subFieldName);
                            subField.setAccessible(true);
                            setField(subField, familyObject, value);
                        }
                    } else {
                        if (variableName != null) {
                            Field field = object.getClass().getDeclaredField(variableName);
                            field.setAccessible(true);
                            setField(field, object, value);
                        }
                    }
                }
                objects.add(object);
            }

            return objects;
        } catch (IOException e) {
            if (rsObj != null)
                rsObj.close();
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } finally {
            if (rsObj != null) {
                rsObj.close(); // Pastikan scanner ditutup di dalam finally
            }
        }

        return null;
    }

    // Di dalam class HBaseCustomClient
    public List<String> getColumns(String tableName, String rowKey, String columnFamily) throws IOException {
        List<String> columnNames = new ArrayList<>();
        Table table = getTable(tableName); // asumsi Anda sudah punya method ini
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addFamily(Bytes.toBytes(columnFamily));
        Result result = table.get(get);

        for (Cell cell : result.listCells()) {
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            columnNames.add(qualifier);
        }

        return columnNames;
    }

    // Untuk menghapus berdasarkan kolom
    public void deleteRecordByColumn(String tableName, String rowKey, String columnFamily, String qualifier)
            throws IOException {
        Table table = getTable(tableName);
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
        table.delete(delete);
    }

    public Map<String, Map<String, String>> getAllRows(TableName tableName) throws IOException {
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            Map<String, Map<String, String>> rows = new HashMap<>();

            for (Result result : scanner) {
                Map<String, String> rowData = new HashMap<>();
                result.getNoVersionMap().forEach((family, columns) -> {
                    columns.forEach((qualifier, value) -> rowData.put(new String(family) + ":" + new String(qualifier),
                            new String(value)));
                });
                rows.put(new String(result.getRow()), rowData);
            }
            return rows;
        }
    }

    public <T> T showDataTable(String tablename, Map<String, String> columnMapping, String uuid, Class<T> modelClass) {
        Result result = null;

        try {
            Table table = connection.getTable(TableName.valueOf(tablename));
            Get get = new Get(Bytes.toBytes(uuid));
            TableDescriptor tableDescriptor = connection.getAdmin().getDescriptor(TableName.valueOf(tablename));
            ColumnFamilyDescriptor[] columnFamilies = tableDescriptor.getColumnFamilies();
            for (ColumnFamilyDescriptor columnFamily : columnFamilies) {
                byte[] family = columnFamily.getName();
                get.addFamily(family);
            }

            result = table.get(get);

            // Create a list to store the objects
            T object = modelClass.newInstance();

            // Do something with the result, e.g. print it to the console
            for (Cell cell : result.rawCells()) {
                // Get the column name
                String familyName = Bytes.toString(CellUtil.cloneFamily(cell));
                String columnName = Bytes.toString(CellUtil.cloneQualifier(cell));

                // Get the variable name from the columnMapping
                String variableName = columnMapping.get(columnName);

                String value = Bytes.toString(CellUtil.cloneValue(cell));
                // Get the value of the cell as a string
                // Check if the variableName contains "department"
                if (columnMapping.containsKey(familyName)) {
                    // Get the subfield name
                    String subFieldName = columnName.substring(columnName.indexOf(".") + 1);
                    // Get the department object from the main object
                    Field familyField = object.getClass().getDeclaredField(familyName);
                    familyField.setAccessible(true);
                    Object familyObject = familyField.get(object);
                    if (familyObject == null) {
                        if (familyField.getType() == List.class) {
                            familyObject = new ArrayList<>();
                            familyField.set(object, familyObject);
                        } else {
                            familyObject = familyField.getType().newInstance();
                            familyField.set(object, familyObject);
                        }
                    }
                    // Set the value to the subfield
                    if (familyObject instanceof List) {
                        Object currentObject = familyObject;
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = null;
                        try {
                            jsonNode = mapper.readTree((String) value);
                        } catch (Exception e) {
                            // Tidak berformat JSON, lakukan konversi biasa
                        }

                        if (jsonNode != null
                                && jsonNode.getNodeType() == JsonNodeFactory.instance.objectNode().getNodeType()) {
                            // Value berformat JSON, lakukan konversi ke Map
                            Map<String, Object> dataList = mapper.readValue((String) value,
                                    new TypeReference<Map<String, Object>>() {
                                    });
                            ((List) currentObject).add(dataList);
                        } else {
                            // Value tidak berformat JSON, lakukan konversi biasa
                            ((List) currentObject).add(value);
                        }
                    } else {
                        Field subField = familyObject.getClass().getDeclaredField(subFieldName);
                        subField.setAccessible(true);
                        setField(subField, familyObject, value);
                    }
                } else {
                    if (variableName != null) {
                        // Set the value to the variable
                        Field field = object.getClass().getDeclaredField(variableName);
                        field.setAccessible(true);
                        setField(field, object, value);
                    }
                }
            }

            // Close the scanner and table objects
            table.close();
            return object;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public <T> T getDataByColumn(String tableName, Map<String, String> columnMapping, String familyName,
            String columnName, String columnValue, Class<T> modelClass) {
        try {
            // Create HBase table object
            Table table = connection.getTable(TableName.valueOf(tableName));

            // Create Scan object to scan table
            Scan scan = new Scan();
            scan.setCaching(100);
            scan.setLimit(1000);

            // Add filter to scan by column value
            Filter filter = new SingleColumnValueFilter(Bytes.toBytes(familyName), Bytes.toBytes(columnName),
                    CompareOperator.EQUAL, Bytes.toBytes(columnValue));
            scan.setFilter(filter);

            // Create a list to store the objects
            T object = modelClass.newInstance();

            // Scan the table and get the data
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                // Do something with the result, e.g. print it to the console
                for (Cell cell : result.rawCells()) {
                    String familyName2 = Bytes.toString(CellUtil.cloneFamily(cell));
                    String columnName2 = Bytes.toString(CellUtil.cloneQualifier(cell));
                    // Get the variable name from the columnMapping
                    String variableName = columnMapping.get(columnName2);

                    if (variableName != null) {
                        // Get the value of the cell as a string
                        String value = Bytes.toString(CellUtil.cloneValue(cell));
                        // Check if the variableName contains "department"
                        if (columnMapping.containsKey(familyName2)) {
                            // Get the subfield name
                            String subFieldName = variableName.substring(variableName.indexOf(".") + 1);
                            // Get the department object from the main object
                            Field familyField = object.getClass().getDeclaredField(familyName2);
                            familyField.setAccessible(true);
                            Object familyObject = familyField.get(object);
                            if (familyObject == null) {
                                familyObject = familyField.getType().newInstance();
                                familyField.set(object, familyObject);
                            }
                            // Set the value to the subfield
                            Field subField = familyObject.getClass().getDeclaredField(subFieldName);
                            subField.setAccessible(true);
                            setField(subField, familyObject, value);
                        } else {
                            // Set the value to the variable
                            Field field = object.getClass().getDeclaredField(variableName);
                            field.setAccessible(true);
                            setField(field, object, value);
                        }
                    }
                }
            }

            // Close the scanner and table objects
            table.close();
            return object;
        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> List<T> getDataListByColumn(String tableName, Map<String, String> columnMapping, String familyName,
            String columnName, String columnValue, Class<T> modelClass, int sizeLimit) {
        ResultScanner rsObj = null;

        try {
            Table table = connection.getTable(TableName.valueOf(tableName));

            Scan s = new Scan();
            s.setCaching(200);
            s.setLimit(sizeLimit);
            TableDescriptor tableDescriptor = connection.getAdmin().getDescriptor(TableName.valueOf(tableName));
            ColumnFamilyDescriptor[] columnFamilies = tableDescriptor.getColumnFamilies();
            for (ColumnFamilyDescriptor columnFamily : columnFamilies) {
                byte[] family = columnFamily.getName();
                s.addFamily(family);
            }

            Filter filter = new SingleColumnValueFilter(Bytes.toBytes(familyName), Bytes.toBytes(columnName),
                    CompareOperator.EQUAL, Bytes.toBytes(columnValue));
            s.setFilter(filter);

            rsObj = table.getScanner(s);

            // Create a list to store the objects
            List<T> objects = new ArrayList<T>();

            for (Result result : rsObj) {
                // Do something with the result, e.g. print it to the console
                T object = modelClass.newInstance();
                for (Cell cell : result.listCells()) {
                    // Get the column name
                    String familyName2 = Bytes.toString(CellUtil.cloneFamily(cell));
                    String columnName2 = Bytes.toString(CellUtil.cloneQualifier(cell));

                    // Get the variable name from the columnMapping
                    String variableName = columnMapping.get(columnName2);

                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    // Get the value of the cell as a string
                    // Check if the variableName contains "department"
                    if (columnMapping.containsKey(familyName2)) {
                        // Get the subfield name
                        String subFieldName = columnName2.substring(columnName2.indexOf(".") + 1);
                        // Get the department object from the main object
                        Field familyField = object.getClass().getDeclaredField(familyName2);
                        familyField.setAccessible(true);
                        Object familyObject = familyField.get(object);
                        if (familyObject == null) {
                            if (familyField.getType() == List.class) {
                                familyObject = new ArrayList<>();
                                familyField.set(object, familyObject);
                            } else {
                                familyObject = familyField.getType().newInstance();
                                familyField.set(object, familyObject);
                            }
                        }
                        // Set the value to the subfield
                        if (familyObject instanceof List) {
                            Object currentObject = familyObject;
                            ObjectMapper mapper = new ObjectMapper();

                            JsonNode jsonNode = null;
                            try {
                                jsonNode = mapper.readTree((String) value);
                            } catch (Exception e) {
                                // Tidak berformat JSON, lakukan konversi biasa
                            }

                            if (jsonNode != null
                                    && jsonNode.getNodeType() == JsonNodeFactory.instance.objectNode().getNodeType()) {
                                // Value berformat JSON, lakukan konversi ke Map
                                Map<String, Object> dataList = mapper.readValue((String) value,
                                        new TypeReference<Map<String, Object>>() {
                                        });
                                ((List) currentObject).add(dataList);
                            } else {
                                // Value tidak berformat JSON, lakukan konversi biasa
                                ((List) currentObject).add(value);
                            }
                        } else {
                            Field subField = familyObject.getClass().getDeclaredField(subFieldName);
                            subField.setAccessible(true);
                            setField(subField, familyObject, value);
                        }
                    } else {
                        if (variableName != null) {
                            // Set the value to the variable
                            Field field = object.getClass().getDeclaredField(variableName);
                            field.setAccessible(true);
                            setField(field, object, value);
                        }
                    }
                }
                objects.add(object);
            }

            // Close the scanner and table objects
            rsObj.close();
            return objects;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            rsObj.close();
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public <T> List<T> getDataListByColumnIndeks(String tableName, Map<String, String> columnMapping,
            String familyName, String columnName, String columnValue, Class<T> modelClass,
            int sizeLimit, Map<String, String> indexedFields) throws IOException {

        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Scan scan = new Scan();
            scan.setCaching(200);
            scan.setLimit(sizeLimit);

            // Add all column families
            TableDescriptor tableDescriptor = connection.getAdmin().getDescriptor(TableName.valueOf(tableName));
            for (ColumnFamilyDescriptor family : tableDescriptor.getColumnFamilies()) {
                scan.addFamily(family.getName());
            }

            // Set filter
            Filter filter = new SingleColumnValueFilter(
                    Bytes.toBytes(familyName),
                    Bytes.toBytes(columnName),
                    CompareOperator.EQUAL,
                    Bytes.toBytes(columnValue));
            scan.setFilter(filter);

            List<T> objects = new ArrayList<>();
            try (ResultScanner scanner = table.getScanner(scan)) {
                for (Result result : scanner) {
                    T object = modelClass.getDeclaredConstructor().newInstance();

                    // Initialize indexed fields with default values
                    if (indexedFields != null) {
                        for (Map.Entry<String, String> entry : indexedFields.entrySet()) {
                            try {
                                Field field = modelClass.getDeclaredField(entry.getKey());
                                field.setAccessible(true);
                                if (field.get(object) == null) {
                                    if ("MAP".equalsIgnoreCase(entry.getValue())) {
                                        field.set(object, new HashMap<>());
                                    } else if ("LIST".equalsIgnoreCase(entry.getValue())) {
                                        field.set(object, new ArrayList<>());
                                    } else if ("INTEGER".equalsIgnoreCase(entry.getValue())) {
                                        field.set(object, 0);
                                    }
                                }
                            } catch (Exception e) {
                                // Ignore if field not found
                            }
                        }
                    }

                    // Process each cell in the result with null safety
                    if (result.listCells() != null) {
                        for (Cell cell : result.listCells()) {
                            try {
                                String family = Bytes.toString(CellUtil.cloneFamily(cell));
                                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                                String value = Bytes.toString(CellUtil.cloneValue(cell));

                                // Add null safety checks
                                if (family == null || qualifier == null) {
                                    continue; // Skip null family/qualifier
                                }

                                // Ensure value is not null (empty string is acceptable)
                                value = (value != null) ? value : "";

                                // Check if this is a nested field (family exists in columnMapping)
                                if (columnMapping.containsKey(family)) {
                                    // Handle nested object (e.g., user.id, taksonomi.namaTaksonomi)
                                    handleNestedField(object, family, qualifier, value);
                                } else {
                                    // Handle regular field
                                    String fieldName = columnMapping.get(qualifier);
                                    if (fieldName != null) {
                                        handleRegularField(object, fieldName, value, indexedFields);
                                    }
                                }
                            } catch (Exception cellException) {
                                // Log and continue processing other cells
                                System.err.println("Error processing cell: " + cellException.getMessage());
                                continue;
                            }
                        }
                    }

                    objects.add(object);
                }
            }
            return objects;
        } catch (Exception e) {
            throw new IOException("Failed to retrieve data from HBase", e);
        }
    }

    private <T> void handleNestedField(T object, String family, String qualifier, String value)
            throws IllegalAccessException, NoSuchFieldException, InstantiationException, NoSuchMethodException,
            InvocationTargetException {
        Field familyField = object.getClass().getDeclaredField(family);
        familyField.setAccessible(true);
        Object familyObject = familyField.get(object);

        if (familyObject == null) {
            familyObject = familyField.getType().getDeclaredConstructor().newInstance();
            familyField.set(object, familyObject);
        }

        // Extract subfield name (everything after the dot)
        String subFieldName = qualifier;
        Field subField = familyObject.getClass().getDeclaredField(subFieldName);
        subField.setAccessible(true);
        setField(subField, familyObject, value);
    }

    private <T> void handleRegularField(T object, String fieldName, String value,
            Map<String, String> indexedFields) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        // Handle special indexed fields
        if (indexedFields != null && indexedFields.containsKey(fieldName)) {
            String fieldType = indexedFields.get(fieldName);
            try {
                if ("MAP".equalsIgnoreCase(fieldType)) {
                    Map<String, Object> mapValue = objectMapper.readValue(value,
                            new TypeReference<Map<String, Object>>() {
                            });
                    field.set(object, mapValue);
                } else if ("LIST".equalsIgnoreCase(fieldType)) {
                    // Handle special cases for bankSoalList
                    if (fieldName.equals("bankSoalList")) {
                        List<BankSoalUjian> listValue = objectMapper.readValue(value,
                                new TypeReference<List<BankSoalUjian>>() {
                                });
                        field.set(object, listValue);
                    } else {
                        List<Object> listValue = objectMapper.readValue(value,
                                new TypeReference<List<Object>>() {
                                });
                        field.set(object, listValue);
                    }
                } else if ("INTEGER".equalsIgnoreCase(fieldType)) {
                    field.set(object, Integer.parseInt(value));
                } else {
                    field.set(object, value);
                }
            } catch (Exception e) {
                // If parsing fails, set default value based on field type
                try {
                    if ("MAP".equalsIgnoreCase(fieldType)) {
                        field.set(object, new HashMap<>());
                    } else if ("LIST".equalsIgnoreCase(fieldType)) {
                        if (fieldName.equals("bankSoalList")) {
                            field.set(object, new ArrayList<BankSoalUjian>());
                        } else {
                            field.set(object, new ArrayList<>());
                        }
                    } else if ("INTEGER".equalsIgnoreCase(fieldType)) {
                        field.set(object, 0);
                    } else {
                        field.set(object, value);
                    }
                } catch (Exception fallbackException) {
                    // Ignore fallback errors
                }
            }
        } else {
            // Handle regular fields
            setField(field, object, value);
        }
    }

    private void setField(Field field, Object object, String value) throws IllegalAccessException {
        // Ubah hak akses field agar dapat diakses
        field.setAccessible(true);

        // Dapatkan tipe data dari field
        Class<?> fieldType = field.getType();

        // Parsing nilai string menjadi nilai sesuai tipe data
        if (fieldType == Integer.class) {
            Integer intValue = Integer.parseInt(value);
            field.set(object, intValue);
        } else if (fieldType == long.class) {
            long longValue = Long.parseLong(value);
            field.setLong(object, longValue);
        } else if (fieldType == float.class) {
            float floatValue = Float.parseFloat(value);
            field.setFloat(object, floatValue);
        } else if (fieldType == Float.class) {
            Float floatValue = Float.parseFloat(value);
            field.set(object, floatValue);
        } else if (fieldType == double.class) {
            double doubleValue = Double.parseDouble(value);
            field.setDouble(object, doubleValue);
        } else if (fieldType == Double.class) {
            Double doubleValue = Double.valueOf(value);
            field.set(object, doubleValue);
        } else if (fieldType == boolean.class) {
            boolean booleanValue = Boolean.parseBoolean(value);
            field.setBoolean(object, booleanValue);
        } else if (fieldType == Boolean.class) {
            Boolean booleanValue;
            if (value.equalsIgnoreCase("true")) {
                booleanValue = Boolean.TRUE;
            } else {
                booleanValue = Boolean.FALSE;
            }
            field.set(object, booleanValue);
        } else if (fieldType == String.class) {
            field.set(object, value);
        } else if (fieldType == Instant.class) {
            Instant instantValue = Instant.parse(value);
            field.set(object, instantValue);
        } else if (fieldType == Question.QuestionType.class) {
            Question.QuestionType instantValue = Question.QuestionType.valueOf(value);
            field.set(object, instantValue);
        } else if (fieldType == Question.AnswerType.class) {
            Question.AnswerType instantValue = Question.AnswerType.valueOf(value);
            field.set(object, instantValue);
        } else if (fieldType == Answer.AnswerType.class) {
            Answer.AnswerType instantValue = Answer.AnswerType.valueOf(value);
            field.set(object, instantValue);
        } else if (fieldType == Question.ExamType.class) {
            Question.ExamType examTypeValue = Question.ExamType.valueOf(value);
            field.set(object, examTypeValue);
        } else if (fieldType == Question.ExamType2.class) {
            Question.ExamType2 examType2Value = Question.ExamType2.valueOf(value);
            field.set(object, examType2Value);
        } else if (fieldType == Question.ExamType3.class) {
            Question.ExamType3 examType3Value = Question.ExamType3.valueOf(value);
            field.set(object, examType3Value);
        } else if (List.class.isAssignableFrom(fieldType)) {
            // Handling List types
            try {
                // Handle special cases for bankSoalList
                if (field.getName().equals("bankSoalList")) {
                    // Parse as List<BankSoalUjian>
                    TypeReference<List<BankSoalUjian>> typeRef = new TypeReference<List<BankSoalUjian>>() {
                    };
                    List<BankSoalUjian> listValue = objectMapper.readValue(value, typeRef);
                    field.set(object, listValue);
                } else {
                    // For other List fields, use List<String> as default
                    TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
                    };
                    List<String> listValue = objectMapper.readValue(value, typeRef);
                    field.set(object, listValue);
                }
            } catch (Exception e) {
                System.out.println("Error parsing List field " + field.getName() + ": " + e.getMessage());
                // Set empty list as fallback
                try {
                    if (field.getName().equals("bankSoalList")) {
                        field.set(object, new ArrayList<BankSoalUjian>());
                    } else {
                        field.set(object, new ArrayList<String>());
                    }
                } catch (Exception fallbackException) {
                    // Ignore fallback errors
                }
            }
        } else if (Map.class.isAssignableFrom(fieldType)) {
            // Handling Map types
            try {
                // Handle specific Map types based on field name
                if (field.getName().equals("skorPerSoal")) {
                    // skorPerSoal is Map<String, Double>
                    TypeReference<Map<String, Double>> typeRef = new TypeReference<Map<String, Double>>() {
                    };
                    Map<String, Double> mapValue = objectMapper.readValue(value, typeRef);
                    field.set(object, mapValue);
                } else if (field.getName().equals("metadata") || field.getName().equals("securityFlags")
                        || field.getName().equals("appealData") || field.getName().equals("pengaturan")
                        || field.getName().equals("catSettings") || field.getName().equals("topicPerformance")
                        || field.getName().equals("conceptMastery") || field.getName().equals("changePatterns")
                        || field.getName().equals("learningInsights")) {
                    // These fields are Map<String, Object>
                    TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
                    };
                    Map<String, Object> mapValue = objectMapper.readValue(value, typeRef);
                    field.set(object, mapValue);
                } else if (field.getName().equals("jawabanPeserta") || field.getName().equals("timeSpentPerQuestion")
                        || field.getName().equals("attemptCountPerQuestion")) {
                    // These fields have mixed types, handle as Map<String, Object> then convert if
                    // needed
                    TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
                    };
                    Map<String, Object> mapValue = objectMapper.readValue(value, typeRef);
                    field.set(object, mapValue);
                } else if (field.getName().equals("jawabanBenar")) {
                    // jawabanBenar is Map<String, Boolean>
                    TypeReference<Map<String, Boolean>> typeRef = new TypeReference<Map<String, Boolean>>() {
                    };
                    Map<String, Boolean> mapValue = objectMapper.readValue(value, typeRef);
                    field.set(object, mapValue);
                } else if (field.getName().equals("answerTimestamps")) {
                    // answerTimestamps is Map<String, Instant> but we'll handle as Map<String,
                    // String> first
                    TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
                    };
                    Map<String, String> tempMap = objectMapper.readValue(value, typeRef);
                    Map<String, Instant> mapValue = new HashMap<>();
                    for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                        try {
                            mapValue.put(entry.getKey(), Instant.parse(entry.getValue()));
                        } catch (Exception e) {
                            System.out.println("Error parsing Instant for answerTimestamps: " + e.getMessage());
                        }
                    }
                    field.set(object, mapValue);
                } else {
                    // Default to Map<String, Object> for other Map fields
                    TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
                    };
                    Map<String, Object> mapValue = objectMapper.readValue(value, typeRef);
                    field.set(object, mapValue);
                }
            } catch (Exception e) {
                System.out.println("Error parsing Map field " + field.getName() + ": " + e.getMessage());
                // Set empty map as fallback
                try {
                    field.set(object, new HashMap<>());
                } catch (Exception fallbackException) {
                    // Ignore fallback errors
                }
            }
        } else {
            // Handle complex objects that might contain special fields like bankSoalList
            try {
                // Try to deserialize as JSON object
                Object complexObject = objectMapper.readValue(value, fieldType);

                // If the complex object is an Ujian and has bankSoalList, handle it specially
                if (fieldType.getName().equals("com.doyatama.university.model.Ujian")) {
                    // Re-deserialize with special handling for bankSoalList
                    JsonNode jsonNode = objectMapper.readTree(value);
                    Object ujianObject = fieldType.newInstance();

                    // Set all fields from JSON
                    Field[] fields = fieldType.getDeclaredFields();
                    for (Field ujianField : fields) {
                        ujianField.setAccessible(true);
                        String fieldName = ujianField.getName();

                        if (jsonNode.has(fieldName)) {
                            JsonNode fieldNode = jsonNode.get(fieldName);
                            if (fieldName.equals("bankSoalList") && fieldNode.isArray()) {
                                // Special handling for bankSoalList
                                List<BankSoalUjian> bankSoalList = objectMapper.readValue(fieldNode.toString(),
                                        new TypeReference<List<BankSoalUjian>>() {
                                        });
                                ujianField.set(ujianObject, bankSoalList);
                            } else if (!fieldNode.isNull()) {
                                // Handle other fields
                                setFieldFromJsonNode(ujianField, ujianObject, fieldNode);
                            }
                        }
                    }
                    field.set(object, ujianObject);
                } else {
                    // For other complex objects, use regular deserialization
                    field.set(object, complexObject);
                }
            } catch (Exception e) {
                // Fallback: just set the string value or log the error
                System.out.println("Error parsing complex object field " + field.getName() + ": " + e.getMessage());
                // Tipe data yang tidak dikenal, lewati saja
                System.out.println("Tipe data " + fieldType + " tidak dikenali.");
            }
        }
    }

    private void setFieldFromJsonNode(Field field, Object object, JsonNode jsonNode) throws IllegalAccessException {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        try {
            if (fieldType == String.class) {
                field.set(object, jsonNode.asText());
            } else if (fieldType == Integer.class || fieldType == int.class) {
                field.set(object, jsonNode.asInt());
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                field.set(object, jsonNode.asBoolean());
            } else if (fieldType == Long.class || fieldType == long.class) {
                field.set(object, jsonNode.asLong());
            } else if (fieldType == Double.class || fieldType == double.class) {
                field.set(object, jsonNode.asDouble());
            } else if (fieldType == Instant.class) {
                field.set(object, Instant.parse(jsonNode.asText()));
            } else if (List.class.isAssignableFrom(fieldType)) {
                // For lists, use the existing JSON parsing logic
                String jsonString = jsonNode.toString();
                if (field.getName().equals("bankSoalList")) {
                    List<BankSoalUjian> listValue = objectMapper.readValue(jsonString,
                            new TypeReference<List<BankSoalUjian>>() {
                            });
                    field.set(object, listValue);
                } else {
                    List<String> listValue = objectMapper.readValue(jsonString,
                            new TypeReference<List<String>>() {
                            });
                    field.set(object, listValue);
                }
            } else if (Map.class.isAssignableFrom(fieldType)) {
                // Handle specific Map types based on field name
                if (field.getName().equals("skorPerSoal")) {
                    // skorPerSoal is Map<String, Double>
                    Map<String, Double> mapValue = objectMapper.readValue(jsonNode.toString(),
                            new TypeReference<Map<String, Double>>() {
                            });
                    field.set(object, mapValue);
                } else if (field.getName().equals("jawabanBenar")) {
                    // jawabanBenar is Map<String, Boolean>
                    Map<String, Boolean> mapValue = objectMapper.readValue(jsonNode.toString(),
                            new TypeReference<Map<String, Boolean>>() {
                            });
                    field.set(object, mapValue);
                } else {
                    // Default to Map<String, Object> for other Map fields
                    Map<String, Object> mapValue = objectMapper.readValue(jsonNode.toString(),
                            new TypeReference<Map<String, Object>>() {
                            });
                    field.set(object, mapValue);
                }
            } else {
                // For other complex objects, try JSON deserialization
                Object value = objectMapper.readValue(jsonNode.toString(), fieldType);
                field.set(object, value);
            }
        } catch (Exception e) {
            System.out.println("Error setting field " + field.getName() + " from JsonNode: " + e.getMessage());
        }
    }
}