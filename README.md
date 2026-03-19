# Medical Clinic Management System
<<<<<<< HEAD
**A robust Java OOP backend demonstrating Hibernate ORM integration and PostgreSQL persistence.**

## Project Overview
This repository contains a specialized Java application developed to manage medical clinic operations. It focuses on implementing a clean **Object-Oriented** architecture to handle core entities, ensuring data integrity through a relational database.

## Tech Stack
=======

**A robust Java OOP backend demonstrating Hibernate ORM integration and PostgreSQL persistence.**


## Project Overview

This repository contains a specialized Java application developed to manage medical clinic operations. It focuses on implementing a clean **Object-Oriented** architecture to handle core entities, ensuring data integrity through a relational database.


## Tech Stack

>>>>>>> a957afa (refactor: Docker db-healthcheck & ReadMe update)
* **Language:** Java 25
* **Framework:** Hibernate ORM (JPA)
* **Database:** PostgreSQL
* **Containerization:** Docker (Separate server container & database container)
* **Build Tool:** Maven

<<<<<<< HEAD
* ## How to Run
* Open a Terminal and navigate to the root folder
* Set up the environment using docker:
    docker-compose up --build --quiet-pull
* Open another Terminal and navigate to /Client-module
* Run the Java Client side in the Terminal:
=======

## How to Run

* Open a Terminal and navigate to the root folder
* Set up the environment using docker:
  ```powershell
  docker-compose up --build -d
  ```
* Open another Terminal and and Run the Java Client side:

>>>>>>> a957afa (refactor: Docker db-healthcheck & ReadMe update)
  **Windows (PowerShell):**
  ```powershell
  .\mvnw.cmd exec:java -pl Client-module "-Dexec.mainClass=Client.`$Terminal"
  ```
  **Linux :**
  ```bash
  chmod +x mvnw
<<<<<<< HEAD
  ./mvnw exec:java -pl Client-module "-Dexec.mainClass=Client.\$Terminal"
  ```    
=======
  ./mvnw exec:java -pl Client-module '-Dexec.mainClass=Client.$Terminal'
  ```    


## Quick Verification Test

To ensure the Database, Server, and Client are communicating correctly, follow this sequence:
1. **Enter `2`** at the main menu to select **Log in**.
2. **Enter Credentials:**
   - **Id:** `Dr. Medic2`
   - **Password:** `2222`
3. **Expected Result:** You should see the Doctor's Menu:

```text
Connected to server.

1. Programare

2. Log in

3. Solicita cont

?. Optiuni

0. Inchide sesiunea

----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9----+

>> 2

Id: Dr. Medic2

Password: 2222

 [Medic-Id: Dr. Medic2 (Medic ortoped)]

1. Consult nou

2. Pacienti

3. Consultari

4. Afisare program

5. Resetare pin

?. Optiuni

0. Log out

```
>>>>>>> a957afa (refactor: Docker db-healthcheck & ReadMe update)
