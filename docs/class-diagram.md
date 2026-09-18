# Class Diagram

```text
                 <<abstract>>
                    Person
                 /          \
                /            \
           Member          Librarian

Book -------------------- Transaction
 |                            |
 | status                     | bookId
 | issuedTo                   | memberId

Library implements LibraryOperations
   |-- List<Book>
   |-- List<Member>
   |-- List<Librarian>
   |-- List<Transaction>
   |
   +-- FileHandler
   +-- ReportService extends Thread
```
