package com.thanhthbm.restaurant.controller;

import com.thanhthbm.restaurant.domain.Restaurant;
import com.thanhthbm.restaurant.service.RestaurantService;
import com.thanhthbm.restaurant.util.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }


    @GetMapping("/restaurants")
    @ApiMessage("Get all restaurants")
    public ResponseEntity<List<Restaurant>> getRestaurants() {
        List<Restaurant> restaurants = this.restaurantService.getRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/{id}")
    @ApiMessage("Get a restaurant by id")
    public ResponseEntity<Restaurant> createRestaurant(@PathVariable long id) {
        Restaurant restaurant = this.restaurantService.getRestaurant(id).get();
        return ResponseEntity.ok(restaurant);
    }

    @PostMapping("/restaurants")
    @ApiMessage("Create a restaurant")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant newRestaurant = this.restaurantService.createRestaurant(restaurant);
        return ResponseEntity.ok(newRestaurant);
    }

//    @PutMapping("/restaurants/{id}")
//    @ApiMessage("Update a restaurant")
//    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable long id, @RequestBody Restaurant restaurant) {
//
//    }
}
