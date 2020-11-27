package com.tenpines.advancetdd;

import java.util.ArrayList;
import java.util.List;

public class TransientCustomerSystem implements CustomerSystem {


    List<Customer> customers;

    @Override
    public void start() {
        this.customers = new ArrayList<Customer>();

    }

    @Override
    public void closeDB() {

    }

    @Override
    public void addCustomer(Customer customer) {

        this.customers.add(customer);

    }

    @Override
    public List getAllCustomers() {
        return this.customers;
    }

    @Override
    public Customer searchCustomer(String id, String type) {
        return this.customers.stream().filter(c-> c.getIdentificationNumber().equals(id)).findFirst().get();
    }

    @Override
    public List getAllAddresses() {
        //this.customers.
        return null;
    }
}
