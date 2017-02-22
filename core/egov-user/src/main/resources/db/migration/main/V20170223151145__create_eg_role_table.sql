CREATE TABLE eg_role (
    id serial NOT NULL primary key,
    name character varying(32) NOT NULL,
    description character varying(128),
    created_date timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by bigint,
    last_modified_by bigint,
    last_modified_date timestamp,
    version bigint,
    CONSTRAINT eg_roles_role_name_key UNIQUE (name)
);

CREATE SEQUENCE seq_eg_role
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
