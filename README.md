# To-Do List Application

## Overview
This is a To-Do List application built with Spring Boot and Kotlin. The application allows users to manage their tasks efficiently by creating and managing to-do lists and task elements. It features user registration, task management, and collaboration functionalities.

## Features
- **User Registration**: Users can create an account to manage their tasks.
- **To-Do Lists**: Users can create, view, and delete their to-do lists.
- **Task Management**: Users can add, remove, and mark tasks as completed.
- **Collaboration**: Users can share to-do lists with others.
- **Password Security**: User passwords are hashed for secure storage.
- **RESTful API**: The application exposes a RESTful API for client-server interactions.

## Technologies Used
- **Backend**: Spring Boot, Kotlin
- **Database**: H2 (in-memory database)
- **Security**: Spring Security
- **Build Tool**: Gradle

## Getting Started

### Prerequisites
- JDK 21
- Gradle
- Kotlin 1.9.25

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/saif-eddine8/todolist.git
   cd todolist
   
### Build the project:
./gradlew build

### Run the application:
./gradlew bootRun

## API Endpoints:

### User Registration:

**POST** `/api/register`

**Request Body:**


 ```json
{
  "username": "string",
  "password": "string"
}
```

### To-Do List Management

#### Create a To-Do List

**POST** `/api/todolists`

**Request Body:**

```json
{
  "name": "string",
  "description": "string"
}
```

### To-Do List Management

#### Get All To-Do Lists

**GET** `/api/todolists`

**Response:**

- **200 OK**: A list of all To-Do lists returned in JSON format.

---

#### Delete a To-Do List

**DELETE** `/api/todolists/{id}`

**Response:**

- **204 No Content**: To-Do list deleted successfully.
- **404 Not Found**: To-Do list with the specified ID does not exist.

### Task Management

#### Add Task to To-Do List

**POST** `/api/todolists/{id}/tasks`

**Request Body:**

```json
{
  "name": "string"
}
```

#### Remove Task

**DELETE** `/api/todolists/{listId}/tasks/{taskName}`

**Response:**

- **204 No Content**: Task removed successfully.
- **404 Not Found**: To-Do list or task does not exist.

#### Mark Task as Completed

**PUT** `/api/todolists/{listId}/tasks/{taskName}/complete`

**Request Body:**

```json
{
  "completed": true
}
```

## Database
The application uses an H2 database. You can access the H2 console at [http://localhost:8080/h2](http://localhost:8080/h2) with the following credentials:

- **Username**: ...
- **Password**: ...

## Running Tests
To run tests, execute:

```bash
./gradlew test
```
## Author
**Saif-Eddine**



