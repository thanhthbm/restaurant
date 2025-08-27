package com.thanhthbm.restaurant.controller;

import com.thanhthbm.restaurant.domain.Combo;
import com.thanhthbm.restaurant.domain.Dish;
import com.thanhthbm.restaurant.domain.MenuItemBase;
import com.thanhthbm.restaurant.domain.request.ReqCreateComboDTO;
import com.thanhthbm.restaurant.domain.response.ResultPaginationDTO;
import com.thanhthbm.restaurant.service.MenuService;
import com.thanhthbm.restaurant.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menu")
    @ApiMessage("Get all menu items")
    public ResponseEntity<ResultPaginationDTO> getAllMenuItems(@Filter Specification<MenuItemBase> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.menuService.getAllMenuItems(spec, pageable));
    }

    @PostMapping("/menu/dishes")
    @ApiMessage("Add a new dish")
    public ResponseEntity<Dish> addDish(@Valid @RequestBody Dish dish) {
        return ResponseEntity.status(HttpStatus.OK).body(this.menuService.createDish(dish));
    }

    @PostMapping("/menu/combos")
    @ApiMessage("Add a new combo with existed dishes")
    public ResponseEntity<Combo> addCombo(@Valid @RequestBody ReqCreateComboDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.menuService.createCombo(dto));
    }
}
