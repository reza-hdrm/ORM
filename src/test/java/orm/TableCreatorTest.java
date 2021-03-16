package orm;


import database.DBConnection;
import orm.entity.Product;
import orm.entity.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.Table;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TableCreatorTest {

    @Test
    public void create() {
        TableCreator tableCreator = new TableCreator();
        tableCreator.create(new User());
        String tableName = "";
        boolean isTableExits = false;
        try {
            Object o = User.class.newInstance();
            Table table = o.getClass().getAnnotation(Table.class);
            tableName = table.name();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            ResultSet resultSet = DBConnection.getDBConnection().getConnection().prepareStatement("SHOW TABLES").executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(tableName))
                    isTableExits = true;
            }
            Assertions.assertEquals( isTableExits, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test1(){
        final String insertQuery = QueryBuilder.getCreateTableQuery(new Product());
        System.out.println(insertQuery);
    }
}