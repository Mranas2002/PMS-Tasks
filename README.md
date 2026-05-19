# IIUI Student Email GUI App

Java Swing application that validates an IIUI email, extracts student information, and saves it to a MySQL database.

## Project Structure

```text
src/main/java/com/iiui/studentapp/
  App.java
  frontend/
    StudentEmailFrame.java
  backend/
    config/DatabaseConfig.java
    dao/DatabaseConnection.java
    dao/StudentDao.java
    model/StudentInfo.java
    service/StudentEmailParser.java
    service/ValidationException.java
sql/
  create_database.sql
lib/
  put mysql-connector-j jar here
build/
  compiled classes
```

## Validation Rules

- Email domain must be `@iiu.edu.pk` or `@student.iiu.edu.pk`.
- Email format example: `rimsha.bsit822@iiu.edu.pk`.
- Email must contain a dot in the local part: `name.degreereg@iiu.edu.pk`.
- Name part before dot must contain alphabets only.
- Degree and Reg# are extracted from the text after the dot.
- Degree title must contain alphabets only and minimum 4 characters.
- Registration number must contain minimum 1 digit and maximum 4 digits.
- Batch is entered separately in the GUI.
- Same email can be saved multiple times.
- For `rimsha.bsit822@iiu.edu.pk`, extracted data is:
  - Name: `Rimsha`
  - Degree Title: `BSIT`
  - Reg #: `822`
  - Batch: entered in GUI, for example `F22`
  - Full Reg No: `822/FOC/BSIT/F22`

## Database Setup in XAMPP

1. Start Apache and MySQL from XAMPP Control Panel.
2. Open phpMyAdmin.
3. Import `sql/create_database.sql`.
4. Default connection settings are in `DatabaseConfig.java`:
   - Database: `iiui_student_db`
   - User: `root`
   - Password: empty

## MySQL Connector

Download MySQL Connector/J and place the `.jar` file inside `lib/`.

Example:

```text
lib/mysql-connector-j-9.x.x.jar
```

## Compile

Run in PowerShell from project root:

```powershell
javac -d build/classes (Get-ChildItem -Recurse src/main/java/*.java).FullName
```

## Run

```powershell
java -cp "build/classes;lib/*" com.iiui.studentapp.App
```
