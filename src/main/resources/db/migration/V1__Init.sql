create table address_book (
    id identity PRIMARY KEY,
    book_name varchar(160) NOT NULL
);

create table contact (
    id identity PRIMARY KEY,
    user_name varchar(160) NOT NULL,
    phone_number bigint NOT NULL,

    constraint uq_name_number UNIQUE(user_name, phone_number)
);

create table recorded_contact (
    id identity PRIMARY KEY,
    address_book_id bigint not null,
    contact_id bigint not null,
--    CONSTRAINT recorded_contact_pkey PRIMARY KEY (id),
    FOREIGN KEY (address_book_id) REFERENCES
                  address_book(id),
    FOREIGN KEY (contact_id) REFERENCES
                  contact(id)

);