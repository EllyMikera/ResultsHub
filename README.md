
# ResultHub – Project Setup Guide

Follow these steps to set up ResultHub on your computer. Make sure you have completed the database setup using the database README before proceeding.

## 1. Install the Required Software

Ensure the following software is installed:

- Java JDK 17 or later
- MySQL Server
- IntelliJ IDEA
- Git (optional if you download the project as a ZIP file)

If any of these are missing, install them before proceeding.

---

## 2. Download the Project

1. Open the project repository on GitHub.
2. Fork the repo
3. Clone the repo
You should see folders such as:

- `config`
- `database`
- `GUI`
- `lib`
- `services`
4. Create your own feature branch where you will write and test your code
---

## 3. Download the Required Libraries

ResultHub requires two external libraries.

### MySQL Connector/J

Allows Java to communicate with the MySQL database.

Download: https://dev.mysql.com/downloads/connector/j/

1. Select **Platform Independent**.
2. Download and extract the ZIP file.
3. Locate the `.jar` file inside it.

### jBCrypt

Used for securely hashing passwords.

Download: https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4

Click the JAR download link.

### Add the Libraries to the Project

1. Open the `ResultHub` folder.
2. Open the `lib` folder.
3. Copy both downloaded `.jar` files into `lib`.

Your `lib` folder should contain the MySQL Connector and jBCrypt libraries.

---

## 4. Add the Libraries to IntelliJ

1. Open the project in IntelliJ IDEA.
2. Click **File → Project Structure**.
3. Select **Libraries**.
4. Click the **+** sign and select **Java**.
5. Navigate to the project's `lib` folder.
6. Select both JAR files.
7. Click **Apply**, then **OK**.

The libraries should now appear under **External Libraries**.

---

## 5. Configure the Environment Variables

The project requires three environment variables to connect to the database.

| Variable | Value |
|---|---|
| DB_URL | `jdbc:mysql://localhost:3306/resulthub` |
| DB_USER | `resulthub_user` |
| DB_PASSWORD | Your MySQL user password |

### Add them in IntelliJ

1. Open **Run → Edit Configurations**.
2. Select your Java application configuration.
3. Find **Environment variables**.
4. Add the three variables above using your actual database password.
5. Click **Apply**, then **OK**.

Do not upload or commit your database password to GitHub.

---

## 6. Test the Database Connection

Before running the main application, confirm that Java can connect to MySQL.

1. Right-click the `config` folder.
2. Select **New → Java Class**.
3. Name the class `TestConnection`.
4. Paste the following code:

```java
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) throws Exception {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Connected to: " + connection.getCatalog());
        }
    }
}
```

5. Click the Run button beside the `main` method.

If everything is configured correctly, you should see:

```text
Connected to: resulthub
```

If the connection fails, check your database credentials, confirm MySQL is running, ensure the libraries have been added correctly and ensure you created the database

**Navigate the database folder to see how to set up the database locally**

After successfully testing the connection, delete `TestConnection.java`. It is only for testing and should not be included in the project.

---

## 7. Run ResultHub

Once all the setup steps are complete:

1. Locate the main Java class containing the `main` method.
2. Open the class.
3. Click the green Run button beside the `main` method.
4. Wait for the application to start.

You are now ready to work on ResultHub.

---

## Setup Checklist

- [ ] Java JDK 17 or later installed
- [ ] MySQL installed and running
- [ ] ResultHub database created
- [ ] Project downloaded and opened in IntelliJ
- [ ] MySQL Connector/J added
- [ ] jBCrypt added
- [ ] Database user created
- [ ] Environment variables configured
- [ ] Database connection tested successfully
- [ ] Project runs successfully

## HoD module foundation

The initial module structure, developer responsibilities, screen requirements, database dependencies, and implementation roadmap are documented in the [HoD Module Implementation Handover](docs/HOD_MODULE_IMPLEMENTATION.md).
