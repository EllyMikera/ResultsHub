# ResultHub

A database-driven Java GUI application for managing student results, timetables, seat allocation and complaints.

This guide shows how to set up the `resulthub` database on your own computer after cloning the repository.

---

## 1. Install MySQL, MySQL Workbench and the command line client

### Windows

1. Download the **MySQL Installer** from https://dev.mysql.com/downloads/installer/ (choose the larger "full" installer).
2. Run it and choose **Custom**, then add these products:
   - MySQL Server
   - MySQL Workbench
   - MySQL Shell (and the MySQL command line client, included with the server)
3. During setup, set a **root password** and remember it.
4. Check the install:

```bash
mysql --version
```

### macOS

Install [Homebrew](https://brew.sh) first if you do not have it, then:

```bash
brew install mysql
brew install --cask mysqlworkbench
brew services start mysql
```

Check the install:

```bash
mysql --version
```

On a fresh Homebrew install the root user has no password. You can set one with:

```bash
mysql_secure_installation
```

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install mysql-server mysql-client
sudo systemctl start mysql
```

MySQL Workbench is not in the default Ubuntu repositories. Download the `.deb` package from https://dev.mysql.com/downloads/workbench/ and install it:

```bash
sudo dpkg -i mysql-workbench-community_*.deb
sudo apt -f install
```

Check the install:

```bash
mysql --version
```

---

## 2. Clone the repository and go to the project folder

```bash
git clone <repository-url>
cd ResultHub
```

Confirm you are in the right place. You should see the `database` folder:

Windows (Command Prompt):

```bash
dir
```

macOS / Linux:

```bash
ls
```

Expected output includes: `config  database  GUI  services  README.md`

---

## 3. Create the `resulthub` database

The database must exist **before** you load the schema and seed data.

Log in to MySQL:

```bash
mysql -u root -p
```

Enter your root password, then run:

```bash
CREATE DATABASE resulthub;
SHOW DATABASES;
EXIT;
```

You should see `resulthub` in the list.

Alternative: in MySQL Workbench, open your local connection, run `CREATE DATABASE resulthub;` in a query tab and click the lightning bolt button.

---

## 4. Load the schema and seed data

Run these commands **from the `ResultHub` project folder** so the relative paths work. Load the schema first, then the seed data.

### macOS / Linux, and Windows Command Prompt (cmd)

```bash
mysql -u root -p resulthub < database/schema.sql
mysql -u root -p resulthub < database/seed.sql
```

On Windows use backslashes if forward slashes fail:

```bash
mysql -u root -p resulthub < database\schema.sql
mysql -u root -p resulthub < database\seed.sql
```

### Windows PowerShell

PowerShell does not support the `<` operator. Either switch to Command Prompt, or use the `source` method below, which works on every system.

### Using the `source` command (works on Windows, macOS and Linux)

From the `ResultHub` folder:

```
mysql -u root -p resulthub
```

Then inside the MySQL prompt:

```
source database/schema.sql;
source database/seed.sql;
```

### Using MySQL Workbench

1. Open your local connection and select the `resulthub` database (double-click it under **Schemas** so it is bold).
2. Choose **File > Open SQL Script** and open `database/schema.sql`. Click the lightning bolt to run it.
3. Repeat for `database/seed.sql`.

---

## 5. Verify the setup

```bash
mysql -u root -p resulthub
```

```bash
SHOW TABLES;
SELECT COUNT(*) FROM Student;
```

You should see all the project tables and a non-zero student count.

---

## 6. Update your connection settings

Open `config/DBConnection.java` and set the URL, username and password to match your local MySQL:

```
jdbc:mysql://localhost:3306/resulthub
```

Do **not** commit your personal password to the repository.

---

## Starting over

To reset your local database to a clean state:

```bash
mysql -u root -p -e "DROP DATABASE resulthub; CREATE DATABASE resulthub;"
mysql -u root -p resulthub < database/schema.sql
mysql -u root -p resulthub < database/seed.sql
```

---

## Troubleshooting

| Problem | Fix |
|---|---|
| `'mysql' is not recognized` (Windows) | Add the MySQL `bin` folder to PATH, then reopen the terminal |
| `command not found: mysql` (macOS/Linux) | Re-run the install step, or start a new terminal |
| `ERROR 1049: Unknown database 'resulthub'` | Create the database first (Step 3) |
| `ERROR 1045: Access denied` | Wrong password. Check the root password you set during install |
| `Can't connect to local MySQL server` | Start MySQL: `brew services start mysql` (macOS), `sudo systemctl start mysql` (Linux), or start the MySQL service in Windows Services |
| `Table already exists` | You already ran the schema. Use the "Starting over" steps |
| `No such file` when loading a script | You are not in the `ResultHub` folder. Run `cd` to it and check with `dir` or `ls` |

---

## Contributing

Never push directly to `main`. Create a branch, push it, and open a pull request for the group leader to review:

```
git checkout -b feature/your-feature-name
git add .
git commit -m "Describe your change"
git push origin feature/your-feature-name
```