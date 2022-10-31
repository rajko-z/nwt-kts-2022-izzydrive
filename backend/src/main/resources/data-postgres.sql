insert into role (name) values ('ROLE_ADMIN');
insert into role (name) values ('ROLE_DRIVER');
insert into role (name) values ('ROLE_PASSENGER');

insert into adresses (city, latitude, longitude, state, street) values ('Novi Sad', 45.242101, 19.800049, 'Serbia', 'Banijska 32');

insert into images (name) values ('audiA6.png');

insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration, image_id)
values
('BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-116IX', 1);

-- password is 123 for all users
insert into admins (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id)
values
(nextval('users_id_gen'), true, false, 'admin0@gmail.com', 'Admin0', 'Admin00', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 1 );

insert into drivers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, active, driver_status, car_id, next_driving_id, reserved_from_client_driving_id)
values
(nextval('users_id_gen'), true, false, 'mika@gmail.com', 'Mika', 'Mikic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2, true, 'FREE', 1, null, null);

insert into passengers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, driving, current_driving_id)
values
(nextval('users_id_gen'), true, false, 'john@gmail.com', 'John', 'Jonson', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 3, false, null);