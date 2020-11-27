package com.tenpines.advancetdd;

public class SystemEnvironment {


    public CustomerSystem getSystem() {

        if(System.getenv("ENV").equals("Development")){
            return new TransientCustomerSystem();
        }
        if(System.getenv("ENV").equals("Prod")){
            return new PersistentCustomerSystem();
        }
        else
            return null;
    }
}
