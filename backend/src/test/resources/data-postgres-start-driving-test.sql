insert into role (name)
values ('ROLE_ADMIN');
insert into role (name)
values ('ROLE_DRIVER');
insert into role (name)
values ('ROLE_PASSENGER');

insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values ('BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-116-IX');

INSERT INTO public.adresses(latitude, longitude, name)
VALUES
    -----
    (45.2421319, 19.8000267, '32, Банијска, Телеп, МЗ Братство телеп, Нови Сад'),
    (45.2647343, 19.8294979, 'Железничка станица, Булевар Јаше Томића, Банатић, МЗ Омалдински покрет, Нови Сад');
------


insert into routes(start_id, end_id)
values (1, 2);

--Drivers
insert into drivers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id,
                     role_id, active, driver_status, car_id, current_driving_id, next_driving_id,
                     reserved_from_client_driving_id)
values (nextval('users_id_gen'), true, false, 'mika@gmail.com', 'Mika', 'Mikic',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511888', 1, null, 2, true, 'TAKEN', 1,
        null, null, null);

insert into driver_locations(email, lat, lon)
values ('mika@gmail.com', 45.260008, 19.808357);


insert into driver_lockers(driver_email, passenger_email, version)
values ('mika@gmail.com', null, 1);

insert into passengers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id,
                        image_id, role_id, driving, current_driving_id, eth_address, secret_key,
                        paying_using_existing_info, once_time_eth_address, once_time_secret_key, approved_paying)
values (nextval('users_id_gen'), true, false, 'john@gmail.com', 'John', 'Jonson',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634447899', 1, null, 3, false, null,
        '0x9EBD578e9ecAf0EEf82fAa07c3983C820704fe17',
        'd533521eb0cc70a0660a91cedeec92fcc919f416063d0d2be1d99d58d6140929', true, null, null, false);


-- 1. banijska - zeleznicka
INSERT INTO public.driving(distance, driving_state, duration, end_date, is_reservation, note, price, start_date,
                           driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start,
                           distance_from_driver_to_start)
VALUES (5614.2, 'WAITING', 441.2, null, false, null, 250, '2023-01-30 15:40:00', 1, 1, null, false, 1,
        0, 0);

UPDATE drivers
SET current_driving_id=1
WHERE id = 1;
UPDATE passengers
SET current_driving_id=1
WHERE id = 2;

-- banijska - zeleznicka
INSERT INTO public.locations(latitude, longitude, driving_id, for_drive, locations_order)
VALUES (45.242176, 19.800172, 1, false, 0),
       (45.24259, 19.79992, 1, false, 1),
       (45.242176, 19.800172, 1, true, 2),
       (45.24259, 19.79992, 1, true, 3),
       (45.242367, 19.799138, 1, true, 4),
       (45.242314, 19.798952, 1, true, 5),
       (45.242293, 19.798878, 1, true, 6),
       (45.242058, 19.799019, 1, true, 7),
       (45.242035, 19.799033, 1, true, 8);