/**
 * CREATE Script for init of DB
 */

-- Create 3 users

insert into user (id,age,city, activated, is_blocked, password, email, role) values 
(1,20,'cairo', true, false,'user01pw', 'user01@test.com', 'ROLE_USER');

insert into user (id,age,city, activated, is_blocked, password, email, role) values 
(2,20,'cairo', true, false, 'user02pw', 'user02@test.com', 'ROLE_MANAGER');

insert into user (id,age,city, activated, is_blocked, password, email, role) values 
(3,20,'cairo', true, false, 'user03pw', 'user03@test.com', 'ROLE_ADMIN');

--create location
INSERT INTO location (id, latitutde, location_name, longtude) 
VALUES (1, '123.23', 'cairo', '1233.222');

-- creat some jogs for testing
INSERT INTO jogging (id, date,distance, period_in_minutes, location_id, user_id) 
VALUES (1, '2017-12-20 15:11:26', '100', '10', '1', '1');

INSERT INTO jogging (id, date,distance, period_in_minutes, location_id, user_id) 
VALUES (2, '2017-12-21 15:11:26', '1000', '100', '1', '1');

INSERT INTO jogging (id, date,distance, period_in_minutes, location_id, user_id) 
VALUES (3,'2017-12-22 15:11:26', '1200', '60', '1', '1');

INSERT INTO jogging (id, date,distance, period_in_minutes, location_id, user_id) 
VALUES (4, '2017-12-23 15:11:26', '1000', '60', '1', '1');
