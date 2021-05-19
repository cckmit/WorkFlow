--Schema Creation
CREATE SCHEMA <schema> AUTHORIZATION workflow;
GRANT ALL ON SCHEMA <schema> TO postgres;
GRANT ALL ON SCHEMA <schema> TO public;

--Example
CREATE SCHEMA git1 AUTHORIZATION workflow;
GRANT ALL ON SCHEMA git1 TO postgres;
GRANT ALL ON SCHEMA git1 TO public;
