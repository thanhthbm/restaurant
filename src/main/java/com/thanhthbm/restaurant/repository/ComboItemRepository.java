package com.thanhthbm.restaurant.repository;

import com.thanhthbm.restaurant.domain.ComboItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComboItemRepository extends JpaRepository<ComboItem,Long> {
    List<ComboItem> findByComboId(Long comboId);
    long countByComboId(Long comboId);
}
