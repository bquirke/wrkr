package com.wrkr.addressBook.domain.dto;


import com.wrkr.addressBook.domain.entities.Contact;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    public ContactDTO(Contact entity){
        this.id = entity.getId();
        this.userName = entity.getUserName();
        this.phoneNumber = entity.getPhoneNumber();
    }

    @Getter
    private Long id;

    @Getter
    private String userName;

    @Getter
    private Long phoneNumber;
}
