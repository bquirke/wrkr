package com.wrkr.addressBook.web.controller;

import com.wrkr.addressBook.domain.dto.AddressBookDTO;
import com.wrkr.addressBook.domain.dto.ContactDTO;
import com.wrkr.addressBook.domain.dto.RecordedContactDTO;
import com.wrkr.addressBook.domain.entities.AddressBook;
import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.entities.RecordedContact;
import com.wrkr.addressBook.domain.repository.AddressBookStore;
import com.wrkr.addressBook.domain.repository.RecordedContactStore;
import com.wrkr.addressBook.exceptions.AddressBookNotFoundException;
import com.wrkr.addressBook.exceptions.ContactException;
import com.wrkr.addressBook.exceptions.NotValidPhoneNumberException;
import com.wrkr.addressBook.service.ContactService;
import com.wrkr.addressBook.service.RecordedContactService;
import com.wrkr.addressBook.web.controllers.AddressBookController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.wrkr.addressBook.web.controllers.AddressBookController.DEMO_BOOK_TWO_ID;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers={AddressBookController.class})
public class AddressBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecordedContactService recordedContactServiceMock;

    @MockBean
    private AddressBookStore addressBookStoreMock;

    @MockBean
    private ContactService contactServiceMock;

    @Test
    public void testGetMethodSuccess() throws Exception {
        //given
        AddressBook bookToReturn = AddressBook.builder()
                .bookName("UnitTestBook")
                .build();
        when(recordedContactServiceMock.listBookContacts(any(), any(),any(), any())).thenReturn(new ArrayList<>());
        when(addressBookStoreMock.findById(anyLong())).thenReturn(Optional.of(bookToReturn));

        //when
        mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void testGetMethodSuccessWhenNoBook2() throws Exception {
        //given
        when(recordedContactServiceMock.listBookContacts(any(), any(),any(), any())).thenReturn(new ArrayList<>());

        //when
        mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void testListBookOneContacts() throws Exception {
        //given
        ContactDTO toReturn = ContactDTO.builder()
                .userName("Unit Test")
                .phoneNumber(123l)
                .build();
        when(recordedContactServiceMock.listBookContacts(any(), any(),any(), any()))
                .thenReturn(new ArrayList<ContactDTO>(Collections.singletonList(toReturn)));

        //when
        mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("recordedContacts"))
                .andExpect(model().attribute("recordedContacts", contains(toReturn)));
    }

    @Test
    public void addNewContactTestSuccess() throws Exception, NotValidPhoneNumberException, ContactException {
        //given
        ContactDTO toReturn = ContactDTO.builder()
                .userName("Unit Test")
                .phoneNumber(123l)
                .build();

        when(recordedContactServiceMock.listBookContacts(any(), any(),any(), any()))
                .thenReturn(new ArrayList<ContactDTO>(Collections.singletonList(toReturn)));

        //when
        mockMvc.perform(post("/newContact")
                        .param("contactName", toReturn.getUserName())
                        .param("phoneNumber", toReturn.getPhoneNumber().toString())
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void addNewContactErrorTest() throws Exception, NotValidPhoneNumberException, ContactException {
        //given
        ContactDTO toReturn = ContactDTO.builder()
                .userName("Unit Test")
                .phoneNumber(123l)
                .build();

        String errorMsg = "Unit test error";
        when(contactServiceMock.addNewContact(any(), any()))
                .thenThrow(new ContactException(errorMsg));

        //when
        mockMvc.perform(post("/newContact")
                        .param("contactName", toReturn.getUserName())
                        .param("phoneNumber", toReturn.getPhoneNumber().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", equalTo(errorMsg)));
    }

    @Test
    public void uniqueContactNamesTest() throws AddressBookNotFoundException, Exception {
        //given
        AddressBookDTO book1 = AddressBookDTO.builder().bookName("Book 1").build();
        AddressBookDTO book2 = AddressBookDTO.builder().bookName("Book 2").build();
        ContactDTO contact1 = ContactDTO.builder().userName("Contact 1").build();
        ContactDTO contact2 = ContactDTO.builder().userName("Contact 2").build();

        RecordedContactDTO addressBook1Contact = RecordedContactDTO.builder()
                .contact(contact1)
                .addressBook(book1)
                .build();
        RecordedContactDTO addressBook2Contact = RecordedContactDTO.builder()
                .contact(contact2)
                .addressBook(book2)
                .build();

        when(recordedContactServiceMock.getUniqueContactNames(any(), any(),any(), any()))
                .thenReturn(new ArrayList<RecordedContactDTO>(Arrays.asList(addressBook1Contact,addressBook2Contact)));

        //when
        mockMvc.perform(get("/uniqueContactNames"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("uniqueContactNames"))
                .andExpect(flash().attribute("uniqueContactNames", contains(addressBook1Contact, addressBook2Contact)));
    }

    @Test
    public void uniqueContactNamesFailureTest() throws AddressBookNotFoundException, Exception {
        //given
        String errorMsg = "Unit test error message";
        when(recordedContactServiceMock.getUniqueContactNames(any(), any(),any(), any()))
                .thenThrow(new AddressBookNotFoundException(errorMsg));

        //when
        mockMvc.perform(get("/uniqueContactNames"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", equalTo(errorMsg)));
    }
}
