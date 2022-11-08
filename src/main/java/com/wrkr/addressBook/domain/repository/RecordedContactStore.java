package com.wrkr.addressBook.domain.repository;

import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.entities.RecordedContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RecordedContactStore extends CrudRepository<RecordedContact, Long> {

    boolean existsByAddressBookIdAndContactId(Long addressBookId, Long contactID);
    @Query("SELECT rc.contact FROM RecordedContact rc WHERE rc.addressBook.id = ?1")
    Page<Contact> findContactsByAddressBookId(Long id, Pageable pageable);

    @Query(value = "SELECT * FROM RECORDED_CONTACT where " +
            "(address_book_id = ?1 or address_book_id = ?2) " +
            "and contact_id in " +
            "(SELECT contact_id FROM RECORDED_CONTACT GROUP BY contact_id HAVING COUNT(contact_id )=1)", nativeQuery = true)
    Page<RecordedContact> findUniqueContacts(Long book1, Long book2, Pageable pageable);
}
