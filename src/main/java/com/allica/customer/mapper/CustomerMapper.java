package com.allica.customer.mapper;

import com.allica.customer.dto.Customer;
import com.allica.customer.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    // Mapping from DTO to Entity
    //@Mapping(target = "uid", ignore = true)
    CustomerEntity toEntity(Customer customer);

    // Mapping from Entity to DTO
    Customer toDto(CustomerEntity customerEntity);
}
