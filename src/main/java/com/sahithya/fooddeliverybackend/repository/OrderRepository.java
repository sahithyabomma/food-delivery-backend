package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.Order;
import com.sahithya.fooddeliverybackend.entity.Payment;
import com.sahithya.fooddeliverybackend.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserIdOrderByCreatedAtDesc(
            UUID userId
    );

    Optional<Order> findByIdAndUserId(
            UUID orderId,
            UUID userId
    );

    List<Order> findByRestaurantIdOrderByCreatedAtDesc(
            UUID restaurantId
    );

    @Query("""
        SELECT o
        FROM Order o
        JOIN FETCH o.restaurant
        WHERE o.user.id = :userId
        ORDER BY o.createdAt DESC
        """)
    List<Order> findUserOrdersWithRestaurant(
            @Param("userId") UUID userId
    );

    @Query("""
        SELECT o
        FROM Order o
        JOIN FETCH o.restaurant
        WHERE o.restaurant.id = :restaurantId
        ORDER BY o.createdAt DESC
        """)
    List<Order> findRestaurantOrdersWithRestaurant(
            @Param("restaurantId") UUID restaurantId
    );

    @Query(
            value = """
                SELECT o
                FROM Order o
                JOIN FETCH o.restaurant
                WHERE o.user.id = :userId
                """,
            countQuery = """
                SELECT COUNT(o)
                FROM Order o
                WHERE o.user.id = :userId
                """
    )
    Page<Order> findUserOrdersWithRestaurant(
            @Param("userId") UUID userId,
            Pageable pageable
    );

    @Query(
            value = """
                SELECT o
                FROM Order o
                JOIN FETCH o.restaurant
                WHERE o.restaurant.id = :restaurantId
                """,
            countQuery = """
                SELECT COUNT(o)
                FROM Order o
                WHERE o.restaurant.id = :restaurantId
                """
    )
    Page<Order> findRestaurantOrdersWithRestaurant(
            @Param("restaurantId") UUID restaurantId,
            Pageable pageable
    );



}
