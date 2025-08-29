package com.thanhthbm.restaurant.repository;

import com.thanhthbm.restaurant.domain.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Table,Long>, JpaSpecificationExecutor<Table> {
    boolean existsByName(String name);
}
