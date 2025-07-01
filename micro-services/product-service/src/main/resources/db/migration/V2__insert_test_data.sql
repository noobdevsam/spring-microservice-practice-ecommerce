-- Insert test categories
insert into category (id, name, description)
values (1, 'Electronics', 'Electronic devices and gadgets'),
       (2, 'Books', 'Various kinds of books'),
       (3, 'Clothing', 'Apparel and accessories'),
       (4, 'Home Appliances', 'Appliances for home use'),
       (5, 'Toys', 'Toys and games for children'),
       (6, 'Sports', 'Sports equipment and accessories'),
       (7, 'Beauty', 'Beauty and personal care products');

-- Insert test products referencing categories
insert into product (name, description, available_quantity, price, category_id)
values ('Smartphone', 'Latest model smartphone', 50, 699.99, 1),
       ('Laptop', 'High performance laptop', 30, 1299.99, 1),
       ('Novel', 'Bestselling fiction novel', 100, 19.99, 2),
       ('Cookbook', 'Healthy recipes cookbook', 60, 24.99, 2),
       ('T-Shirt', '100% cotton t-shirt', 200, 9.99, 3),
       ('Jeans', 'Denim jeans', 150, 39.99, 3),
       ('Microwave', '800W microwave oven', 20, 89.99, 4),
       ('Vacuum Cleaner', 'Bagless vacuum cleaner', 15, 129.99, 4),
       ('Action Figure', 'Superhero action figure', 80, 14.99, 5),
       ('Board Game', 'Family board game', 40, 29.99, 5),
       ('Football', 'Official size football', 60, 25.99, 6),
       ('Tennis Racket', 'Lightweight tennis racket', 25, 79.99, 6),
       ('Shampoo', 'Herbal shampoo', 120, 7.99, 7),
       ('Face Cream', 'Moisturizing face cream', 90, 15.99, 7);
