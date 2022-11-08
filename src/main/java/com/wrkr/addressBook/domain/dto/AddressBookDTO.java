package com.wrkr.addressBook.domain.dto;

import com.wrkr.addressBook.domain.entities.AddressBook;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBookDTO {

    public AddressBookDTO(AddressBook addressBook){
        this.id = addressBook.getId();
        this.bookName = addressBook.getBookName();
    }
    @Id
    @Getter
    private Long id;

    @Getter
    private String bookName;
}
