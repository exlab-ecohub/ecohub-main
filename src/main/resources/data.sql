INSERT INTO ROLES (name)
VALUES ('ROLE_SUPERADMIN'),
       ('ROLE_ADMIN'),
       ('ROLE_USER');

-- todo пароли должны быть закодированы, как при создании нового пользователя
-- INSERT INTO USERS (username, password, email, role_id)
-- VALUES ('superadmin', 'superadmin', null, 1),
--        ('admin', 'adminadmin', null, 2);

INSERT INTO RECYCLABLE_TYPES (name)
VALUES ('PLASTIC'),
       ('PAPER'),
       ('GLASS'),
       ('BULKY_WASTE'),
       ('APPLIANCES'),
       ('HAZARDOUS_WASTE'),
       ('METAL'),
       ('CLOTHES');