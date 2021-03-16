package util;

import exception.EntityException;

import javax.persistence.Entity;


public class Annotation {
    //TODO change to BeanValidation with annotation
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