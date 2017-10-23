package org.commando.sample.product.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.action.AbstractAction;
import org.commando.exception.DispatchException;
import org.commando.sample.product.api.GetProductCommand;
import org.commando.sample.product.api.ProductResult;
import org.commando.sample.product.model.Product;
import org.commando.sample.product.repo.ProductRepository;
import org.commando.spring.core.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@DispatchAction
public class GetProductAction extends AbstractAction<GetProductCommand, ProductResult> {

	private final Log LOG = LogFactory.getLog(GetProductAction.class);

	private final ProductRepository productRepository;


	@Autowired
	public GetProductAction(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	@Transactional
	public ProductResult execute(final GetProductCommand getProductCommand) throws DispatchException {
		Product product = new Product(getProductCommand.getProductId(), "Microservices with Commando", BigDecimal.TEN, 5);
//		Product product = productRepository.findOne(getProductCommand.getProductId());
		return new ProductResult(getProductCommand.getCommandId(), product);
	}

}
