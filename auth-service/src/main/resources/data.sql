-- Insert initial permissions

INSERT INTO permissions (name, resource, action, description)
VALUES ('CREATE_QUIZ', 'QUIZ', 'CREATE', 'Create new quizzes'),

       ('READ_QUIZ', 'QUIZ', 'READ', 'View quizzes'),

       ('UPDATE_QUIZ', 'QUIZ', 'UPDATE', 'Update own quizzes'),

       ('DELETE_QUIZ', 'QUIZ', 'DELETE', 'Delete own quizzes'),

       ('CREATE_QUESTIONS', 'QUESTION', 'CREATE', 'Create questions'),

       ('READ_QUESTIONS', 'QUESTION', 'READ', 'Read questions'),

       ('UPDATE_QUESTIONS', 'QUESTION', 'UPDATE', 'Update questions'),

       ('DELETE_QUESTION', 'QUESTION', 'DELETE', 'Delete own questions'),

       ('MANAGE_USERS', 'USER', 'MANAGE', 'Manage all users');


-- Insert roles

INSERT INTO roles (name, description)
VALUES ('USER', 'Regular user'),

       ('ADMIN', 'Administrator'),

       ('MODERATOR', 'Content moderator');


-- Assign permissions to roles

INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8), -- USER role

       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (2, 8),-- ADMIN role (all permissions)

       (3, 9); -- MODERATOR role