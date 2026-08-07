package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.RegisterRestaurantRequest;
import com.sahithya.fooddeliverybackend.dto.response.RegisterRestaurantResponse;
import com.sahithya.fooddeliverybackend.dto.response.RestaurantMenuResponse;
import com.sahithya.fooddeliverybackend.entity.Address;
import com.sahithya.fooddeliverybackend.entity.MenuCategory;
import com.sahithya.fooddeliverybackend.entity.MenuItem;
import com.sahithya.fooddeliverybackend.entity.Restaurant;
import com.sahithya.fooddeliverybackend.exception.InvalidRestaurantHoursException;
import com.sahithya.fooddeliverybackend.exception.RestaurantEmailAlreadyExistsException;
import com.sahithya.fooddeliverybackend.exception.RestaurantNotFoundException;
import com.sahithya.fooddeliverybackend.mapper.RestaurantMapper;
import com.sahithya.fooddeliverybackend.repository.AddressRepository;
import com.sahithya.fooddeliverybackend.repository.MenuCategoryRepository;
import com.sahithya.fooddeliverybackend.repository.MenuItemRepository;
import com.sahithya.fooddeliverybackend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final RestaurantMapper restaurantMapper;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(
            RestaurantRepository restaurantRepository,
            AddressRepository addressRepository,
            RestaurantMapper restaurantMapper,
            MenuCategoryRepository menuCategoryRepository,
            MenuItemRepository menuItemRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.restaurantMapper = restaurantMapper;
        this.menuCategoryRepository = menuCategoryRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public RegisterRestaurantResponse register(
            RegisterRestaurantRequest request
    ) {
        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (restaurantRepository.existsByEmail(normalizedEmail)) {
            throw new RestaurantEmailAlreadyExistsException(
                    normalizedEmail
            );
        }

        validateOpeningHours(request);

        Address address =
                restaurantMapper.toAddressEntity(request);

        Address savedAddress =
                addressRepository.save(address);

        Instant now = Instant.now();

        Restaurant restaurant = restaurantMapper.toEntity(
                request,
                savedAddress,
                normalizedEmail,
                now
        );

        Restaurant savedRestaurant =
                restaurantRepository.save(restaurant);

        return restaurantMapper.toRegisterResponse(savedRestaurant);
    }

    private void validateOpeningHours(
            RegisterRestaurantRequest request
    ) {
        if (!request.getOpeningTime()
                .isBefore(request.getClosingTime())) {

            throw new InvalidRestaurantHoursException(
                    "Opening time must be before closing time"
            );
        }
    }

    @Transactional(readOnly = true)
    public RestaurantMenuResponse getMenu(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(
                        () -> new RestaurantNotFoundException(restaurantId)
                );

        List<MenuCategory> categories =
                menuCategoryRepository
                        .findByRestaurantIdOrderByNameAsc(restaurantId);

        List<MenuItem> menuItems =
                menuItemRepository
                        .findByCategoryRestaurantIdAndAvailableTrueAndDeletedFalseOrderByNameAsc(
                                restaurantId
                        );

        return restaurantMapper.toMenuResponse(
                restaurant,
                categories,
                menuItems
        );
    }
}