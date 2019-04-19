CREATE TABLE service (
id          integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
name        varchar(255),
description varchar(255),
min         int,
max         int,
rate        double,
limits      integer
);

INSERT INTO service(name, description, min, max, rate, limits) VALUES('Mineral baths', 'Mineral baths', 30, 60, 2.5, 65535);

INSERT INTO service(name, description, min, max, rate, limits) VALUES('Facial services', 'normal or collagen', 30, 60, 2.00, 1);

INSERT INTO service(name, description, min, max, rate, limits) VALUES('Massage services', 'Swedish, shiatsu, or deep tissue', 30, 60, 3.0, 1);

INSERT INTO service(name, description, min, max, rate, limits) VALUES('Specialty treatment services', 'hot stone, sugar scrub, herbal body wrap, or botanical mud wrap', 60, 90, 3.5, 1);