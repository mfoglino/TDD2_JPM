package com.tenpines.advancetdd;

import java.util.List;

public interface CustomerSystem {
    void start();

    void closeDB();

    void addCustomer(Customer customer);

    List getAllCustomers();

    Customer searchCustomer(String id, String type);

    List getAllAddresses();
}
