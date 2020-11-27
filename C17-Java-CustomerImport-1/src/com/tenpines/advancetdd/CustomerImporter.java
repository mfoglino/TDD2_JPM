package com.tenpines.advancetdd;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

class CustomerImporter {
    public static final String REGISTRO_DE_CUSTOMER_INVALIDO = "Registro Invalido. Cantidad de campos inadecuada.";
    public static final String REGISTRO_DE_ADDRESS_INVALIDO = "Error. Direccion con menos campos de los esperados";
    static final String ENCABEZADO_REGISTRO_CLIENTE_INVALIDO = "Encabezado Cliente Invalido";
    public static final String NO_EXISTE_CLIENTE_PARA_ASIGNAR_ESTA_DIRECCION = "No existe cliente para asignar esta direccion.";
    private Reader reader;
    private CustomerSystem customerSystem;
    private String line;
    private Customer newCustomer;
    private String[] record;

    public CustomerImporter(Reader reader, CustomerSystem customerSystem) {
        this.reader = reader;
        this.customerSystem = customerSystem;
    }

    public void execute() throws IOException {
        LineNumberReader lineReader = new LineNumberReader(reader);

        line = lineReader.readLine();
        while (line !=null) {
            createRecord();
            importRecord();

            line = lineReader.readLine();
        }

        reader.close();
    }

    private void importRecord() {
        if (recordIsACustomer()){
            importCustomer();
        }
        else if (recordIsAnAddress()) {
            importAddress();
        }
        else {
            throw new RuntimeException(ENCABEZADO_REGISTRO_CLIENTE_INVALIDO);
        }
    }

    private void createRecord() {
        record = line.split(",");
    }

    private void importCustomer() {
        if(record.length != 5)
            throw new IndexOutOfBoundsException(REGISTRO_DE_CUSTOMER_INVALIDO);

        newCustomer = new Customer();
        newCustomer.setFirstName(record[1]);
        newCustomer.setLastName(record[2]);
        newCustomer.setIdentificationType(record[3]);
        newCustomer.setIdentificationNumber(record[4]);
        customerSystem.addCustomer(newCustomer);
    }

    private void importAddress() {
        if(record.length != 6)
            throw new IndexOutOfBoundsException(REGISTRO_DE_ADDRESS_INVALIDO);

        if(newCustomer == null)
            throw new RuntimeException(NO_EXISTE_CLIENTE_PARA_ASIGNAR_ESTA_DIRECCION);

        Address newAddress = new Address();
        newCustomer.addAddress(newAddress);
        newAddress.setStreetName(record[1]);
        newAddress.setStreetNumber(Integer.parseInt(record[2]));
        newAddress.setTown(record[3]);
        newAddress.setZipCode(Integer.parseInt(record[4]));
        newAddress.setProvince(record[5]);
    }




    private boolean recordIsAnAddress() {
        return line.startsWith("A");
    }

    private boolean recordIsACustomer() {
        return line.startsWith("C");
    }
}
