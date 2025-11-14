Angular + Spring Boot + OIDC + Postgres Docker Setup
This project demonstrates the applications with:

Frontend: Angular served via Nginx
Backend: Spring Boot microservice
OIDC: Mock OpenID Connect provider
Database: Postgres
All services are orchestrated with Docker Compose.

Prerequisites
Docker
Docker compose
Node.js (for local Angular build if needed)
Angular cli
Folder Structure
project-root/ ├── frontend/ # Angular project + Dockerfile + nginx.conf ├── backend/ # Spring Boot project + Dockerfile ├── mock-oidc/ # Mock OIDC provider + Dockerfile ├── docker-compose.yml └── README.md

Angular Frontend
The Angular app is built and served via Nginx:

Dockerfile builds the app using ng build --configuration production.
Output is copied to /usr/share/nginx/html inside Nginx.
SPA routing handled with try_files $uri /index.html;.
Build Angular locally (optional)
cd frontend
npm install
npx ng build --configuration production --project client-org
Build & Run All Services
cd project-root
docker-compose up --build
Backend API Endpoints
Example: Get User Organizations

GET http://localhost:8080/api/organizations/user/test@example.com Example HTTP/1.1
Host: localhost:8080
Authorization: Bearer <JWT_ACCESS_TOKEN>
Content-Type: application/json

| Header        | Description                  | Required |
| ------------- | ---------------------------- | -------- |
| Authorization | Bearer token (JWT from OIDC) | Yes      |
| Content-Type  | application/json             | Yes      |

Response Example

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
Stop and Clean
docker-compose down -v
