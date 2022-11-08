package com.wrkr.addressBook.web.controllers;

import com.wrkr.addressBook.domain.dto.ContactDTO;
import com.wrkr.addressBook.domain.entities.AddressBook;
import com.wrkr.addressBook.domain.entities.Contact;
import com.wrkr.addressBook.domain.entities.RecordedContact;
import com.wrkr.addressBook.domain.repository.AddressBookStore;
import com.wrkr.addressBook.domain.repository.RecordedContactStore;
import com.wrkr.addressBook.exceptions.AddressBookNotFoundException;
import com.wrkr.addressBook.exceptions.ContactAlreadyInAddressBookException;
import com.wrkr.addressBook.exceptions.ContactException;
import com.wrkr.addressBook.exceptions.NotValidPhoneNumberException;
import com.wrkr.addressBook.service.ContactService;
import com.wrkr.addressBook.service.RecordedContactService;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class AddressBookController {

    public static final long DEMO_BOOK_TWO_ID = 2l;
    @Autowired
    private RecordedContactService recordedContactService;

    @Autowired
    private AddressBookStore addressBookStore;

    @Autowired
    private ContactService contactService;

    // All endpoints redirect back to here so this is where we populate both tables
    @GetMapping()
    public String get(Model model, @RequestParam(defaultValue = "0") Integer page,
                      @RequestParam(defaultValue = "contact.userName") String sortBy,
                      @RequestParam(defaultValue = "1000") Integer limit,
                      @RequestParam(defaultValue = "1") Long addressBookId) {

        List<ContactDTO> toTest = recordedContactService.listBookContacts(page, sortBy, limit, addressBookId);
        model.addAttribute("recordedContacts",
                recordedContactService.listBookContacts(page, sortBy, limit, addressBookId));

        // Add book 2 demo contacts
        model.addAttribute("bookTwoContacts",
                recordedContactService.listBookContacts(page, sortBy, limit, DEMO_BOOK_TWO_ID));

        Optional<AddressBook> recordedContact = addressBookStore.findById(DEMO_BOOK_TWO_ID);
        model.addAttribute("bookTwoNameDemo", recordedContact.isPresent() ?
                recordedContact.get().getBookName() : "No book two added");

        return "index";
    }


    @GetMapping(value = "listAddressBook")
    public String getDefaultAddressBook(Model model,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "contact.userName") String sortBy,
                                        @RequestParam(defaultValue = "1000") Integer limit,
                                        @RequestParam(defaultValue = "1") Long addressBookId){

        model.addAttribute("recordedContacts",
                recordedContactService.listBookContacts(page, sortBy, limit, addressBookId));

        // Add book 2 demo contacts
        model.addAttribute("bookTwoContacts",
                recordedContactService.listBookContacts(page, sortBy, limit, DEMO_BOOK_TWO_ID));

        return "redirect:/";
    }

    @PostMapping("/newContact")
    public String saveNewContact(Model model, HttpServletRequest request,
                                       @RequestParam(name = "contactName") String contactName,
                                       @RequestParam(name = "phoneNumber") String phoneNumber,
                                       @RequestParam(name = "addressBookID", defaultValue = "1") Long addressBookId,
                                       @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "contact.userName") String sortBy,
                                       @RequestParam(defaultValue = "1000") Integer limit,
                                       RedirectAttributes redirectAttributes)
            throws NotValidPhoneNumberException, AddressBookNotFoundException {

        Contact newContact;
        try{
            newContact = contactService.addNewContact(contactName, phoneNumber);

            // add the new contact to the default address book
            recordedContactService.saveNewRecordedContact(newContact, addressBookId);

        }catch (ContactException | ContactAlreadyInAddressBookException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }

        return "redirect:/";
    }

    @GetMapping("/uniqueContactNames")
    public String uniqueContactNamesBetweenTwoAddressBooks(Model model, RedirectAttributes redirectAttributes,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "1000") Integer limit,
                                                           @RequestParam(defaultValue = "1") Long addressBookIdOne,
                                                           @RequestParam(defaultValue = "1") Long addressBookIdTwo) {
        try {
            redirectAttributes.addFlashAttribute("uniqueContactNames",
                    recordedContactService.getUniqueContactNames(page, limit,addressBookIdOne, addressBookIdTwo));
        } catch (AddressBookNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }
}
