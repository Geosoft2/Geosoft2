Outlier Detection
===============

Java program that provides left or two sided Moving Window Median outlier detection

You'll need to add the following external libraries to your classpath:

log4j: http://logging.apache.org/log4j/1.2/

JDBC4 Postgresql Driver: http://jdbc.postgresql.org/download.html

Furthermore you'll need a config.properties file with the following content in your project folder.
Specify your database username, password, url, the window width for left sided outlier detection, the window widths for the two sided window used for the Air Quality Egg and Lanuv data and the factor used in the Running Median class for sensibility.

Set "reset_outlier_information=true" if outlier detection should be performed for all values stored in the database and allready validated values will be checked again (otherwise false).

    db_username=[name of your database user]
    db_password=[password for database user]
    db_url=[url to database, fomatted as: ' //hostname:port/database name ']
    window_width=30
    aqe_window_width=25 
    lanuv_window_width=11
    border_multiplicator=1.5
    reset_outlier_information=true
