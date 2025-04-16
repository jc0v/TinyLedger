# Tiny Ledger

This application is a small ledger that supports the following features

- Creating accounts with optional initial balance
- View account balance
- Credit/Debit an account
- View all transactions on an account
- Transfer between accounts

This application is written using Java 21, Spring Boot 3 and using a H2 in memory datastore.

To run it please execute 
```
./mvnw spring-boot:run 
```

## APIs

### To create an account.
```
POST - localhost:8080/v1/account?initialBalance=100
```
Initial balance param is optional

The response will contain the created account id used for subsequent requests

eg

```json
{
    "id": 1,
    "balance": 100
}
```

### To view an account balance
```
GET - localhost:8080/v1/account/{id}/balance
```

### To view all transactions on an account
```
GET - localhost:8080/v1/account/{id}/transactions
```

### To credit or debit an account
```
POST - localhost:8080/v1/account/{id}/transaction/{amount}
```
For a credit use a positive number and negative for debit. Zero is rejected.

### To transfer between accounts

```
POST - localhost:8080/v1/account/{id}/transfer/{toAccountId}/{amount}
```
Only positive amounts are supported
