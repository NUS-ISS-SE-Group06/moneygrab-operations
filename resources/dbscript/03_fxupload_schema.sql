use fxuploaddb;


CREATE TABLE fx_upload (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           currency_code CHAR(3) NOT NULL,
                           bid DECIMAL(18,8) NOT NULL,
                           ask DECIMAL(18,8) NOT NULL,
                           spread DECIMAL(18,8) NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           created_by INT,
                           updated_by INT NULL
);


CREATE TABLE compute_rates (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               currency_code CHAR(3) NOT NULL,
                               unit VARCHAR(50) NULL,
                               raw_bid DECIMAL(18,8),
                               raw_ask DECIMAL(18,8),
                               spread DECIMAL(18,8),
                               skew DECIMAL(18,8),
                               ws_bid DECIMAL(18,8),
                               ws_ask DECIMAL(18,8),
                               ref_bid DECIMAL(18,8),
                               dp_bid DECIMAL(18,8),
                               mar_bid DECIMAL(18,8),
                               cf_bid DECIMAL(18,8),
                               rt_bid DECIMAL(18,8),
                               ref_ask DECIMAL(18,8),
                               dp_ask DECIMAL(18,8),
                               mar_ask DECIMAL(18,8),
                               cf_ask DECIMAL(18,8),
                               rt_ask DECIMAL(18,8),
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               created_by INT,
                               updated_by INT NULL
);
