/**
 * CREATE Script for init of DB
 */

-- Create 3 users

insert into user (activated, is_blocked, password, email, role) values 
(false, false,'user01pw', 'user01', 'ROLE_USER');

insert into user (activated, is_blocked, password, email, role) values 
(true, false, 'user02pw', 'user02', 'ROLE_MANAGER');

insert into user (activated, is_blocked, password, email, role) values 
(true, false, 'user03pw', 'user03', 'ROLE_ADMIN');