package com.thanhthbm.restaurant.service;


import com.thanhthbm.restaurant.domain.Table;
import com.thanhthbm.restaurant.domain.response.ResultPaginationDTO;
import com.thanhthbm.restaurant.repository.TableRepository;
import com.thanhthbm.restaurant.util.exception.ResourceAlreadyExistsException;
import com.thanhthbm.restaurant.util.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TableService {
    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public ResultPaginationDTO getAllTables(Specification<Table> specification, Pageable pageable) {
        Page<Table> tables = tableRepository.findAll(pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta =  new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotal(tables.getTotalElements());
        meta.setPages(tables.getTotalPages());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(tables.getContent());

        return resultPaginationDTO;
    }

    public Table findById(long id) {
        Optional<Table> tableOptional = this.tableRepository.findById(id);
        if (!tableOptional.isPresent()) {
            throw new ResourceNotFoundException("Table not found");
        }
        return tableOptional.get();
    }

    public Table create(Table table) {
        if (this.tableRepository.existsByName(table.getName())) {
            throw new ResourceAlreadyExistsException("Table with name " + table.getName() + "already exists");
        }
        return this.tableRepository.save(table);
    }

    public Table update(Long id, Table table) {
        if (!this.tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table not found");
        }
        Table currentTable = this.tableRepository.findById(id).get();
        currentTable.setName(table.getName());
        currentTable.setActive(table.isActive());
        currentTable.setCapacity(table.getCapacity());
        currentTable.setQrCode(table.getQrCode());



        return this.tableRepository.save(currentTable);
    }

    public void delete(Long id) {
        if (!this.tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table not found");
        }
        this.tableRepository.deleteById(id);
    }




}
