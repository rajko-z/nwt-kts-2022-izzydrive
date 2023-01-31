insert into role (name) values ('ROLE_DRIVER');
insert into role (name) values ('ROLE_PASSENGER');


INSERT INTO public.adresses(latitude, longitude, name)
VALUES
    (45.2421319, 19.8000267, '32, Банијска, Телеп, МЗ Братство телеп, Нови Сад');

insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-116-IX'),
    ('BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-111-IX'),
    ('PET;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-112-IX'),
    ('FOOD;BAGGAGE', 'AVERAGE', 5, 'Audi A6', 'NS-113-IX'),
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-114-IX');

--Drivers
insert into drivers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, active, driver_status, car_id, current_driving_id, next_driving_id, reserved_from_client_driving_id)
values
    (nextval('users_id_gen'), true, false, 'mika@gmail.com', 'Mika', 'Mikic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511888', 1, null, 1, true, 'RESERVED', 1, null, null, null),
    (nextval('users_id_gen'), true, false, 'predrag@gmail.com', 'Predrag', 'Macogovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511877', 1, null, 1, true, 'RESERVED', 2, null,null,null),
    (nextval('users_id_gen'), true, false, 'milan@gmail.com', 'Milan', 'Maric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511811', 1, null, 1, true, 'RESERVED', 3, null,null,null),
    (nextval('users_id_gen'), true, false, 'petar@gmail.com', 'Petar', 'Ilic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511822', 1, null, 1, true, 'RESERVED', 4, null,null,null),
    (nextval('users_id_gen'), true, false, 'marko@gmail.com', 'Marko', 'Lekovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511337', 1, null, 1, true, 'RESERVED', 5,null,null,null);

insert into driver_locations(email, lat, lon)
values
    ('mika@gmail.com',45.260008, 19.808357),
    ('predrag@gmail.com', 45.246848,  19.801531),
    ('milan@gmail.com', 45.254365, 19.848213),
    ('petar@gmail.com', 45.237430, 19.731582),
    ('marko@gmail.com',45.238356, 19.808732);

insert into driver_lockers(driver_email, passenger_email, version)
values
    ('mika@gmail.com', null, 1),
    ('predrag@gmail.com', null, 1),
    ('milan@gmail.com', null, 1),
    ('petar@gmail.com', null, 1),
    ('marko@gmail.com', null, 1);


insert into passengers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, driving, current_driving_id, eth_address, secret_key, paying_using_existing_info, once_time_eth_address, once_time_secret_key, approved_paying)
values
    (nextval('users_id_gen'), true, false, 'john@gmail.com', 'John', 'Jonson', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634447899', 1, null, 2, false, null, '0x9EBD578e9ecAf0EEf82fAa07c3983C820704fe17', 'd533521eb0cc70a0660a91cedeec92fcc919f416063d0d2be1d99d58d6140929', true, null, null, false);
