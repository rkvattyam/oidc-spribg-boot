# 🧠 Credentials -app

This project is a full-stack Spring Boot application that:

- Validates the header bearer token for all API end points
- Saves/ retrieves dataof an user in an organisation

---

## 🚀 Features

- ✅ Save users with in an organisation 
- ✅ retrieves organisation and provides endpoint to register new user in an organisation
- ✅ Edit and delete credential of an user in an organisation

---

## 🛠 Tech Stack

- Java 21+
- Spring Boot
- PostgreSQL (via Docker)
- Docker Compose

---

## 📦 Getting Started

### 1. Clone and Build the Project

```bash
 download the project 
./mvnw clean install
```

---

## 📬 API Endpoints

### ➕ `POST /users`

Save user to an organization.

#### Request body:
**Headers:**
- `Authorization`: Bearer token (required)
- `Content-Type`: application/json (required)
```json
{
  "email": "testeee52@example.com",
  "firstName": "fname2",
  "lastName": "lnam2e",
  "organizations": [
    {"id": 22, "name": "Org 1"}
  ]
}


```
#### Response:
```json
{
  "id": 26,
  "firstName": "John",
  "lastName": "Doe",
  "email": "johnDoe@example.com",
  "organizations": [
    {
      "id": 22,
      "name": "GlobalSoft GmbH",
      "vat": "VAT654321",
      "sapId": "SAP002"
    }
  ]
}
```


---

### ➕ `POST /credentials`

Save API credentials created by the user in an organisation.

#### Request body:
**Headers:**
- `Authorization`: Bearer token (required)
- `Content-Type`: application/json (required)
```json
{
  "userId": 19,
  "organizationId": 21,
  "clientSecret": "super-secret-two"
}
```
#### Response:
```json
{
	"id": 9,
	"clientId": "0eb46aca-9bf0-447d-8746-763d816c9e56",
	"userId": 19,
	"organizationId": 21,
	"creationDate": "2025-11-13T19:14:55.8105588",
	"expiryDate": "2026-11-13T19:14:55.8105588"
}
```
---

### ➕ `GET /organizations/user/test@example.com`

Retrieve list of organisation of an user.

#### Request body:
**Headers:**
- `Authorization`: Bearer token (required)
- `Content-Type`: application/json (required)

#### Response:
```json
{
  "userId": null,
  "organizations": [
    {
      "id": 21,
      "name": "TechNova Ltd",
      "vat": "VAT123456",
      "sapId": "SAP001"
    },
    {
      "id": 22,
      "name": "GlobalSoft GmbH",
      "vat": "VAT654321",
      "sapId": "SAP002"
    },
    {
      "id": 23,
      "name": "InnoCore Systems",
      "vat": "VAT789012",
      "sapId": "SAP003"
    }
  ]
}
```

### ➕ `GET /credentials/21?email=john@example.com`

Retrieve list of credentials  of an user in that organisation.

#### Request body:
**Headers:**
- `Authorization`: Bearer token (required)
- `Content-Type`: application/json (required)

#### Response:
```json
[
  {
    "id": 9,
    "clientId": "0eb46aca-9bf0-447d-8746-763d816c9e56",
    "userId": 19,
    "organizationId": 21,
    "creationDate": "2025-11-13T19:14:55.810559",
    "expiryDate": "2026-11-13T19:14:55.810559"
  },
  {
    "id": 8,
    "clientId": "e4852d25-b95a-4e2b-abb5-598bb222d780",
    "userId": 19,
    "organizationId": 21,
    "creationDate": "2025-11-13T19:14:03.021939",
    "expiryDate": "2026-11-13T19:14:03.021939"
  }
]


```
### ➕ `PUT /credentials/{clientId}}`

Update the secret of the credential.

#### Request body:
**Headers:**
- `Authorization`: Bearer token (required)
- `Content-Type`: application/json (required)

#### Response:
```json
{
  "id": 8,
  "clientId": "e4852d25-b95a-4e2b-abb5-598bb222d780",
  "clientSecret": "super-secret-update",
  "creationDate": "2025-11-13T19:14:03.021939",
  "expiryDate": "2026-11-13T19:14:03.021939",
  "organization": {
    "id": 21,
    "name": "TechNova Ltd",
    "vat": "VAT123456",
    "sapId": "SAP001"
  },
  "createdBy": {
    "id": 19,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com"
  }
}
```

## 📄 License

MIT License