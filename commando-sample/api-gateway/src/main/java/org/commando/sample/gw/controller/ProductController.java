package org.commando.sample.gw.controller;

import org.commando.exception.DispatchException;
import org.commando.sample.product.api.GetProductCommand;
import org.commando.sample.product.dispatcher.ProductDispatcher;
import org.commando.sample.product.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductDispatcher productDispatcher;

	public ProductController(ProductDispatcher productDispatcher) {
		this.productDispatcher = productDispatcher;
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable Long productId) throws DispatchException {
        return this.productDispatcher.dispatchSync(new GetProductCommand(productId)).getValue();
    }


}
