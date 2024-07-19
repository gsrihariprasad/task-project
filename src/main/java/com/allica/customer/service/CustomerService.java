package com.allica.customer.service;

import com.allica.customer.dto.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {


     Mono<Customer> createCustomer(Customer customer);

     Flux<Customer> getAllCustomers();

     Mono<Customer> getCustomer(Long id);
}
