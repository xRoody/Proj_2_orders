package com.example.orders.entityes;

import com.example.orders.EmbededIds.ChangedId;
import lombok.*;

import javax.persistence.*;

@Table(name = "changes")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangedCharacteristic {
    @EmbeddedId
    private ChangedId changedId;
    private Integer quantity;
}
