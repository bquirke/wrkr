package com.wrkr.addressBook.domain.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recorded_contact")
@ToString
@JsonSerialize
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordedContact {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    //TODO delete getter setter.... just for testing
    @Getter@Setter
    private Long id;


    @ManyToOne
    @JoinColumn(name = "address_book_id")
    @Getter@Setter
    AddressBook addressBook;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @Getter@Setter
    Contact contact;
}
