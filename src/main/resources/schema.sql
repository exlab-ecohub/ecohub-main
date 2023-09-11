DROP TABLE IF EXISTS
    TOKENS,
    FEEDBACKS,
    USERS,
    ROLES,
    RECYCLING_POINTS,
    WORKING_HOURS,
    RECYCLABLE_TYPES,
    RECYCLING_POINTS_RECYCLABLE_TYPES;

CREATE TABLE IF NOT EXISTS ROLES
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar(20) UNIQUE                                  NOT NULL
);

CREATE TABLE IF NOT EXISTS USERS
(
    id       bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    username varchar(20) UNIQUE                                  NOT NULL,
    password varchar                                             NOT NULL,
    email    varchar(50) UNIQUE,
    role_id  bigint REFERENCES ROLES (id) ON DELETE CASCADE      NOT NULL
);

CREATE TABLE IF NOT EXISTS TOKENS
(
    id            bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    refresh_token varchar UNIQUE                                      NOT NULL,
    token_type    varchar                                             NOT NULL,
    token_purpose varchar                                             NOT NULL,
    remember_me   boolean,
    revoked       boolean,
    expired       boolean,
    user_id       bigint REFERENCES USERS (id) ON DELETE CASCADE      NOT NULL
);


CREATE TABLE IF NOT EXISTS FEEDBACKS
(
    id                      serial NOT NULL,
    user_id                 bigint REFERENCES USERS (id) ON DELETE CASCADE,
    name                    character varying,
    email                   character varying,
    message_topic           character varying,
    message_content         text,
    message_time            timestamp,
    admin_id                bigint REFERENCES USERS (id) ON DELETE CASCADE,
    response_content        text,
    response_time           timestamp,
    response_status         character varying,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS RECYCLING_POINTS
(
    id           bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name         varchar(100),
    address      varchar(100),
    phone_number varchar(30),
    website      varchar(100),
    latitude     numeric,
    longitude    numeric,
    displayed    boolean
);

CREATE TABLE IF NOT EXISTS WORKING_HOURS
(
    recycling_point_id bigint REFERENCES RECYCLING_POINTS (id) ON DELETE CASCADE,
    day_of_week        smallint,
    opening_time       time,
    closing_time       time,
    lunch_start_time   time,
    lunch_end_time     time,
    PRIMARY KEY (recycling_point_id, day_of_week)
);

CREATE TABLE IF NOT EXISTS RECYCLABLE_TYPES
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar(100) UNIQUE                                 NOT NULL
);

CREATE TABLE IF NOT EXISTS RECYCLING_POINTS_RECYCLABLE_TYPES
(
    recycling_point_id bigint REFERENCES RECYCLING_POINTS (id) ON DELETE CASCADE,
    recyclable_type_id bigint REFERENCES RECYCLABLE_TYPES (id) ON DELETE CASCADE,
    PRIMARY KEY (recycling_point_id, recyclable_type_id)
);