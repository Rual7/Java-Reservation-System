# Java Reservation System

This project implements a **client–server reservation system** developed in Java.
The application allows multiple clients to connect simultaneously to a server, view available slots, create reservations, and manage their bookings.

The system is implemented as a **console-based application** and focuses on networking, concurrency, and database persistence.

## Technologies

* Java
* TCP Sockets
* Multithreading
* Quarkus
* Hibernate / JPA
* PostgreSQL
* Maven

## Architecture

The project follows a **client–server architecture**.

* The **server** manages connections, processes commands, and interacts with the database.
* Multiple **clients** can connect simultaneously using TCP sockets.
* Each client is handled in a separate thread.

## Project Structure

### Server

```
reservation.system
│
├── entity
│   ├── Reservation
│   └── Slot
│
├── protocol
│   ├── Command
│   ├── ReserveCommand
│   ├── CancelCommand
│   ├── ListCommand
│   ├── MyCommand
│   ├── ExitCommand
│   ├── InvalidCommand
│   └── ServerResponse
│
├── server
│   ├── SocketServer
│   └── ClientHandler
│
├── services
│   ├── ReservationService
│   └── ClientSession
│
└── startup
    ├── DataLoader
    └── ServerStartup
```

### Client

```
client_app
└── ReservationClient
```

## Features

The system supports the following operations:

* **LIST** – display available reservation slots
* **RESERVE** – create a reservation
* **MY** – view personal reservations
* **CANCEL** – cancel a reservation
* **EXIT** – terminate the client session

## Concurrency

The server supports multiple clients simultaneously by creating a **separate thread for each connection**.

This ensures that multiple users can interact with the reservation system at the same time.

## Database

Reservations are persisted using:

* **Hibernate**
* **JPA**
* **PostgreSQL**

The database stores reservation data and ensures that reservations remain consistent.

## Purpose

The purpose of this project was to practice:

* client–server architecture
* socket-based communication
* multithreading in Java
* database persistence using JPA
* clean separation between protocol, services, and entities
