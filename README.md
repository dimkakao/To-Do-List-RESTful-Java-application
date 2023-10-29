# Java RESTful Todo List Application

This Java-based RESTful Todo List project is designed to efficiently manage tasks and to-do items through a structured API architecture. It was developed in a team of 2 people.

---

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Task Management:**
  - Create, Retrieve, Update, and Delete tasks.
  - Assign priorities to tasks, add them to ToDo lists.

- **User Authentication:**
  - Secure user registration and login functionality.
  - Authentication and authorization mechanisms for secure access.

- **RESTful API Endpoints:**
  - Well-defined API endpoints to manage tasks and user-related functionalities.
  - Support for HTTP methods (GET, POST, PUT, DELETE) for task operations.

- **Structured Database:**
  - Integration with a PostgreSQL RDBMS for storing and managing tasks and user information.
  - Efficient data handling ensuring data integrity and security.

- **Robustness:**
  - Error handling and robust architecture ensuring a smooth user experience.

---

## Technologies Used

- **Java:** Core language for backend development.
- **Spring:** Framework for building RESTful APIs.
- **Database:** Integration with the PostgreSQL database for data storage.
- **RESTful Principles:** Adherence to RESTful design for API development.
- **Security Features:** Implementation of secure authentication and authorization mechanisms.

---

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/dimkakao/To-Do-List.-RESTful-Java-application
    ```
2. Set up and configure the required dependencies and environment.
3. Using IntelliJ IDEA, run the main class "ToDoListApplication" of the application.
4. After the first launch, change the name of the "schema.sql" file in the main/resources folder.
---

## Usage

1. Start the application server.
2. Access the application using appropriate endpoints for task management and user-related functionalities.

---

## Endpoints
### User Todos
- **GET /api/users/{userId}/todos**
  - Retrieve all todos associated with a specific user.

- **POST /api/users/{userId}/todos**
  - Create a new todo for a specific user.

- **GET /api/users/{userId}/todos/{todoId}**
  - Retrieve details of a specific todo for a user.

- **PUT /api/users/{userId}/todos/{todoId}**
  - Update details of a specific todo for a user.

- **DELETE /api/users/{userId}/todos/{todoId}**
  - Delete a specific todo belonging to a user.

- **GET /api/users/{userId}/todos/{todoId}/collaborators**
  - Retrieve the list of collaborators for a specific todo of a user.

- **POST /api/users/{userId}/todos/{todoId}/collaborators**
  - Add a new collaborator to a specific todo of a user.

- **DELETE /api/users/{userId}/todos/{todoId}/collaborators**
  - Remove a collaborator from a specific todo of a user.

### User Authentication
- **POST /auth/login**
  - Authenticate and log in a user.

### Users
- **GET /api/users**
  - Retrieve all users.

- **POST /api/users**
  - Create a new user.

- **GET /api/users/{id}**
  - Retrieve details of a specific user.

- **PUT /api/users/{id}**
  - Update details of a specific user.

- **DELETE /api/users/{id}**
  - Delete a specific user.

### Todo Tasks
- **GET /api/users/{userId}/todos/{todoId}/tasks**
  - Retrieve all tasks associated with a specific todo for a user.

- **POST /api/users/{userId}/todos/{todoId}/tasks**
  - Create a new task for a specific todo of a user.

- **GET /api/users/{userId}/todos/{todoId}/tasks/{taskId}**
  - Retrieve details of a specific task for a user's todo.

- **PUT /api/users/{userId}/todos/{todoId}/tasks/{taskId}**
  - Update details of a specific task for a user's todo.

- **DELETE /api/users/{userId}/todos/{todoId}/tasks/{taskId}**
  - Delete a specific task associated with a user's todo.

Note: Detailed documentation for each endpoint is available in the API documentation.

---

## Contributing

Contributions are welcome! If you'd like to contribute to the project, please follow these steps:
1. Fork the project.
2. Create your feature branch.
3. Commit your changes.
4. Push to the branch.
5. Open a pull request.

---

## License

This project is licensed under the [Your License] License - see the [LICENSE](LICENSE) file for details.

---


# Java RESTful project
## To-Do List Project.

You can use [Postman](https://www.postman.com/downloads/) to check the functionality

There are three predefined users in the DB with roles ADMIN and USER.

| Login         | Password | Role  |
|---------------|:--------:|:-----:|
| mike@mail.com |   1111   | ADMIN |
| nick@mail.com |   2222   | USER  |
| nora@mail.com |   3333   | USER  |
