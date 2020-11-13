package util;

import annotation.Entity;
import exception.EntityException;

public class Annotation {
    //todo
    public static boolean entityAnnotationDeclared(Object object) {
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null)
            return false;
        return true;
    }
}