package com.wrkr.addressBook.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter@Setter
    private String bookName;

}
