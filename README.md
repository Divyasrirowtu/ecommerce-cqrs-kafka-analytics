🛒 Ecommerce CQRS Kafka Analytics

A production-ready Ecommerce backend built using Spring Boot, Apache Kafka, and CQRS architecture, containerized with Docker, deployed on AWS EC2, secured with Nginx reverse proxy and HTTPS (SSL).

📌 Project Overview

This project demonstrates:

CQRS (Command Query Responsibility Segregation)

Event-driven architecture using Kafka

Docker-based containerization

Production deployment on AWS EC2

Nginx reverse proxy configuration

HTTPS setup using Let's Encrypt SSL

Auto-restart production container setup

🏗️ Architecture
Client (Browser)
        ↓
HTTPS (443)
        ↓
Nginx Reverse Proxy
        ↓
Docker Container (Spring Boot App)
        ↓
Kafka (Event Streaming)
        ↓
Command Service → Database (Write)
Query Service   → Database (Read)

Tech Stack
| Layer            | Technology         |
| ---------------- | ------------------ |
| Backend          | Spring Boot        |
| Messaging        | Apache Kafka       |
| Architecture     | CQRS               |
| Containerization | Docker             |
| Reverse Proxy    | Nginx              |
| Cloud            | AWS EC2            |
| SSL              | Let's Encrypt      |
| Build Tool       | Maven              |
| Database         | MySQL / PostgreSQL |

⚙️ Step-by-Step Implementation
✅ Step 1–10: Project Setup

Created Spring Boot services (Command & Query)

Configured Kafka producer & consumer

Implemented CQRS pattern

Configured application.properties

Built and tested locally

Run locally:

mvn clean install
mvn spring-boot:run
✅ Step 11–15: Dockerization
1️⃣ Create Dockerfile
FROM openjdk:17
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
2️⃣ Build Docker Image
docker build -t ecommerce-app .
3️⃣ Run Container
docker run -p 8080:8080 ecommerce-app
✅ Step 16–18: AWS EC2 Deployment
1️⃣ Launch EC2 Instance (Ubuntu)
2️⃣ SSH into EC2
ssh -i "your-key.pem" ubuntu@EC2_PUBLIC_IP
3️⃣ Install Docker
sudo apt update
sudo apt install docker.io -y
✅ Step 19: Configure Nginx Reverse Proxy

Install Nginx:

sudo apt install nginx -y

Edit config:

sudo nano /etc/nginx/sites-available/default

Add:

server {
    listen 80;

    location / {
        proxy_pass http://localhost:8080;
    }
}

Restart Nginx:

sudo systemctl restart nginx
✅ Step 20: Production Docker Setup (Auto Restart)

Run container with restart policy:

docker run -d \
  --name ecommerce-container \
  -p 8080:8080 \
  --restart always \
  ecommerce-app

Now container:

Restarts if it crashes

Restarts if EC2 reboots

✅ Step 21: Enable HTTPS (SSL)

Install Certbot:

sudo apt install certbot python3-certbot-nginx -y

Generate SSL:

sudo certbot --nginx

This:

Installs SSL

Redirects HTTP → HTTPS

Enables auto-renewal

🚀 Final Production Setup

✔ Dockerized Application
✔ Running on AWS EC2
✔ Nginx Reverse Proxy
✔ HTTPS Enabled
✔ Auto Restart Enabled
✔ Event-Driven CQRS Architecture

📂 Project Structure
ecommerce-cqrs-kafka-analytics/
│
├── command-service/
├── query-service/
├── analytics-dashboard/
├── Dockerfile
├── pom.xml
└── README.md

🧠 Key Concepts Demonstrated

Event-driven Microservices

Kafka Producer & Consumer

CQRS Pattern Implementation

Reverse Proxy Configuration

Production Cloud Deployment

SSL Security Configuration

Container Lifecycle Management

🎯 How to Run (Full Production Flow)

Clone repo

Build JAR

Build Docker image

Deploy on EC2

Configure Nginx

Enable SSL

Access via domain

👩‍💻 Author

Divya Sri Rowtu