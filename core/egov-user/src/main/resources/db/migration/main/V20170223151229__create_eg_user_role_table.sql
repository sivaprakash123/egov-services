CREATE TABLE eg_user_role (
    role_id bigint NOT NULL references eg_role(id),
    user_id bigint NOT NULL references eg_user(id)
);