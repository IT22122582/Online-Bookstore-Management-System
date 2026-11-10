# MySQL setup

The application now stores users, books, authors, borrowing transactions, and reviews in MySQL. It creates its tables on startup. On the first startup against empty tables, it imports the matching records from `data/*.txt`; the original files are left in place.

1. Start MySQL and create the database (or use a MySQL account allowed to create it):

   ```sql
   CREATE DATABASE library_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Set the connection details in the terminal where the app will run. The defaults are `localhost:3306`, database `library_management`, user `root`, and an empty password.

   ```sh
   export DB_URL='jdbc:mysql://localhost:3306/library_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
   export DB_USERNAME='your_mysql_user'
   export DB_PASSWORD='your_mysql_password'
   ```

3. From the project folder, start the application:

   ```sh
   ./mvnw spring-boot:run
   ```

   Then open <http://localhost:8082>.

The connection values are read from environment variables and are not stored in the repository. If using an existing database URL, set `DB_URL` to that server and database.
