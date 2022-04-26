package com.example.orders.entityes;

import javax.persistence.*;

@Entity
@Table(name = "offer_order_card")
public class OfferOrderCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;
    @Column(name = "offer_id")
    private Long offerId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
