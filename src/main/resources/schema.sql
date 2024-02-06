CREATE TABLE `user`
(
    `id`         bigint AUTO_INCREMENT PRIMARY KEY,
    `email_address`      varchar(150) NOT NULL,
    `login`      varchar(150) NOT NULL,
    `password`   varchar(255) NOT NULL,
    `user_role`       varchar(255) NOT NULL,
    `creation_date` timestamp    NOT NULL
);

CREATE TABLE `ticket`
(
    `id`            bigint AUTO_INCREMENT PRIMARY KEY,
    `user_id`       bigint       NOT NULL,
    `subject`       varchar(355) NOT NULL,
    `creation_date` timestamp    NOT NULL,
    `description`   varchar(500) NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `chat_message`
(
    `id`            bigint AUTO_INCREMENT PRIMARY KEY,
    `message`       varchar(355) NOT NULL,
    `sender_id`     bigint       NOT NULL,
    `receiver_id`   bigint       NOT NULL,
    `creation_date` timestamp    NOT NULL,
    `ticket_id`     bigint       NOT NULL,
    FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`)
);

CREATE TABLE `exercise`
(
    `id`            bigint AUTO_INCREMENT PRIMARY KEY,
    `name`          varchar(300) NOT NULL,
    `muscle_group`  varchar(255) NOT NULL,
    `user_id`       bigint,
    `admin_created` boolean,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
CREATE TABLE `training_routine`
(
    `id`           bigint AUTO_INCREMENT PRIMARY KEY,
    `routine_name` varchar(500) NOT NULL,
    `user_id`      bigint       NOT NULL,
    `is_archived`  boolean,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
CREATE TABLE `training_session`
(
    `id`            bigint AUTO_INCREMENT PRIMARY KEY,
    `routine_id`    bigint    NOT NULL,
    `user_id`       bigint    NOT NULL,
    `training_date` timestamp NOT NULL,
    FOREIGN KEY (`routine_id`) REFERENCES `training_routine` (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
CREATE TABLE `training_log`
(
    `id`                  bigint AUTO_INCREMENT PRIMARY KEY,
    `reps`                int    NOT NULL,
    `weight`              int,
    `exercise_id`         bigint,
    `training_session_id` bigint NOT NULL,
    `personal_notes`      varchar(300),
    FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`id`),
    FOREIGN KEY (`training_session_id`) REFERENCES `training_session` (`id`)
);

CREATE TABLE `training_routine_exercise`
(
    `training_routine_id` bigint NOT NULL,
    `exercise_id`         bigint NOT NULL,
    PRIMARY KEY (`training_routine_id`, `exercise_id`),
    FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`id`),
    FOREIGN KEY (`training_routine_id`) REFERENCES `training_routine` (`id`)
);
