package orm;

import annotation.Column;
import annotation.Id;
import annotation.Table;
import exception.EntityException;
import util.Annotation;

import java.lang.reflect.Field;


public class QueryBuilder {
    //TODO Show query in application.property
    public static String getInsertQuery(Object object) {

        if (!Annotation.entityAnnotationDeclared(object))
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }

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
        if (query.toString().trim().endsWith(","))
            query = new StringBuilder(query.substring(0, query.length() - 1));
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
        if (query.toString().trim().endsWith(",")) {
            query = new StringBuilder(query.substring(0, query.length() - 1));
        }
        query.append(")");
        System.out.println(query);
        return query.toString();
    }

    public static String getUpdateQuery(Object object) {
        if (!Annotation.entityAnnotationDeclared(object))
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        String query = "UPDATE " + table.name() + " SET ";
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
                            query += column.name() + "='" + field.get(object) + "',";
                        else
                            query += column.name() + "=" + field.get(object) + ",";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (query.trim().endsWith(",")) {
            query = query.substring(0, query.length() - 1);
        }
        query += " WHERE " + idColumn + "=" + oid;
        System.out.println(query);
        return query;
    }

    public static String getSelectQuery(Object object, Object id) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        if (!Annotation.entityAnnotationDeclared(object))
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
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
        String query = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = " + id;
        System.out.println(query);
        return query;
    }

    public static String getDeleteQuery(Object object) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        if (!Annotation.entityAnnotationDeclared(object))
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
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
        if (!Annotation.entityAnnotationDeclared(object))
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        Field[] fields = object.getClass().getDeclaredFields();
        String tableName = table.name();
        String query = "SELECT * FROM " + tableName;
        System.out.println(query);
        return query;
    }
}
