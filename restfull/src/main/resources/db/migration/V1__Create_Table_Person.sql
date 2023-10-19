CREATE TABLE IF NOT EXISTS person (
  id SERIAL PRIMARY KEY NOT NULL,
  first_name varchar(80) NOT NULL,
  last_name varchar(80) NOT NULL,
  address varchar(100) NOT NULL,
  gender varchar(6) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS person_id_seq;

ALTER TABLE person ALTER COLUMN id SET DEFAULT nextval('person_id_seq'::regclass);
