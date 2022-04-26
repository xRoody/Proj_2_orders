package com.example.orders.entityes;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "status")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;
    @Column(name = "value", unique = true, nullable = false)
    private String value;
}
