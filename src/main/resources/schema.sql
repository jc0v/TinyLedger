CREATE TABLE IF NOT EXISTS account
(
        id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
		balance DECIMAL(20,2)
);

CREATE TABLE IF NOT EXISTS transaction
(
        id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
        account_id int NOT NULL,
        type varchar(64) NOT NULL,
        amount DECIMAL(20,2) NOT NULL,
        tran_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_account_transaction FOREIGN KEY (account_id) REFERENCES account (id)
);