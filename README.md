WellNest: Workplace Wellness & Stress Management Platform

WellNest is a comprehensive full-stack application designed to promote mental well-being and manage stress within a corporate environment. It provides tools for employees to monitor their well-being, engage in positive activities, and seek support when needed, while offering management insights to foster a healthier work culture.

Core Features

Employee Dashboard: A personalized space for users to track their well-being metrics.
Stress & Mood Tracking: Regular check-ins for users to log their stress levels and mood, providing a personal history and valuable data for organizational insights.
Wellness Challenges: Individual and team-based challenges to encourage healthy habits and build team cohesion.
Support System: A dedicated portal for employees to raise support requests. A support team panel allows for efficient management and resolution of these requests.


Tech Stack

Backend: Java, Spring Boot, Spring Data JPA, Spring Security
Frontend: React.js, JavaScript, CSS
Database:Mysql

Project Structure

The project is organized into two main directories:

-   `backend/`: Contains the Spring Boot application that provides the REST API.
-   `frontend/`: Contains the React.js client application.


Getting Started

To get the application running locally, follow these steps:

Prerequisites

*   Java 17 or later
*   Maven
*   Node.js & npm


Backend Setup

1.  Navigate to the backend directory:
    ```sh
    cd backend/stressbackend
    ```

2.  Configure the database:
    Open `src/main/resources/application.properties` and update the `spring.datasource` properties with your database URL, username, and password.

3.  Run the application:
    ```sh
    mvn spring-boot:run
    ```
    The backend server will start on `http://localhost:8080`.

Frontend Setup

1.  Navigate to the frontend directory:
    ```sh
    cd frontend
    ```

2.  Install dependencies:
    ```sh
    npm install
    ```

3.  Run the application:
    ```sh
    npm start
    ```
    The React development server will start on `http://localhost:3000`.

You can now access the application in your browser!Make Sure to add configurations(db,email)in application.properties. 
