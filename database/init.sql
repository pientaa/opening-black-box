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


--
-- Legal Notice
--
-- This document and associated source code (the "Work") is a part of a
-- benchmark specification maintained by the TPC.
--
-- The TPC reserves all right, title, and interest to the Work as provided
-- under U.S. and international laws, including without limitation all patent
-- and trademark rights therein.
--
-- No Warranty
--
-- 1.1 TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, THE INFORMATION
--     CONTAINED HEREIN IS PROVIDED "AS IS" AND WITH ALL FAULTS, AND THE
--     AUTHORS AND DEVELOPERS OF THE WORK HEREBY DISCLAIM ALL OTHER
--     WARRANTIES AND CONDITIONS, EITHER EXPRESS, IMPLIED OR STATUTORY,
--     INCLUDING, BUT NOT LIMITED TO, ANY (IF ANY) IMPLIED WARRANTIES,
--     DUTIES OR CONDITIONS OF MERCHANTABILITY, OF FITNESS FOR A PARTICULAR
--     PURPOSE, OF ACCURACY OR COMPLETENESS OF RESPONSES, OF RESULTS, OF
--     WORKMANLIKE EFFORT, OF LACK OF VIRUSES, AND OF LACK OF NEGLIGENCE.
--     ALSO, THERE IS NO WARRANTY OR CONDITION OF TITLE, QUIET ENJOYMENT,
--     QUIET POSSESSION, CORRESPONDENCE TO DESCRIPTION OR NON-INFRINGEMENT
--     WITH REGARD TO THE WORK.
-- 1.2 IN NO EVENT WILL ANY AUTHOR OR DEVELOPER OF THE WORK BE LIABLE TO
--     ANY OTHER PARTY FOR ANY DAMAGES, INCLUDING BUT NOT LIMITED TO THE
--     COST OF PROCURING SUBSTITUTE GOODS OR SERVICES, LOST PROFITS, LOSS
--     OF USE, LOSS OF DATA, OR ANY INCIDENTAL, CONSEQUENTIAL, DIRECT,
--     INDIRECT, OR SPECIAL DAMAGES WHETHER UNDER CONTRACT, TORT, WARRANTY,
--     OR OTHERWISE, ARISING IN ANY WAY OUT OF THIS OR ANY OTHER AGREEMENT
--     RELATING TO THE WORK, WHETHER OR NOT SUCH AUTHOR OR DEVELOPER HAD
--     ADVANCE NOTICE OF THE POSSIBILITY OF SUCH DAMAGES.
--
-- Contributors:
-- Gradient Systems

create table customer_address
(
    ca_address_sk    integer  not null,
    ca_address_id    char(16) not null,
    ca_street_number char(10),
    ca_street_name   varchar(60),
    ca_street_type   char(15),
    ca_suite_number  char(10),
    ca_city          varchar(60),
    ca_county        varchar(30),
    ca_state         char(2),
    ca_zip           char(10),
    ca_country       varchar(20),
    ca_gmt_offset    decimal(5, 2),
    ca_location_type char(20),
    primary key (ca_address_sk)
);

copy customer_address(ca_address_sk, ca_address_id, ca_street_number, ca_street_name, ca_street_type, ca_suite_number,
    ca_city, ca_county, ca_state, ca_zip, ca_country, ca_gmt_offset, ca_location_type)
    from '/var/lib/postgresql/data/customer_address.dat' delimiter ',' csv;

create table customer_demographics
(
    cd_demo_sk            integer not null,
    cd_gender             char(1),
    cd_marital_status     char(1),
    cd_education_status   char(20),
    cd_purchase_estimate  integer,
    cd_credit_rating      char(10),
    cd_dep_count          integer,
    cd_dep_employed_count integer,
    cd_dep_college_count  integer,
    primary key (cd_demo_sk)
);

copy customer_demographics(cd_demo_sk, cd_gender, cd_marital_status, cd_education_status, cd_purchase_estimate, cd_credit_rating,
    cd_dep_count, cd_dep_employed_count, cd_dep_college_count)
    from '/var/lib/postgresql/data/customer_demographics.dat' delimiter ',' csv;

create table date_dim
(
    d_date_sk           integer  not null,
    d_date_id           char(16) not null,
    d_date              date,
    d_month_seq         integer,
    d_week_seq          integer,
    d_quarter_seq       integer,
    d_year              integer,
    d_dow               integer,
    d_moy               integer,
    d_dom               integer,
    d_qoy               integer,
    d_fy_year           integer,
    d_fy_quarter_seq    integer,
    d_fy_week_seq       integer,
    d_day_name          char(9),
    d_quarter_name      char(6),
    d_holiday           char(1),
    d_weekend           char(1),
    d_following_holiday char(1),
    d_first_dom         integer,
    d_last_dom          integer,
    d_same_day_ly       integer,
    d_same_day_lq       integer,
    d_current_day       char(1),
    d_current_week      char(1),
    d_current_month     char(1),
    d_current_quarter   char(1),
    d_current_year      char(1),
    primary key (d_date_sk)
);

copy date_dim(d_date_sk, d_date_id, d_date,
    d_month_seq, d_week_seq, d_quarter_seq ,
    d_year, d_dow, d_moy, d_dom,
    d_qoy, d_fy_year, d_fy_quarter_seq, d_fy_week_seq,
    d_day_name, d_quarter_name, d_holiday, d_weekend,
    d_following_holiday, d_first_dom, d_last_dom,
    d_same_day_ly, d_same_day_lq, d_current_day,
    d_current_week, d_current_month, d_current_quarter, d_current_year)
    from '/var/lib/postgresql/data/date_dim.dat' delimiter ',' csv;


create table warehouse
(
    w_warehouse_sk    integer  not null,
    w_warehouse_id    char(16) not null,
    w_warehouse_name  varchar(20),
    w_warehouse_sq_ft integer,
    w_street_number   char(10),
    w_street_name     varchar(60),
    w_street_type     char(15),
    w_suite_number    char(10),
    w_city            varchar(60),
    w_county          varchar(30),
    w_state           char(2),
    w_zip             char(10),
    w_country         varchar(20),
    w_gmt_offset      decimal(5, 2),
    primary key (w_warehouse_sk)
);

copy warehouse(w_warehouse_sk, w_warehouse_id, w_warehouse_name,
    w_warehouse_sq_ft, w_street_number, w_street_name,
    w_street_type, w_suite_number, w_city, w_county,
    w_state, w_zip, w_country, w_gmt_offset)
    from '/var/lib/postgresql/data/warehouse.dat' delimiter ',' csv;

create table ship_mode
(
    sm_ship_mode_sk integer  not null,
    sm_ship_mode_id char(16) not null,
    sm_type         char(30),
    sm_code         char(10),
    sm_carrier      char(20),
    sm_contract     char(20),
    primary key (sm_ship_mode_sk)
);

copy ship_mode(sm_ship_mode_sk, sm_ship_mode_id, sm_type,
    sm_code, sm_carrier, sm_contract)
    from '/var/lib/postgresql/data/ship_mode.dat' delimiter ',' csv;

create table time_dim
(
    t_time_sk   integer  not null,
    t_time_id   char(16) not null,
    t_time      integer,
    t_hour      integer,
    t_minute    integer,
    t_second    integer,
    t_am_pm     char(2),
    t_shift     char(20),
    t_sub_shift char(20),
    t_meal_time char(20),
    primary key (t_time_sk)
);

copy time_dim(t_time_sk, t_time_id, t_time,
    t_hour, t_minute, t_second,
    t_am_pm, t_shift, t_sub_shift, t_meal_time)
    from '/var/lib/postgresql/data/time_dim.dat' delimiter ',' csv;

create table reason
(
    r_reason_sk   integer  not null,
    r_reason_id   char(16) not null,
    r_reason_desc char(100),
    primary key (r_reason_sk)
);

copy reason(r_reason_sk, r_reason_id, r_reason_desc)
    from '/var/lib/postgresql/data/reason.dat' delimiter ',' csv;

create table income_band
(
    ib_income_band_sk integer not null,
    ib_lower_bound    integer,
    ib_upper_bound    integer,
    primary key (ib_income_band_sk)
);

copy income_band(ib_income_band_sk, ib_lower_bound, ib_upper_bound)
    from '/var/lib/postgresql/data/income_band.dat' delimiter ',' csv;

create table item
(
    i_item_sk        integer  not null,
    i_item_id        char(16) not null,
    i_rec_start_date date,
    i_rec_end_date   date,
    i_item_desc      varchar(200),
    i_current_price  decimal(7, 2),
    i_wholesale_cost decimal(7, 2),
    i_brand_id       integer,
    i_brand          char(50),
    i_class_id       integer,
    i_class          char(50),
    i_category_id    integer,
    i_category       char(50),
    i_manufact_id    integer,
    i_manufact       char(50),
    i_size           char(20),
    i_formulation    char(20),
    i_color          char(20),
    i_units          char(10),
    i_container      char(10),
    i_manager_id     integer,
    i_product_name   char(50),
    primary key (i_item_sk)
);

copy item(i_item_sk, i_item_id, i_rec_start_date,
    i_rec_end_date, i_item_desc, i_current_price,
    i_wholesale_cost, i_brand_id, i_brand, i_class_id,
    i_class, i_category_id, i_category, i_manufact_id,
    i_manufact, i_size, i_formulation, i_color,
    i_units, i_container, i_manager_id, i_product_name)
    from '/var/lib/postgresql/data/item.dat' delimiter ',' csv;

create table store
(
    s_store_sk         integer  not null,
    s_store_id         char(16) not null,
    s_rec_start_date   date,
    s_rec_end_date     date,
    s_closed_date_sk   integer,
    s_store_name       varchar(50),
    s_number_employees integer,
    s_floor_space      integer,
    s_hours            char(20),
    s_manager          varchar(40),
    s_market_id        integer,
    s_geography_class  varchar(100),
    s_market_desc      varchar(100),
    s_market_manager   varchar(40),
    s_division_id      integer,
    s_division_name    varchar(50),
    s_company_id       integer,
    s_company_name     varchar(50),
    s_street_number    varchar(10),
    s_street_name      varchar(60),
    s_street_type      char(15),
    s_suite_number     char(10),
    s_city             varchar(60),
    s_county           varchar(30),
    s_state            char(2),
    s_zip              char(10),
    s_country          varchar(20),
    s_gmt_offset       decimal(5, 2),
    s_tax_precentage   decimal(5, 2),
    primary key (s_store_sk)
);

copy store(s_store_sk, s_store_id, s_rec_start_date,
    s_rec_end_date, s_closed_date_sk, s_store_name,
    s_number_employees, s_floor_space, s_hours, s_manager,
    s_market_id, s_geography_class, s_market_desc, s_market_manager,
    s_division_id, s_division_name, s_company_id, s_company_name,
    s_street_number, s_street_name, s_street_type, s_suite_number
    s_city, s_county, s_state, s_zip
    s_country, s_gmt_offset, s_tax_precentage)
    from '/var/lib/postgresql/data/store.dat' delimiter ',' csv;

create table call_center
(
    cc_call_center_sk integer  not null,
    cc_call_center_id char(16) not null,
    cc_rec_start_date date,
    cc_rec_end_date   date,
    cc_closed_date_sk integer,
    cc_open_date_sk   integer,
    cc_name           varchar(50),
    cc_class          varchar(50),
    cc_employees      integer,
    cc_sq_ft          integer,
    cc_hours          char(20),
    cc_manager        varchar(40),
    cc_mkt_id         integer,
    cc_mkt_class      char(50),
    cc_mkt_desc       varchar(100),
    cc_market_manager varchar(40),
    cc_division       integer,
    cc_division_name  varchar(50),
    cc_company        integer,
    cc_company_name   char(50),
    cc_street_number  char(10),
    cc_street_name    varchar(60),
    cc_street_type    char(15),
    cc_suite_number   char(10),
    cc_city           varchar(60),
    cc_county         varchar(30),
    cc_state          char(2),
    cc_zip            char(10),
    cc_country        varchar(20),
    cc_gmt_offset     decimal(5, 2),
    cc_tax_percentage decimal(5, 2),
    primary key (cc_call_center_sk)
);

copy call_center(cc_call_center_sk, cc_call_center_id, cc_rec_start_date,
    cc_rec_end_date, cc_closed_date_sk, cc_open_date_sk,
    cc_name, cc_class, cc_employees, cc_sq_ft,
    cc_hours, cc_manager, cc_mkt_id, cc_mkt_class,
    cc_mkt_desc, cc_market_manager, cc_division, cc_division_name,
    cc_company, cc_company_name, cc_street_number, cc_street_name
    cc_street_type, cc_suite_number, cc_city, cc_county
    cc_state, cc_zip, cc_country, cc_gmt_offset, cc_tax_percentage)
    from '/var/lib/postgresql/data/call_center.dat' delimiter ',' csv;

create table customer
(
    c_customer_sk          integer  not null,
    c_customer_id          char(16) not null,
    c_current_cdemo_sk     integer,
    c_current_hdemo_sk     integer,
    c_current_addr_sk      integer,
    c_first_shipto_date_sk integer,
    c_first_sales_date_sk  integer,
    c_salutation           char(10),
    c_first_name           char(20),
    c_last_name            char(30),
    c_preferred_cust_flag  char(1),
    c_birth_day            integer,
    c_birth_month          integer,
    c_birth_year           integer,
    c_birth_country        varchar(20),
    c_login                char(13),
    c_email_address        char(50),
    c_last_review_date     char(10),
    primary key (c_customer_sk)
);

copy customer(
    c_customer_sk ,
    c_customer_id ,
    c_current_cdemo_sk ,
    c_current_hdemo_sk ,
    c_current_addr_sk ,
    c_first_shipto_date_sk ,
    c_first_sales_date_sk ,
    c_salutation ,
    c_first_name ,
    c_last_name ,
    c_preferred_cust_flag ,
    c_birth_day ,
    c_birth_month ,
    c_birth_year ,
    c_birth_country ,
    c_login ,
    c_email_address ,
    c_last_review_date )
    from '/var/lib/postgresql/data/customer.dat' delimiter ',' csv;

create table web_site
(
    web_site_sk        integer  not null,
    web_site_id        char(16) not null,
    web_rec_start_date date,
    web_rec_end_date   date,
    web_name           varchar(50),
    web_open_date_sk   integer,
    web_close_date_sk  integer,
    web_class          varchar(50),
    web_manager        varchar(40),
    web_mkt_id         integer,
    web_mkt_class      varchar(50),
    web_mkt_desc       varchar(100),
    web_market_manager varchar(40),
    web_company_id     integer,
    web_company_name   char(50),
    web_street_number  char(10),
    web_street_name    varchar(60),
    web_street_type    char(15),
    web_suite_number   char(10),
    web_city           varchar(60),
    web_county         varchar(30),
    web_state          char(2),
    web_zip            char(10),
    web_country        varchar(20),
    web_gmt_offset     decimal(5, 2),
    web_tax_percentage decimal(5, 2),
    primary key (web_site_sk)
);

copy web_site(
    web_site_sk ,
    web_site_id ,
    web_rec_start_date ,
    web_rec_end_date ,
    web_name ,
    web_open_date_sk ,
    web_close_date_sk ,
    web_class ,
    web_manager ,
    web_mkt_id ,
    web_mkt_class ,
    web_mkt_desc ,
    web_market_manager ,
    web_company_id ,
    web_company_name ,
    web_street_number ,
    web_street_name ,
    web_street_type ,
    web_suite_number ,
    web_city ,
    web_county ,
    web_state ,
    web_zip ,
    web_country ,
    web_gmt_offset ,
    web_tax_percentage )
    from '/var/lib/postgresql/data/web_site.dat' delimiter ',' csv;

create table store_returns
(
    sr_returned_date_sk   integer,
    sr_return_time_sk     integer,
    sr_item_sk            integer not null,
    sr_customer_sk        integer,
    sr_cdemo_sk           integer,
    sr_hdemo_sk           integer,
    sr_addr_sk            integer,
    sr_store_sk           integer,
    sr_reason_sk          integer,
    sr_ticket_number      integer not null,
    sr_return_quantity    integer,
    sr_return_amt         decimal(7, 2),
    sr_return_tax         decimal(7, 2),
    sr_return_amt_inc_tax decimal(7, 2),
    sr_fee                decimal(7, 2),
    sr_return_ship_cost   decimal(7, 2),
    sr_refunded_cash      decimal(7, 2),
    sr_reversed_charge    decimal(7, 2),
    sr_store_credit       decimal(7, 2),
    sr_net_loss           decimal(7, 2),
    primary key (sr_item_sk, sr_ticket_number)
);

copy store_returns(
    sr_returned_date_sk ,
    sr_return_time_sk ,
    sr_item_sk ,
    sr_customer_sk ,
    sr_cdemo_sk ,
    sr_hdemo_sk ,
    sr_addr_sk ,
    sr_store_sk ,
    sr_reason_sk ,
    sr_ticket_number ,
    sr_return_quantity ,
    sr_return_amt ,
    sr_return_tax ,
    sr_return_amt_inc_tax ,
    sr_fee ,
    sr_return_ship_cost ,
    sr_refunded_cash ,
    sr_reversed_charge ,
    sr_store_credit ,
    sr_net_loss )
    from '/var/lib/postgresql/data/store_returns.dat' delimiter ',' csv;

create table household_demographics
(
    hd_demo_sk        integer not null,
    hd_income_band_sk integer,
    hd_buy_potential  char(15),
    hd_dep_count      integer,
    hd_vehicle_count  integer,
    primary key (hd_demo_sk)
);

copy household_demographics(
    hd_demo_sk ,
    hd_income_band_sk ,
    hd_buy_potential ,
    hd_dep_count ,
    hd_vehicle_count )
    from '/var/lib/postgresql/data/household_demographics.dat' delimiter ',' csv;

create table web_page
(
    wp_web_page_sk      integer  not null,
    wp_web_page_id      char(16) not null,
    wp_rec_start_date   date,
    wp_rec_end_date     date,
    wp_creation_date_sk integer,
    wp_access_date_sk   integer,
    wp_autogen_flag     char(1),
    wp_customer_sk      integer,
    wp_url              varchar(100),
    wp_type             char(50),
    wp_char_count       integer,
    wp_link_count       integer,
    wp_image_count      integer,
    wp_max_ad_count     integer,
    primary key (wp_web_page_sk)
);

copy web_page(
    wp_web_page_sk ,
    wp_web_page_id ,
    wp_rec_start_date ,
    wp_rec_end_date ,
    wp_creation_date_sk ,
    wp_access_date_sk ,
    wp_autogen_flag ,
    wp_customer_sk ,
    wp_url ,
    wp_type ,
    wp_char_count ,
    wp_link_count ,
    wp_image_count ,
    wp_max_ad_count )
    from '/var/lib/postgresql/data/web_page.dat' delimiter ',' csv;

create table promotion
(
    p_promo_sk        integer  not null,
    p_promo_id        char(16) not null,
    p_start_date_sk   integer,
    p_end_date_sk     integer,
    p_item_sk         integer,
    p_cost            decimal(15, 2),
    p_response_target integer,
    p_promo_name      char(50),
    p_channel_dmail   char(1),
    p_channel_email   char(1),
    p_channel_catalog char(1),
    p_channel_tv      char(1),
    p_channel_radio   char(1),
    p_channel_press   char(1),
    p_channel_event   char(1),
    p_channel_demo    char(1),
    p_channel_details varchar(100),
    p_purpose         char(15),
    p_discount_active char(1),
    primary key (p_promo_sk)
);

copy promotion(
    p_promo_sk ,
    p_promo_id ,
    p_start_date_sk ,
    p_end_date_sk ,
    p_item_sk ,
    p_cost ,
    p_response_target ,
    p_promo_name ,
    p_channel_dmail ,
    p_channel_email ,
    p_channel_catalog ,
    p_channel_tv ,
    p_channel_radio ,
    p_channel_press ,
    p_channel_event ,
    p_channel_demo ,
    p_channel_details ,
    p_purpose ,
    p_discount_active )
    from '/var/lib/postgresql/data/promotion.dat' delimiter ',' csv;

create table catalog_page
(
    cp_catalog_page_sk     integer  not null,
    cp_catalog_page_id     char(16) not null,
    cp_start_date_sk       integer,
    cp_end_date_sk         integer,
    cp_department          varchar(50),
    cp_catalog_number      integer,
    cp_catalog_page_number integer,
    cp_description         varchar(100),
    cp_type                varchar(100),
    primary key (cp_catalog_page_sk)
);

copy catalog_page(
    cp_catalog_page_sk ,
    cp_catalog_page_id ,
    cp_start_date_sk ,
    cp_end_date_sk ,
    cp_department ,
    cp_catalog_number ,
    cp_catalog_page_number ,
    cp_description ,
    cp_type )
    from '/var/lib/postgresql/data/catalog_page.dat' delimiter ',' csv;

create table inventory
(
    inv_date_sk          integer not null,
    inv_item_sk          integer not null,
    inv_warehouse_sk     integer not null,
    inv_quantity_on_hand integer,
    primary key (inv_date_sk, inv_item_sk, inv_warehouse_sk)
);

copy inventory(
    inv_date_sk ,
    inv_item_sk ,
    inv_warehouse_sk ,
    inv_quantity_on_hand )
    from '/var/lib/postgresql/data/inventory.dat' delimiter ',' csv;

create table catalog_returns
(
    cr_returned_date_sk      integer,
    cr_returned_time_sk      integer,
    cr_item_sk               integer not null,
    cr_refunded_customer_sk  integer,
    cr_refunded_cdemo_sk     integer,
    cr_refunded_hdemo_sk     integer,
    cr_refunded_addr_sk      integer,
    cr_returning_customer_sk integer,
    cr_returning_cdemo_sk    integer,
    cr_returning_hdemo_sk    integer,
    cr_returning_addr_sk     integer,
    cr_call_center_sk        integer,
    cr_catalog_page_sk       integer,
    cr_ship_mode_sk          integer,
    cr_warehouse_sk          integer,
    cr_reason_sk             integer,
    cr_order_number          integer not null,
    cr_return_quantity       integer,
    cr_return_amount         decimal(7, 2),
    cr_return_tax            decimal(7, 2),
    cr_return_amt_inc_tax    decimal(7, 2),
    cr_fee                   decimal(7, 2),
    cr_return_ship_cost      decimal(7, 2),
    cr_refunded_cash         decimal(7, 2),
    cr_reversed_charge       decimal(7, 2),
    cr_store_credit          decimal(7, 2),
    cr_net_loss              decimal(7, 2),
    primary key (cr_item_sk, cr_order_number)
);

copy catalog_returns(
    cr_returned_date_sk ,
    cr_returned_time_sk ,
    cr_item_sk ,
    cr_refunded_customer_sk ,
    cr_refunded_cdemo_sk ,
    cr_refunded_hdemo_sk ,
    cr_refunded_addr_sk ,
    cr_returning_customer_sk,
    cr_returning_cdemo_sk ,
    cr_returning_hdemo_sk ,
    cr_returning_addr_sk ,
    cr_call_center_sk ,
    cr_catalog_page_sk ,
    cr_ship_mode_sk ,
    cr_warehouse_sk ,
    cr_reason_sk ,
    cr_order_number ,
    cr_return_quantity ,
    cr_return_amount ,
    cr_return_tax ,
    cr_return_amt_inc_tax ,
    cr_fee ,
    cr_return_ship_cost ,
    cr_refunded_cash ,
    cr_reversed_charge ,
    cr_store_credit ,
    cr_net_loss )
    from '/var/lib/postgresql/data/catalog_returns.dat' delimiter ',' csv;

create table web_returns
(
    wr_returned_date_sk      integer,
    wr_returned_time_sk      integer,
    wr_item_sk               integer not null,
    wr_refunded_customer_sk  integer,
    wr_refunded_cdemo_sk     integer,
    wr_refunded_hdemo_sk     integer,
    wr_refunded_addr_sk      integer,
    wr_returning_customer_sk integer,
    wr_returning_cdemo_sk    integer,
    wr_returning_hdemo_sk    integer,
    wr_returning_addr_sk     integer,
    wr_web_page_sk           integer,
    wr_reason_sk             integer,
    wr_order_number          integer not null,
    wr_return_quantity       integer,
    wr_return_amt            decimal(7, 2),
    wr_return_tax            decimal(7, 2),
    wr_return_amt_inc_tax    decimal(7, 2),
    wr_fee                   decimal(7, 2),
    wr_return_ship_cost      decimal(7, 2),
    wr_refunded_cash         decimal(7, 2),
    wr_reversed_charge       decimal(7, 2),
    wr_account_credit        decimal(7, 2),
    wr_net_loss              decimal(7, 2),
    primary key (wr_item_sk, wr_order_number)
);

copy web_returns(
    wr_returned_date_sk ,
    wr_returned_time_sk ,
    wr_item_sk ,
    wr_refunded_customer_sk ,
    wr_refunded_cdemo_sk ,
    wr_refunded_hdemo_sk ,
    wr_refunded_addr_sk ,
    wr_returning_customer_sk ,
    wr_returning_cdemo_sk ,
    wr_returning_hdemo_sk ,
    wr_returning_addr_sk ,
    wr_web_page_sk ,
    wr_reason_sk ,
    wr_order_number ,
    wr_return_quantity ,
    wr_return_amt ,
    wr_return_tax ,
    wr_return_amt_inc_tax ,
    wr_fee ,
    wr_return_ship_cost ,
    wr_refunded_cash ,
    wr_reversed_charge ,
    wr_account_credit ,
    wr_net_loss )
    from '/var/lib/postgresql/data/web_returns.dat' delimiter ',' csv;

create table web_sales
(
    ws_sold_date_sk          integer,
    ws_sold_time_sk          integer,
    ws_ship_date_sk          integer,
    ws_item_sk               integer not null,
    ws_bill_customer_sk      integer,
    ws_bill_cdemo_sk         integer,
    ws_bill_hdemo_sk         integer,
    ws_bill_addr_sk          integer,
    ws_ship_customer_sk      integer,
    ws_ship_cdemo_sk         integer,
    ws_ship_hdemo_sk         integer,
    ws_ship_addr_sk          integer,
    ws_web_page_sk           integer,
    ws_web_site_sk           integer,
    ws_ship_mode_sk          integer,
    ws_warehouse_sk          integer,
    ws_promo_sk              integer,
    ws_order_number          integer not null,
    ws_quantity              integer,
    ws_wholesale_cost        decimal(7, 2),
    ws_list_price            decimal(7, 2),
    ws_sales_price           decimal(7, 2),
    ws_ext_discount_amt      decimal(7, 2),
    ws_ext_sales_price       decimal(7, 2),
    ws_ext_wholesale_cost    decimal(7, 2),
    ws_ext_list_price        decimal(7, 2),
    ws_ext_tax               decimal(7, 2),
    ws_coupon_amt            decimal(7, 2),
    ws_ext_ship_cost         decimal(7, 2),
    ws_net_paid              decimal(7, 2),
    ws_net_paid_inc_tax      decimal(7, 2),
    ws_net_paid_inc_ship     decimal(7, 2),
    ws_net_paid_inc_ship_tax decimal(7, 2),
    ws_net_profit            decimal(7, 2),
    primary key (ws_item_sk, ws_order_number)
);

copy web_sales(
    ws_sold_date_sk ,
    ws_sold_time_sk ,
    ws_ship_date_sk ,
    ws_item_sk ,
    ws_bill_customer_sk ,
    ws_bill_cdemo_sk ,
    ws_bill_hdemo_sk ,
    ws_bill_addr_sk ,
    ws_ship_customer_sk ,
    ws_ship_cdemo_sk ,
    ws_ship_hdemo_sk ,
    ws_ship_addr_sk ,
    ws_web_page_sk ,
    ws_web_site_sk ,
    ws_ship_mode_sk ,
    ws_warehouse_sk ,
    ws_promo_sk ,
    ws_order_number ,
    ws_quantity ,
    ws_wholesale_cost ,
    ws_list_price ,
    ws_sales_price ,
    ws_ext_discount_amt ,
    ws_ext_sales_price ,
    ws_ext_wholesale_cost ,
    ws_ext_list_price ,
    ws_ext_tax ,
    ws_coupon_amt ,
    ws_ext_ship_cost ,
    ws_net_paid ,
    ws_net_paid_inc_tax ,
    ws_net_paid_inc_ship ,
    ws_net_paid_inc_ship_tax ,
    ws_net_profit )
    from '/var/lib/postgresql/data/web_sales.dat' delimiter ',' csv;


create table catalog_sales
(
    cs_sold_date_sk          integer,
    cs_sold_time_sk          integer,
    cs_ship_date_sk          integer,
    cs_bill_customer_sk      integer,
    cs_bill_cdemo_sk         integer,
    cs_bill_hdemo_sk         integer,
    cs_bill_addr_sk          integer,
    cs_ship_customer_sk      integer,
    cs_ship_cdemo_sk         integer,
    cs_ship_hdemo_sk         integer,
    cs_ship_addr_sk          integer,
    cs_call_center_sk        integer,
    cs_catalog_page_sk       integer,
    cs_ship_mode_sk          integer,
    cs_warehouse_sk          integer,
    cs_item_sk               integer not null,
    cs_promo_sk              integer,
    cs_order_number          integer not null,
    cs_quantity              integer,
    cs_wholesale_cost        decimal(7, 2),
    cs_list_price            decimal(7, 2),
    cs_sales_price           decimal(7, 2),
    cs_ext_discount_amt      decimal(7, 2),
    cs_ext_sales_price       decimal(7, 2),
    cs_ext_wholesale_cost    decimal(7, 2),
    cs_ext_list_price        decimal(7, 2),
    cs_ext_tax               decimal(7, 2),
    cs_coupon_amt            decimal(7, 2),
    cs_ext_ship_cost         decimal(7, 2),
    cs_net_paid              decimal(7, 2),
    cs_net_paid_inc_tax      decimal(7, 2),
    cs_net_paid_inc_ship     decimal(7, 2),
    cs_net_paid_inc_ship_tax decimal(7, 2),
    cs_net_profit            decimal(7, 2),
    primary key (cs_item_sk, cs_order_number)
);

copy catalog_sales(
    cs_sold_date_sk ,
    cs_sold_time_sk ,
    cs_ship_date_sk ,
    cs_bill_customer_sk ,
    cs_bill_cdemo_sk ,
    cs_bill_hdemo_sk ,
    cs_bill_addr_sk ,
    cs_ship_customer_sk ,
    cs_ship_cdemo_sk ,
    cs_ship_hdemo_sk ,
    cs_ship_addr_sk ,
    cs_call_center_sk ,
    cs_catalog_page_sk ,
    cs_ship_mode_sk ,
    cs_warehouse_sk ,
    cs_item_sk ,
    cs_promo_sk ,
    cs_order_number ,
    cs_quantity ,
    cs_wholesale_cost ,
    cs_list_price ,
    cs_sales_price ,
    cs_ext_discount_amt ,
    cs_ext_sales_price ,
    cs_ext_wholesale_cost ,
    cs_ext_list_price ,
    cs_ext_tax ,
    cs_coupon_amt ,
    cs_ext_ship_cost ,
    cs_net_paid ,
    cs_net_paid_inc_tax ,
    cs_net_paid_inc_ship ,
    cs_net_paid_inc_ship_tax ,
    cs_net_profit )
    from '/var/lib/postgresql/data/catalog_sales.dat' delimiter ',' csv;


create table store_sales
(
    ss_sold_date_sk       integer,
    ss_sold_time_sk       integer,
    ss_item_sk            integer not null,
    ss_customer_sk        integer,
    ss_cdemo_sk           integer,
    ss_hdemo_sk           integer,
    ss_addr_sk            integer,
    ss_store_sk           integer,
    ss_promo_sk           integer,
    ss_ticket_number      integer not null,
    ss_quantity           integer,
    ss_wholesale_cost     decimal(7, 2),
    ss_list_price         decimal(7, 2),
    ss_sales_price        decimal(7, 2),
    ss_ext_discount_amt   decimal(7, 2),
    ss_ext_sales_price    decimal(7, 2),
    ss_ext_wholesale_cost decimal(7, 2),
    ss_ext_list_price     decimal(7, 2),
    ss_ext_tax            decimal(7, 2),
    ss_coupon_amt         decimal(7, 2),
    ss_net_paid           decimal(7, 2),
    ss_net_paid_inc_tax   decimal(7, 2),
    ss_net_profit         decimal(7, 2),
    primary key (ss_item_sk, ss_ticket_number)
);

copy store_sales(
    ss_sold_date_sk ,
    ss_sold_time_sk ,
    ss_item_sk ,
    ss_customer_sk ,
    ss_cdemo_sk ,
    ss_hdemo_sk ,
    ss_addr_sk ,
    ss_store_sk ,
    ss_promo_sk ,
    ss_ticket_number ,
    ss_quantity ,
    ss_wholesale_cost ,
    ss_list_price ,
    ss_sales_price ,
    ss_ext_discount_amt ,
    ss_ext_sales_price ,
    ss_ext_wholesale_cost ,
    ss_ext_list_price ,
    ss_ext_tax ,
    ss_coupon_amt ,
    ss_net_paid ,
    ss_net_paid_inc_tax ,
    ss_net_profit )
    from '/var/lib/postgresql/data/store_sales.dat' delimiter ',' csv;