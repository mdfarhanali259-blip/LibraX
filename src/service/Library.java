package service;

import exception.BookNotAvailableException;
import exception.InvalidInputException;
import exception.MemberNotFoundException;
import model.Book;
import model.Librarian;
import model.Member;
import model.Transaction;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core business layer of LibraX.
 * Handles books, members, circulation, persistence and analytics.
 */
public class Library implements LibraryOperations {
    private static final String DATA_DIRECTORY = "data/";
    private static final int LOAN_DAYS = 14;
    private static final double DAILY_FINE = 5.0;

    private final List<Book> books;
    private final List<Member> members;
    private final List<Librarian> librarians;
    private final List<Transaction> transactions;

    public Library() {
        books = FileHandler.load(DATA_DIRECTORY + "books.dat", new ArrayList<>());
        members = FileHandler.load(DATA_DIRECTORY + "members.dat", new ArrayList<>());
        librarians = FileHandler.load(DATA_DIRECTORY + "librarians.dat", new ArrayList<>());
        transactions = FileHandler.load(DATA_DIRECTORY + "transactions.dat", new ArrayList<>());
        Transaction.synchronizeNextId(transactions);
    }

    public List<Book> getBooks() { return Collections.unmodifiableList(books); }
    public List<Member> getMembers() { return Collections.unmodifiableList(members); }
    public List<Librarian> getLibrarians() { return Collections.unmodifiableList(librarians); }
    public List<Transaction> getTransactions() { return Collections.unmodifiableList(transactions); }

    public Book findBook(String id) {
        if (id == null) return null;
        return books.stream()
                .filter(book -> book.getId().equalsIgnoreCase(id.trim()))
                .findFirst().orElse(null);
    }

    public Member findMember(String id) {
        if (id == null) return null;
        return members.stream()
                .filter(member -> member.getId().equalsIgnoreCase(id.trim()))
                .findFirst().orElse(null);
    }

    @Override
    public void addBook(Book book) throws InvalidInputException {
        validateText(book.getId(), "Book ID");
        validateText(book.getTitle(), "Book title");
        validateText(book.getAuthor(), "Author");
        validateText(book.getCategory(), "Category");

        if (findBook(book.getId()) != null)
            throw new InvalidInputException("A book with this ID already exists.");

        books.add(book);
        save();
    }

    public void updateBook(String id, String title, String author, String category)
            throws InvalidInputException {
        Book book = requireBook(id);
        validateText(title, "Title");
        validateText(author, "Author");
        validateText(category, "Category");
        book.setTitle(title.trim());
        book.setAuthor(author.trim());
        book.setCategory(category.trim());
        save();
    }

    @Override
    public void addMember(Member member) throws InvalidInputException {
        validateText(member.getId(), "Member ID");
        validateText(member.getName(), "Member name");
        validateEmail(member.getEmail());

        if (findMember(member.getId()) != null)
            throw new InvalidInputException("A member with this ID already exists.");

        members.add(member);
        save();
    }

    @Override
    public void addLibrarian(Librarian librarian) throws InvalidInputException {
        validateText(librarian.getId(), "Librarian ID");
        validateText(librarian.getName(), "Librarian name");

        boolean exists = librarians.stream()
                .anyMatch(x -> x.getId().equalsIgnoreCase(librarian.getId()));
        if (exists) throw new InvalidInputException("Librarian ID already exists.");

        librarians.add(librarian);
        save();
    }

    @Override
    public void issueBook(String bookId, String memberId) throws Exception {
        Book book = requireBook(bookId);
        Member member = findMember(memberId);

        if (member == null)
            throw new MemberNotFoundException("Member '" + memberId + "' was not found.");

        if (book.getStatus() == Book.Status.ISSUED)
            throw new BookNotAvailableException("Book is already issued to another member.");

        if (!member.canBorrow())
            throw new InvalidInputException(
                    "Borrowing limit reached for " + member.getMembershipType() + " membership.");

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(LOAN_DAYS);

        book.issueTo(member.getId());
        member.addBook(book.getId());
        transactions.add(new Transaction(book.getId(), member.getId(), issueDate, dueDate));
        save();
    }

    @Override
    public double returnBook(String bookId, String memberId) throws Exception {
        Book book = requireBook(bookId);
        Member member = findMember(memberId);

        if (member == null)
            throw new MemberNotFoundException("Member '" + memberId + "' was not found.");

        if (book.getStatus() == Book.Status.AVAILABLE)
            throw new InvalidInputException("Book is not currently issued.");

        if (!member.hasBook(bookId))
            throw new InvalidInputException("This member does not have the selected book.");

        Transaction transaction = transactions.stream()
                .filter(Transaction::isOpen)
                .filter(t -> t.getBookId().equalsIgnoreCase(bookId))
                .filter(t -> t.getMemberId().equalsIgnoreCase(memberId))
                .findFirst().orElse(null);

        book.returnBook();
        member.removeBook(bookId);

        double fine = 0;
        if (transaction != null) {
            transaction.close(LocalDate.now());
            fine = transaction.getFine();
        }

        save();
        return fine;
    }

    public boolean removeBook(String id) {
        Book book = findBook(id);
        if (book == null || book.getStatus() == Book.Status.ISSUED) return false;
        books.remove(book);
        save();
        return true;
    }

    public List<Book> searchBooks(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        return books.stream()
                .filter(book -> book.getId().toLowerCase().contains(q)
                        || book.getTitle().toLowerCase().contains(q)
                        || book.getAuthor().toLowerCase().contains(q)
                        || book.getCategory().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Transaction> getOverdueTransactions() {
        return transactions.stream()
                .filter(Transaction::isOpen)
                .filter(t -> LocalDate.now().isAfter(LocalDate.parse(t.getDueDate())))
                .collect(Collectors.toList());
    }

    public long availableBookCount() {
        return books.stream().filter(b -> b.getStatus() == Book.Status.AVAILABLE).count();
    }

    public long issuedBookCount() {
        return books.stream().filter(b -> b.getStatus() == Book.Status.ISSUED).count();
    }

    public double totalCollectedFines() {
        return transactions.stream().mapToDouble(Transaction::getFine).sum();
    }

    /** Returns how many times each book has appeared in circulation history. */
    public Map<String, Long> mostBorrowedBooks() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getBookId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    /** Returns how many transactions are associated with each member. */
    public Map<String, Long> mostActiveMembers() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getMemberId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    /** Estimates fines currently outstanding on open overdue loans. */
    public double outstandingOverdueFines() {
        LocalDate today = LocalDate.now();
        return getOverdueTransactions().stream()
                .mapToDouble(t -> Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(
                        LocalDate.parse(t.getDueDate()), today)) * DAILY_FINE)
                .sum();
    }

    public String buildReport() {
        Map<String, Long> categories = books.stream()
                .collect(Collectors.groupingBy(Book::getCategory, TreeMap::new, Collectors.counting()));
        List<Transaction> overdue = getOverdueTransactions();
        double availabilityRate = books.isEmpty() ? 0.0 : (availableBookCount() * 100.0 / books.size());

        StringBuilder report = new StringBuilder();
        report.append("\n============================================================\n");
        report.append("                 LIBRAX ANALYTICS REPORT\n");
        report.append("============================================================\n");
        report.append(String.format("%-25s : %d%n", "Total books", books.size()));
        report.append(String.format("%-25s : %d%n", "Available books", availableBookCount()));
        report.append(String.format("%-25s : %d%n", "Issued books", issuedBookCount()));
        report.append(String.format("%-25s : %.1f%%%n", "Availability rate", availabilityRate));
        report.append(String.format("%-25s : %d%n", "Registered members", members.size()));
        report.append(String.format("%-25s : %d%n", "Librarians", librarians.size()));
        report.append(String.format("%-25s : %d%n", "Total transactions", transactions.size()));
        report.append(String.format("%-25s : %d%n", "Overdue loans", overdue.size()));
        report.append(String.format("%-25s : INR %.2f%n", "Collected fines", totalCollectedFines()));
        report.append(String.format("%-25s : INR %.2f%n", "Outstanding overdue fines", outstandingOverdueFines()));

        report.append("\nBOOKS BY CATEGORY\n");
        report.append("-------------------\n");
        categories.forEach((category, count) ->
                report.append(String.format("  %-28s %d%n", category, count)));

        report.append("\nMOST BORROWED BOOKS\n");
        report.append("--------------------\n");
        mostBorrowedBooks().entrySet().stream().limit(5).forEach(entry -> {
            Book book = findBook(entry.getKey());
            String title = book == null ? entry.getKey() : book.getTitle();
            report.append(String.format("  %-7s %-35s %d borrow(s)%n",
                    entry.getKey(), title, entry.getValue()));
        });

        report.append("\nMOST ACTIVE MEMBERS\n");
        report.append("--------------------\n");
        mostActiveMembers().entrySet().stream().limit(5).forEach(entry -> {
            Member member = findMember(entry.getKey());
            String name = member == null ? entry.getKey() : member.getName();
            report.append(String.format("  %-7s %-25s %d transaction(s)%n",
                    entry.getKey(), name, entry.getValue()));
        });

        report.append("\nOVERDUE LOANS\n");
        report.append("--------------\n");
        if (overdue.isEmpty()) {
            report.append("  No overdue loans.\n");
        } else {
            overdue.forEach(t -> {
                Book book = findBook(t.getBookId());
                Member member = findMember(t.getMemberId());
                String title = book == null ? t.getBookId() : book.getTitle();
                String name = member == null ? t.getMemberId() : member.getName();
                long days = Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(
                        LocalDate.parse(t.getDueDate()), LocalDate.now()));
                double estimatedFine = days * DAILY_FINE;
                report.append(String.format(
                        "  TX-%d | %-7s | %-22s | %-16s | %d day(s) | INR %.2f%n",
                        t.getId(), t.getBookId(), title, name, days, estimatedFine));
            });
        }

        report.append("\n============================================================\n");
        return report.toString();
    }

    /** Adds a controlled demonstration transaction for the bundled sample dataset. */
    public void addDemoTransaction(String bookId, String memberId, LocalDate issueDate, LocalDate dueDate,
                                   LocalDate returnDate) throws Exception {
        Book book = requireBook(bookId);
        Member member = findMember(memberId);
        if (member == null) throw new MemberNotFoundException("Member '" + memberId + "' was not found.");
        if (book.getStatus() == Book.Status.ISSUED)
            throw new BookNotAvailableException("Book is already issued.");
        Transaction transaction = new Transaction(book.getId(), member.getId(), issueDate, dueDate);
        if (returnDate != null) {
            transaction.close(returnDate);
        } else {
            book.issueTo(member.getId());
            member.addBook(book.getId());
        }
        transactions.add(transaction);
    }

    public void save() {
        try {
            FileHandler.save(DATA_DIRECTORY + "books.dat", new ArrayList<>(books));
            FileHandler.save(DATA_DIRECTORY + "members.dat", new ArrayList<>(members));
            FileHandler.save(DATA_DIRECTORY + "librarians.dat", new ArrayList<>(librarians));
            FileHandler.save(DATA_DIRECTORY + "transactions.dat", new ArrayList<>(transactions));
        } catch (Exception ex) {
            System.err.println("Warning: unable to save library data: " + ex.getMessage());
        }
    }

    private Book requireBook(String id) throws InvalidInputException {
        Book book = findBook(id);
        if (book == null) throw new InvalidInputException("Book '" + id + "' was not found.");
        return book;
    }

    private static void validateText(String value, String field) throws InvalidInputException {
        if (value == null || value.trim().isEmpty())
            throw new InvalidInputException(field + " cannot be empty.");
    }

    private static void validateEmail(String email) throws InvalidInputException {
        validateText(email, "Email");
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new InvalidInputException("Please enter a valid email address.");
    }
}
