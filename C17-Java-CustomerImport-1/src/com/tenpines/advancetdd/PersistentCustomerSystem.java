package com.tenpines.advancetdd;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.List;

public class PersistentCustomerSystem implements CustomerSystem {
    public Session session;

    public PersistentCustomerSystem() {
    }

    @Override
    public void start() {
        Configuration configuration = new Configuration();
        configuration.configure();

        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Override
    public void closeDB() {
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void addCustomer(Customer customer) {
        session.persist(customer);
    }

    @Override
    public List getAllCustomers() {
        return session.createCriteria(Customer.class).list();
    }

    @Override
    public Customer searchCustomer(String id, String type) {
        Criteria crit = session.createCriteria(Customer.class);
        crit.add(Restrictions.eq("identificationNumber", id));
        crit.add(Restrictions.eq("identificationType", type));
        List<Customer> results = crit.list();

        return results.get(0);
    }

    @Override
    public List getAllAddresses() {
        return session.createCriteria(Address.class).list();
    }
}