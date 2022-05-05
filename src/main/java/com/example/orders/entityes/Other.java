package com.example.orders.entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "other")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Other {
    @Id
    @Column(name = "id", unique = true)
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
}
