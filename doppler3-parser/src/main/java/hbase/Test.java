package hbase;


//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.apache.hadoop.hbase.HColumnDescriptor;
//import org.apache.hadoop.hbase.HTableDescriptor;
//import org.apache.hadoop.hbase.KeyValue;
//import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class Test {

//
//
//    public static void main(String[] args) throws IOException {
//
//        query();
//    }
//
//    public static void query() throws IOException {
//        Configuration config = HBaseConfiguration.create();
//        config.set("hbase.zookeeper.quorum", "192.168.0.89");
//        System.out.println( " 1111!");
//
//        HTablePool pool = new HTablePool(config, 10);
////        HTable table = (HTable) pool.getTable("t1");
//        HTable table = new HTable(config, "t1");
//        System.out.println(table);
//        System.out.println( " 22222!");
//        try {
//            ResultScanner rs = table.getScanner(new Scan());
//            System.out.println( " 33333!");
//            for (Result r : rs) {
//                System.out.println(" get rowkey:" + new String(r.getRow()));
//                for (KeyValue keyValue : r.raw()) {
//                    System.out.println("column:" + new String(keyValue.getFamily())
//                            + "====value:" + new String(keyValue.getValue()));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("query ok");
//    }
//
//
//    public static void createTable() throws IOException {
//        Configuration config = HBaseConfiguration.create();
//        config.set("hbase.zookeeper.quorum", "192.168.0.89");
//        System.out.println( " 1111!");
//
//        HBaseAdmin admin = new HBaseAdmin(config);
//        System.out.println( " 22222!");
//
//        HTableDescriptor tableDescriptor = new HTableDescriptor("birds");
//        tableDescriptor.addFamily(new HColumnDescriptor("column1"));
//        tableDescriptor.addFamily(new HColumnDescriptor("column2"));
//        tableDescriptor.addFamily(new HColumnDescriptor("column3"));
//        admin.createTable(tableDescriptor);
//        System.out.println( "createTable  ok");
//    }
//


}
