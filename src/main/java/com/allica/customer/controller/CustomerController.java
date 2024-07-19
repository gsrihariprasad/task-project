package com.allica.customer.controller;

import com.allica.customer.dto.Customer;
import com.allica.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public Mono<ResponseEntity<Customer>> createCustomer(@RequestBody  @Valid Customer customer,
                                                         @RequestHeader("correlationId") String correlationId) {
        logger.info("createCustomer() Received customer detail correlationId {} ",correlationId);
        return customerService.createCustomer(customer)
                .doOnNext(s->  logger.info("createCustomer() Received customer detail correlationId {}", correlationId))
                .map(customerResponse-> ResponseEntity.status(HttpStatus.CREATED).body(customerResponse))
                .onErrorResume(throwable -> {
                    logger.error("exception occurred while saving customer details ", throwable);
                    return Mono.error(new RuntimeException(throwable.getMessage()));
                });
    }

    @GetMapping
    public Flux<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Customer>> getCustomersById(@PathVariable Long id) {
        logger.info("getCustomersById {} ", id);
        return customerService.getCustomer(id)
                .map(customer -> ResponseEntity.status(HttpStatus.OK).body(customer));
    }
}
