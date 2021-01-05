INSERT INTO roles(description)
VALUES ('Admin'),
       ('Manager'),
       ('Employee') ON CONFLICT DO NOTHING;
