package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.request.RegisterRestaurantRequest;
import com.sahithya.fooddeliverybackend.dto.response.AddressResponse;
import com.sahithya.fooddeliverybackend.dto.response.MenuCategoryWithItemsResponse;
import com.sahithya.fooddeliverybackend.dto.response.MenuItemSummaryResponse;
import com.sahithya.fooddeliverybackend.dto.response.RegisterRestaurantResponse;
import com.sahithya.fooddeliverybackend.dto.response.RestaurantMenuResponse;
import com.sahithya.fooddeliverybackend.entity.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    private final AddressMapper addressMapper;
    private final MenuItemMapper menuItemMapper;

    public RestaurantMapper(
            AddressMapper addressMapper,
            MenuItemMapper menuItemMapper
    ) {
        this.addressMapper = addressMapper;
        this.menuItemMapper = menuItemMapper;
    }

    public Address toAddressEntity(RegisterRestaurantRequest request) {
        return addressMapper.toEntity(request.getAddress());
    }

    public Restaurant toEntity(
            RegisterRestaurantRequest request,
            Address address,
            User owner,
            String normalizedEmail,
            Instant now
    ) {
        return new Restaurant(
                UUID.randomUUID(),
                request.getName().trim(),
                normalizedEmail,
                request.getPhoneNumber().trim(),
                RestaurantStatus.ACTIVE,
                request.getOpeningTime(),
                request.getClosingTime(),
                address,
                owner,
                now,
                now
        );
    }

    public RegisterRestaurantResponse toRegisterResponse(
            Restaurant restaurant
    ) {
        AddressResponse addressResponse =
                addressMapper.toResponse(restaurant.getAddress());

        return new RegisterRestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getEmail(),
                restaurant.getPhoneNumber(),
                restaurant.getStatus(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                addressResponse,
                restaurant.getCreatedAt()
        );
    }

    public RestaurantMenuResponse toMenuResponse(
            Restaurant restaurant,
            List<MenuCategory> categories,
            List<MenuItem> menuItems,
            Map<UUID, Inventory> inventoryByMenuItem
    ) {
        Map<UUID, List<MenuItem>> itemsByCategory =
                menuItems.stream()
                        .collect(Collectors.groupingBy(
                                item -> item.getCategory().getId()
                        ));

        List<MenuCategoryWithItemsResponse> categoryResponses =
                categories.stream()
                        .map(category -> {
                            List<MenuItemSummaryResponse> itemResponses =
                                    itemsByCategory
                                            .getOrDefault(
                                                    category.getId(),
                                                    List.of()
                                            )
                                            .stream()
                                            .map(menuItem -> {
                                                Inventory inventory =
                                                        inventoryByMenuItem.get(
                                                                menuItem.getId()
                                                        );

                                                boolean inStock =
                                                        inventory != null
                                                                && inventory.getAvailableQuantity() > 0;

                                                return menuItemMapper
                                                        .toSummaryResponse(
                                                                menuItem,
                                                                inStock
                                                        );
                                            })
                                            .toList();

                            return new MenuCategoryWithItemsResponse(
                                    category.getId(),
                                    category.getName(),
                                    itemResponses
                            );
                        })
                        .toList();

        return new RestaurantMenuResponse(
                restaurant.getId(),
                restaurant.getName(),
                categoryResponses
        );
    }
}
