package org.commando.sample.product.api;

import org.commando.command.AbstractCommand;

/**
 *
 */
public class GetProductCommand extends AbstractCommand<ProductResult> {
	private final Long productId;

	public GetProductCommand(Long productId) {
		this.productId = productId;
	}

	public Long getProductId() {
		return productId;
	}
}
