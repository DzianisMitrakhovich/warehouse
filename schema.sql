USE warehouse;

CREATE TABLE articles (
    article_id INT PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    -- num of articles in stock
    stock INT,
    CONSTRAINT quantity_column_positive CHECK (stock >= 0)
);

CREATE TABLE products (
    product_id INT PRIMARY KEY UNIQUE NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    UNIQUE (name)
);

CREATE TABLE product_parts (
    article_id INT,
    product_id INT,
    amount INT CONSTRAINT product_parts_positive_amount CHECK (amount > 0),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (article_id) REFERENCES articles(article_id),
    PRIMARY KEY(product_id, article_id)
);