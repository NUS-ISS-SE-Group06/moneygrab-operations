use fxuploaddb;

-- Purpose: Store the latest FX data for each currency.
CREATE TABLE fx_upload (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           currency_code CHAR(3) NOT NULL,
                           bid DECIMAL(18,8) NOT NULL,
                           ask DECIMAL(18,8) NOT NULL,
                           spread DECIMAL(18,8) NOT NULL,
                           uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           uploaded_by INT,
                           UNIQUE (currency_code)
);