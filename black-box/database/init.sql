create table measurements_input
(
    id                    int,
    device_id             int,
    device_measurement_id int,
    date_time             timestamp,
    energy                numeric(8, 4),
    outside_temperature   numeric(8, 2),
    wind                  numeric(5, 2),
    humidity              numeric(5, 2),
    sky_condition         numeric(1, 0),
    day_length            numeric(5, 2),
    day_type              numeric(1, 0),
    season                numeric(1, 0)
);

copy measurements_input (id, device_id, device_measurement_id, date_time, energy, outside_temperature, wind, humidity,
                         sky_condition, day_length, day_type, season)
    from '/var/lib/postgresql/data/measurements.csv' delimiter ';' csv header;

--- ***** ---
CREATE OR REPLACE FUNCTION generateTestTables(n integer)
    RETURNS VOID AS
$$
DECLARE
    number_of_records integer := 10;
    test_table_name text    := 'test_input_';
BEGIN
    LOOP
        test_table_name := test_table_name || number_of_records::text;
        EXECUTE 'CREATE VIEW ' || test_table_name || ' AS SELECT * FROM measurements_input WHERE device_measurement_id <= ' || number_of_records;
        number_of_records := number_of_records * 10;
        test_table_name := 'test_input_';
        EXIT WHEN number_of_records = n;
    END LOOP;
END
$$ language plpgsql;

-- 1585135
-- +/- połowa
CREATE VIEW test_input_one_half AS SELECT * FROM measurements_input WHERE device_measurement_id <= 15788;

-- 792567
-- +/- 1/4
CREATE VIEW test_input_one_quarter AS SELECT * FROM measurements_input WHERE device_measurement_id <= 7749;

-- 1/8
CREATE VIEW test_input_one_eighth AS SELECT * FROM measurements_input WHERE device_measurement_id <= 4014;

SELECT generateTestTables(100000);
--- ***** ---

CREATE OR REPLACE FUNCTION create_table_one_half_without_column(
    column_name text default 'day_type'
) RETURNS void AS
$$
DECLARE
    name text := 'test_input_one_half_' || column_name;
BEGIN
    --     return name;
    EXECUTE format('CREATE TABLE %I AS SELECT * FROM test_input_one_half', name);
    EXECUTE format('ALTER TABLE %I DROP COLUMN %I', name, column_name);
END;
$$
    language plpgsql;

CREATE OR REPLACE FUNCTION create_table_one_quarter_without_column(
    column_name text default 'day_type'
) RETURNS void AS
$$
DECLARE
    name text := 'test_input_one_quarter_' || column_name;
BEGIN
    --     return name;
    EXECUTE format('CREATE TABLE %I AS SELECT * FROM test_input_one_quarter', name);
    EXECUTE format('ALTER TABLE %I DROP COLUMN %I', name, column_name);
END;
$$
    language plpgsql;

CREATE OR REPLACE FUNCTION create_table_one_eighth_without_column(
    column_name text default 'day_type'
) RETURNS void AS
$$
DECLARE
    name text := 'test_input_one_eighth_' || column_name;
BEGIN
    --     return name;
    EXECUTE format('CREATE TABLE %I AS SELECT * FROM test_input_one_eighth', name);
    EXECUTE format('ALTER TABLE %I DROP COLUMN %I', name, column_name);
END;
$$
    language plpgsql;

select create_table_one_eighth_without_column('season');
select create_table_one_eighth_without_column('day_type');
select create_table_one_eighth_without_column('day_length');
select create_table_one_eighth_without_column('sky_condition');
select create_table_one_eighth_without_column('humidity');
select create_table_one_eighth_without_column('wind');
select create_table_one_eighth_without_column('outside_temperature');
select create_table_one_eighth_without_column('energy');
select create_table_one_eighth_without_column('date_time');
select create_table_one_eighth_without_column('device_measurement_id');
select create_table_one_eighth_without_column('device_id');
select create_table_one_eighth_without_column('id');

select create_table_one_quarter_without_column('season');
select create_table_one_quarter_without_column('day_type');
select create_table_one_quarter_without_column('day_length');
select create_table_one_quarter_without_column('sky_condition');
select create_table_one_quarter_without_column('humidity');
select create_table_one_quarter_without_column('wind');
select create_table_one_quarter_without_column('outside_temperature');
select create_table_one_quarter_without_column('energy');
select create_table_one_quarter_without_column('date_time');
select create_table_one_quarter_without_column('device_measurement_id');
select create_table_one_quarter_without_column('device_id');
select create_table_one_quarter_without_column('id');

select create_table_one_half_without_column('season');
select create_table_one_half_without_column('day_type');
select create_table_one_half_without_column('day_length');
select create_table_one_half_without_column('sky_condition');
select create_table_one_half_without_column('humidity');
select create_table_one_half_without_column('wind');
select create_table_one_half_without_column('outside_temperature');
select create_table_one_half_without_column('energy');
select create_table_one_half_without_column('date_time');
select create_table_one_half_without_column('device_measurement_id');
select create_table_one_half_without_column('device_id');
select create_table_one_half_without_column('id');
