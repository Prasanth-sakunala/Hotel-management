# BlueWave Hotel Management Application

A full-stack hotel management system with user authentication, room booking, admin management, and AWS S3 image upload support.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [AWS S3 Integration](#aws-s3-integration)
- [API Endpoints](#api-endpoints)
- [Environment Variables](#environment-variables)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- User registration, login, and profile management
- JWT-based authentication and authorization
- Room listing, searching, and booking
- Admin dashboard for managing rooms and bookings
- Image upload for rooms using AWS S3
- Responsive UI with React
- RESTful API with Spring Boot

---

## Tech Stack

- **Frontend:** React, CSS
- **Backend:** Spring Boot, Spring Security, JWT, JPA/Hibernate
- **Database:** (Configure your preferred DB, e.g., MySQL/PostgreSQL/H2)
- **Cloud Storage:** AWS S3 (for room images)
- **Build Tools:** Maven (backend), npm/yarn (frontend)

---

## Project Structure

```
hotel-management/
│
├── frontend/                  # React frontend
│   ├── public/
│   ├── src/
│   ├── package.json
│   └── ...
│
└── hotelapplicationBackend/   # Spring Boot backend
    ├── src/
    ├── pom.xml
    └── ...
```

---

## Getting Started

### Backend Setup

1. **Navigate to the backend directory:**
   ```sh
   cd hotelapplicationBackend
   ```

2. **Configure your database and AWS S3 credentials:**
   - Update `src/main/resources/application.properties` with your DB and AWS S3 credentials (see [AWS S3 Integration](#aws-s3-integration)).

3. **Build and run the backend:**
   ```sh
   ./mvnw spring-boot:run
   ```
   The backend will start on [http://localhost:8080](http://localhost:8080).

### Frontend Setup

1. **Navigate to the frontend directory:**
   ```sh
   cd frontend
   ```

2. **Install dependencies:**
   ```sh
   npm install
   ```

3. **Start the frontend:**
   ```sh
   npm start
   ```
   The frontend will start on [http://localhost:3000](http://localhost:3000).

---

## AWS S3 Integration

The backend uses AWS S3 for storing room images. The service is implemented in [`com.booking.hotelapplication.services.AwsS3Service`](hotelapplicationBackend/src/main/java/com/booking/hotelapplication/services/AwsS3Service.java).

**Configuration:**

Add the following properties to your `hotelapplicationBackend/src/main/resources/application.properties`:

```
aws.s3.access.key=YOUR_AWS_ACCESS_KEY
aws.s3.secret.key=YOUR_AWS_SECRET_KEY
```

- The S3 bucket name is set as `hotel-application-images` by default in the service.
- Images are uploaded using the `saveImagesToS3(MultipartFile photo)` method.
- Make sure your AWS credentials have permission to upload to the specified bucket.

---

## API Endpoints

- **Authentication:** `/auth/**`
- **Rooms:** `/rooms/**`
- **Bookings:** `/bookings/**`
- **User Management:** `/users/**` (protected)

See backend controller classes for detailed endpoint documentation.

---

## Environment Variables

- **Frontend:**  
  Configure API base URLs if needed (see `src/service/ApiService.js`).

- **Backend:**  
  Set DB credentials and AWS S3 keys in `application.properties`.

---

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a pull request

---
