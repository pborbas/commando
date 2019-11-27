package org.commando.sample.gw.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.exception.DispatchException;
import org.commando.sample.customer.api.command.CustomerResult;
import org.commando.sample.customer.api.command.GetCustomerCommand;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.gw.resource.PurchaseResource;
import org.commando.sample.product.api.GetProductCommand;
import org.commando.sample.product.api.ProductResult;
import org.commando.sample.product.dispatcher.ProductDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

	private static final Log LOG = LogFactory.getLog(PurchaseController.class);

    private final CustomerDispatcher customerDispatcher;
    private final ProductDispatcher productDispatcher;

	@Autowired
	public PurchaseController(CustomerDispatcher customerDispatcher, ProductDispatcher productDispatcher) {
		this.customerDispatcher = customerDispatcher;
		this.productDispatcher = productDispatcher;
	}

	/**
	 * curl -X POST -H "Content-Type: application/json" -d '{"customer" : {"customerId":1}, "product" : {"productId":1}, "quantity" : 2}' "http://localhost:8880/purchases"
	 */

	@RequestMapping(method = RequestMethod.POST)
	public Mono<PurchaseResource> createPurchase(@RequestBody PurchaseResource purchaseResource) throws DispatchException {
		Mono<CustomerResult> customerResultMono = this.customerDispatcher
				.dispatch(new GetCustomerCommand(purchaseResource.getCustomer().getCustomerId()));
		Mono<ProductResult> productResultMono = Mono.fromFuture(this.productDispatcher
				.dispatch(new GetProductCommand(purchaseResource.getProduct().getProductId())));
		return Mono.zip(customerResultMono, productResultMono, (customerResult, productResult) -> {
			purchaseResource.setCustomer(customerResult.getValue());
			purchaseResource.setProduct(productResult.getValue());
			purchaseResource.setPrice(productResult.getValue().getPrice().multiply(BigDecimal.valueOf(purchaseResource.getQuantity())));
			return purchaseResource;
		});
	}
}
