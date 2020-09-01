package com.erc.integration;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.ibm.db2.jcc.DB2Driver");
                settings.put(Environment.URL, "jdbc:db2://192.168.1.123:50000/MINDRAYDBT");
                settings.put(Environment.USER, "mindray");
                settings.put(Environment.PASS, "dbtest");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.DB2Dialect");
                settings.put(Environment.SHOW_SQL, true);
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(MindrayMessage.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}