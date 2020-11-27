package com.tenpines.advancetdd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.tenpines.advancetdd.CustomerImporter.REGISTRO_DE_ADDRESS_INVALIDO;
import static com.tenpines.advancetdd.CustomerImporter.REGISTRO_DE_CUSTOMER_INVALIDO;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerImporterTest {

    //private final PersistentCustomerSystem system = new PersistentCustomerSystem();
    //private final TransientCustomerSystem system = new TransientCustomerSystem();

    private final CustomerSystem system = new SystemEnvironment().getSystem();





    @AfterEach
    public void tearDown() {
        system.closeDB();
    }

    @BeforeEach
    public void setUp() {
        system.start();
    }

    @org.junit.jupiter.api.Test
    void ImportingPepeRecordsTest() throws IOException {


        new CustomerImporter(readData(), system).execute();


        List<Customer> customers = system.getAllCustomers();

        assertEquals(2, customers.size());


        Customer customer = system.searchCustomer("22333444", "D");

        verifyCustomerIsCorrect(customer, "Pepe", "Sanchez", "D", "22333444");


        //List<Address> addresses = system.getAllAddresses();
        //assertEquals(addresses.size(), 3);



        ///// address
        Set<Address> direcciones = customer.getAddresses();
        assertEquals(direcciones.size(), 2);

        Optional<Address> maybeAddress = customer.addressAt("Maipu");
        Address a = maybeAddress.get();
        VerifyAddressIsCorrect(a);
    }

    private void VerifyAddressIsCorrect(Address a) {
        assertEquals(a.getStreetName(), "Maipu");
        assertEquals(a.getTown(), "Florida");
        assertEquals(a.getProvince(), "Buenos Aires");
        assertEquals(a.getStreetNumber(), 888);
        assertEquals(a.getZipCode(), 1122);
    }

    private void verifyCustomerIsCorrect(Customer customer, String pepe, String sanchez, String d, String s) {
        assertEquals(customer.getFirstName(), pepe);
        assertEquals(customer.getLastName(), sanchez);
        assertEquals(customer.getIdentificationType(), d);
        assertEquals(customer.getIdentificationNumber(), s);
    }

    @org.junit.jupiter.api.Test
    void ImportingJuanRecords() throws IOException {

        new CustomerImporter(readData(), system).execute();
        List<Customer> customers = system.getAllCustomers();
        assertEquals(2, customers.size());


        Customer customer = system.searchCustomer("23-25666777-9", "C");

        verifyCustomerIsCorrect(customer, "Juan", "Perez", "C", "23-25666777-9");

        Set<Address> direcciones = customer.getAddresses();
        assertEquals(direcciones.size(), 1);

    }

    @org.junit.jupiter.api.Test
    void ImportingAnCustomerRegisterWithLessColumnsThanExpectedTest() throws IOException {

        StringReader reader = new StringReader(
                "C,Pepe,Sanchez,D");

        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> new CustomerImporter(reader, system).execute());
        assertEquals(REGISTRO_DE_CUSTOMER_INVALIDO, exception.getMessage());

        List<Customer> customers = system.getAllCustomers();
        assertEquals(0, customers.size());


        reader.close();
    }

    @org.junit.jupiter.api.Test
    void ImportingAnCustomerRegisterWithMoreColumnsThanExpectedTest() throws IOException {

        StringReader reader = new StringReader(
                "C,Pepe,Sanchez,D,22333444,UnaYapa\n");

        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> new CustomerImporter(reader, system).execute());
        assertEquals(REGISTRO_DE_CUSTOMER_INVALIDO, exception.getMessage());

        reader.close();
    }

    @org.junit.jupiter.api.Test
    void ImportingAnAddressRegisterWithLessColumnsThanExpectedTest() throws IOException {

        StringReader readerWithBadAddress = new StringReader(
                "C,Pepe,Sanchez,D,22333444\n" +
                        "A,San Martin,3322,Olivos,1636");

        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> new CustomerImporter(readerWithBadAddress, system).execute());
        assertEquals(REGISTRO_DE_ADDRESS_INVALIDO, exception.getMessage());


        readerWithBadAddress.close();
    }

    @org.junit.jupiter.api.Test
    void ImportingAnAddressRegisterWithMoreColumnsThanExpectedTest() throws IOException {

        StringReader readerWithBadAddress = new StringReader(
                "C,Pepe,Sanchez,D,22333444\n" +
                        "A,San Martin,3322,Olivos,1636,Roca,otraYapa");

        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> new CustomerImporter(readerWithBadAddress, system).execute());
        assertEquals(REGISTRO_DE_ADDRESS_INVALIDO, exception.getMessage());

        readerWithBadAddress.close();
    }


    @org.junit.jupiter.api.Test
    void NingunClienteEsImportadoCuandoElEncabezadoEsInadecuadoTest() throws IOException {

        StringReader reader = new StringReader(
                "H,Pepe,Sanchez,D,22333444\n");

        Throwable exception = assertThrows(Exception.class, () -> new CustomerImporter(reader, system).execute());
        assertEquals(CustomerImporter.ENCABEZADO_REGISTRO_CLIENTE_INVALIDO, exception.getMessage());

        List<Customer> customers = system.getAllCustomers();
        assertEquals(0, customers.size());

        reader.close();
    }

    @org.junit.jupiter.api.Test
    void NoSePuedeImportarUnaAddressSiNoExisteClienteTest() throws IOException {

        StringReader reader = new StringReader(
                "A,San Martin,3322,Olivos,1636,BsAs");

        Throwable exception = assertThrows(Exception.class, () -> new CustomerImporter(reader, system).execute());
        assertEquals(CustomerImporter.NO_EXISTE_CLIENTE_PARA_ASIGNAR_ESTA_DIRECCION, exception.getMessage());

        //List<Address> addresses = system.getAllAddresses();
        //assertEquals(addresses.size(), 0);

        reader.close();
    }


    private StringReader readData() throws FileNotFoundException {


        StringReader reader = new StringReader("C,Pepe,Sanchez,D,22333444\n" +
                "A,San Martin,3322,Olivos,1636,BsAs\n" +
                "A,Maipu,888,Florida,1122,Buenos Aires\n" +
                "C,Juan,Perez,C,23-25666777-9\n" +
                "A,Alem,1122,CABA,1001,CABA");

        return reader;
    }

}