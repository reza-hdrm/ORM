package orm;

import util.Annotation;

import javax.persistence.*;
import java.lang.reflect.Field;


public class QueryBuilder {
    private static final boolean IS_SHOW_SQL = Config.getDBShowSql();

    private QueryBuilder() {

    }

    public static String getInsertQuery(Object object) {
        Annotation.entityAnnotationDeclared(object.getClass());

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

    public static String getUpdateQuery(Class<?> clazz) {
        Annotation.entityAnnotationDeclared(clazz);
        Table table = clazz.getDeclaredAnnotation(Table.class);
        StringBuilder query = new StringBuilder("UPDATE " + table.name() + " SET ");
        Field[] fields = clazz.getDeclaredFields();
        Object oid = null;
        String idColumn = "";
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                try {
                    oid = field.get(clazz);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                idColumn = column.name();
            }
            if (column != null) {
                try {
                    if (id == null)
                        if (field.getType().getSimpleName().endsWith("String"))
                            query.append(column.name()).append("='").append(field.get(clazz)).append("',");
                        else
                            query.append(column.name()).append("=").append(field.get(clazz)).append(",");
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

    public static String getSelectQuery(Class<?> clazz, Object id) {
        Table table = clazz.getClass().getDeclaredAnnotation(Table.class);
        Annotation.entityAnnotationDeclared(clazz);
        Field[] fields = clazz.getClass().getDeclaredFields();
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

    public static String getDeleteQuery(Class<?> clazz) {
        Table table = clazz.getDeclaredAnnotation(Table.class);
        Annotation.entityAnnotationDeclared(clazz);
        String query = "DELETE FROM " + table.name() + " WHERE ";
        Field[] fields = clazz.getDeclaredFields();
        Object oid = null;
        String idColumn = "";
        for (Field field : fields) {
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                Column column = field.getAnnotation(Column.class);
                try {
                    oid = field.get(clazz);
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

    public static String getSelectAllQuery(Class<?> clazz) {
        Annotation.entityAnnotationDeclared(clazz);
        Table table = clazz.getDeclaredAnnotation(Table.class);
        String tableName = table.name();
        String query = "SELECT * FROM " + tableName;
        System.out.println(query);
        return query;
    }

    public static String getCreateTableQuery(Class<?> clazz) {
        Table table = clazz.getDeclaredAnnotation(Table.class);
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table.name() + " (");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            java.lang.annotation.Annotation[] annotations = field.getDeclaredAnnotations();
            for (java.lang.annotation.Annotation annotation : annotations)
                if (annotation instanceof Column) {
                    Column column = field.getAnnotation(Column.class);
                    query.append(column.name()).append(" ").append(
                            field.getType().getSimpleName().equals("String") ? "VARCHAR" : field.getType().getSimpleName()
                    ).append("(").append(column.length()).append(")");
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
        if (IS_SHOW_SQL)
            System.out.println(query);
    }

}
