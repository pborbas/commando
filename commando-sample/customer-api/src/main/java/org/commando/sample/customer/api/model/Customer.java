package org.commando.sample.customer.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer {

    @Id
	@GeneratedValue
    Long customerId;

    String name;

    private Customer() {
    }

	public Customer(String name) {
		this.name = name;
	}

	public Customer(final long customerId, final String name) {
        this.name=name;
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(final Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
