/**
 * CREATE Script for init of DB
 */

-- Create 3 users

insert into user (id, activated, is_blocked, password, email, role) values 
(1, true, false,'user01pw', 'user01', 'ROLE_USER');

insert into user (id, activated, is_blocked, password, email, role) values 
(2, true, false, 'user02pw', 'user02', 'ROLE_MANAGER');

insert into user (id, activated, is_blocked, password, email, role) values 
(3, true, false, 'user03pw', 'user03', 'ROLE_ADMIN');

--create location
INSERT INTO location (id, latitutde, location_name, longtude) 
VALUES (1, '123.23', 'cairo', '1233.222');

-- creat some jogs for testing
INSERT INTO jogging (id, date,distance, period_in_minutes, location_id, user_id) 
VALUES (1, '2017-12-12 15:11:26', '100', '10', '1', '1');

INSERT INTO jogging (id, date,distance, period_in_minutes, location_id, user_id) 
VALUES (2, '2017-12-11 15:11:26', '1000', '100', '1', '1');

INSERT INTO jogging (id, date,distance, period_in_minutes, location_id, user_id) 
VALUES (3,'2017-12-10 15:11:26', '1200', '60', '1', '1');

INSERT INTO jogging (id, date,distance, period_in_minutes, location_id, user_id) 
VALUES (4, '2017-12-09 15:11:26', '1000', '60', '1', '1');
