# 💱 MoneyChanger API – Backend Setup (Spring Boot + MySQL)

This README walks you through setting up the MoneyChanger backend API, including database schema, test data, and running in IntelliJ.

---

## 📁 Project Structure

```
moneychanger/
├── src/
├── pom.xml
├── application.properties
├── README.md
```

---

## ✅ Prerequisites

- Java 17+
- Maven
- MySQL (local or AWS RDS)
- IntelliJ IDEA or Spring Tool Suite
- Postman (optional)

---

## 🏗️ Step-by-Step Setup

### 1️⃣ Create Database

In DBeaver or MySQL CLI:

```
CREATE DATABASE moneychanger_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Then **import the schema and data**:

```
-- Step 1: Schema
Run `02_moneychanger_schema.sql`

-- Step 2: Sample Test Data
Run `05_moneychanger_test_data.sql`
```

---

### 2️⃣ Configure `application.properties`

In `src/main/resources/application.properties`, add your DB info:

```
spring.datasource.url=jdbc:mysql://localhost:3306/moneychanger_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.port=8080
```

---

### 3️⃣ Open in IntelliJ

1. Go to `File > Open` and select your `pom.xml`.
2. Let IntelliJ finish importing Maven dependencies.
3. Right-click `MoneyChangerApplication.java` > Run.

The backend will start at: `http://localhost:8080`

---

## 🔍 Test with Postman

### Sample Endpoints

- GET `/api/moneychangers`
- POST `/api/moneychangers`
- PUT `/api/moneychangers/{id}`
- DELETE `/api/moneychangers/{id}`

Headers:
```
Content-Type: application/json
```

Example JSON for POST:

```json
{
  "companyName": "Test MoneyChanger",
  "email": "test@example.com",
  "address": "1 ABC Road, Singapore",
  "schemeId": 1,
  "createdBy": 1
}
```

---

## 🧪 Testing

You can now:
- Create: Add new money changers.
- Read: Fetch all money changers.
- Update: Modify details.
- Delete: Soft-delete staff or changers.

---

## 🤝 Roles

- **Admin** can create/update/delete staff.
- **Staff** cannot delete admin.

Enforced in business logic (`AccountService.java`).

---

## 🔐 Auth (Future Scope)

In production, integrate AWS Cognito token validation.

---

## 💬 Need Help?

Just ask ChatGPT again!