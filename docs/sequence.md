# Issue Book — Sequence Diagram

```text
Librarian -> Library : issueBook(bookId, memberId)
Library -> Library : findBook()
Library -> Library : findMember()
Library -> Book : issueTo(memberId)
Library -> Member : addBook(bookId)
Library -> Transaction : create(issueDate, dueDate)
Library -> FileHandler : save()
FileHandler --> Library : persistence complete
Library --> Librarian : issue successful
```
