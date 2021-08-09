CREATE TABLE product (
     id varchar(255) NOT NULL,
     description varchar(255) NOT NULL,
     name varchar(255) NOT NULL,
     price numeric(19, 2) NOT NULL,
     CONSTRAINT product_pkey PRIMARY KEY (id)
);
