insert into role (name) values ('ROLE_ADMIN');
insert into role (name) values ('ROLE_DRIVER');
insert into role (name) values ('ROLE_PASSENGER');

insert into images (name) values ('audiA6.png');

insert into adresses (city, latitude, longitude, state, street) values ('Novi Sad', 45.242101, 19.800049, 'Serbia', 'Banijska 32');



--Cars
insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-116IX');

insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-111IX');

insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('PET;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-112IX');

insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('FOOD;BAGGAGE', 'AVERAGE', 5, 'Audi A6', 'NS-113IX');
insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-114IX');
insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('PET;BAGGAGE', 'AVERAGE', 5, 'Audi A6', 'NS-115IX');
insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('PET;BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-117IX');
insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('PET;BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-118IX');

-- password is 123 for all users
insert into admins (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id)
values
(nextval('users_id_gen'), true, false, 'admin0@gmail.com', 'Admin0', 'Admin00', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, 1, 1 );


--Drivers
insert into drivers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, active, driver_status, car_id, next_driving_id, reserved_from_client_driving_id, lat, lon)
values
    (nextval('users_id_gen'), true, false, 'mika@gmail.com', 'Mika', 'Mikic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, 1, 2, true, 'FREE', 1, null, null, 45.247027, 19.800059),
    (nextval('users_id_gen'), true, false, 'predrag@gmail.com', 'Predrag', 'Macogovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123451', 1, 1, 2, true, 'FREE', 8,null,null, 45.246848,  19.801531),
    (nextval('users_id_gen'), true, false, 'milan@gmail.com', 'Milan', 'Maric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123453', 1, 1, 2, true, 'FREE', 2, null,null,45.254365, 19.848213),
    (nextval('users_id_gen'), true, false, 'petar@gmail.com', 'Petar', 'Ilic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123454', 1, 1, 2, true, 'FREE', 3, null,null,45.237430, 19.731582),
    (nextval('users_id_gen'), true, false, 'marko@gmail.com', 'Marko', 'Lekovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123455', 1, 1, 2, true, 'FREE', 4,null,null, 45.238356, 19.808732),
    (nextval('users_id_gen'), true, false, 'ljubisa@gmail.com', 'Ljubisa', 'Bobic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123457', 1, 1, 2, true, 'FREE', 5,null,null, 45.248177, 19.839459),
    (nextval('users_id_gen'), true, false, 'sava@gmail.com', 'Sava', 'Peric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123458', 1, 1, 2, true, 'FREE', 6, null,null,45.237163, 19.839484),
    (nextval('users_id_gen'), true, false, 'milojko@gmail.com', 'Milojko', 'Dragic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123459', 1, 1, 2, true, 'FREE', 7,null,null, 45.260964, 19.829406);


insert into passengers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, driving, current_driving_id)
values
(nextval('users_id_gen'), true, false, 'john@gmail.com', 'John', 'Jonson', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, 1, 3, false, null);
--(nextval('users_id_gen'), true, false, 'natasha.lakovic@gmail.com', 'Natasa', 'Lakovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 3, false, null);