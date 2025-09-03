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
    public Dish getDish(Long id) {
        Optional<Dish> dishOptional = this.dishRepository.findById(id);
        if (!dishOptional.isPresent()) {
            throw new ResourceNotFoundException("Dish not found");
        }
        return dishOptional.get();
    }

    public ResultPaginationDTO getAllDishes(Specification<Dish> specification, Pageable pageable) {
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        Page<Dish> dishPage = this.dishRepository.findAll(specification, pageable);
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotal(dishPage.getTotalElements());
        meta.setPages(dishPage.getTotalPages());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(dishPage.getContent());
        return resultPaginationDTO;
    }

    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish updateDish(Long id, Dish dish) {
        if (!dishRepository.existsById(id)) {
            throw  new ResourceNotFoundException("Dish with id " + id + " not found");
        }

        return dishRepository.save(dish);
    }



//    public void deleteDish(Long id) {
//        if (!dishRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Dish with id " + id + " not found");
//        }
//        dishRepository.deleteById(id);
//    }



    /* ======================= COMBO ======================= */
    public Combo createCombo(ReqCreateComboDTO dto) {
        return comboRepository.save(convertCreateComboDTO(dto));
    }


    public Combo getCombo(Long id) {
        Optional<Combo> comboOptional = this.comboRepository.findById(id);
        if (!comboOptional.isPresent()) {
            throw new ResourceNotFoundException("Combo with id " + id + " not found");
        }
        return comboOptional.get();
    }

    public ResultPaginationDTO getAllCombos(Specification<Combo> specification,  Pageable pageable) {
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        Page<Combo> comboPage = this.comboRepository.findAll(specification, pageable);
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotal(comboPage.getTotalElements());
        meta.setPages(comboPage.getTotalPages());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(comboPage.getContent());
        return resultPaginationDTO;
    }

    public void deleteCombo(Long id) {
        if (!comboRepository.existsById(id)) {
            throw new ResourceNotFoundException("Combo with id " + id + " not found");
        }
        this.comboRepository.deleteById(id);
    }

    public Combo updateCombo(Long id, ReqCreateComboDTO dto) {
        if (!comboRepository.existsById(id)) {
            throw  new ResourceNotFoundException("Combo with id " + id + " not found");
        }
        return comboRepository.save(convertCreateComboDTO(dto));
    }

    public Combo convertCreateComboDTO(ReqCreateComboDTO dto) {
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
            comboItem.setPrice(dish.getPrice());

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
        return combo;
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

    public BigDecimal getCurrentPrice(MenuItemBase item) {
        if (item instanceof Combo c) {
            return c.getPrice();
        } else if (item instanceof Dish d) {
            return d.getPrice();
        }
        else{
            throw new IllegalArgumentException("Invalid item type");
        }
    }

}
