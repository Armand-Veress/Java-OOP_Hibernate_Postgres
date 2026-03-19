# Medical Clinic Management System
**A robust Java OOP backend demonstrating Hibernate ORM integration and PostgreSQL persistence.**

## Project Overview
This repository contains a specialized Java application developed to manage medical clinic operations. It focuses on implementing a clean **Object-Oriented** architecture to handle core entities, ensuring data integrity through a relational database.

## Tech Stack
* **Language:** Java 25
* **Framework:** Hibernate ORM (JPA)
* **Database:** PostgreSQL
* **Containerization:** Docker (Separate server container & database container)
* **Build Tool:** Maven

* ## How to Run
* Open a Terminal and navigate to the root folder
* Set up the environment using docker:
    docker-compose up --build --quiet-pull
* Open another Terminal and navigate to /Client-module
* Run the Java Client side in the Terminal:
    ** Windows (PowerShell): **
      .\mvnw.cmd exec:java -pl Client-module "-Dexec.mainClass=Client.\`$Terminal"
    ** Linux : **
      chmod +x mvnw
      ./mvnw exec:java -pl Client-module "-Dexec.mainClass=Client.\$Terminal"
      
