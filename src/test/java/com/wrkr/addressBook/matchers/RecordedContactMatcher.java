package com.wrkr.addressBook.matchers;

import com.wrkr.addressBook.domain.entities.AddressBook;
import com.wrkr.addressBook.domain.entities.RecordedContact;
import org.mockito.ArgumentMatcher;

public class RecordedContactMatcher implements ArgumentMatcher<RecordedContact> {

    public RecordedContactMatcher(RecordedContact left){
        this.left = left;
    }

    RecordedContact left;

    @Override
    public boolean matches(RecordedContact right) {
        return (left.getContact().equals(right.getContact()))
                && (left.getAddressBook().equals(right.getAddressBook()));
    }
}
