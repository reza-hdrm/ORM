package util;

import annotation.Entity;
import exception.EntityException;

public class Annotation {
    public static void entityAnnotationDeclared(Object object) {
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null)
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
    }
}