package org.commando.sample.gw.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.exception.DispatchException;
import org.commando.result.ResultFuture;
import org.commando.sample.customer.api.command.CustomerResult;
import org.commando.sample.customer.api.command.GetCustomerCommand;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.customer.api.model.Customer;
import org.commando.sample.gw.resource.PurchaseResource;
import org.commando.sample.product.api.GetProductCommand;
import org.commando.sample.product.api.ProductResult;
import org.commando.sample.product.dispatcher.ProductDispatcher;
import org.commando.sample.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

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
	public PurchaseResource createPurchase(@RequestBody PurchaseResource purchaseResource) throws DispatchException {
		ResultFuture<CustomerResult> customerFuture = this.customerDispatcher
				.dispatch(new GetCustomerCommand(purchaseResource.getCustomer().getCustomerId()));
		ResultFuture<ProductResult> productFuture = this.productDispatcher
				.dispatch(new GetProductCommand(purchaseResource.getProduct().getProductId()));

		Customer customer = customerFuture.getResult().getValue();
		Product product = productFuture.getResult(3, TimeUnit.SECONDS).getValue();
		purchaseResource.setCustomer(customer);
		purchaseResource.setProduct(product);
		purchaseResource.setPrice(product.getPrice().multiply(BigDecimal.valueOf(purchaseResource.getQuantity())));
//		LOG.info("Purchase created");
		return purchaseResource;
	}
}
