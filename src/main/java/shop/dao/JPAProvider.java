package shop.dao;

import javax.persistence.*;

public class JPAProvider {

    private static final String PERSISTENCE_UNIT = "shop-unit";
    private static final EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
