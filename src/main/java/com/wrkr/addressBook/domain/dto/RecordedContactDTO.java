package com.wrkr.addressBook.domain.dto;

import com.wrkr.addressBook.domain.entities.AddressBook;
import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.entities.RecordedContact;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordedContactDTO {

    public RecordedContactDTO(RecordedContact entity){
        this.id = entity.getId();
        this.addressBook = new AddressBookDTO(entity.getAddressBook());
        this.contact = new ContactDTO(entity.getContact());
    }


    @Getter
    private Long id;

    @Getter
    AddressBookDTO addressBook;

    @Getter
    ContactDTO contact;
}
