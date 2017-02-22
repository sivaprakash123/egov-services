INSERT INTO eg_user (
id, title, salutation, dob, locale, username, password, pwd_expiry_date, mobile_number, alt_contact_number, email_id, created_date,
last_modified_date, created_by, last_modified_by, active, name, gender, pan, aadhaar_number, type, version, guardian, guardian_relation
)
VALUES (
1, 'title', 'Mrs', '1990-07-23 00:00:00', 'en_IN', 'userName1', '$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK',
'2020-12-31 00:00:00', '9731123456', '080292575', 'email1@gmail.com', '2010-01-01 00:00:00', '2015-01-01 00:00:00', 0, 0, true,
'Name of the user1', 0, 'ABCDE1234F', '12346789011', 'EMPLOYEE', 0, 'Guardian name', 'Guardian Relation');

INSERT INTO eg_user (
id, title, salutation, dob, locale, username, password, pwd_expiry_date, mobile_number, alt_contact_number, email_id, created_date,
last_modified_date, created_by, last_modified_by, active, name, gender, pan, aadhaar_number, type, version, guardian, guardian_relation
)
VALUES (
2, 'title', 'Mrs', '1990-07-23 00:00:00', 'en_IN', 'userName2', '$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK',
'2020-12-31 00:00:00', '9731123456', '080292575', 'email2@gmail.com', '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, true,
'Name of the user2', 0, 'ABCDE1234F', '12346789011', 'EMPLOYEE', 0, 'Guardian name', 'Guardian Relation');