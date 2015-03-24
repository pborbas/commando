package org.commando.sample.billing.repo;

import org.commando.sample.billing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
