package orm;

import annotation.Table;
import database.DBConnection;
import entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
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
            Assert.assertEquals("Table is exits :", isTableExits, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}