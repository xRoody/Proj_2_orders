package com.example.orders.EmbededIds;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
@Builder
public class ChangedId implements  Serializable{
    private Long id;
    private Long cardId;
}
