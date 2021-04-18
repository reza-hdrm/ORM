import engine.EntityManagerFactoryImpl;

import javax.persistence.EntityManagerFactory;

public class Persistence {
    private Persistence() {

    }

    public static EntityManagerFactory createEntityManagerFactory(String appProperties) {
        return new EntityManagerFactoryImpl();
    }
}
