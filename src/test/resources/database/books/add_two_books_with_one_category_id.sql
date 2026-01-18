insert into books (id,title,author,isbn,price,description,cover_image) values (1,'Book1','Jane Doe',123456,1.00,'book1','image1');
insert into books (id,title,author,isbn,price,description,cover_image) values (2,'Book2','JohnDoe',654321,1.00,'book2','image2');
insert into categories (id,name,description) values (1,'Novel','something');
insert into books_categories (book_id,category_id) values (1,1);
insert into books_categories (book_id,category_id) values (2,1);