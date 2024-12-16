--liquibase formatted sql
--changeset ism:create-new-users

INSERT INTO users (email, password, role) VALUES ('admin@admin.ru', '$2a$10$dTki50TTyfNFoK1cC/HJY.gV4rYLc/uuB5JmnboPILxta62deLxRO', 'ROLE_ADMIN');
