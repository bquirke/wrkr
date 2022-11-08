package com.wrkr.addressBook.service;

import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.repository.ContactStore;
import com.wrkr.addressBook.exceptions.ContactException;
import com.wrkr.addressBook.exceptions.NotValidPhoneNumberException;
import com.wrkr.addressBook.service.ContactService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ContactServiceTest {

    @InjectMocks
    ContactService underTest;

    @Mock
    ContactStore contactStoreMock;

    @Test
    public void addNewContactTestSuccess() throws NotValidPhoneNumberException, ContactException {
        //given
        String contactName = "Unit Test";
        String phoneNumber = "123";

        Contact contactToVerify = Contact.builder()
                .userName(contactName)
                .phoneNumber(Long.parseLong(phoneNumber))
                .build();
        when(contactStoreMock.findByUserNameAndPhoneNumber(any(), any())).thenReturn(null);
        when(contactStoreMock.save(any())).thenReturn(contactToVerify);

        //when
        Contact result = underTest.addNewContact(contactName, phoneNumber);

        //then
        assertEquals(contactToVerify, result);
    }

    @Test()
    public void addNewContactWhoIsAlreadyAdded() throws NotValidPhoneNumberException, ContactException {
        //given
        String contactName = "Unit Test";
        String phoneNumber = "123";

        Contact contactToVerify = Contact.builder()
                .userName(contactName)
                .phoneNumber(Long.parseLong(phoneNumber))
                .build();
        when(contactStoreMock.findByUserNameAndPhoneNumber(any(), any())).thenReturn(contactToVerify);

        //when
        Contact result = underTest.addNewContact(contactName, phoneNumber);

        //then
        assertEquals(contactToVerify, result);
    }

    @Test(expected = NotValidPhoneNumberException.class)
    public void addNewContactBadPhoneNumberTest() throws NotValidPhoneNumberException, ContactException {
        //given
        String contactName = "Unit Test";
        String phoneNumber = "1-2-3"; // no symbols allowed in contact phone-number

        when(contactStoreMock.findByUserNameAndPhoneNumber(any(), any())).thenReturn(null);

        //when
        Contact result = underTest.addNewContact(contactName, phoneNumber);
    }

    @Test(expected = ContactException.class)
    public void verifyNumber() throws NotValidPhoneNumberException, ContactException {
        //given
        String contactName = "Unit Test";
        String phoneNumber = "123";

        when(contactStoreMock.findByUserNameAndPhoneNumber(any(), any())).thenReturn(null);
        when(contactStoreMock.existsByPhoneNumber(anyLong())).thenReturn(true);

        //when
        boolean result = underTest.verifyNameAndPhoneNumber(contactName, phoneNumber);
    }

    @Test
    public void isValidNumberWithNotValidNumber() {
        //given
        String phoneNumber = "1-2-3"; // no symbols allowed in contact phone-number

        //when
        boolean result = underTest.isValidMobileNo(phoneNumber);

        //then
        assertFalse(result);
    }


    @Test
    public void isValidNumber() {
        //given
        String phoneNumber = "123"; // no symbols allowed in contact phone-number

        //when
        boolean result = underTest.isValidMobileNo(phoneNumber);

        //then
        assertTrue(result);
    }

}
