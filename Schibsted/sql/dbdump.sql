CREATE TABLE users (
	user_id integer PRIMARY KEY,
	user_name text UNIQUE NOT NULL,
	user_password text NOT NULL
);

INSERT INTO users(user_name, user_password) VALUES('admin', 'd033e22ae348aeb5660fc2140aec35850c4da997');
INSERT INTO users(user_name, user_password) VALUES('user1', 'f0578f1e7174b1a41c4ea8c6e17f7a8a3b88c92a');
INSERT INTO users(user_name, user_password) VALUES('user2', '8be52126a6fde450a7162a3651d589bb51e9579d');
INSERT INTO users(user_name, user_password) VALUES('user3', 'de2a4d5751ab06dc4f987142db57c26d50925c8a');
INSERT INTO users(user_name, user_password) VALUES('user4', '2db4c1811f424582a90f8d7ee77995cf018d9443');
INSERT INTO users(user_name, user_password) VALUES('user5', '9e5ca6b0ffb417997ffb844c76f9c24bbc20fe88');

CREATE TABLE roles (
	role_id integer PRIMARY KEY,
	role_name text DEFAULT '',
	role_page text DEFAULT ''
);

INSERT INTO roles(role_name) VALUES('ADMIN');
INSERT INTO roles(role_name, role_page) VALUES('PAGE_1', 'page_1');
INSERT INTO roles(role_name, role_page) VALUES('PAGE_2', 'page_2');
INSERT INTO roles(role_name, role_page) VALUES('PAGE_3', 'page_3');

CREATE TABLE user_has_role (
	fk_user_id integer,
	fk_role_id integer
);

INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(1, 1);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(1, 2);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(1, 3);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(1, 4);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(2, 2);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(3, 3);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(4, 4);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(5, 2);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(5, 3);
INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(5, 4);