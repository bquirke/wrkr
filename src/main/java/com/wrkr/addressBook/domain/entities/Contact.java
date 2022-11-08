package com.wrkr.addressBook.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Contact {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter@Setter
    private String userName;

    @Getter@Setter
    private Long phoneNumber;

}
