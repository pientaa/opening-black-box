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
CREATE
OR REPLACE FUNCTION generateTestTables(n integer)
    RETURNS VOID AS
$$
DECLARE
number_of_records integer := 10;
    test_table_name
text    := 'test_input_';
BEGIN
    LOOP
test_table_name := test_table_name || number_of_records::text;
EXECUTE 'CREATE VIEW ' || test_table_name ||
        ' AS SELECT * FROM measurements_input WHERE device_measurement_id <= ' || number_of_records;
number_of_records
:= number_of_records * 10;
        test_table_name
:= 'test_input_';
        EXIT
WHEN number_of_records = n;
END LOOP;
END
$$
language plpgsql;

-- 1585135
-- +/- 1/2
CREATE VIEW test_input_one_half AS
SELECT *
FROM measurements_input
WHERE device_measurement_id <= 15788;

-- 792567
-- +/- 1/4
CREATE VIEW test_input_one_quarter AS
SELECT *
FROM measurements_input
WHERE device_measurement_id <= 7749;

-- 1/8
CREATE VIEW test_input_one_eighth AS
SELECT *
FROM measurements_input
WHERE device_measurement_id <= 4014;

SELECT generateTestTables(100000);

create table stage_metrics
(
    id                    serial  not null
        constraint stage_metrics_pkey primary key,
    function_name         text,
    stage_id              integer not null,
    num_tasks             integer not null,
    submission_time       bigint,
    completion_time       bigint,
    executor_run_time     bigint  not null,
    result_size           bigint  not null,
    jvm_gc_time           bigint  not null,
    peak_execution_memory bigint  not null,
    disk_bytes_spilled    bigint  not null,
    memory_bytes_spilled  bigint  not null
);

create table task_metrics
(
    id                    serial  not null
        constraint task_metrics_pkey primary key,
    function_name         text,
    stage_id              integer not null,
    task_type             text,
    executor_run_time     bigint  not null,
    result_size           bigint  not null,
    jvm_gc_time           bigint  not null,
    peak_execution_memory bigint  not null,
    disk_bytes_spilled    bigint  not null,
    memory_bytes_spilled  bigint  not null
);

