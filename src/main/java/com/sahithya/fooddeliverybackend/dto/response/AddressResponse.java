package com.sahithya.fooddeliverybackend.dto.response;

import java.util.UUID;

public class AddressResponse {

    private final UUID id;
    private final String locality;
    private final String city;
    private final String state;
    private final String pincode;
    private final Double latitude;
    private final Double longitude;

    public AddressResponse(
            UUID id,
            String locality,
            String city,
            String state,
            String pincode,
            Double latitude,
            Double longitude
    ) {
        this.id = id;
        this.locality = locality;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UUID getId() {
        return id;
    }

    public String getLocality() {
        return locality;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}