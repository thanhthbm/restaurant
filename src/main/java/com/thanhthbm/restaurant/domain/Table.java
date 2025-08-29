package com.thanhthbm.restaurant.domain;

import com.thanhthbm.restaurant.util.SecurityUtil;
import com.thanhthbm.restaurant.util.constant.TableStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@jakarta.persistence.Table(name = "tables")
public class Table extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "table name must not be blank")
    private String name;
    private int capacity;
    private String qrCode;

    @Enumerated(EnumType.STRING)
    private TableStatus status;



    private boolean active = true;

}
