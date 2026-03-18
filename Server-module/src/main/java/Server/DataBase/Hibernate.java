package Server.DataBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Hibernate {
    private static final EntityManagerFactory emf = buildEMF();

    @SuppressWarnings("CallToPrintStackTrace")
    private static EntityManagerFactory buildEMF(){
        try {
            return Persistence.createEntityManagerFactory("hibernate-jpa-unit");
        }catch (Exception ex){
            ex.printStackTrace();
            throw new ExceptionInInitializerError("Couldn't connect");
        }
    }
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}
