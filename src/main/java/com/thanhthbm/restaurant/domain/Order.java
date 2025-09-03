package com.thanhthbm.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thanhthbm.restaurant.util.constant.OrderStatus;
import com.thanhthbm.restaurant.util.constant.OrderType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User staff;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY)
    private User customer;

    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String note;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"orders"})
    @JoinTable(
           name = "order_table",
          joinColumns = @JoinColumn(name = "order_id"),
           inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    private List<com.thanhthbm.restaurant.domain.Table> tables;

}
