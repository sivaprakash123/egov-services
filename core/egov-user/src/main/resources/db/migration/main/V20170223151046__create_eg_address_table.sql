CREATE TABLE eg_address (
    house_no_bldg_apt character varying(32),
    sub_district character varying(100),
    post_office character varying(100),
    landmark character varying(256),
    country character varying(50),
    user_id bigint not null references eg_user(id),
    type character varying(50),
    street_road_line character varying(256),
    city_town_village character varying(256),
    area_locality_sector character varying(256),
    district character varying(100),
    state character varying(100),
    pin_code character varying(10),
    id serial NOT NULL primary key,
    version bigint DEFAULT 0
);