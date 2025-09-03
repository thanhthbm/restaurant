package com.thanhthbm.restaurant.controller;

import com.thanhthbm.restaurant.domain.Table;
import com.thanhthbm.restaurant.domain.response.ResultPaginationDTO;
import com.thanhthbm.restaurant.service.TableService;
import com.thanhthbm.restaurant.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/tables")
    @ApiMessage("Get all tables with pagination")
    public ResponseEntity<ResultPaginationDTO> getTables(@Filter Specification<Table> specification, Pageable pageable) {
        return ResponseEntity.ok().body(this.tableService.getAllTables(specification, pageable));
    }

    @GetMapping("/tables/{id}")
    @ApiMessage("Get a table with id")
    public ResponseEntity<Table> getTableById(@PathVariable long id) {
        return ResponseEntity.ok().body(this.tableService.findById(id));
    }

    @PostMapping("/tables")
    @ApiMessage("Create a table")
    public ResponseEntity<Table> createTable(@Valid @RequestBody Table table) {
        return ResponseEntity.ok().body(this.tableService.create(table));
    }

    @PutMapping("/tables/{id}")
    @ApiMessage("Update a table")
    public ResponseEntity<Table> updateTable(@PathVariable long id, @Valid @RequestBody Table table) {
        return ResponseEntity.ok().body(this.tableService.update(id, table));
    }

    @DeleteMapping("/tables/{id}")
    @ApiMessage("Delete a table")
    public ResponseEntity<Void> deleteTable(@PathVariable long id) {
        this.tableService.delete(id);
        return ResponseEntity.ok().build();
    }
}
