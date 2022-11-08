package com.wrkr.addressBook.domain.repository;

import com.wrkr.addressBook.domain.entities.Contact;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactStore extends CrudRepository<Contact, Long> {

    Contact findByUserNameAndPhoneNumber(String userName, Long phoneNumber);

    boolean existsByPhoneNumber(long parseLong);
}
