/**
 * CREATE Script for init of DB
 */

-- Create 3 users

insert into user (active, password, email, role) values 
(false,'user01pw', 'user01', 'ROLE_USER');

insert into user (active, password, email, role) values 
(true,'user02pw', 'user02', 'ROLE_MANAGER');

insert into user (active, password, email, role) values 
(true,'user03pw', 'user03', 'ROLE_ADMIN');