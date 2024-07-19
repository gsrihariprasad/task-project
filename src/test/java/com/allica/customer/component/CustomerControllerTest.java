package com.allica.customer.component;

import com.allica.customer.TestUtils;
import com.allica.customer.controller.CustomerController;
import com.allica.customer.dto.Customer;
import com.allica.customer.entity.CustomerEntity;
import com.allica.customer.mapper.CustomerMapper;
import com.allica.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CustomerControllerTest {

    @Autowired
    CustomerController controller;

    @MockBean
    CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<CustomerEntity> captor;

    @Test
    @DirtiesContext
    public void createCustomer_whenValidCustomerDetails_thenSavedInDb(){

        Customer customer = TestUtils.getCustomer("customer_request.json");
        CustomerEntity entity = CustomerMapper.INSTANCE.toEntity(customer);
        Mono<ResponseEntity<Customer>> customerMono = controller.createCustomer(customer, "101");
        Mockito.when(customerRepository.save(captor.capture())).thenReturn(entity);
        StepVerifier.create(customerMono)
                .assertNext(responseEntity -> {
                   Customer customer1 = responseEntity.getBody();
                   assertEquals(HttpStatus.CREATED ,responseEntity.getStatusCode());
                    assertNotNull(customer1);
                    assertNotNull(customer1.getDateOfBirth());
                    assertNotNull(customer1.getFirstName());
                    assertNotNull(customer1.getLastName());
                })
                .verifyComplete();
        verify(customerRepository, times(1)).save(any());
        verify(customerRepository, times(0)).findAll();
        verify(customerRepository, times(0)).findById(anyLong());
    }

    //@Test
    public void getCustomersById_whenGivenCustomerIdDetails_returnResult(){

        CustomerEntity entity = TestUtils.getCustomerEntity("customer_entity.json");
        Mockito.when(customerRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        Mono<ResponseEntity<Customer>> customerMono = controller.getCustomersById(10l);

        StepVerifier.create(customerMono)
                .verifyComplete();
        verify(customerRepository, times(0)).save(any());
        verify(customerRepository, times(0)).findAll();
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    @DirtiesContext
    public void createCustomer_getAllCustomerDetails_returnResult(){

        CustomerEntity entity = TestUtils.getCustomerEntity("customer_entity.json");
        Flux<Customer> customers = controller.getAllCustomers();
        Mockito.when(customerRepository.findAll()).thenReturn(List.of(entity));
        StepVerifier.create(customers)
                .verifyComplete();
        verify(customerRepository, times(0)).save(any());
        verify(customerRepository, times(1)).findAll();
        verify(customerRepository, times(0)).findById(anyLong());
    }


    @Test
    @DirtiesContext
    public void createCustomer_whenFirstNameIsEmpty_thenValidationFails(){

        Customer customer = TestUtils.getCustomer("customer_failed_request.json");
        CustomerEntity entity = CustomerMapper.INSTANCE.toEntity(customer);
        Mockito.when(customerRepository.save(captor.capture())).thenReturn(entity);

        try {
            Mono<ResponseEntity<Customer>> customerMono = controller.createCustomer(customer, "101");
            StepVerifier.create(customerMono)
                    .verifyError();
        } catch (Exception exp){
            exp.printStackTrace();
        }
        verify(customerRepository, times(0)).save(any());
        verify(customerRepository, times(0)).findAll();
        verify(customerRepository, times(0)).findById(anyLong());
    }
}
