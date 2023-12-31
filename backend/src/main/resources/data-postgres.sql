insert into role (name) values ('ROLE_ADMIN');
insert into role (name) values ('ROLE_DRIVER');
insert into role (name) values ('ROLE_PASSENGER');

insert into images (name) values ('john@gmail.com.jpg'); --1
insert into images (name) values ('marko@gmail.com.jpg'); --2
insert into images (name) values ('natasha.lakovic@gmail.com.jpg'); --3
insert into images (name) values ('bob@gmail.com.jpg'); --4
insert into images (name) values ('admin0@gmail.com.jpg'); --5
insert into images (name) values ('mika@gmail.com.jpg'); --6
insert into images (name) values ('kate@gmail.com.jpg'); --7
insert into images (name) values ('sara@gmail.com.jpg'); --8
insert into images (name) values ('petar@gmail.com.jpg'); --9
insert into images (name) values ('predrag@gmail.com.jpg'); --10
insert into images (name) values ('milan@gmail.com.jpg'); --11

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


-- password is 123 for all users
insert into admins (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id)
values
    (nextval('users_id_gen'), true, false, 'admin0@gmail.com', 'Admin0', 'Admin00', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634567811', 1, 5, 1);


--Drivers
insert into drivers (id, activated, blocked, email, first_name, last_name, password, phone_number, address_id, image_id, role_id, active, driver_status, car_id, current_driving_id, next_driving_id, reserved_from_client_driving_id)
values
    (nextval('users_id_gen'), true, false, 'mika@gmail.com', 'Mika', 'Mikic', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511888', 1, 6, 2, false, 'FREE', 1, null, null, null),
    (nextval('users_id_gen'), true, false, 'predrag@gmail.com', 'Predrag', 'Macogovic', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511877', 1, 10, 2, false, 'FREE', 8, null,null,null),
    (nextval('users_id_gen'), true, false, 'milan@gmail.com', 'Milan', 'Maric', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511811', 1, 11, 2, false, 'FREE', 2, null,null,null),
    (nextval('users_id_gen'), true, false, 'petar@gmail.com', 'Petar', 'Ilic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '+381634511822', 1, 9, 2, false, 'FREE', 3, null,null,null),
    (nextval('users_id_gen'), true, false, 'marko@gmail.com', 'Marko', 'Lekovic', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511337', 1, 2, 2, false, 'FREE', 4,null,null,null),
    (nextval('users_id_gen'), true, false, 'ljubisa@gmail.com', 'Ljubisa', 'Bobic', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511823', 1, null, 2, false, 'FREE', 5,null,null,null),
    (nextval('users_id_gen'), true, false, 'sava@gmail.com', 'Sava', 'Peric', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511855', 1, null, 2, false, 'FREE', 6, null,null,null),
    (nextval('users_id_gen'), true, false, 'milojko@gmail.com', 'Milojko', 'Dragic', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511111', 1, null, 2, false, 'FREE', 7,null,null,null);

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
    (nextval('users_id_gen'), true, false, 'john@gmail.com', 'John', 'Jonson', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634447899', 1, 1, 3, false, null, '0x9EBD578e9ecAf0EEf82fAa07c3983C820704fe17', 'd533521eb0cc70a0660a91cedeec92fcc919f416063d0d2be1d99d58d6140929', true, null, null, false),
    (nextval('users_id_gen'), true, false, 'natasha.lakovic@gmail.com', 'Natasa', 'Lakovic', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511133', 1, 3, 3, false, null, '0x8928447a9b14D7E7C1920D5fAa18b4988d6452a5', '856fa3e9eb2e0189d499c08b94d716b43760e73494eae45de273e756cb5194b5', false, null, null, false),
    (nextval('users_id_gen'), true, false, 'bob@gmail.com', 'Bob', 'Bobic', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511122', 1, 4, 3,false, null, '0x0739eb5cb09cB2acCf324eE434cFF7805d90A168', '54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799', true, null, null, false),
    (nextval('users_id_gen'), true, false, 'sara@gmail.com', 'Sara', 'Saric', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634511144', 1, 8, 3, false,null, '0xBD25D223968485Bba38Ede0Aca128967BE127a72', 'd7e0d8e79a824146d65e15909ff3023e9c1f329e9a63f0ebb1ecf4531ba23595', true, null, null, false),
    (nextval('users_id_gen'), true, false, 'kate@gmail.com', 'Kate', 'Katen', '$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG', '+381634555555', 1, 7, 3, false, null, '0xd92E11030327cE455D12116c0328134AA32DBE8e', 'cb152afdf9004fc8d60a3cbcc96dbcd53bd31425479655624f943e97150c7123', false, null, null, false);

insert into working_intervals(start_time, end_time, driver_id)
values
    ('2023-01-06 22:10:10', '2023-01-07 00:10:15', 2),
    ('2023-01-07 05:10:10', '2023-01-07 08:15:15', 2),
    ('2023-01-07 11:15:10', '2023-01-07 12:00:15', 2),
    ('2023-01-07 22:00:00', '2023-01-07 22:15:00' , 2);


INSERT INTO notification(price, reservation_date, message, duration, distance, start_location, end_location, user_email, notification_status, creation_date)
VALUES (1000, '2023-01-16 22:00:00', 'New reservation', 441.2, 5614.2, '32, Банијска, Телеп, МЗ Братство телеп, Нови Сад', 'Железничка станица, Булевар Јаше Томића, Банатић, МЗ Омалдински покрет, Нови Сад', 'john@gmail.com', 'NEW_RESERVATION', '2023-01-16 20:00:00');

INSERT INTO notification(price, reservation_date, message, duration, distance, start_location, end_location, user_email, notification_status, creation_date)
VALUES (1200, '2023-01-16 22:00:00', 'Rejected reservation', 441.2, 5614.2, '32, Банијска, Телеп, МЗ Братство телеп, Нови Сад', 'Железничка станица, Булевар Јаше Томића, Банатић, МЗ Омалдински покрет, Нови Сад', 'john@gmail.com', 'REJECTED_RESERVATION', '2023-01-16 20:10:00');


-- 1. banijska - zeleznicka
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start)
VALUES (5614.2, 'FINISHED', 441.2, '2023-02-02 18:10:00', false, null, 250, '2023-02-02 18:00:00', 2, 1, null, false, 1, 0, 0);

INSERT INTO evaluations(driver_rate, text, timestamp, vehicle_grade, driving_id)
values
(4.5, 'It was a good ride', '2023-02-02 18:12:00', '2.3', 1),
(4.1, 'Vozac je vozio vrlo prijatno i oslobodjeno, u sustini, zurio samm je rekao, ali nisam bezao ako na to mislite, ja sam zurio kad sam izasao', '2023-02-02 18:20:00', '2.0', 1),
(4.0, 'Mozda su stakla bila zamagljena....Visee', '2023-02-02 18:15:00', '4.8', 1);

-- 2 cara dusana - safarikova multistanice
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start)
VALUES (2900.9, 'FINISHED', 313.7, '2023-02-01 18:15:00', false, null, 500, '2023-02-01 18:00:00', 3, 3, null, false, 1, 0, 0);

-- 3. heroja pinkija - stojana novakovica
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start)
VALUES (3164.2, 'FINISHED', 317.5, '2023-01-26 22:10:00', false, null, 500, '2023-01-26 22:00:00', 4, 2, null, false, 1, 0, 0);

-- 4
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start)
VALUES (3164.2, 'FINISHED', 313.7, '2023-01-13 12:10:00', false, null, 1200, '2023-01-13 12:00:00', 4, 2, null, false, 1, 0, 0);
--5
INSERT INTO public.driving(
    distance, driving_state, duration, end_date, is_reservation, note, price, start_date, driver_id, route_id, reservation_date, locked, version, duration_from_driver_to_start, distance_from_driver_to_start)
VALUES (3164.2, 'FINISHED', 313.7, '2023-01-10 15:10:00', false, null, 5000, '2023-01-10 15:00:00', 4, 2, null, false, 1, 0, 0);


INSERT INTO passengers_drivings (passenger_id, driving_id)
VALUES
    (10, 1),
    (10, 2),
    (10,3),
    (10,4),
    (10,5);

INSERT INTO favourite_routes (passenger_id, route_id)
VALUES
    (10,3),
    (10,2);


-- banijska - zeleznicka
INSERT INTO public.locations(latitude, longitude, driving_id, for_drive, locations_order)
VALUES
    (45.242176, 19.800172, 1, true, 0),
    (45.24259, 19.79992, 1, true, 1),
    (45.242367, 19.799138, 1, true, 2),
    (45.242314, 19.798952, 1, true, 3),
    (45.242293, 19.798878, 1, true, 4),
    (45.242058, 19.799019, 1, true, 5),
    (45.242035, 19.799033, 1, true, 6),
    (45.241932, 19.7991, 1, true, 7),
    (45.241959, 19.798827, 1, true, 8),
    (45.241963, 19.798783, 1, true, 9),
    (45.242026, 19.798153, 1, true, 10),
    (45.242093, 19.79747, 1, true, 11),
    (45.242113, 19.797284, 1, true, 12),
    (45.242148, 19.79707, 1, true, 13),
    (45.242229, 19.796806, 1, true, 14),
    (45.242321, 19.796593, 1, true, 15),
    (45.242473, 19.796343, 1, true, 16),
    (45.242653, 19.796168, 1, true, 17),
    (45.242818, 19.796007, 1, true, 18),
    (45.243482, 19.795617, 1, true, 19),
    (45.244299, 19.795115, 1, true, 20),
    (45.244334, 19.795095, 1, true, 21),
    (45.244461, 19.795014, 1, true, 22),
    (45.244553, 19.794956, 1, true, 23),
    (45.244701, 19.794864, 1, true, 24),
    (45.244728, 19.794848, 1, true, 25),
    (45.246697, 19.793628, 1, true, 26),
    (45.246831, 19.793544, 1, true, 27),
    (45.246975, 19.793456, 1, true, 28),
    (45.248006, 19.792821, 1, true, 29),
    (45.248475, 19.792527, 1, true, 30),
    (45.248716, 19.792373, 1, true, 31),
    (45.24891, 19.792251, 1, true, 32),
    (45.24892, 19.792245, 1, true, 33),
    (45.248938, 19.792234, 1, true, 34),
    (45.248964, 19.792218, 1, true, 35),
    (45.249047, 19.792166, 1, true, 36),
    (45.249137, 19.792109, 1, true, 37),
    (45.249231, 19.79205, 1, true, 38),
    (45.249248, 19.792039, 1, true, 39),
    (45.249459, 19.791907, 1, true, 40),
    (45.250018, 19.791556, 1, true, 41),
    (45.251241, 19.790789, 1, true, 42),
    (45.252067, 19.790271, 1, true, 43),
    (45.252115, 19.79024, 1, true, 44),
    (45.25224, 19.790162, 1, true, 45),
    (45.253148, 19.789592, 1, true, 46),
    (45.253414, 19.789415, 1, true, 47),
    (45.253441, 19.789398, 1, true, 48),
    (45.253523, 19.789343, 1, true, 49),
    (45.253546, 19.789411, 1, true, 50),
    (45.253561, 19.789454, 1, true, 51),
    (45.253703, 19.789866, 1, true, 52),
    (45.253964, 19.790621, 1, true, 53),
    (45.255064, 19.793814, 1, true, 54),
    (45.255175, 19.794135, 1, true, 55),
    (45.255188, 19.794172, 1, true, 56),
    (45.255198, 19.794202, 1, true, 57),
    (45.255238, 19.794317, 1, true, 58),
    (45.25527, 19.794411, 1, true, 59),
    (45.255303, 19.794507, 1, true, 60),
    (45.255314, 19.794538, 1, true, 61),
    (45.255588, 19.795334, 1, true, 62),
    (45.256478, 19.797934, 1, true, 63),
    (45.256858, 19.799016, 1, true, 64),
    (45.25708, 19.799661, 1, true, 65),
    (45.257139, 19.799832, 1, true, 66),
    (45.25734, 19.800416, 1, true, 67),
    (45.25742, 19.800647, 1, true, 68),
    (45.257539, 19.800992, 1, true, 69),
    (45.257576, 19.801099, 1, true, 70),
    (45.257601, 19.801171, 1, true, 71),
    (45.257631, 19.801258, 1, true, 72),
    (45.257663, 19.801352, 1, true, 73),
    (45.257872, 19.801958, 1, true, 74),
    (45.258123, 19.802685, 1, true, 75),
    (45.258388, 19.803454, 1, true, 76),
    (45.258488, 19.803743, 1, true, 77),
    (45.258642, 19.804192, 1, true, 78),
    (45.259143, 19.805628, 1, true, 79),
    (45.259389, 19.806377, 1, true, 80),
    (45.259782, 19.807497, 1, true, 81),
    (45.259992, 19.808113, 1, true, 82),
    (45.259998, 19.808174, 1, true, 83),
    (45.260007, 19.808242, 1, true, 84),
    (45.260008, 19.808357, 1, true, 85),
    (45.260003, 19.808447, 1, true, 86),
    (45.259998, 19.808566, 1, true, 87),
    (45.259993, 19.808595, 1, true, 88),
    (45.25999, 19.808643, 1, true, 89),
    (45.259992, 19.80869, 1, true, 90),
    (45.259999, 19.808737, 1, true, 91),
    (45.26001, 19.808781, 1, true, 92),
    (45.260026, 19.808823, 1, true, 93),
    (45.260047, 19.808861, 1, true, 94),
    (45.26007, 19.808894, 1, true, 95),
    (45.260097, 19.808922, 1, true, 96),
    (45.260112, 19.808934, 1, true, 97),
    (45.260204, 19.809001, 1, true, 98),
    (45.260265, 19.809068, 1, true, 99),
    (45.260296, 19.809101, 1, true, 100),
    (45.26032, 19.809137, 1, true, 101),
    (45.260347, 19.809188, 1, true, 102),
    (45.260392, 19.809291, 1, true, 103),
    (45.260427, 19.809369, 1, true, 104),
    (45.260494, 19.809551, 1, true, 105),
    (45.260764, 19.810321, 1, true, 106),
    (45.260959, 19.810875, 1, true, 107),
    (45.260972, 19.810912, 1, true, 108),
    (45.260983, 19.810942, 1, true, 109),
    (45.261022, 19.811053, 1, true, 110),
    (45.261056, 19.811146, 1, true, 111),
    (45.261073, 19.811191, 1, true, 112),
    (45.261686, 19.812855, 1, true, 113),
    (45.2617, 19.812883, 1, true, 114),
    (45.261713, 19.812912, 1, true, 115),
    (45.26176, 19.813018, 1, true, 116),
    (45.26181, 19.813112, 1, true, 117),
    (45.261827, 19.813142, 1, true, 118),
    (45.261925, 19.813327, 1, true, 119),
    (45.262068, 19.813579, 1, true, 120),
    (45.26232, 19.81402, 1, true, 121),
    (45.263175, 19.815509, 1, true, 122),
    (45.263586, 19.816211, 1, true, 123),
    (45.263961, 19.816869, 1, true, 124),
    (45.263979, 19.816904, 1, true, 125),
    (45.26399, 19.816924, 1, true, 126),
    (45.264063, 19.817059, 1, true, 127),
    (45.263985, 19.81715, 1, true, 128),
    (45.263967, 19.81717, 1, true, 129),
    (45.263705, 19.817473, 1, true, 130),
    (45.263394, 19.817825, 1, true, 131),
    (45.26306, 19.81821, 1, true, 132),
    (45.262951, 19.818336, 1, true, 133),
    (45.262924, 19.818367, 1, true, 134),
    (45.262914, 19.818379, 1, true, 135),
    (45.262785, 19.818527, 1, true, 136),
    (45.262784, 19.818564, 1, true, 137),
    (45.262787, 19.818607, 1, true, 138),
    (45.262798, 19.81866, 1, true, 139),
    (45.262897, 19.818837, 1, true, 140),
    (45.262911, 19.818861, 1, true, 141),
    (45.263014, 19.819044, 1, true, 142),
    (45.263265, 19.819491, 1, true, 143),
    (45.263351, 19.819643, 1, true, 144),
    (45.263457, 19.819833, 1, true, 145),
    (45.263604, 19.8201, 1, true, 146),
    (45.263691, 19.820299, 1, true, 147),
    (45.2637, 19.820321, 1, true, 148),
    (45.263711, 19.82035, 1, true, 149),
    (45.263744, 19.820441, 1, true, 150),
    (45.263774, 19.820537, 1, true, 151),
    (45.263783, 19.820567, 1, true, 152),
    (45.263817, 19.820682, 1, true, 153),
    (45.263897, 19.82105, 1, true, 154),
    (45.263947, 19.821391, 1, true, 155),
    (45.26396, 19.821755, 1, true, 156),
    (45.263959, 19.821949, 1, true, 157),
    (45.263958, 19.822085, 1, true, 158),
    (45.263933, 19.822594, 1, true, 159),
    (45.263884, 19.823455, 1, true, 160),
    (45.263872, 19.823663, 1, true, 161),
    (45.263868, 19.823736, 1, true, 162),
    (45.263866, 19.823767, 1, true, 163),
    (45.263864, 19.823795, 1, true, 164),
    (45.263853, 19.823979, 1, true, 165),
    (45.263843, 19.824098, 1, true, 166),
    (45.26384, 19.824136, 1, true, 167),
    (45.263826, 19.824309, 1, true, 168),
    (45.263763, 19.825483, 1, true, 169),
    (45.263717, 19.826339, 1, true, 170),
    (45.263688, 19.827032, 1, true, 171),
    (45.263691, 19.827612, 1, true, 172),
    (45.263692, 19.827734, 1, true, 173),
    (45.263719, 19.828116, 1, true, 174),
    (45.263778, 19.828572, 1, true, 175),
    (45.263855, 19.829164, 1, true, 176),
    (45.263955, 19.829826, 1, true, 177),
    (45.263961, 19.829867, 1, true, 178),
    (45.263965, 19.829894, 1, true, 179),
    (45.264019, 19.8302, 1, true, 180),
    (45.264062, 19.830391, 1, true, 181),
    (45.264168, 19.830332, 1, true, 182),
    (45.264238, 19.830398, 1, true, 183),
    (45.264272, 19.830408, 1, true, 184),
    (45.264312, 19.830416, 1, true, 185),
    (45.26435, 19.83041, 1, true, 186),
    (45.264372, 19.830407, 1, true, 187),
    (45.264394, 19.830404, 1, true, 188),
    (45.264413, 19.8304, 1, true, 189),
    (45.264477, 19.83039, 1, true, 190),
    (45.264631, 19.830337, 1, true, 191),
    (45.264899, 19.830196, 1, true, 192),
    (45.265012, 19.830139, 1, true, 193),
    (45.265107, 19.830075, 1, true, 194),
    (45.265149, 19.830016, 1, true, 195),
    (45.265181, 19.829921, 1, true, 196),
    (45.26519, 19.829817, 1, true, 197),
    (45.265176, 19.829714, 1, true, 198),
    (45.26514, 19.829622, 1, true, 199),
    (45.265086, 19.829552, 1, true, 200),
    (45.265018, 19.829508, 1, true, 201),
    (45.26497, 19.829505, 1, true, 202),
    (45.264919, 19.829508, 1, true, 203),
    (45.264773, 19.829591, 1, true, 204),
    (45.264761, 19.829598, 1, true, 205);

-- cara dusana - safarikova
INSERT INTO public.locations(latitude, longitude, driving_id, for_drive, locations_order)
VALUES
    (45.240498, 19.825371, 2, true, 0),
    (45.241385, 19.825323, 2, true, 1),
    (45.24143, 19.825321, 2, true, 2),
    (45.242185, 19.82529, 2, true, 3),
    (45.243039, 19.82526, 2, true, 4),
    (45.243076, 19.825259, 2, true, 5),
    (45.243833, 19.825231, 2, true, 6),
    (45.243865, 19.825231, 2, true, 7),
    (45.243963, 19.825228, 2, true, 8),
    (45.244057, 19.825224, 2, true, 9),
    (45.245074, 19.82518, 2, true, 10),
    (45.245682, 19.82516, 2, true, 11),
    (45.245735, 19.825158, 2, true, 12),
    (45.245836, 19.825155, 2, true, 13),
    (45.246641, 19.825118, 2, true, 14),
    (45.247175, 19.825094, 2, true, 15),
    (45.247205, 19.825093, 2, true, 16),
    (45.247337, 19.825087, 2, true, 17),
    (45.248157, 19.825074, 2, true, 18),
    (45.248346, 19.825065, 2, true, 19),
    (45.248539, 19.825038, 2, true, 20),
    (45.248722, 19.824959, 2, true, 21),
    (45.248924, 19.82484, 2, true, 22),
    (45.249048, 19.824759, 2, true, 23),
    (45.249106, 19.824721, 2, true, 24),
    (45.24912, 19.82471, 2, true, 25),
    (45.249139, 19.824696, 2, true, 26),
    (45.249235, 19.824611, 2, true, 27),
    (45.249294, 19.824572, 2, true, 28),
    (45.249345, 19.824546, 2, true, 29),
    (45.249387, 19.824525, 2, true, 30),
    (45.249427, 19.824516, 2, true, 31),
    (45.249466, 19.824511, 2, true, 32),
    (45.249734, 19.824499, 2, true, 33),
    (45.250044, 19.824505, 2, true, 34),
    (45.250105, 19.824506, 2, true, 35),
    (45.250235, 19.824507, 2, true, 36),
    (45.250685, 19.824501, 2, true, 37),
    (45.250754, 19.8245, 2, true, 38),
    (45.250764, 19.8245, 2, true, 39),
    (45.250776, 19.8245, 2, true, 40),
    (45.250797, 19.824499, 2, true, 41),
    (45.250832, 19.824499, 2, true, 42),
    (45.250854, 19.824498, 2, true, 43),
    (45.253178, 19.824439, 2, true, 44),
    (45.253859, 19.824424, 2, true, 45),
    (45.25407, 19.824412, 2, true, 46),
    (45.254087, 19.824586, 2, true, 47),
    (45.254102, 19.824733, 2, true, 48),
    (45.254131, 19.824991, 2, true, 49),
    (45.254223, 19.82579, 2, true, 50),
    (45.254247, 19.826006, 2, true, 51),
    (45.254263, 19.826147, 2, true, 52),
    (45.254345, 19.826884, 2, true, 53),
    (45.254351, 19.826937, 2, true, 54),
    (45.254376, 19.827163, 2, true, 55),
    (45.254398, 19.827364, 2, true, 56),
    (45.254422, 19.827591, 2, true, 57),
    (45.254495, 19.828276, 2, true, 58),
    (45.254506, 19.828384, 2, true, 59),
    (45.254523, 19.828549, 2, true, 60),
    (45.254537, 19.828696, 2, true, 61),
    (45.254563, 19.828967, 2, true, 62),
    (45.254692, 19.830322, 2, true, 63),
    (45.2547, 19.830412, 2, true, 64),
    (45.254717, 19.830592, 2, true, 65),
    (45.254729, 19.830732, 2, true, 66),
    (45.254772, 19.831272, 2, true, 67),
    (45.254775, 19.831304, 2, true, 68),
    (45.254787, 19.831483, 2, true, 69),
    (45.254799, 19.831646, 2, true, 70),
    (45.254807, 19.831759, 2, true, 71),
    (45.254853, 19.832348, 2, true, 72),
    (45.254857, 19.832398, 2, true, 73),
    (45.254863, 19.832464, 2, true, 74),
    (45.254949, 19.833337, 2, true, 75),
    (45.254958, 19.833427, 2, true, 76),
    (45.254965, 19.833502, 2, true, 77),
    (45.254976, 19.833619, 2, true, 78),
    (45.255005, 19.83393, 2, true, 79),
    (45.255073, 19.834644, 2, true, 80),
    (45.255089, 19.834812, 2, true, 81),
    (45.255092, 19.834836, 2, true, 82),
    (45.255104, 19.83492, 2, true, 83),
    (45.255108, 19.834946, 2, true, 84),
    (45.255136, 19.835144, 2, true, 85),
    (45.255173, 19.835302, 2, true, 86),
    (45.255196, 19.835412, 2, true, 87),
    (45.255207, 19.835452, 2, true, 88),
    (45.255248, 19.835601, 2, true, 89),
    (45.255269, 19.835649, 2, true, 90),
    (45.255286, 19.83574, 2, true, 91),
    (45.2553, 19.835848, 2, true, 92),
    (45.255291, 19.835909, 2, true, 93),
    (45.255285, 19.835953, 2, true, 94),
    (45.25526, 19.83609, 2, true, 95),
    (45.255239, 19.836211, 2, true, 96),
    (45.255191, 19.836466, 2, true, 97),
    (45.255112, 19.836904, 2, true, 98),
    (45.255072, 19.837153, 2, true, 99),
    (45.255048, 19.837376, 2, true, 100),
    (45.255032, 19.837561, 2, true, 101),
    (45.255016, 19.837789, 2, true, 102),
    (45.255001, 19.838067, 2, true, 103),
    (45.254998, 19.83815, 2, true, 104),
    (45.254976, 19.83871, 2, true, 105),
    (45.254966, 19.838846, 2, true, 106),
    (45.254952, 19.83899, 2, true, 107),
    (45.254907, 19.839299, 2, true, 108),
    (45.254762, 19.840177, 2, true, 109),
    (45.254622, 19.841011, 2, true, 110),
    (45.254616, 19.84106, 2, true, 111),
    (45.254446, 19.841022, 2, true, 112),
    (45.254279, 19.840991, 2, true, 113),
    (45.254193, 19.840981, 2, true, 114),
    (45.254192, 19.841019, 2, true, 115),
    (45.254175, 19.841104, 2, true, 116),
    (45.254175, 19.841104, 2, true, 117);


-- heroja pinkija - stojana novakovica
INSERT INTO public.locations(latitude, longitude, driving_id, for_drive, locations_order)
VALUES
    (45.231324, 19.812617, 3, true, 0),
    (45.23104, 19.812177, 3, true, 1),
    (45.231061, 19.811863, 3, true, 2),
    (45.231167, 19.810876, 3, true, 3),
    (45.231205, 19.810517, 3, true, 4),
    (45.231263, 19.809945, 3, true, 5),
    (45.231311, 19.809474, 3, true, 6),
    (45.232145, 19.809638, 3, true, 7),
    (45.23218, 19.809645, 3, true, 8),
    (45.233003, 19.809808, 3, true, 9),
    (45.233442, 19.809886, 3, true, 10),
    (45.233864, 19.809965, 3, true, 11),
    (45.234703, 19.810122, 3, true, 12),
    (45.235424, 19.810255, 3, true, 13),
    (45.235558, 19.810279, 3, true, 14),
    (45.236011, 19.810368, 3, true, 15),
    (45.236401, 19.810451, 3, true, 16),
    (45.237207, 19.810612, 3, true, 17),
    (45.237633, 19.810699, 3, true, 18),
    (45.237749, 19.810719, 3, true, 19),
    (45.238105, 19.810783, 3, true, 20),
    (45.238435, 19.810847, 3, true, 21),
    (45.238861, 19.810927, 3, true, 22),
    (45.238951, 19.810944, 3, true, 23),
    (45.239749, 19.811102, 3, true, 24),
    (45.239787, 19.811109, 3, true, 25),
    (45.240075, 19.811168, 3, true, 26),
    (45.240166, 19.81119, 3, true, 27),
    (45.24021, 19.811221, 3, true, 28),
    (45.240254, 19.811249, 3, true, 29),
    (45.240303, 19.811275, 3, true, 30),
    (45.240374, 19.81131, 3, true, 31),
    (45.240459, 19.811349, 3, true, 32),
    (45.240475, 19.811355, 3, true, 33),
    (45.240529, 19.811376, 3, true, 34),
    (45.240555, 19.811384, 3, true, 35),
    (45.240635, 19.811414, 3, true, 36),
    (45.240684, 19.811429, 3, true, 37),
    (45.240712, 19.811436, 3, true, 38),
    (45.24073, 19.811442, 3, true, 39),
    (45.240812, 19.811459, 3, true, 40),
    (45.240828, 19.811461, 3, true, 41),
    (45.240854, 19.811465, 3, true, 42),
    (45.240887, 19.811469, 3, true, 43),
    (45.240918, 19.811469, 3, true, 44),
    (45.240949, 19.81146, 3, true, 45),
    (45.240964, 19.811456, 3, true, 46),
    (45.240983, 19.811448, 3, true, 47),
    (45.241005, 19.811427, 3, true, 48),
    (45.241048, 19.811368, 3, true, 49),
    (45.241449, 19.811137, 3, true, 50),
    (45.242975, 19.810256, 3, true, 51),
    (45.24447, 19.809362, 3, true, 52),
    (45.244957, 19.809077, 3, true, 53),
    (45.244999, 19.809052, 3, true, 54),
    (45.245413, 19.808814, 3, true, 55),
    (45.246255, 19.808328, 3, true, 56),
    (45.246384, 19.808249, 3, true, 57),
    (45.24673, 19.808039, 3, true, 58),
    (45.247023, 19.807864, 3, true, 59),
    (45.247221, 19.807798, 3, true, 60),
    (45.247379, 19.807764, 3, true, 61),
    (45.247437, 19.807751, 3, true, 62),
    (45.247461, 19.807747, 3, true, 63),
    (45.24748, 19.807744, 3, true, 64),
    (45.247519, 19.807734, 3, true, 65),
    (45.247613, 19.807711, 3, true, 66),
    (45.247665, 19.807693, 3, true, 67),
    (45.247728, 19.807671, 3, true, 68),
    (45.247846, 19.807624, 3, true, 69),
    (45.248426, 19.807392, 3, true, 70),
    (45.24885, 19.807188, 3, true, 71),
    (45.249082, 19.80707, 3, true, 72),
    (45.249248, 19.806966, 3, true, 73),
    (45.249828, 19.806581, 3, true, 74),
    (45.251234, 19.805633, 3, true, 75),
    (45.251268, 19.805612, 3, true, 76),
    (45.251312, 19.805581, 3, true, 77),
    (45.252131, 19.805014, 3, true, 78),
    (45.252453, 19.8048, 3, true, 79),
    (45.252849, 19.804538, 3, true, 80),
    (45.253009, 19.804578, 3, true, 81),
    (45.25307, 19.804593, 3, true, 82),
    (45.253085, 19.804597, 3, true, 83),
    (45.253099, 19.804603, 3, true, 84),
    (45.253155, 19.804628, 3, true, 85),
    (45.253214, 19.80469, 3, true, 86),
    (45.253296, 19.804817, 3, true, 87),
    (45.253404, 19.805018, 3, true, 88),
    (45.253594, 19.805576, 3, true, 89),
    (45.253988, 19.806734, 3, true, 90),
    (45.254, 19.80677, 3, true, 91),
    (45.254011, 19.806799, 3, true, 92),
    (45.254048, 19.80691, 3, true, 93),
    (45.254116, 19.806865, 3, true, 94),
    (45.254223, 19.806794, 3, true, 95),
    (45.254244, 19.80678, 3, true, 96),
    (45.254264, 19.806764, 3, true, 97),
    (45.254331, 19.80672, 3, true, 98),
    (45.254456, 19.806639, 3, true, 99),
    (45.254519, 19.806598, 3, true, 100),
    (45.254559, 19.806572, 3, true, 101),
    (45.255007, 19.806254, 3, true, 102),
    (45.255179, 19.806138, 3, true, 103),
    (45.25522, 19.80611, 3, true, 104);