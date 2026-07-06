ALTER TABLE order_book
ADD CONSTRAINT unique_order_book UNIQUE (order_id, book_id);