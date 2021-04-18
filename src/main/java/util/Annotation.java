package util;

import exception.EntityException;

import javax.persistence.Entity;
import java.util.Objects;


public class Annotation {
    private Annotation(){

    }
    //TODO change to BeanValidation with annotation
    public static void entityAnnotationDeclared(Class<?> clazz) {
        if (Objects.isNull(clazz.getDeclaredAnnotation(Entity.class)))
            try {
                throw new EntityException(clazz);
            } catch (EntityException e) {
                e.printStackTrace();
            }
    }
}