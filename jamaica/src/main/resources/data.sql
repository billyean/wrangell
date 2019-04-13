CREATE TABLE service (
id          integer PRIMARY KEY,
name        varchar(255),
description varchar(255),
timeTypes   varchar(255),
rate        double,
limits      integer
);

INSERT INTO service VALUES(1, 'Mineral baths', 'Mineral baths', '30, 60', 2.5, 65535);

INSERT INTO service VALUES(2, 'Facial services', 'normal or collagen', '30, 60', 2.00, 1);

INSERT INTO service VALUES(3, 'Massage services', 'Swedish, shiatsu, or deep tissue', '30, 60', 3.0, 1);

INSERT INTO service VALUES(4, 'Specialty treatment services', 'hot stone, sugar scrub, herbal body wrap, or botanical mud wrap', '60, 90', 3.5, 1);