package orm;

import util.Annotation;

import javax.persistence.*;
import java.lang.reflect.Field;


public class QueryBuilder {
    private static boolean isShowSql = Config.getDBShowSql();

    public static String getInsertQuery(Object object) {
        Annotation.entityAnnotationDeclared(object);

        StringBuilder query = new StringBuilder("INSERT INTO ");
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        query.append(table.name()).append("(");
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column != null)
                query.append(column.name()).append(",");
        }
        query = clearCommon(query);

        query.append(") VALUES (");
        for (Field field : fields) {
            try {
                if (field.getType().getSimpleName().endsWith("String"))
                    query.append("'").append(field.get(object)).append("' ,");
                else
                    query.append(field.get(object)).append(",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        query = clearCommon(query);
        query.append(")");

        showSqlStatement(query);

        return query.toString();
    }

    private static StringBuilder clearCommon(StringBuilder query) {
        if (query.toString().trim().endsWith(","))
            query = new StringBuilder(query.substring(0, query.length() - 1));
        return query;
    }

    public static String getUpdateQuery(Object object) {
        Annotation.entityAnnotationDeclared(object);
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        StringBuilder query = new StringBuilder("UPDATE " + table.name() + " SET ");
        Field[] fields = object.getClass().getDeclaredFields();
        Object oid = null;
        String idColumn = "";
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                try {
                    oid = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                idColumn = column.name();
            }
            if (column != null) {
                try {
                    if (id == null)
                        if (field.getType().getSimpleName().endsWith("String"))
                            query.append(column.name()).append("='").append(field.get(object)).append("',");
                        else
                            query.append(column.name()).append("=").append(field.get(object)).append(",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        query = clearCommon(query);
        query.append(" WHERE ").append(idColumn).append("=").append(oid);
        showSqlStatement(query);
        return query.toString();
    }

    public static String getSelectQuery(Object object, Object id) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        Annotation.entityAnnotationDeclared(object);
        Field[] fields = object.getClass().getDeclaredFields();
        String tableName = table.name();
        String idColumnName = "";
        for (Field field : fields) {
            Id idAnnotation = field.getDeclaredAnnotation(Id.class);
            if (idAnnotation != null) {
                Column column = field.getDeclaredAnnotation(Column.class);
                idColumnName = column.name();
            }
        }
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(tableName).append(" WHERE ").append(idColumnName).append(" = ").append(id);
        showSqlStatement(query);
        return query.toString();
    }

    public static String getDeleteQuery(Object object) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        Annotation.entityAnnotationDeclared(object);
        String query = "DELETE FROM " + table.name() + " WHERE ";
        Field[] fields = object.getClass().getDeclaredFields();
        Object oid = null;
        String idColumn = "";
        for (Field field : fields) {
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                Column column = field.getAnnotation(Column.class);
                try {
                    oid = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                idColumn = column.name();
            }
        }
        query += idColumn + " = " + oid;
        System.out.println(query);
        return query;
    }

    public static String getSelectAllQuery(Object object) {
        Annotation.entityAnnotationDeclared(object);
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        String tableName = table.name();
        String query = "SELECT * FROM " + tableName;
        System.out.println(query);
        return query;
    }

    public static String getCreateTableQuery(Object object) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table.name() + " (");
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            java.lang.annotation.Annotation[] annotations = field.getDeclaredAnnotations();
            for (java.lang.annotation.Annotation annotation : annotations)
                if (annotation instanceof Column) {
                    Column column = field.getAnnotation(Column.class);
                    query.append(column.name()).append(" ").append(field.getType().getSimpleName().equals("String")?"VARCHAR":field.getType().getSimpleName()).append("(").append(column.length()).append(")");
                    GeneratedValue id = field.getDeclaredAnnotation(GeneratedValue.class);
                    if (id != null)
                        if (id.strategy().equals(GenerationType.AUTO))
                            query.append(" NOT NULL PRIMARY KEY AUTO_INCREMENT");
                        else
                            query.append(" NOT NULL PRIMARY KEY");
                    query.append(",");
                }
        }
        query = clearCommon(query);
        query.append(");");
        showSqlStatement(query);
        return query.toString();
    }

    private static void showSqlStatement(StringBuilder query) {
        if (isShowSql)
            System.out.println(query);
    }

}
