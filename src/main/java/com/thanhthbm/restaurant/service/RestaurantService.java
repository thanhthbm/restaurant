package com.thanhthbm.restaurant.service;

import com.thanhthbm.restaurant.domain.Restaurant;
import com.thanhthbm.restaurant.repository.RestaurantRepository;
import com.thanhthbm.restaurant.util.exception.ResourceAlreadyExistsException;
import com.thanhthbm.restaurant.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> getRestaurant(long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found");
        }
        return restaurantRepository.findById(id);
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        if  (restaurantRepository.existsById(restaurant.getId())) {
            throw new ResourceAlreadyExistsException("Restaurant already exists");
        }
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(long id, Restaurant restaurant) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found");
        }
        return restaurantRepository.save(restaurant);
    }
}
