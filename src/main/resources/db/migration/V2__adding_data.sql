-- default address book
insert into address_book (id, book_name) values (1, 'Default Address Book');

-- data for demo purposes of part 3 of task
insert into address_book (id, book_name) values (2, 'Second Address Book for demo purposes');


insert into contact (user_name, phone_number) values ('Tim', 123456);
insert into contact (user_name, phone_number) values ('Timmy', 09876);
insert into contact (user_name, phone_number) values ('Timothy', 34567);

insert into recorded_contact (address_book_id, contact_id) values (2,(select id from contact where user_name = 'Tim'));
insert into recorded_contact (address_book_id, contact_id) values (2,(select id from contact where user_name = 'Timmy'));
insert into recorded_contact (address_book_id, contact_id) values (2,(select id from contact where user_name = 'Timothy'));
