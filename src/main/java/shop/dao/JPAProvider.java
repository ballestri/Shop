package shop.dao;

import javax.persistence.*;

public class JPAProvider {
    private static final EntityManagerFactory  entityManagerFactory;//instate of session for connect to database
    static{
        entityManagerFactory  = Persistence.createEntityManagerFactory("shop-unit");
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
