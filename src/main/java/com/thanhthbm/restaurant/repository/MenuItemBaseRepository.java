package com.thanhthbm.restaurant.repository;

import com.thanhthbm.restaurant.domain.MenuItemBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuItemBaseRepository extends JpaRepository<MenuItemBase,Long>, JpaSpecificationExecutor<MenuItemBase> {

}
