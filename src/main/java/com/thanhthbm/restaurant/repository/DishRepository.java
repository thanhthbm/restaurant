package com.thanhthbm.restaurant.repository;

import com.thanhthbm.restaurant.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish,Long>, JpaSpecificationExecutor<Dish> {

}
