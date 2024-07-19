package com.allica.customer.service.impl;

import com.allica.customer.dto.Customer;
import com.allica.customer.mapper.CustomerMapper;
import com.allica.customer.repository.CustomerRepository;
import com.allica.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    public Mono<Customer> createCustomer(Customer customer) {
        return Mono.just(customer)
                .map(CustomerMapper.INSTANCE::toEntity)
                .doOnNext(customerEntity ->
                        logger.info("conversion from customerdto to customerentity using mapstruct completed. {} ", customerEntity.getFirstName()))
                .map(customerRepository::save)
                .doOnNext(customerEntity ->
                        logger.info("customer {} successfully saved in database.", customerEntity.getUid()))
                .map(response -> customer)
                .onErrorResume(throwable -> {
                    logger.error("exception occurred while mapping or saving customer ", throwable);
                    return Mono.empty();
        });
    }

    public Flux<Customer> getAllCustomers() {
        return Flux.fromIterable(customerRepository.findAll())
                .doOnNext(customerEntity -> logger.info("Retrieved customer details customerId {} ", customerEntity.getUid()))
                .map(CustomerMapper.INSTANCE::toDto);
    }

    public Mono<Customer> getCustomer(Long id) {
        return Mono.just(customerRepository.findById(id))
                .map(Optional::get)
                .filter(customer -> {
                    if(ObjectUtils.isEmpty(customer) && !StringUtils.hasText(customer.getFirstName())){
                        throw new RuntimeException("customer not available "+ id);
                    }
                    return true;
                })
                .map(CustomerMapper.INSTANCE::toDto)
                .onErrorResume(throwable -> {
                    String exp_msg = String.format("exception occurred while reading customer {} data from database", id);
                    logger.error(exp_msg);
                    throw new RuntimeException(exp_msg);
                }).switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("there is customer available for "+ id))));
    }
}
