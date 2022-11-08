package com.wrkr.addressBook.service;

import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.repository.ContactStore;
import com.wrkr.addressBook.exceptions.ContactException;
import com.wrkr.addressBook.exceptions.NotValidPhoneNumberException;
import com.wrkr.addressBook.web.controllers.AddressBookController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);
    public static final String INVALID_PHONE_NUM_MSG = "Invalid Phone Number. Please use just digits and keep it to 8 characters";

    @Autowired
    private ContactStore contactStore;


    public Contact addNewContact(String contactName, String phoneNumber) throws NotValidPhoneNumberException, ContactException {
        LOGGER.info("Attempting to save new contact");

        // Always trim strings coming from the frontend
        phoneNumber = phoneNumber.trim();

        if(isValidMobileNo(phoneNumber)){
            //Check is there a contact with this name and number combo
            Contact newContact = contactStore.findByUserNameAndPhoneNumber(contactName, Long.parseLong(phoneNumber));
            if(newContact == null) {
                // add the new contact
                if (verifyNameAndPhoneNumber(contactName, phoneNumber)) {

                    newContact = Contact.builder()
                            .userName(contactName.trim())
                            .phoneNumber(Long.parseLong(phoneNumber))
                            .build();

                    return contactStore.save(newContact);
                }
                else {
                    LOGGER.error("Could not add Contact {} with number {}", contactName, phoneNumber);
                    throw new ContactException(String.format("Could not add Contact %s with number %s", contactName, phoneNumber));
                }
            }
            else {
                LOGGER.info("Existing contact {} already exists", contactName);
                // return the existing contact
                return newContact;
            }
        }
        else {
            LOGGER.error(INVALID_PHONE_NUM_MSG);
            throw new NotValidPhoneNumberException(INVALID_PHONE_NUM_MSG);
        }

    }

    protected boolean verifyNameAndPhoneNumber(String contactName, String phoneNumber)
            throws NotValidPhoneNumberException, ContactException {
        if(isValidMobileNo(phoneNumber)){
            if(!contactStore.existsByPhoneNumber(Long.parseLong(phoneNumber))){
                return true;
            }
            else {
                LOGGER.error("Contact with phone number {} already added", phoneNumber);
                throw new ContactException(String.format("Contact with phone number %s already added", phoneNumber));
            }
        }
        else {
            LOGGER.error(INVALID_PHONE_NUM_MSG);
            throw new NotValidPhoneNumberException(INVALID_PHONE_NUM_MSG);
        }
    }

    public boolean isValidMobileNo(String str)
    {
        // This regex could be as sophisticated as you wanted it to be
        // Per region phone number matching, etc.....
        // For the purpose of this demo its just checking that it is all digits
        return str.matches("[0-9]+");
    }
}
