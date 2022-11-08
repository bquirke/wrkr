package com.wrkr.addressBook.service;


import com.wrkr.addressBook.domain.entities.AddressBook;
import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.entities.RecordedContact;
import com.wrkr.addressBook.domain.repository.AddressBookStore;
import com.wrkr.addressBook.domain.repository.RecordedContactStore;
import com.wrkr.addressBook.exceptions.AddressBookNotFoundException;
import com.wrkr.addressBook.exceptions.ContactAlreadyInAddressBookException;
import com.wrkr.addressBook.matchers.RecordedContactMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RecordedContactServiceTest {

    @InjectMocks
    RecordedContactService underTest;

    @Mock
    RecordedContactStore recordedContactStoreMock;

    @Mock
    AddressBookStore addressBookStoreMock;

    @Test
    public void saveNewRecordedContactTest() throws AddressBookNotFoundException, ContactAlreadyInAddressBookException {
        //given
        AddressBook abMock = AddressBook.builder().id(1l).bookName("Unit Test Address Book").build();
        Contact contactMock = Contact.builder().id(2l).userName("Unit Test Contact").phoneNumber(1234l).build();
        RecordedContact mockToVerify = RecordedContact.builder().id(null).contact(contactMock).addressBook(abMock).build();

        when(addressBookStoreMock.findById(anyLong())).thenReturn(Optional.of(abMock));
        when(recordedContactStoreMock.existsByAddressBookIdAndContactId(anyLong(), anyLong())).thenReturn(false);

        //when
        underTest.saveNewRecordedContact(contactMock, abMock.getId());

        //then
        Mockito.verify(recordedContactStoreMock, Mockito.times(1)).save(argThat(new RecordedContactMatcher(mockToVerify)));
    }

    @Test(expected = ContactAlreadyInAddressBookException.class)
    public void saveNewRecordedContactAlreadySaved() throws AddressBookNotFoundException, ContactAlreadyInAddressBookException {
        //given
        AddressBook abMock = AddressBook.builder().id(1l).bookName("Unit Test Address Book").build();
        Contact contactMock = Contact.builder().id(2l).userName("Unit Test Contact").phoneNumber(1234l).build();

        when(addressBookStoreMock.findById(anyLong())).thenReturn(Optional.of(abMock));
        when(recordedContactStoreMock.existsByAddressBookIdAndContactId(anyLong(), anyLong())).thenReturn(true);

        //when
        underTest.saveNewRecordedContact(contactMock, abMock.getId());

    }

    @Test(expected = AddressBookNotFoundException.class)
    public void saveNewRecordedContactAddressBookNotFound() throws AddressBookNotFoundException, ContactAlreadyInAddressBookException {
        //given
        AddressBook abMock = AddressBook.builder().id(1l).bookName("Unit Test Address Book").build();
        Contact contactMock = Contact.builder().id(2l).userName("Unit Test Contact").phoneNumber(1234l).build();

        when(addressBookStoreMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        underTest.saveNewRecordedContact(contactMock, abMock.getId());

    }
}
