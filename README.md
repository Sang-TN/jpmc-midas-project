# J.P. Morgan Chase & Co. - Midas Core Simulation

This repository contains my completed solution for the **J.P. Morgan Chase Software Engineering Virtual Experience**. The project simulates "Midas Core," a backend system for processing financial transactions with real-time incentive logic.

##  Key Features & Implementation
* **Task 1-3: Event-Driven Architecture**
    * Configured a **Spring Boot** application with **Apache Kafka**.
    * Implemented a `KafkaConsumer` to listen for transaction events and persist data using **Spring Data JPA** and an H2 database.
* **Task 4: REST API Integration**
    * Integrated an external **Incentives API** using `RestTemplate`.
    * Implemented logic to fetch transaction bonuses dynamically and update user balances accordingly.
* **Task 5: REST Controller Development**
    * Developed a custom **REST API** endpoint (`/balance`) to expose user balances.
    * Configured the application to run on a specific port (33400) as required by the simulation's specifications.

##  Tech Stack
* **Language:** Java 17
* **Framework:** Spring Boot 3
* **Messaging:** Apache Kafka
* **Persistence:** Hibernate / JPA / H2 (In-Memory Database)
* **Integration:** RestTemplate (JSON POST/GET)
* **Tools:** Maven, Git, IntelliJ IDEA

##  How to Run
1. Ensure a Kafka broker is running on `localhost:9092`.
2. Start the external incentive API provided in the `services/` folder.
3. Run `MidasCoreApplication.java` or use `mvn spring-boot:run`.
4. Access the balance endpoint via `http://localhost:33400/balance?userId=1`.
