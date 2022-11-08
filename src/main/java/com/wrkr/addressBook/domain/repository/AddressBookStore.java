package com.wrkr.addressBook.domain.repository;

import com.wrkr.addressBook.domain.entities.AddressBook;
import org.springframework.data.repository.CrudRepository;

public interface AddressBookStore extends CrudRepository<AddressBook, Long> {

}
