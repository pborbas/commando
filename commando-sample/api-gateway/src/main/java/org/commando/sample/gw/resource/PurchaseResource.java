package org.commando.sample.gw.resource;

import org.commando.sample.product.model.Product;
import org.commando.sample.customer.api.model.Customer;

import java.math.BigDecimal;

/**
 *
 */
public class PurchaseResource {
	private Customer customer;
	private Product product;
	private Integer quantity;
	private BigDecimal price;

	private PurchaseResource() {
	}

	public PurchaseResource(Customer customer, Product product, Integer quantity, BigDecimal price) {
		this.customer = customer;
		this.product = product;
		this.quantity = quantity;
		this.price = price;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Product getProduct() {
		return product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public PurchaseResource setCustomer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public PurchaseResource setProduct(Product product) {
		this.product = product;
		return this;
	}

	public PurchaseResource setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	public PurchaseResource setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}
}
