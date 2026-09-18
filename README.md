🚀 LibraX — Smart Library Management System
📌 Overview

LibraX — Smart Library Management System is a standalone Core Java desktop application designed to automate and organize common library operations such as book management, member management, book issue and return, searching, due-date tracking, overdue monitoring, fine calculation, transaction management, data persistence, and library analytics.

The system provides two user interfaces: a Command-Line Interface (CLI) for menu-driven operation and a Java Swing Dashboard for graphical interaction. Both interfaces use the same underlying library management and service logic, keeping the application's functionality consistent across the two modes.

LibraX is developed as a practical application of Core Java and Object-Oriented Programming concepts. The project incorporates classes and objects, encapsulation, inheritance, abstraction, interfaces, polymorphism, method overloading and overriding, enums, collections, exception handling, custom exceptions, Java I/O, object serialization, date and time handling, multithreading, and Java Swing.

The project uses local serialized .dat files for persistence and does not require JDBC, MySQL, or any external database server.

🎯 Key Features
📚 Book Management

LibraX provides complete management of the library's book inventory.

Add new books
Update existing book information
Remove books
Search books
Search by:
Book ID
Title
Author
Category
Track book availability
Track currently issued books
Maintain book metadata

Each book is represented as a Java object and managed through the library service layer.

👥 Member Management

The member management module maintains information about registered library members.

Register members
Store member details
View member information
Track borrowed books
Maintain membership status
Connect members with circulation transactions

Member records are represented using Java classes and stored through the application's persistence mechanism.

🔄 Book Circulation

The circulation module manages the complete issue and return workflow.

Issue an available book
Associate a book with a member
Generate issue date
Generate due date
Track active loans
Return books
Update book availability
Close completed transactions
Detect overdue loans
Calculate applicable fines

The system validates circulation operations before modifying the stored data.

🔎 Library Search

The search functionality allows users to locate books using different attributes.

Supported search fields include:

Book ID
Book title
Author
Category

Search results are generated from the in-memory collection maintained by the Library service.

📊 Reports & Analytics

LibraX includes a dedicated reporting module for analysing library activity.

The reporting system provides:

Inventory summary
Total books
Total members
Total librarians
Currently issued books
Overdue loans
Collected fines
Outstanding overdue fine estimates
Book availability rate
Books grouped by category
Most-borrowed books
Most-active members
Detailed overdue-loan information

Reports can also be exported to:

data/library-report.txt
🧠 Core Java Concepts Used

LibraX is designed around concepts from the Core Java curriculum.

🔹 Classes and Objects

The system models real-world library entities using Java classes.

Important classes include:

Book
Member
Librarian
Transaction
Library
FileHandler
ReportService

Objects of these classes represent books, people, transactions, and application services.

🔹 Encapsulation

Class fields are controlled through access modifiers and methods.

For example, the model classes keep their data within the object and provide appropriate methods for accessing or modifying that data.

This helps maintain controlled access to application state.

🔹 Constructors

Constructors are used to initialise objects when books, members, librarians, and transactions are created.

They ensure that newly created objects begin with appropriate values.

🔹 this Keyword

The this keyword is used to refer to the current object when constructor parameters and instance variables have the same names.

🔹 super Keyword

The super keyword is used in the inheritance hierarchy when accessing members or constructors of the parent class.

🔹 Inheritance

LibraX demonstrates inheritance through the Person hierarchy.

              Person
              /    \
             /      \
        Member     Librarian

Member and Librarian share common person-related characteristics while providing their own specialised information.

🔹 Abstract Classes

The Person class forms the abstract/generalised layer of the person hierarchy.

It allows common characteristics to be defined at a higher level while specialised classes represent individual roles.

🔹 Interfaces

The project includes the LibraryOperations interface.

It defines the operations expected from the library management service.

LibraryOperations
        |
        v
     Library

This separates the operation contract from its implementation.

🔹 Method Overloading

Overloaded methods are used where appropriate to provide multiple ways of performing related operations with different parameters.

🔹 Method Overriding

Specialised classes can override inherited behaviour where role-specific implementation is required.

🔹 Polymorphism

Polymorphism is demonstrated through references to common parent types and interfaces.

For example:

Person
 ├── Member
 └── Librarian

Objects of specialised classes can be handled through their common abstraction.

🔹 static and final

The project uses static and final where values or members should belong to the class or remain constant.

🔹 Enums

Enums are used to represent fixed categories of application states and types.

Examples include membership and transaction/book status values.

Using enums prevents inconsistent string-based state representation.

📦 Major Functional Modules

LibraX is organised into multiple functional modules.

Module 1 — Book Management

Responsible for:

Add
Update
Remove
Search
Availability
Module 2 — Member Management

Responsible for:

Registration
Member Records
Borrowing Information
Membership Status
Module 3 — Circulation

Responsible for:

Issue
Return
Due Dates
Overdue Tracking
Fine Calculation
Transactions
Module 4 — Search

Responsible for:

Title Search
Author Search
Category Search
Book ID Search
Module 5 — Reports & Analytics

Responsible for:

Inventory
Availability
Categories
Borrowing Statistics
Member Activity
Overdue Loans
Fines
Report Export
🖥️ User Interfaces

LibraX supports two interfaces.

💻 Command-Line Interface

The CLI provides a menu-driven interface for interacting with the library.

Example:

========================================
       LIBRAX LIBRARY MANAGEMENT
========================================

1. Book Management
2. Member Management
3. Issue / Return Book
4. Search Library
5. Reports & Analytics
6. Librarian Management
7. Save Data
8. Exit

The CLI allows users to perform library operations without requiring a graphical environment.

🖼️ Java Swing Dashboard

The project also includes a graphical desktop dashboard developed using Java Swing.

The dashboard provides:

Library statistics
Total books
Members
Issued books
Overdue information
Recent activity
Quick actions
Library overview
Book management access
Member management access
Circulation access
Reports access

The Swing interface communicates with the same underlying service layer used by the application.

📊 Reports & Analytics

The reporting system is implemented through the ReportService class.

It provides analytical information from the library's current collections.

📦 Inventory Summary

Displays information such as:

Total number of books
Total members
Total librarians
Number of issued books
Number of available books
📈 Availability Analysis

The system calculates the proportion of books currently available.

The demonstration dataset contains:

Total Books       : 40
Currently Issued  : 8
Availability      : 80%
📚 Category Analysis

Books can be grouped according to their category, allowing the system to display the distribution of the collection.

🔄 Borrowing Analysis

The reporting system identifies books that have been borrowed frequently.

This provides a basic view of borrowing patterns within the demonstration dataset.

👥 Member Activity

The system can identify members with higher borrowing activity based on transaction history.

⚠️ Overdue Analysis

The report identifies loans whose due dates have passed while the book remains issued.

The demonstration dataset contains:

Overdue Loans : 3
💰 Fine Analysis

The system maintains fine-related information for overdue transactions.

The demonstration dataset contains:

Fines Collected : ₹75
📄 Report Export

Generated analytical information can be exported to:

data/library-report.txt

This provides a persistent text-based report in addition to the information displayed by the application.

🧵 Multithreading

LibraX demonstrates Java multithreading through the reporting module.

ReportService extends Java's Thread class and can perform report generation as a background operation.

Conceptually:

User
  |
  v
Generate Report
  |
  v
ReportService Thread
  |
  v
Process Library Data
  |
  v
Generate Analytics
  |
  v
library-report.txt

This demonstrates the practical use of the Java Thread class within a real application workflow.

⚠️ Exception Handling

Exception handling is used to prevent invalid library operations from terminating the application unexpectedly.

The project includes custom exceptions such as:

BookNotAvailableException

Used when an unavailable/issued book is requested for another issue operation.

MemberNotFoundException

Used when a requested member record cannot be found.

InvalidInputException

Used for invalid application input or invalid operations.

Example workflow:

Attempt to issue unavailable book
              |
              v
BookNotAvailableException
              |
              v
Exception handled
              |
              v
Meaningful message displayed

This provides controlled error handling instead of allowing invalid operations to continue.

💾 Data Persistence

LibraX uses Java File I/O and object serialization for local data persistence.

The project stores application data in:

data/
├── books.dat
├── librarians.dat
├── members.dat
└── transactions.dat

Java object serialization allows application objects to be converted into a persistent representation and restored when the application starts again.

This means the project can maintain data between executions without requiring:

JDBC
MySQL
PostgreSQL
Oracle
Any external database server
📅 Date & Time Management

The project uses Java's java.time API for handling library dates.

Date and time functionality is used for:

Issue dates
Due dates
Return dates
Overdue calculations
Transaction tracking

This provides a structured approach to date-based library operations.

🏗️ System Architecture

LibraX follows a layered package-oriented architecture.

                    USER
                      |
             +--------+--------+
             |                 |
            CLI              Swing
             |                 |
             +--------+--------+
                      |
                      v
             +------------------+
             |  Service Layer   |
             |------------------|
             | Library          |
             | ReportService    |
             | FileHandler      |
             +--------+---------+
                      |
                      v
             +------------------+
             |   Model Layer    |
             |------------------|
             | Person           |
             | Member           |
             | Librarian        |
             | Book             |
             | Transaction      |
             +--------+---------+
                      |
                      v
             +------------------+
             | File Persistence  |
             |------------------|
             | books.dat        |
             | members.dat      |
             | librarians.dat   |
             | transactions.dat |
             +------------------+
Application Layer

Contains the application's entry points and graphical interface.

src/app/
├── LibraryManagementApp.java
└── LibraryGUI.java
Model Layer

Contains the entities used by the library.

src/model/
├── Book.java
├── Librarian.java
├── Member.java
├── Person.java
└── Transaction.java
Service Layer

Contains the main application logic.

src/service/
├── FileHandler.java
├── Library.java
├── LibraryOperations.java
└── ReportService.java
Exception Layer

Contains custom application exceptions.

src/exception/
├── BookNotAvailableException.java
├── InvalidInputException.java
└── MemberNotFoundException.java
🔄 Application Workflow
                         START
                           |
                           v
                  Load Persisted Data
                           |
                           v
                      Main Menu
                           |
       +-------------------+-------------------+
       |                   |                   |
       v                   v                   v
 Book Management    Member Management     Circulation
       |                   |                   |
 Add / Update /      Register / View       Issue / Return
 Remove / Search          |               Due / Overdue
       |                   |                   |
       +-------------------+-------------------+
                           |
                           v
                    Reports & Analytics
                           |
                           v
                     Export Report
                           |
                           v
                      Save Data
                           |
                           v
                          EXIT

The Swing dashboard provides a graphical entry point to the same core library operations.

📁 Project Structure
LibraX_Final/
│
├── data/
│   ├── books.dat
│   ├── librarians.dat
│   ├── members.dat
│   └── transactions.dat
│
├── docs/
│   ├── architecture.md
│   ├── class-diagram.md
│   ├── sequence.md
│   ├── use-case.md
│   └── workflow.md
│
├── REPORT/
│   └── LibraX_Project_Report.pdf
│
├── screenshots/
│   ├── cli-output.png
│   └── swing-dashboard.png
│
├── src/
│   ├── app/
│   │   ├── LibraryGUI.java
│   │   └── LibraryManagementApp.java
│   │
│   ├── exception/
│   │   ├── BookNotAvailableException.java
│   │   ├── InvalidInputException.java
│   │   └── MemberNotFoundException.java
│   │
│   ├── model/
│   │   ├── Book.java
│   │   ├── Librarian.java
│   │   ├── Member.java
│   │   ├── Person.java
│   │   └── Transaction.java
│   │
│   └── service/
│       ├── FileHandler.java
│       ├── Library.java
│       ├── LibraryOperations.java
│       └── ReportService.java
│
├── tests/
│   └── TestCases.md
│
├── .gitignore
├── README.md
├── run.bat
└── statement.md
📋 Demonstration Dataset

LibraX includes a populated demonstration dataset so that the system can be tested without manually entering all records.

Data	Quantity
Books	40
Members	20
Librarians	3
Transactions	25
Currently Issued Books	8
Overdue Loans	3
Fines Collected	₹75
Availability	80%

The dataset contains multiple book categories, members, librarians, transaction records, issue dates, due dates, and different transaction states.

The data is intentionally connected so that books, members, and transactions form a consistent demonstration environment.

🗂️ Data Files

The persistent data files are stored separately from the source code.

data/
│
├── books.dat
├── members.dat
├── librarians.dat
└── transactions.dat

The application loads these records during startup and saves changes when required.

🖼️ Output Screenshots
🔹 Command-Line Interface

The CLI demonstrates the menu-driven operation of LibraX.

Figure: LibraX Command-Line Interface showing library operations and system output.

🔹 Swing Dashboard

The Swing interface provides a graphical representation of the library's current information.

Figure: LibraX Java Swing dashboard displaying library statistics and recent activity.

🧪 Testing

The project includes a dedicated test-case document:

tests/TestCases.md

The validation scenarios cover core application behaviour.

ID	Scenario	Expected Result
TC01	Start application with empty data folder	Demo data is initialized
TC02	Add a unique book	Book is added and persisted
TC03	Add duplicate book ID	Validation error is shown
TC04	Search by title/author/category	Matching books are listed
TC05	Issue an available book	Book becomes issued and due date is created
TC06	Issue an already issued book	BookNotAvailableException is handled
TC07	Return a borrowed book	Book becomes available and transaction closes
TC08	Return an unissued book	Validation error is shown
TC09	Generate report	Analytics are displayed and exported
TC10	Restart application	Previously saved data is loaded

The application was also checked for successful compilation, startup, data loading, book searching, report generation, GUI launching, and dashboard display.

🧩 Design Documentation

Additional design artifacts are provided in the docs/ directory.

Architecture
docs/architecture.md

Describes the application's layered architecture.

Class Diagram
docs/class-diagram.md

Describes relationships between model and service classes.

Use Case
docs/use-case.md

Describes the primary librarian interactions with the system.

Sequence Diagram
docs/sequence.md

Describes the book issue workflow between the major objects.

Workflow
docs/workflow.md

Describes the overall application flow.

📄 Project Report

The complete academic project report is available at:

REPORT/LibraX_Project_Report.pdf

The report covers:

Introduction
Problem Statement
Objectives
Functional Requirements
Non-Functional Requirements
System Architecture
Use Case Diagram
Workflow
Sequence Diagram
Class Diagram
Design Decisions
Implementation
Core Java Concepts
Screenshots and Results
Testing
Challenges
Learning Outcomes
Future Enhancements
Conclusion
References
⚙️ Technologies Used
Technology / Concept	Purpose
Java	Core application development
Java Swing	Graphical desktop interface
ArrayList / List	In-memory data management
Java I/O	File operations
Object Serialization	Data persistence
java.time	Date and due-date handling
Exceptions	Error handling
Custom Exceptions	Domain-specific validation
Interfaces	Operation contracts
Abstract Classes	Object hierarchy
Inheritance	Member/Librarian specialization
Polymorphism	Object-oriented behaviour
Enums	Status and membership types
Thread	Background report generation
🔐 Data & Application Design

The application separates its responsibilities across different packages rather than placing all functionality inside one large class.

The primary responsibilities are:

Model
  ↓
Represents application data

Service
  ↓
Implements business operations

Exception
  ↓
Handles invalid operations

Application
  ↓
Provides CLI and Swing interfaces

This organisation makes the project easier to understand, maintain, test, and extend.

🎯 Project Objective

The primary objective of LibraX is to demonstrate how Core Java programming concepts can be combined to develop a practical software application.

Instead of implementing isolated examples of classes, inheritance, collections, exceptions, file handling, and threads, the project integrates these concepts into a single application with interacting modules.

The system therefore demonstrates the transition from individual Java concepts to a structured software solution.

🎓 Academic Relevance

LibraX is directly aligned with Core Java and Object-Oriented Programming concepts.

The project demonstrates:

Classes & Objects
        ↓
Encapsulation
        ↓
Inheritance
        ↓
Abstraction
        ↓
Interfaces
        ↓
Polymorphism
        ↓
Collections
        ↓
Exception Handling
        ↓
File I/O & Serialization
        ↓
Multithreading
        ↓
Java Swing

The implementation provides practical context for these concepts through library operations rather than treating them as independent code examples.

🏆 Project Highlights
✓ Standalone Core Java application
✓ 14 Java source files
✓ Modular package architecture
✓ Command-Line Interface
✓ Java Swing Dashboard
✓ 40-book demonstration dataset
✓ 20-member dataset
✓ 3 librarians
✓ 25 transaction records
✓ File-based persistence
✓ Object serialization
✓ Java Collections
✓ Inheritance
✓ Abstract classes
✓ Interfaces
✓ Method overloading
✓ Method overriding
✓ Polymorphism
✓ Enums
✓ Custom exceptions
✓ Java Date & Time API
✓ Multithreading
✓ Library analytics
✓ Report generation
✓ Report export
✓ UML/design documentation
✓ Test cases
✓ Academic project report
🚀 Future Enhancements

The current system uses local file-based persistence and is designed as a standalone desktop application. Possible future extensions include:

User authentication
Role-based access control
Book reservation system
Barcode scanning
QR-code based book identification
Email due-date notifications
Advanced filtering and sorting
Database integration
Multi-user network support
Cloud-based data storage
Fine payment integration
Enhanced reporting dashboards
Automated backup and restore

These features can be added without changing the fundamental purpose of the existing library management system.

📌 Project Scope

The current version focuses on the core activities required for a standalone library management application:

Book Management
        +
Member Management
        +
Librarian Management
        +
Book Circulation
        +
Search
        +
Overdue & Fine Tracking
        +
Reports & Analytics
        +
File Persistence
        +
CLI
        +
Swing Dashboard

External database connectivity and online multi-user functionality are outside the scope of the current implementation.

💡 Why LibraX?

Libraries require coordination between physical resources, members, and borrowing records.

LibraX brings these operations together in a single application while demonstrating important Java programming concepts.

The project combines:

Real-world problem solving + Object-Oriented Programming + Core Java + File Handling + Collections + Exception Handling + Multithreading + Desktop GUI

into one integrated software system.

📚 Repository Documentation

Additional project information is available through:

README.md
statement.md
docs/
REPORT/
tests/

These files provide the project description, problem statement, system design, testing information, and academic documentation.

👨‍💻 Author

MD FARHAN ALI

VIT Bhopal University
CSE – AI/ML
Registration Number: 25BAI10792

📜 License

This project was developed as an academic Core Java project for educational and demonstration purposes.
