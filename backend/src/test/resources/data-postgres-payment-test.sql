insert into role (name) values ('ROLE_DRIVER');
insert into role (name) values ('ROLE_PASSENGER');


INSERT INTO public.adresses(latitude, longitude, name)
VALUES
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

insert into cars (car_accommodations, car_type, max_num_of_passengers, model, registration)
values
    --1
    ('BABY;BAGGAGE', 'PREMIUM', 5, 'Audi A6', 'NS-100-IX'),
    --2
    ('BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-101-IX'),
    --3
    ('PET;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-102-IX'),
    --4
    ('FOOD;BAGGAGE', 'AVERAGE', 5, 'Audi A6', 'NS-103-IX'),
    --5
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-104-IX'),
    --6
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-105-IX'),
    --7
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-106-IX'),
    --8
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-107-IX'),
    --9
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-108-IX'),
    --10
    ('BABY;BAGGAGE', 'REGULAR', 5, 'Audi A6', 'NS-109-IX');

--Drivers
insert into drivers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, active, driver_status, car_id, current_driving_id, next_driving_id, reserved_from_client_driving_id)
values
    --1
    (nextval('users_id_gen'), true, false, 'mika@gmail.com', 'Mika', 'Mikic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511888', 1, null, 1, true, 'RESERVED', 1, null, null, null),
    --2
    (nextval('users_id_gen'), true, false, 'predrag@gmail.com', 'Predrag', 'Macogovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511877', 1, null, 1, true, 'FREE', 2, null,null,null),
    --3
    (nextval('users_id_gen'), true, false, 'milan@gmail.com', 'Milan', 'Maric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511811', 1, null, 1, true, 'FREE', 3, null,null,null),
    --4
    (nextval('users_id_gen'), true, false, 'petar@gmail.com', 'Petar', 'Ilic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511822', 1, null, 1, true, 'RESERVED', 4, null,null,null),
    --5
    (nextval('users_id_gen'), true, false, 'marko@gmail.com', 'Marko', 'Lekovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511337', 1, null, 1, true, 'FREE', 5,null,null,null),
    --6
    (nextval('users_id_gen'), true, false, 'd_test6@gmail.com', 'Test6', 'Test6', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511337', 1, null, 1, true, 'FREE', 6,null,null,null),
    --7
    (nextval('users_id_gen'), true, false, 'd_testd7@gmail.com', 'Test7', 'Test7', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511337', 1, null, 1, true, 'FREE', 7,null,null,null),
    --8
    (nextval('users_id_gen'), true, false, 'd_test8@gmail.com', 'Test8', 'Test8', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511337', 1, null, 1, true, 'FREE', 8,null,null,null),
    --9
    (nextval('users_id_gen'), true, false, 'd_test9@gmail.com', 'Test9', 'Test9', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511337', 1, null, 1, true, 'FREE', 9,null,null,null),
    --10
    (nextval('users_id_gen'), true, false, 'd_test10@gmail.com', 'Test10', 'Test10', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511337', 1, null, 1, true, 'FREE', 10,null,null,null);

insert into driver_locations(email, lat, lon)
values
    ('mika@gmail.com',45.260008, 19.808357),
    ('predrag@gmail.com', 45.246848,  19.801531),
    ('milan@gmail.com', 45.254365, 19.848213),
    ('petar@gmail.com', 45.237430, 19.731582),
    ('marko@gmail.com',45.238356, 19.808732),
    ('d_test6@gmail.com',45.238356, 19.808732),
    ('d_test7@gmail.com',45.238356, 19.808732),
    ('d_test8@gmail.com',45.238356, 19.808732),
    ('d_test9@gmail.com',45.238356, 19.808732),
    ('d_test10@gmail.com',45.238356, 19.808732);

insert into driver_lockers(driver_email, passenger_email, version)
values
    ('mika@gmail.com', null, 1),
    ('predrag@gmail.com', null, 1),
    ('milan@gmail.com', null, 1),
    ('petar@gmail.com', null, 1),
    ('marko@gmail.com', null, 1),
    ('d_test6@gmail.com', null, 1),
    ('d_test7@gmail.com', null, 1),
    ('d_test8@gmail.com', null, 1),
    ('d_test9@gmail.com', null, 1),
    ('d_test10@gmail.com', null, 1);

insert into passengers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, driving, current_driving_id, eth_address, secret_key, paying_using_existing_info, once_time_eth_address, once_time_secret_key, approved_paying)
values
    --11
    (nextval('users_id_gen'), true, false, 'john@gmail.com', 'John', 'Jonson', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634447899', 1, null, 2, false, null, '0x9EBD578e9ecAf0EEf82fAa07c3983C820704fe17', 'd533521eb0cc70a0660a91cedeec92fcc919f416063d0d2be1d99d58d6140929', true, null, null, false),
    --12
    (nextval('users_id_gen'), true, false, 'sara@gmail.com', 'Sara', 'Saric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2, false,null, '0xBD25D223968485Bba38Ede0Aca128967BE127a72', 'd7e0d8e79a824146d65e15909ff3023e9c1f329e9a63f0ebb1ecf4531ba23595', true, null, null, false),
    --13
    (nextval('users_id_gen'), true, false, 'kate@gmail.com', 'Kate', 'Katen', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2, false, null, null, null, false, null, null, false),
    --14
    (nextval('users_id_gen'), true, false, 'bob@gmail.com', 'Bob', 'Bobic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, '0x0739eb5cb09cB2acCf324eE434cFF7805d90A168', '54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799', true, null, null, false),
    --15
    (nextval('users_id_gen'), true, false, 'test1@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, '0x0739eb5cb09cB2acCf324eE434cFF7805d90A168', '54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799', true, null, null, false),
    --16
    (nextval('users_id_gen'), true, false, 'test2@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, '0x0739eb5cb09cB2acCf324eE434cFF7805d90A168', '54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799', true, null, null, true),
    --17
    (nextval('users_id_gen'), true, false, 'test3@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, null, null, true, null, null, true),
    --18
    (nextval('users_id_gen'), true, false, 'test4@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, null, null, true, null, null, false),
    --19
    (nextval('users_id_gen'), true, false, 'test5@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, null,null, true, null, null, false),
    --20
    (nextval('users_id_gen'), true, false, 'test6@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, null, null, true, null, null, false),
    --21
    (nextval('users_id_gen'), true, false, 'test7@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, '0x8928447a9b14D7E7C1920D5fAa18b4988d6452a5', '856fa3e9eb2e0189d499c08b94d716b43760e73494eae45de273e756cb5194b5', true, null, null, false),
    --22
    (nextval('users_id_gen'), true, false, 'test8@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, '0x0739eb5cb09cB2acCf324eE434cFF7805d90A168', '54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799', true, null, null, false),
    --23
    (nextval('users_id_gen'), true, false, 'test9@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, '0x0739eb5cb09cB2acCf324eE434cFF7805d90A168', '54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799', true, null, null, false),
    --24
    (nextval('users_id_gen'), true, false, 'test10@gmail.com', 'Test', 'Test', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456', 1, null, 2,false, null, '0x0739eb5cb09cB2acCf324eE434cFF7805d90A168', '54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799', true, null, null, false);



-- 1
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start, creation_date)
VALUES (5614.2, 'PAYMENT', 441.2, '2023-01-16 18:10:00', false, null, 250, null, 6, null, null, false, 1, 0, 0, '2023-01-10 18:10:00');
-- driver id je test6
update passengers set current_driving_id=1 where id=16; --test2

--2
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start, creation_date)
VALUES (5614.2, 'PAYMENT', 441.2, '2023-01-16 18:10:00', false, null, 250, null, 7, null, null, false, 1, 0, 0, current_timestamp);
-- driver id je test7
update passengers set current_driving_id=2 where id=17; --test3


--3
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start, creation_date)
VALUES (5614.2, 'PAYMENT', 441.2, '2023-01-16 18:10:00', false, null, 250, null, 8, null, null, false, 1, 0, 0, current_timestamp);
-- driver id je test8
update passengers set current_driving_id=3 where id=18; --test4


--4
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start, creation_date)
VALUES (5614.2, 'PAYMENT', 441.2, '2023-01-16 18:10:00', false, null, 250, null, 9, null, null, false, 1, 0, 0, current_timestamp);
-- driver id je test9
update passengers set current_driving_id=4 where id=19; --test5


--5
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start, creation_date)
VALUES (5614.2, 'PAYMENT', 441.2, '2023-01-16 18:10:00', false, null, 250, null, 10, null, null, false, 1, 0, 0, current_timestamp);
-- driver id je test10
update passengers set current_driving_id=5 where id=20; --test6


--6
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start, creation_date)
VALUES (5614.2, 'PAYMENT', 441.2, '2023-01-16 18:10:00', false, null, 10, null, 5, 1, null, false, 1,50, 100, current_timestamp);
-- driver id je 5 marko
update passengers set current_driving_id=6 where id=14; --bob
update drivers set current_driving_id=6 where id=5;



--7 sara i john
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start, creation_date)
VALUES (5614.2, 'PAYMENT', 441.2, '2023-01-16 18:10:00', false, null, 3, null, 5, 1, null, false, 1,50, 100, current_timestamp);
-- driver id je 3 milan
update passengers set current_driving_id=7 where id=11; --john
update passengers set current_driving_id=7 where id=12; --sara

update drivers set current_driving_id=7 where id=3;



--8
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start, creation_date)
VALUES (5614.2, 'PAYMENT', 441.2, '2023-01-16 18:10:00', false, null, 3, null, 2, 1, null, false, 1,50, 100, current_timestamp);
-- driver id je 2
update passengers set current_driving_id=8 where id=21; -- test7
update passengers set current_driving_id=8 where id=22; --test8

update drivers set current_driving_id=8 where id=2;