INSERT INTO user(id, email, password)
VALUES (1, 'admin@byom.de', '{noop}admin'), (2, 'nati@byom.de', '{noop}1234'), (3, 'krzychu@byom.de', '{noop}1234');

INSERT INTO user_role(user_id, role)
VALUES (1, 'ROLE_ADMIN'), (1, 'ROLE_USER'), (2, 'ROLE_USER'), (3, 'ROLE_USER');