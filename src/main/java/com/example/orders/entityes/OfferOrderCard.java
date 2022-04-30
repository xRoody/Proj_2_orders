package com.example.orders.entityes;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "offer_order_card")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OfferOrderCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;
    @Column(name = "offer_id")
    private Long offerId;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order thisOrder;
}
