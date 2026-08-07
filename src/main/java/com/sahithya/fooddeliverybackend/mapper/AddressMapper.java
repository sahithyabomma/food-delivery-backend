package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.request.RegisterAddressRequest;
import com.sahithya.fooddeliverybackend.dto.response.AddressResponse;
import com.sahithya.fooddeliverybackend.entity.Address;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddressMapper {

    public Address toEntity(RegisterAddressRequest request) {
        return new Address(
                UUID.randomUUID(),
                request.getLocality().trim(),
                request.getCity().trim(),
                request.getState().trim(),
                request.getPincode().trim(),
                request.getLatitude(),
                request.getLongitude()
        );
    }

    public AddressResponse toResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getLocality(),
                address.getCity(),
                address.getState(),
                address.getPincode(),
                address.getLatitude(),
                address.getLongitude()
        );
    }
}