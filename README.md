# GymTracker Backend Server

Welcome to the GymTracker backend server repository! This Java-based server is an essential part of the GymTracker app, providing the necessary functionality to track gym activities and progress.

## Table of Contents
- [Introduction](#introduction)
- [Technologies](#technologies)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)

## Introduction

The GymTracker Backend Server is the backbone of the GymTracker app, responsible for handling workout data and user authentication. This server is built with Java and Spring Boot, making it robust and flexible for your gym tracking needs.

## Technologies

The GymTracker Backend Server utilizes the following technologies:

- **Java**: The primary programming language for the application.
- **Spring Boot**: A powerful framework for building Java applications.
- **Spring Data JPA**: Simplifies database interactions using the Java Persistence API.
- **Hibernate**: Provides the ORM implementation for mapping Java objects to database entities.
- **Maven**: Handles project dependencies and build processes.
- **Swagger**: Generates API documentation for easy reference.
- **Lombok**: Reduces boilerplate code with annotations for data classes.
- **JUnit**: Facilitates unit testing.
- **Spring Security**: Provides authentication and authorization for the application.
- **ModelMapper**: Simplifies object mapping between DTOs and entities.

## Installation

To set up the GymTracker backend server locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/zephir0/GymTracker.git

2. Navigate to the project directory:
   ```bash
   cd GymTracker

3. Install the application using Maven:
   ```bash
   mvn install

4. Run the application:
   ```bash
   mvn spring-boot:run

## Usage
The GymTracker backend server provides RESTful API endpoints for managing workouts, users, and authentication.


## API Documentation
For detailed information about the available API endpoints and how to use them, refer to the [API Documentation](https://zephir0.github.io/GymTracker/).

## Contributing
Contributions to the GymTracker backend server are welcome! To contribute:

Fork the repository.
1. Create a new branch: git checkout -b feature/your-feature-name
2. Make your changes and commit them: git commit -m "Add your message here"
3. Push to the branch: git push origin feature/your-feature-name
4. Open a pull request, detailing your changes and their benefits.