package com.wrkr.addressBook.service;

import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.repository.ContactStore;
import com.wrkr.addressBook.exceptions.ContactException;
import com.wrkr.addressBook.exceptions.NotValidPhoneNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private ContactStore contactStore;


    public Contact addNewContact(String contactName, String phoneNumber) throws NotValidPhoneNumberException, ContactException {
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
                    throw new ContactException(String.format("Could not add Contact %s with number %s", contactName, phoneNumber));
                }
            }
            else {
                // return the existing contact
                return newContact;
            }
        }
        else {
            throw new NotValidPhoneNumberException("Invalid Phone Number. Please use just digits and keep it to 8 characters");
        }

    }

    protected boolean verifyNameAndPhoneNumber(String contactName, String phoneNumber)
            throws NotValidPhoneNumberException, ContactException {
        if(isValidMobileNo(phoneNumber)){
            if(!contactStore.existsByPhoneNumber(Long.parseLong(phoneNumber))){
                return true;
            }
            else {
                throw new ContactException(String.format("Contact with phone number %s already added", phoneNumber));
            }
        }
        else {
            throw new NotValidPhoneNumberException("Invalid Phone Number. Please use just digits and keep it to 8 characters");
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
