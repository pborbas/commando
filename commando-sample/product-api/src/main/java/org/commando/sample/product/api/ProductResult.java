package org.commando.sample.product.api;

import org.commando.result.AbstractSimpleResult;
import org.commando.sample.product.model.Product;

/**
 *
 */
public class ProductResult extends AbstractSimpleResult<Product> {

	private ProductResult() {
	}

	public ProductResult(String commandId, Product value) {
		super(commandId, value);
	}
}
