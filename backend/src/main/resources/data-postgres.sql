insert into role (name) values ('ROLE_ADMIN');
insert into role (name) values ('ROLE_DRIVER');
insert into role (name) values ('ROLE_PASSENGER');

insert into images (name) values ('john@gmail.com.jpg');

insert into adresses (city, latitude, longitude, state, street) values ('Novi Sad', 45.242101, 19.800049, 'Serbia', 'Banijska 32');


insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
('BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-116IX');

-- password is 123 for all users
insert into admins (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id)
values
(nextval('users_id_gen'), true, false, 'admin0@gmail.com', 'Admin0', 'Admin00', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634567811', 1, 1, 1 );

insert into drivers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, active, driver_status, car_id, next_driving_id, reserved_from_client_driving_id)
values
(nextval('users_id_gen'), true, false, 'mika@gmail.com', 'Mika', 'Mikic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511812', 1, 1, 2, true, 'FREE', 1, null, null);

insert into passengers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, driving, current_driving_id)
values
(nextval('users_id_gen'), true, false, 'john@gmail.com', 'John', 'Jonson', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634447822', 1, null, 3, false, null);
-- (nextval('users_id_gen'), true, false, 'natasha.lakovic@gmail.com', 'Natasa', 'Lakovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381620047822', 1, null, 3, false, null);