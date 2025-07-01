CREATE TABLE category
(
    id            INT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)       NULL,
    `description` VARCHAR(255)       NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE product
(
    id                 INT AUTO_INCREMENT NOT NULL,
    name               VARCHAR(255)       NULL,
    `description`      VARCHAR(255)       NULL,
    available_quantity DOUBLE             NOT NULL,
    price              DECIMAL            NULL,
    category_id        INT                NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);