# java-kanban

A lightweight, pure Java task tracker app implementing a kanban-style workflow.

[![OpenJDK 21](https://img.shields.io/badge/OpenJDK-21-red?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![JSON API](https://img.shields.io/badge/API-JSON-lightgrey?logo=json)](https://www.json.org/json-en.html)
[![Gson](https://img.shields.io/badge/JSON-Gson-blue?logo=google)](https://github.com/google/gson)
[![No Spring](https://img.shields.io/badge/dependency%20free-No_Spring-success)](https://spring.io/)
[![Pure Java](https://img.shields.io/badge/stack-Pure_Java-yellow)](https://dev.java/)

## Features

### Core Functionality
- **Task Management**:
    - Create, update, and delete tasks
    - Track task status
    - Manage task duration and timelines
- **Epic/Subtask System**:
    - Create complex tasks (epics) composed of smaller subtasks
    - Automatic calculation of epic timelines based on subtasks
- **History Tracking**:
    - View recently accessed tasks
- **Priority System**:
    - Tasks sorted by start time using `TreeSet` with custom comparator

### Storage Options
- **In-Memory Storage**: Fast runtime storage
- **File-Based Storage**: Persistent storage to file

### REST API
- HTTP endpoints for basic operations (GET/POST/DELETE)
- JSON serialization using Gson with custom converters


## Getting Started

### Prerequisites
- Java 21 or later
- Maven 3.9 or later
- Git 2.34.1 or later

### Clone the Repository
  ```sh
  git clone https://github.com/DawydowGerman/java-kanban.git
  ```
  ```sh
  cd java-kanban
  ```

### Build with Maven
  ```sh
  mvn clean package
  ```

### Run the Application

- Option A: Run directly with Maven
  ```sh
  mvn exec:java -Dexec.mainClass="ru.practicum.kanban.Main"
  ```

- Option B: Run from built JAR
  ```sh
  java -jar target/kanban-1.0-SNAPSHOT.jar
  ```