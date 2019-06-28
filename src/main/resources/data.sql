INSERT INTO PIE_FILLING(id, name) VALUES (1, 'cranberry');
INSERT INTO PIE_FILLING(id, name) VALUES (2, 'strawberry');
INSERT INTO PRODUCT (id, type, filling_id, price, description) VALUES (1, 'PIE', 1, 100, 'Granny visit');
INSERT INTO PRODUCT (id, type, filling_id, price, description) VALUES (2, 'PIE', 2, 200, 'Lovely cake');
INSERT INTO BEVERAGE_TYPE (id, name) VALUES (1, 'Soda');
INSERT INTO BEVERAGE_TYPE (id, name) VALUES (2, 'Tea');
INSERT INTO PRODUCT (id, type, type_id, price, description) VALUES (3, 'BEVERAGE', 1, 20, 'Cola 0');
INSERT INTO PRODUCT (id, type, type_id, price, description) VALUES (4, 'BEVERAGE', 1, 25, 'Sprite');
INSERT INTO PRODUCT (id, type, type_id, price, description) VALUES (5, 'BEVERAGE', 2, 15, 'Greenfield');
INSERT INTO CUSTOMER (id, name, phone) VALUES (1, 'Ivan', '0291234567')