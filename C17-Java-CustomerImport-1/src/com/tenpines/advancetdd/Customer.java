package com.tenpines.advancetdd;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table( name = "CUSTOMERS" )
public class Customer {

	@Id
	@GeneratedValue
	private long id;
	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@Pattern(regexp="D|C")
	private String identificationType;

	@NotEmpty
	private String identificationNumber;

	@OneToMany(cascade = CascadeType.ALL)
	@OrderColumn(name = "streetName")
	private Set<Address> addresses;
	
	public Customer()
	{
		addresses = new HashSet<Address>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public void addAddress(Address anAddress){
		addresses.add(anAddress);
	}

	public Set<Address> getAddresses(){
		return (Set<Address>) this.addresses;
	}

	public Optional<Address> addressAt(String aStreetName) {
		return this.addresses.stream().filter(anAddress -> anAddress.isAt(aStreetName))
				.findFirst();
	}
}
