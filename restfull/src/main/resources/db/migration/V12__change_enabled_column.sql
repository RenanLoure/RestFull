ALTER TABLE person
ADD COLUMN enabled_temp boolean;

UPDATE person
SET enabled_temp = (enabled = B'1');

ALTER TABLE person
DROP COLUMN enabled;

ALTER TABLE person
RENAME COLUMN enabled_temp TO enabled;
