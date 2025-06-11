USE moneychangerdb;

-- Truncate all dependent tables first
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE money_changer_photo;
TRUNCATE TABLE accounts;
TRUNCATE TABLE money_changer;
TRUNCATE TABLE scheme;
TRUNCATE TABLE currency_code;
SET FOREIGN_KEY_CHECKS = 1;

-- Insert schemes
INSERT INTO scheme (id, name_tag, description, is_default) VALUES
                                                               (1, 'Scheme 1', '',0),
                                                               (2, 'Scheme 2','',0),
                                                               (3, 'Scheme 3','',0),
                                                               (4, 'Scheme 4','',0),
                                                               (5, 'Scheme 5','',0),
                                                               (6, 'Scheme 6','',0),
                                                               (7, 'Scheme 7','',0),
                                                               (8, 'Scheme 8','',0),
                                                               (9, 'Scheme 9','',0),
                                                               (10, 'Scheme 10','',0);

-- Insert money_changer
INSERT INTO money_changer (id, company_name, email, date_of_incorporation, address, country, postal_code, notes, uen, scheme_id, created_by, updated_by) VALUES
                                                                                                                                                             (1, 'Company 1', 'company1@example.com', '2010-04-04', '123 Road St #1', 'Singapore', 'S12134', 'Test notes 1', 'UEN1XYZ', 1, NULL, NULL),
                                                                                                                                                             (2, 'Company 2', 'company2@example.com', '2010-08-02', '123 Road St #2', 'Singapore', 'S12234', 'Test notes 2', 'UEN2XYZ', 2, NULL, NULL),
                                                                                                                                                             (3, 'Company 3', 'company3@example.com', '2010-02-02', '123 Road St #3', 'Singapore', 'S12334', 'Test notes 3', 'UEN3XYZ', 3, NULL, NULL),
                                                                                                                                                             (4, 'Company 4', 'company4@example.com', '2010-09-03', '123 Road St #4', 'Singapore', 'S12434', 'Test notes 4', 'UEN4XYZ', 4, NULL, NULL),
                                                                                                                                                             (5, 'Company 5', 'company5@example.com', '2010-03-07', '123 Road St #5', 'Singapore', 'S12534', 'Test notes 5', 'UEN5XYZ', 5, NULL, NULL),
                                                                                                                                                             (6, 'Company 6', 'company6@example.com', '2010-01-08', '123 Road St #6', 'Singapore', 'S12634', 'Test notes 6', 'UEN6XYZ', 6, NULL, NULL),
                                                                                                                                                             (7, 'Company 7', 'company7@example.com', '2010-02-09', '123 Road St #7', 'Singapore', 'S12734', 'Test notes 7', 'UEN7XYZ', 7, NULL, NULL),
                                                                                                                                                             (8, 'Company 8', 'company8@example.com', '2010-04-01', '123 Road St #8', 'Singapore', 'S12834', 'Test notes 8', 'UEN8XYZ', 8, NULL, NULL),
                                                                                                                                                             (9, 'Company 9', 'company9@example.com', '2010-01-04', '123 Road St #9', 'Singapore', 'S12934', 'Test notes 9', 'UEN9XYZ', 9, NULL, NULL),
                                                                                                                                                             (10, 'Company 10', 'company10@example.com', '2010-05-01', '123 Road St #10', 'Singapore', 'S121034', 'Test notes 10', 'UEN10XYZ', 10, NULL, NULL);

-- Insert accounts
INSERT INTO accounts (id, money_changer_id, role, email, created_by, updated_by) VALUES
                                                                                     (1, 1, 'admin', 'user1@example.com', NULL, NULL),
                                                                                     (2, 2, 'admin', 'user2@example.com', NULL, NULL),
                                                                                     (3, 3, 'staff', 'user3@example.com', NULL, NULL),
                                                                                     (4, 4, 'staff', 'user4@example.com', NULL, NULL),
                                                                                     (5, 5, 'staff', 'user5@example.com', NULL, NULL),
                                                                                     (6, 6, 'staff', 'user6@example.com', NULL, NULL),
                                                                                     (7, 7, 'admin', 'user7@example.com', NULL, NULL),
                                                                                     (8, 8, 'admin', 'user8@example.com', NULL, NULL),
                                                                                     (9, 9, 'staff', 'user9@example.com', NULL, NULL),
                                                                                     (10, 10, 'admin', 'user10@example.com', NULL, NULL);

-- Insert money_changer_photo
INSERT INTO money_changer_photo (id, money_changer_id, photo_url, created_by, updated_by) VALUES
                                                                                              (1, 1, 'https://example.com/photo1.jpg', NULL, NULL),
                                                                                              (2, 2, 'https://example.com/photo2.jpg', NULL, NULL),
                                                                                              (3, 3, 'https://example.com/photo3.jpg', NULL, NULL),
                                                                                              (4, 4, 'https://example.com/photo4.jpg', NULL, NULL),
                                                                                              (5, 5, 'https://example.com/photo5.jpg', NULL, NULL),
                                                                                              (6, 6, 'https://example.com/photo6.jpg', NULL, NULL),
                                                                                              (7, 7, 'https://example.com/photo7.jpg', NULL, NULL),
                                                                                              (8, 8, 'https://example.com/photo8.jpg', NULL, NULL),
                                                                                              (9, 9, 'https://example.com/photo9.jpg', NULL, NULL),
                                                                                              (10, 10, 'https://example.com/photo10.jpg', NULL, NULL);
INSERT INTO currency_code(id,currency,description) VALUES
                                                       (1,'SGD', ''),
                                                       (2,'USD', ''),
                                                       (3,'EUR', ''),
                                                       (4,'MYR', ''),
                                                       (5,'IDR', '');