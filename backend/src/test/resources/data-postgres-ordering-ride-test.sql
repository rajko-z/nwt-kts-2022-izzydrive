insert into role (name) values ('ROLE_ADMIN');
insert into role (name) values ('ROLE_DRIVER');
insert into role (name) values ('ROLE_PASSENGER');

insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    ('BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-116-IX'),
    ('BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-111-IX'),
    ('PET;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-112-IX'),
    ('FOOD;BAGGAGE', 'AVERAGE', 5, 'Audi A6', 'NS-113-IX'),
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-114-IX'),
    ('PET;BAGGAGE', 'AVERAGE', 5, 'Audi A6', 'NS-115-IX'),
    ('PET;BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-117-IX'),
    ('PET;BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-118-IX');

INSERT INTO public.adresses(latitude, longitude, name)
VALUES
    -----
    (45.2421319, 19.8000267, '32, Банијска, Телеп, МЗ Братство телеп, Нови Сад'),
    (45.2647343, 19.8294979, 'Железничка станица, Булевар Јаше Томића, Банатић, МЗ Омалдински покрет, Нови Сад'),
    ------

    --------
    (45.2314466, 19.8124555, '94, Хероја Пинкија, Телеп, МЗ Јужни телеп, Нови Сад'),
    (45.2551314,19.8058461, '7, Стојана Новаковића, Бистрица, МЗ Бистрица, Нови Сад'),
    -------------

    -- pocetna
    (45.2405031,19.8255683, '73, Цара Душана, Адамовићево насеље, МЗ 7. Јули, Нови Сад'),
    -- 1. medju --
    (45.2501044,19.824673, '8, Хајдук Вељкова, Сајмиште, МЗ Народни хероји, Нови Сад'),
    --2. medju --
    (45.2542956,19.8276181, '33, Новосадског Сајма, Сајмиште, МЗ Народни хероји, Нови Сад'),
    -- kranja --
    (45.2542534, 19.8411433, '6, Шафарикова, Роткварија, МЗ Житни трг, Нови Сад');


insert into routes(start_id, end_id)
values
    (1,2),
    (3,4),
    (5,8);

insert into intermediate_stations(route_id, address_id, station_order)
values
    (3, 6, 1),
    (3, 7, 2);


--Drivers
insert into drivers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, active, driver_status, car_id, current_driving_id, next_driving_id, reserved_from_client_driving_id)
values
    --1
    (nextval('users_id_gen'), true, false, 'mika@gmail.com', 'Mika', 'Mikic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511888', 1, null, 2, true, 'FREE', 1, null, null, null),
    --2
    (nextval('users_id_gen'), true, false, 'predrag@gmail.com', 'Predrag', 'Macogovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511877', 1, null, 2, true, 'FREE', 8, null,null,null),
    --3
    (nextval('users_id_gen'), true, false, 'milan@gmail.com', 'Milan', 'Maric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511811', 1, null, 2, true, 'FREE', 2, null,null,null),
    --4
    (nextval('users_id_gen'), true, false, 'petar@gmail.com', 'Petar', 'Ilic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511822', 1, null, 2, false, 'FREE', 3, null,null,null),
    --5
    (nextval('users_id_gen'), true, false, 'marko@gmail.com', 'Marko', 'Lekovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511337', 1, null, 2, false, 'FREE', 4,null,null,null);

insert into driver_locations(email, lat, lon)
values
    ('mika@gmail.com',45.260008, 19.808357),
    ('predrag@gmail.com', 45.246848,  19.801531),
    ('milan@gmail.com', 45.254365, 19.848213),
    ('petar@gmail.com', 45.237430, 19.731582),
    ('marko@gmail.com',45.238356, 19.808732),
    ('ljubisa@gmail.com', 45.248177, 19.839459),
    ('sava@gmail.com', 45.237163, 19.839484),
    ('milojko@gmail.com',  45.260964, 19.829406);


insert into driver_lockers(driver_email, passenger_email, version)
values
    ('mika@gmail.com', null, 1),
    ('predrag@gmail.com', null, 1),
    ('milan@gmail.com', null, 1),
    ('petar@gmail.com', null, 1),
    ('marko@gmail.com', null, 1),
    ('ljubisa@gmail.com', null, 1),
    ('sava@gmail.com', null, 1),
    ('milojko@gmail.com', null, 1);

insert into passengers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, driving, current_driving_id, eth_address, secret_key, paying_using_existing_info, once_time_eth_address, once_time_secret_key, approved_paying)
values
    --6
    (nextval('users_id_gen'), true, false, 'john@gmail.com', 'John', 'Jonson', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634447899', 1, null, 3, false, null, '0x9EBD578e9ecAf0EEf82fAa07c3983C820704fe17', 'd533521eb0cc70a0660a91cedeec92fcc919f416063d0d2be1d99d58d6140929', true, null, null, false),
    --7
    (nextval('users_id_gen'), true, false, 'natasha.lakovic@gmail.com', 'Natasa', 'Lakovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 3, false, null, null, null, false, null, null, false),
    --8
    (nextval('users_id_gen'), true, false, 'bob@gmail.com', 'Bob', 'Bobic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 3,false, null, '0x0739eb5cb09cB2acCf324eE434cFF7805d90A168', '54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799', true, null, null, false),
    --9
    (nextval('users_id_gen'), true, false, 'sara@gmail.com', 'Sara', 'Saric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 3, false,null, '0xBD25D223968485Bba38Ede0Aca128967BE127a72', 'd7e0d8e79a824146d65e15909ff3023e9c1f329e9a63f0ebb1ecf4531ba23595', true, null, null, false),
    --10
    (nextval('users_id_gen'), true, false, 'kate@gmail.com', 'Kate', 'Katen', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 3, false, null, null, null, false, null, null, false);


insert into working_intervals(start_time, end_time, driver_id)
values
    ('2023-01-06 22:10:10', '2023-01-07 00:10:15', 1),
    ('2023-01-07 05:10:10', '2023-01-07 08:15:15', 1),
    ('2023-01-07 11:15:10', '2023-01-07 12:00:15', 1),
    ('2023-01-07 22:00:00', '2023-01-07 22:15:00' , 1);


-- 1. banijska - zeleznicka
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start)
VALUES (5614.2, 'FINISHED', 441.2, '2023-01-16 18:10:00', false, null, 250, '2023-01-16 18:00:00', 1, 1, null, false, 1, 0, 0);

update passengers set current_driving_id=1 where id=6; --john
update passengers set current_driving_id=1 where id=7; --natasha
update drivers set current_driving_id=1 where id=1;

-- banijska - zeleznicka
INSERT INTO public.locations(latitude, longitude, driving_id, for_drive, locations_order)
VALUES
    (45.242176, 19.800172, 1, true, 0),
    (45.24259, 19.79992, 1, true, 1),
    (45.242367, 19.799138, 1, true, 2);