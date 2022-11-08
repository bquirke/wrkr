package com.wrkr.addressBook.service;

import com.wrkr.addressBook.domain.dto.ContactDTO;
import com.wrkr.addressBook.domain.dto.RecordedContactDTO;
import com.wrkr.addressBook.domain.entities.AddressBook;
import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.entities.RecordedContact;
import com.wrkr.addressBook.domain.repository.AddressBookStore;
import com.wrkr.addressBook.domain.repository.RecordedContactStore;
import com.wrkr.addressBook.exceptions.AddressBookNotFoundException;
import com.wrkr.addressBook.exceptions.ContactAlreadyInAddressBookException;
import com.wrkr.addressBook.web.controllers.AddressBookController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordedContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordedContactService.class);

    @Autowired
    private RecordedContactStore recordedContactStore;

    @Autowired
    private AddressBookStore addressBookStore;

    public void saveNewRecordedContact(Contact contact, Long addressBookId) throws AddressBookNotFoundException, ContactAlreadyInAddressBookException {
        LOGGER.info("Attempting to save new recorded contact");

        Optional<AddressBook> addressBook = addressBookStore.findById(addressBookId);
        if(addressBook.isPresent()){
            //check that the contact isnt already stored in this address book
            if(!recordedContactStore.existsByAddressBookIdAndContactId(addressBookId, contact.getId())){
                RecordedContact recordToSave = RecordedContact.builder()
                        .contact(contact)
                        .addressBook(addressBook.get())
                        .build();

                recordedContactStore.save(recordToSave);
            }

            else {
                throw new ContactAlreadyInAddressBookException(String.format(
                        "Contact with name %s already recorded in address book with name %s",
                        contact.getUserName(),addressBook.get().getBookName()));
            }

        }
        else {
            LOGGER.error("No Address Book with ID {}", addressBookId);
            throw new AddressBookNotFoundException(String.format("No Address Book with ID %s", addressBookId));
        }
    }

    public List<ContactDTO> listBookContacts(Integer page, String sortBy, Integer limit, Long addressBookId) {
        return recordedContactStore.findContactsByAddressBookId(addressBookId ,PageRequest.of(page, limit, Sort.by(sortBy)))
                .stream()
                .map(ContactDTO::new)
                .collect(Collectors.toList());
    }

    public List<RecordedContactDTO> getUniqueContactNames(Integer page, Integer limit, Long addressBookIdOne, Long addressBookIdTwo) throws AddressBookNotFoundException {

        if(addressBookStore.existsById(addressBookIdOne)){
            if (addressBookStore.existsById(addressBookIdTwo)){

                return recordedContactStore.findUniqueContacts(addressBookIdOne, addressBookIdTwo, PageRequest.of(page, limit))
                        .stream()
                        .map(RecordedContactDTO::new)
                        .collect(Collectors.toList());
            }
            else {
                throw new AddressBookNotFoundException(String.format("Address book with id %s not found", addressBookIdTwo));
            }
        }
        else {
            throw new AddressBookNotFoundException(String.format("Address book with id %s not found", addressBookIdOne));
        }
    }
}
