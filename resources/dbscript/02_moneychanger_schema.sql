use moneychangerdb;

-- STEP 1: Create scheme table
CREATE TABLE scheme (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name_tag VARCHAR(100) NOT NULL,
                        description VARCHAR(500),
                        is_default BOOLEAN DEFAULT FALSE,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        created_by INT,
                        updated_by INT NULL,
                        is_deleted TINYINT(1) DEFAULT 0
);

-- STEP 2: Create money_changer table
CREATE TABLE money_changer (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               company_name VARCHAR(255) NOT NULL,
                               email VARCHAR(255) NOT NULL UNIQUE,
                               date_of_incorporation DATE,
                               address TEXT,
                               country VARCHAR(100),
                               postal_code VARCHAR(20),
                               notes TEXT,
                               uen VARCHAR(100),
                               scheme_id INT,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               created_by INT,
                               updated_by INT NULL,
                               is_deleted TINYINT(1) DEFAULT 0
);

-- STEP 3: Create accounts table
CREATE TABLE accounts (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          money_changer_id INT NOT NULL,
                          role VARCHAR(100) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          created_by INT,
                          updated_by INT NULL,
                          is_deleted TINYINT(1) DEFAULT 0
);

-- STEP 4: Create currency_code table
CREATE TABLE currency_code (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               currency VARCHAR(10) NOT NULL,
                               description VARCHAR(255)
);

-- STEP 5: Create commission_rate table
CREATE TABLE commission_rate (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 currency_id INT NOT NULL,
                                 scheme_id   INT NOT NULL,
                                 rate DECIMAL(18,8) NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 created_by INT,
                                 updated_by INT NULL,
                                 is_deleted TINYINT(1) DEFAULT 0,
                                 CONSTRAINT fk_commission_rate_currency FOREIGN KEY (currency_id) REFERENCES currency_code(id),
                                 CONSTRAINT fk_commission_rate_scheme FOREIGN KEY (scheme_id) REFERENCES scheme(id),
                                 CONSTRAINT fk_commission_rate_created_by FOREIGN KEY (created_by) REFERENCES accounts(id),
                                 CONSTRAINT fk_commission_rate_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id)
);

-- STEP 6: Create company_commission_scheme table
CREATE TABLE company_commission_scheme (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           money_changer_id INT NOT NULL,
                                           scheme_id INT NOT NULL,
                                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           created_by INT,
                                           updated_by INT NULL,
                                           is_deleted TINYINT(1) DEFAULT 0,
                                           CONSTRAINT fk_company_commission_money_changer FOREIGN KEY (money_changer_id) REFERENCES money_changer(id),
                                           CONSTRAINT fk_company_commission_scheme FOREIGN KEY (scheme_id) REFERENCES scheme(id),
                                           CONSTRAINT fk_company_commission_created_by FOREIGN KEY (created_by) REFERENCES accounts(id),
                                           CONSTRAINT fk_company_commission_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id)
);


-- STEP 7: Create money_changer_photo table
CREATE TABLE money_changer_photo (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     money_changer_id INT NOT NULL,
                                     photo_data LONGBLOB NOT NULL,
                                     photo_filename VARCHAR(255),
                                     photo_mimetype VARCHAR(100),
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     created_by INT,
                                     updated_by INT NULL,
                                     is_deleted TINYINT(1) DEFAULT 0,
                                     CONSTRAINT fk_photo_money_changer FOREIGN KEY (money_changer_id) REFERENCES money_changer(id),
                                     CONSTRAINT fk_photo_created_by FOREIGN KEY (created_by) REFERENCES accounts(id),
                                     CONSTRAINT fk_photo_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id)
);

-- STEP 8: Create money_changer_kyc table
CREATE TABLE money_changer_kyc (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   money_changer_id INT NOT NULL,
                                   document_data LONGBLOB NOT NULL,
                                   document_filename VARCHAR(255),
                                   document_mimetype VARCHAR(100),
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   created_by INT,
                                   updated_by INT NULL,
                                   is_deleted TINYINT(1) DEFAULT 0,
                                   CONSTRAINT fk_kyc_money_changer FOREIGN KEY (money_changer_id) REFERENCES money_changer(id) ON DELETE CASCADE,
                                   CONSTRAINT fk_kyc_created_by FOREIGN KEY (created_by) REFERENCES accounts(id),
                                   CONSTRAINT fk_kyc_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id)
);


-- STEP 9: Create money_changer_location table
CREATE TABLE money_changer_location (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        money_changer_id INT NOT NULL,
                                        location_name VARCHAR(255) NOT NULL,
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        created_by INT,
                                        updated_by INT NULL,
                                        is_deleted TINYINT(1) DEFAULT 0,
                                        CONSTRAINT fk_location_money_changer FOREIGN KEY (money_changer_id) REFERENCES money_changer(id) ON DELETE CASCADE,
                                        CONSTRAINT fk_location_created_by FOREIGN KEY (created_by) REFERENCES accounts(id),
                                        CONSTRAINT fk_location_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id)
);


-- STEP 10: Create money_changer_currency table
CREATE TABLE money_changer_currency (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   money_changer_id INT NOT NULL,
                                   currency_id INT NOT NULL,
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   created_by INT,
                                   updated_by INT NULL,
                                   is_deleted TINYINT(1) DEFAULT 0,
                                   CONSTRAINT fk_mcur_money_changer FOREIGN KEY (money_changer_id) REFERENCES money_changer(id),
                                   CONSTRAINT fk_mcur_currency FOREIGN KEY (currency_id) REFERENCES currency_code(id),
                                   CONSTRAINT fk_mcur_created_by FOREIGN KEY (created_by) REFERENCES accounts(id),
                                   CONSTRAINT fk_mcur_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id)
);


-- STEP 11: Create customer table
CREATE TABLE customer (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          customer_name VARCHAR(255) NOT NULL,
                          password_hash VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          created_by INT,
                          updated_by INT NULL,
                          is_deleted TINYINT(1) DEFAULT 0,
                          CONSTRAINT fk_customer_created_by FOREIGN KEY (created_by) REFERENCES accounts(id),
                          CONSTRAINT fk_customer_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id)
);

-- STEP 11: Create compute_rates table
CREATE TABLE compute_rates (
                               currency_code CHAR(3) NOT NULL PRIMARY KEY,
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
                               processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               processed_by INT NULL,
                               CONSTRAINT fk_compute_rates_processed_by FOREIGN KEY (processed_by) REFERENCES accounts(id)
);



ALTER TABLE money_changer ADD CONSTRAINT fk_money_changer_scheme FOREIGN KEY (scheme_id) REFERENCES scheme(id);
ALTER TABLE money_changer ADD CONSTRAINT fk_money_changer_created_by FOREIGN KEY (created_by) REFERENCES accounts(id);
ALTER TABLE money_changer ADD CONSTRAINT fk_money_changer_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id);

ALTER TABLE accounts ADD CONSTRAINT fk_accounts_money_changer FOREIGN KEY (money_changer_id) REFERENCES money_changer(id);
ALTER TABLE accounts ADD CONSTRAINT fk_accounts_created_by FOREIGN KEY (created_by) REFERENCES accounts(id);
ALTER TABLE accounts ADD CONSTRAINT fk_accounts_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id);

ALTER TABLE scheme ADD CONSTRAINT fk_commission_scheme_created_by FOREIGN KEY (created_by) REFERENCES accounts(id);
ALTER TABLE scheme ADD CONSTRAINT fk_commission_scheme_updated_by FOREIGN KEY (updated_by) REFERENCES accounts(id);



