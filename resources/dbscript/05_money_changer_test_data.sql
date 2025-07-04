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
INSERT INTO scheme (id,
                    name_tag,
                    description,
                    is_default
) VALUES
    (1, 'VIP 1', 'Premium commission rate for top-tier VIP clients',0),
    (2, 'VIP 2','Exclusive commission rate for long-term or high-volume customers',0),
    (3, 'VIP 3','Specialized rate for priority and valued clients',0),
    (4, 'VIP 4','Tailored commission rate for elite customers and special accounts',0),
    (5, 'PROMO','Promotional commission rate for limited-time offers and campaigns',0);


-- Insert money_changer
INSERT INTO money_changer (
    id,
    company_name,
    email,
    date_of_incorporation,
    address,
    country,
    postal_code,
    notes,
    uen,
    scheme_id,
    created_by,
    updated_by
) VALUES
      (1, 'Raffles Exchange Pte Ltd', 'raffles.exchange@example.com', '2010-04-04', '1 Raffles Place', 'Singapore', '048616', 'Test notes 1', 'UEN1XYZ', 1, NULL, NULL),
      (2, 'Marina Money Changer', 'marina.moneychanger@example.com', '2010-08-02', '2 Marina Blvd', 'Singapore', '018797', 'Test notes 2', 'UEN2XYZ', 2, NULL, NULL),
      (3, 'Robinson Forex Services', 'robinson.forex@example.com', '2010-02-02', '3 Robinson Rd', 'Singapore', '068895', 'Test notes 3', 'UEN3XYZ', 3, NULL, NULL),
      (4, 'Shenton Remittance & Exchange', 'shenton.remittance@example.com', '2010-09-03', '4 Shenton Way', 'Singapore', '068807', 'Test notes 4', 'UEN4XYZ', 4, NULL, NULL),
      (5, 'Temasek Currency Services', 'temasek.currency@example.com', '2010-03-07', '5 Temasek Blvd', 'Singapore', '038985', 'Test notes 5', 'UEN5XYZ', 5, NULL, NULL),
      (6, 'Orchard Money Exchange', 'orchard.exchange@example.com', '2010-01-08', '6 Orchard Rd', 'Singapore', '238801', 'Test notes 6', 'UEN6XYZ', 1, NULL, NULL),
      (7, 'Beach Road Forex', 'beachroad.forex@example.com', '2010-02-09', '7 Beach Rd', 'Singapore', '189702', 'Test notes 7', 'UEN7XYZ', 2, NULL, NULL),
      (8, 'Serangoon Money Services', 'serangoon.money@example.com', '2010-04-01', '8 Serangoon Rd', 'Singapore', '217964', 'Test notes 8', 'UEN8XYZ', 3, NULL, NULL),
      (9, 'Tampines Currency Exchange', 'tampines.exchange@example.com', '2010-01-04', '9 Tampines Ave', 'Singapore', '529551', 'Test notes 9', 'UEN9XYZ', 4, NULL, NULL),
      (10, 'Bukit Timah Money Changer', 'bukittimah.money@example.com', '2010-05-01', '10 Bukit Timah Rd', 'Singapore', '229840', 'Test notes 10', 'UEN10XYZ', 5, NULL, NULL);


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


INSERT INTO company_commission_scheme(id,money_changer_id,scheme_id,is_deleted) VALUES
                                                                                    (1,1,1,0),
                                                                                    (2,2,2,0),
                                                                                    (3,3,3,0),
                                                                                    (4,4,1,0);



INSERT INTO currency_code (
    id,
    currency,
    description
) VALUES
      (1, 'AED', 'United Arab Emirates Dirham'),
      (2, 'AFN', 'Afghan Afghani'),
      (3, 'ALL', 'Albanian Lek'),
      (4, 'AMD', 'Armenian Dram'),
      (5, 'AOA', 'Angolan Kwanza'),
      (6, 'ARS', 'Argentine Peso'),
      (7, 'AUD', 'Australian Dollar'),
      (8, 'AWG', 'Aruban Florin'),
      (9, 'AZN', 'Azerbaijani Manat'),
      (10, 'BAM', 'Bosnia and Herzegovina Convertible Mark'),
      (11, 'BBD', 'Barbadian Dollar'),
      (12, 'BDT', 'Bangladeshi Taka'),
      (13, 'BGN', 'Bulgarian Lev'),
      (14, 'BHD', 'Bahraini Dinar'),
      (15, 'BIF', 'Burundian Franc'),
      (16, 'BMD', 'Bermudian Dollar'),
      (17, 'BND', 'Brunei Dollar'),
      (18, 'BOB', 'Boliviano'),
      (19, 'BOV', 'Bolivian Mvdol'),
      (20, 'BRL', 'Brazilian Real'),
      (21, 'BSD', 'Bahamian Dollar'),
      (22, 'BTN', 'Bhutanese Ngultrum'),
      (23, 'BWP', 'Botswana Pula'),
      (24, 'BYN', 'Belarusian Ruble'),
      (25, 'BZD', 'Belize Dollar'),
      (26, 'CAD', 'Canadian Dollar'),
      (27, 'CDF', 'Congolese Franc'),
      (28, 'CHE', 'WIR Euro'),
      (29, 'CHF', 'Swiss Franc'),
      (30, 'CHW', 'WIR Franc'),
      (31, 'CLF', 'Chilean Unidad de Fomento'),
      (32, 'CLP', 'Chilean Peso'),
      (33, 'CNY', 'Chinese Yuan'),
      (34, 'COP', 'Colombian Peso'),
      (35, 'COU', 'Unidad de Valor Real'),
      (36, 'CRC', 'Costa Rican Colón'),
      (37, 'CUP', 'Cuban Peso'),
      (38, 'CVE', 'Cape Verdean Escudo'),
      (39, 'CZK', 'Czech Koruna'),
      (40, 'DJF', 'Djiboutian Franc'),
      (41, 'DKK', 'Danish Krone'),
      (42, 'DOP', 'Dominican Peso'),
      (43, 'DZD', 'Algerian Dinar'),
      (44, 'EGP', 'Egyptian Pound'),
      (45, 'ERN', 'Eritrean Nakfa'),
      (46, 'ETB', 'Ethiopian Birr'),
      (47, 'EUR', 'Euro'),
      (48, 'FJD', 'Fijian Dollar'),
      (49, 'FKP', 'Falkland Islands Pound'),
      (50, 'GBP', 'Pound Sterling'),
      (51, 'GEL', 'Georgian Lari'),
      (52, 'GHS', 'Ghanaian Cedi'),
      (53, 'GIP', 'Gibraltar Pound'),
      (54, 'GMD', 'Gambian Dalasi'),
      (55, 'GNF', 'Guinean Franc'),
      (56, 'GTQ', 'Guatemalan Quetzal'),
      (57, 'GYD', 'Guyana Dollar'),
      (58, 'HKD', 'Hong Kong Dollar'),
      (59, 'HNL', 'Honduran Lempira'),
      (60, 'HRK', 'Croatian Kuna'),
      (61, 'HTG', 'Haitian Gourde'),
      (62, 'HUF', 'Hungarian Forint'),
      (63, 'IDR', 'Indonesian Rupiah'),
      (64, 'ILS', 'Israeli New Shekel'),
      (65, 'INR', 'Indian Rupee'),
      (66, 'IQD', 'Iraqi Dinar'),
      (67, 'IRR', 'Iranian Rial'),
      (68, 'ISK', 'Icelandic Króna'),
      (69, 'JMD', 'Jamaican Dollar'),
      (70, 'JOD', 'Jordanian Dinar'),
      (71, 'JPY', 'Japanese Yen'),
      (72, 'KES', 'Kenyan Shilling'),
      (73, 'KGS', 'Kyrgyzstani Som'),
      (74, 'KHR', 'Cambodian Riel'),
      (75, 'KMF', 'Comorian Franc'),
      (76, 'KPW', 'North Korean Won'),
      (77, 'KRW', 'South Korean Won'),
      (78, 'KWD', 'Kuwaiti Dinar'),
      (79, 'KYD', 'Cayman Islands Dollar'),
      (80, 'KZT', 'Kazakhstani Tenge'),
      (81, 'LAK', 'Lao Kip'),
      (82, 'LBP', 'Lebanese Pound'),
      (83, 'LKR', 'Sri Lankan Rupee'),
      (84, 'LRD', 'Liberian Dollar'),
      (85, 'LSL', 'Lesotho Loti'),
      (86, 'LYD', 'Libyan Dinar'),
      (87, 'MAD', 'Moroccan Dirham'),
      (88, 'MDL', 'Moldovan Leu'),
      (89, 'MGA', 'Malagasy Ariary'),
      (90, 'MKD', 'Macedonian Denar'),
      (91, 'MMK', 'Myanmar Kyat'),
      (92, 'MNT', 'Mongolian Tugrik'),
      (93, 'MOP', 'Macanese Pataca'),
      (94, 'MRU', 'Mauritanian Ouguiya'),
      (95, 'MUR', 'Mauritian Rupee'),
      (96, 'MVR', 'Maldivian Rufiyaa'),
      (97, 'MWK', 'Malawian Kwacha'),
      (98, 'MXN', 'Mexican Peso'),
      (99, 'MXV', 'Mexican Unidad de Inversión (UDI)'),
      (100, 'MYR', 'Malaysian Ringgit'),
      (101, 'MZN', 'Mozambican Metical'),
      (102, 'NAD', 'Namibian Dollar'),
      (103, 'NGN', 'Nigerian Naira'),
      (104, 'NIO', 'Nicaraguan Córdoba'),
      (105, 'NOK', 'Norwegian Krone'),
      (106, 'NPR', 'Nepalese Rupee'),
      (107, 'NZD', 'New Zealand Dollar'),
      (108, 'OMR', 'Omani Rial'),
      (109, 'PAB', 'Panamanian Balboa'),
      (110, 'PEN', 'Peruvian Sol'),
      (111, 'PGK', 'Papua New Guinean Kina'),
      (112, 'PHP', 'Philippine Peso'),
      (113, 'PKR', 'Pakistani Rupee'),
      (114, 'PLN', 'Polish Złoty'),
      (115, 'PYG', 'Paraguayan Guaraní'),
      (116, 'QAR', 'Qatari Riyal'),
      (117, 'RON', 'Romanian Leu'),
      (118, 'RSD', 'Serbian Dinar'),
      (119, 'RUB', 'Russian Ruble'),
      (120, 'RWF', 'Rwandan Franc'),
      (121, 'SAR', 'Saudi Riyal'),
      (122, 'SBD', 'Solomon Islands Dollar'),
      (123, 'SCR', 'Seychellois Rupee'),
      (124, 'SDG', 'Sudanese Pound'),
      (125, 'SEK', 'Swedish Krona'),
      (126, 'SGD', 'Singapore Dollar'),
      (127, 'SHP', 'Saint Helena Pound'),
      (128, 'SLL', 'Sierra Leonean Leone'),
      (129, 'SOS', 'Somali Shilling'),
      (130, 'SRD', 'Surinamese Dollar'),
      (131, 'SSP', 'South Sudanese Pound'),
      (132, 'STN', 'São Tomé and Príncipe Dobra'),
      (133, 'SVC', 'Salvadoran Colón'),
      (134, 'SYP', 'Syrian Pound'),
      (135, 'SZL', 'Swazi Lilangeni'),
      (136, 'THB', 'Thai Baht'),
      (137, 'TJS', 'Tajikistani Somoni'),
      (138, 'TMT', 'Turkmenistan Manat'),
      (139, 'TND', 'Tunisian Dinar'),
      (140, 'TOP', 'Tongan Paʻanga'),
      (141, 'TRY', 'Turkish Lira'),
      (142, 'TTD', 'Trinidad and Tobago Dollar'),
      (143, 'TWD', 'New Taiwan Dollar'),
      (144, 'TZS', 'Tanzanian Shilling'),
      (145, 'UAH', 'Ukrainian Hryvnia'),
      (146, 'UGX', 'Ugandan Shilling'),
      (147, 'USD', 'United States Dollar'),
      (148, 'USN', 'United States Dollar (Next day)'),
      (149, 'UYI', 'Uruguay Peso en Unidades Indexadas (UI)'),
      (150, 'UYU', 'Uruguayan Peso'),
      (151, 'UYW', 'Unidad Previsional Uruguay'),
      (152, 'UZS', 'Uzbekistan Som'),
      (153, 'VES', 'Venezuelan Bolívar Soberano'),
      (154, 'VND', 'Vietnamese Đồng'),
      (155, 'VUV', 'Vanuatu Vatu'),
      (156, 'WST', 'Samoan Tala'),
      (157, 'XAF', 'CFA Franc BEAC'),
      (158, 'XAG', 'Silver (one troy ounce)'),
      (159, 'XAU', 'Gold (one troy ounce)'),
      (160, 'XBA', 'European Composite Unit'),
      (161, 'XBB', 'European Monetary Unit'),
      (162, 'XBC', 'European Unit of Account (XBC)'),
      (163, 'XBD', 'European Unit of Account (XBD)'),
      (164, 'XCD', 'East Caribbean Dollar'),
      (165, 'XDR', 'Special Drawing Rights'),
      (166, 'XOF', 'CFA Franc BCEAO'),
      (167, 'XPD', 'Palladium (one troy ounce)'),
      (168, 'XPF', 'CFP Franc'),
      (169, 'XPT', 'Platinum (one troy ounce)'),
      (170, 'XSU', 'SUCRE'),
      (171, 'XTS', 'Testing Code'),
      (172, 'XXX', 'No Currency'),
      (173, 'YER', 'Yemeni Rial'),
      (174, 'ZAR', 'South African Rand'),
      (175, 'ZMW', 'Zambian Kwacha'),
      (176, 'ZWL', 'Zimbabwean Dollar');
