# LibraX Validation Test Cases

| ID | Scenario | Expected Result |
|---|---|---|
| TC01 | Start application with empty data folder | Demo data is initialized |
| TC02 | Add a unique book | Book is added and persisted |
| TC03 | Add duplicate book ID | Validation error is shown |
| TC04 | Search by title/author/category | Matching books are listed |
| TC05 | Issue an available book | Book becomes ISSUED and due date is created |
| TC06 | Issue an already issued book | `BookNotAvailableException` is handled |
| TC07 | Return a borrowed book | Book becomes AVAILABLE and transaction closes |
| TC08 | Return an unissued book | Validation error is shown |
| TC09 | Generate report | Analytics are displayed and exported |
| TC10 | Restart application | Previously saved data is loaded |


## Analytics Validation

| Test | Expected Result |
|---|---|
| Generate analytics report | Shows inventory, members, transactions, availability rate and fines |
| View most-borrowed books | Displays ranked circulation counts from transaction history |
| View most-active members | Displays members ranked by transaction count |
| View overdue loans | Displays overdue transaction, member, days overdue and estimated fine |
| Export report | Creates `data/library-report.txt` successfully |
| Restart application | Previously saved records remain available |
