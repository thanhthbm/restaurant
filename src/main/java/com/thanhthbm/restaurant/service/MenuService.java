package com.thanhthbm.restaurant.service;

import com.thanhthbm.restaurant.domain.Combo;
import com.thanhthbm.restaurant.domain.ComboItem;
import com.thanhthbm.restaurant.domain.Dish;
import com.thanhthbm.restaurant.domain.MenuItemBase;
import com.thanhthbm.restaurant.domain.request.ReqCreateComboDTO;
import com.thanhthbm.restaurant.domain.response.ResultPaginationDTO;
import com.thanhthbm.restaurant.repository.ComboItemRepository;
import com.thanhthbm.restaurant.repository.ComboRepository;
import com.thanhthbm.restaurant.repository.DishRepository;
import com.thanhthbm.restaurant.repository.MenuItemBaseRepository;
import com.thanhthbm.restaurant.util.constant.PriceMode;
import com.thanhthbm.restaurant.util.exception.ResourceAlreadyExistsException;
import com.thanhthbm.restaurant.util.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuService {
    private final MenuItemBaseRepository menuItemBaseRepository;
    private final DishRepository dishRepository;
    private final ComboRepository comboRepository;

    public MenuService(MenuItemBaseRepository menuItemBaseRepository, DishRepository dishRepository, ComboRepository comboRepository) {
        this.menuItemBaseRepository = menuItemBaseRepository;
        this.dishRepository = dishRepository;
        this.comboRepository = comboRepository;
    }

    /* ======================= DISH ======================= */
    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish updateDish(Long id, Dish dish) {
        if (!dishRepository.existsById(id)) {
            throw  new ResourceNotFoundException("Dish with id " + id + " not found");
        }

        return dishRepository.save(dish);
    }

    public void deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish with id " + id + " not found");
        }
        dishRepository.deleteById(id);
    }

    /* ======================= COMBO ======================= */
//    public Combo createCombo(Combo combo) {
//        // Nếu client không set priceMode -> mặc định SUM
//        if (combo.getPriceMode() == null) {
//            combo.setPriceMode(PriceMode.SUM);
//        }
//
//        if (combo.getPriceMode() == PriceMode.FIXED) {
//            // FIXED thì phải có price
//            if (combo.getPrice() == null) {
//                throw new IllegalArgumentException("Combo price must be set when FIXED");
//            }
//        } else { // SUM
//            // Tính giá theo dish * quantity
//            BigDecimal total = combo.getItems().stream()
//                    .map(it -> it.getDish().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            combo.setPrice(total);
//        }
//
//        // Gắn combo cho từng item (tránh null FK)
//        combo.getItems().forEach(it -> it.setCombo(combo));
//
//        return comboRepository.save(combo);
//    }


    public Combo createCombo(ReqCreateComboDTO dto) {
        if (dto.getPriceMode() == null) {
            dto.setPriceMode(PriceMode.SUM); // default
        }

        Combo combo = new Combo();
        combo.setName(dto.getName());
        combo.setCategory(dto.getCategory());
        combo.setDescription(dto.getDescription());
        combo.setNote(dto.getNote());
        combo.setPriceMode(dto.getPriceMode());

        BigDecimal total = BigDecimal.ZERO;

        for (ReqCreateComboDTO.DishCombo dc : dto.getItems()) {
            Dish dish = dishRepository.findById(dc.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Dish not found: id = " + dc.getId()));

            ComboItem comboItem = new ComboItem();
            comboItem.setDish(dish);
            comboItem.setQuantity(dc.getQuantity());

            combo.addItem(comboItem);

            total = total.add(dish.getPrice().multiply(BigDecimal.valueOf(dc.getQuantity())));
        }

        if (dto.getPriceMode() == PriceMode.FIXED) {
            if (dto.getPrice() == null) {
                throw new IllegalArgumentException("Price must be provided when priceMode is FIXED");
            }
            combo.setPrice(dto.getPrice());
        } else {
            combo.setPrice(total);
        }
        return comboRepository.save(combo);
    }


    //get all menu items
    public ResultPaginationDTO getAllMenuItems(Specification<MenuItemBase> spec, Pageable pageable) {
        ResultPaginationDTO dto = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        Page<MenuItemBase> items = this.menuItemBaseRepository.findAll(spec, pageable);

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotal(items.getTotalElements());
        meta.setPages(items.getTotalPages());

        dto.setMeta(meta);
        dto.setResult(items.getContent());
        return dto;
    }



}
