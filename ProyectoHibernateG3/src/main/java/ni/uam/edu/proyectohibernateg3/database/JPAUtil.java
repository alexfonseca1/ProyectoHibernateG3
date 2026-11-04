package ni.uam.edu.proyectohibernateg3.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAUtil {

    private static EntityManagerFactory entityManagerFactory;

    public static synchronized void iniciar() {
        if (entityManagerFactory == null
                || !entityManagerFactory.isOpen()) {

            String password = System.getenv("BD_PASSWORD");

            if (password == null || password.isEmpty()) {
                throw new RuntimeException(
                        "La variable de entorno BD_PASSWORD está vacía."
                );
            }

            Map<String, Object> properties = new HashMap<>();
            properties.put(
                    "jakarta.persistence.jdbc.password",
                    password
            );

            entityManagerFactory =
                    Persistence.createEntityManagerFactory(
                            "bibliotecaPU",
                            properties
                    );
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        iniciar();
        return entityManagerFactory;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static synchronized void cerrar() {
        if (entityManagerFactory != null) {
            if (entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }
            entityManagerFactory = null;
        }
    }
}