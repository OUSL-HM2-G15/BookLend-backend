📚 BookLend – Backend API

A Spring Boot backend for the BookLend project, providing APIs for user authentication, book management, and borrowing features.
This service uses MySQL as the database and REST API principles for communication.

🚀 Tech Stack

Java 17+
Spring Boot
Spring Web
Spring Data JPA
Spring Validation
Spring Security (if added later)
MySQL (local development)
Hibernate (JPA provider)
Maven for dependency management

📂 Project Structure of main folders
booklend-backend/
 ├─ sql/
 │   ├─ 01__initial_schema.sql
 │   └─ 02__initial_data.sql
 ├─ src/
 │   ├─ main/
 │   │   ├─ java/com/booklend/backend/...
 │   │   └─ resources/
 │   │       └─ application.properties
 ├─ pom.xml
 ├─ .gitignore
 ├─ README.md
 └─ .env.properties  (NOT in Git - create this file manually)

⚙️ Getting Started (For Developers)
1️⃣ Clone the repository
git clone https://github.com/OUSL-HM2-G15/booklend-backend.git
cd booklend-backend

2️⃣ Install Requirements
Make sure you have:
Java 17 or above
MySQL Workbench or DBeaver or phpMyAdmin
MySql server
Maven

3️⃣ Create the MySQL Database

Open MySQL Workbench or terminal:
CREATE DATABASE booklend;

## Running SQL Files Manually

### 1. SQL Folder Structure

All SQL files are located in the `sql/` folder inside the project.

**Tip:** Files are prefixed with numbers to maintain the correct execution order.

### 2. Running SQL Files via GUI Tools (Optional) or MySQL CLI

You can run the SQL scripts using tools like:

* **MySQL Workbench**
* **DBeaver**
* **phpMyAdmin**

Steps:

1. Open the tool and connect to your MySQL database.
2. Open the SQL file.
3. Execute the script.
4. Repeat for all files in order.

4️⃣ Configure Environment Variables

The file .env.properties is NOT included in the repo.

Create a new file:

.env.properties (must be in the SAME folder as pom.xml - booklend-backend/.env.properties)

Paste this and fill your details:

DB_HOST=localhost
DB_PORT=3306
DB_NAME=booklend
DB_USERNAME=yourUsername
DB_PASSWORD=yourPassword

5️⃣ Run the Application

Using Maven:
mvn spring-boot:run

Or using an IDE (VS Code / IntelliJ):
Run the BooklendBackendApplication class.

🧪 Testing the API

Once the backend is running:

Open in browser/Postman:

http://localhost:8080

you should see: Spring Boot is working!

📤 Environment & Security Notes

These files are intentionally not included in the repository:

❌ Database credentials
❌ Local MySQL Workbench configurations

Included instead:

✔ .gitignore to prevent secrets from being pushed

🤝 Team Collaboration Guide
Every developer must:

Pull the latest code
Ensure .env.properties exists with your own credentials
Start the app

DO NOT COMMIT:
Database credentials
.env.properties
IDE settings
Local DB files

Git is configured to ignore these.

👨‍💻 Contributing Workflow

Create a new branch:

feature/<task-name>
fix/<bug-name>
refactor/<module>

Commit changes

Push and open a Pull Request