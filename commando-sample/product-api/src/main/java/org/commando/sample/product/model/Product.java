package org.commando.sample.product.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long productId;
    String title;
    BigDecimal price;
    Integer stock;

	public Product() {
	}

	public Product(Long productId, String title, BigDecimal price, Integer stock) {
		this.productId = productId;
		this.title = title;
		this.price = price;
		this.stock = stock;
	}

	public Long getProductId() {
		return productId;
	}

	public Product setProductId(Long productId) {
		this.productId = productId;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Product setTitle(String title) {
		this.title = title;
		return this;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Product setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}

	public Integer getStock() {
		return stock;
	}

	public Product setStock(Integer stock) {
		this.stock = stock;
		return this;
	}
}
