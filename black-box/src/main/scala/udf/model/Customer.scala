package udf.model

case class Customer(
    c_customer_sk: Integer,
    c_customer_id: String,
    c_current_cdemo_sk: Integer,
    c_current_hdemo_sk: Integer,
    c_current_addr_sk: Integer,
    c_first_shipto_date_sk: Integer,
    c_first_sales_date_sk: Integer,
    c_salutation: String,
    c_first_name: String,
    c_last_name: String,
    c_preferred_cust_flag: String,
    c_birth_day: Integer,
    c_birth_month: Integer,
    c_birth_year: Integer,
    c_birth_country: String,
    c_login: String,
    c_email_address: String,
    c_last_review_date: String
)

