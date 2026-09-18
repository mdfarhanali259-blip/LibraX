package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Records one book issue/return lifecycle. */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1001;

    private final int id;
    private final String bookId;
    private final String memberId;
    private final String issueDate;
    private final String dueDate;
    private String returnDate;
    private double fine;

    public Transaction(String bookId, String memberId, LocalDate issueDate, LocalDate dueDate) {
        this.id = nextId++;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate.toString();
        this.dueDate = dueDate.toString();
    }

    /** Keeps generated transaction IDs unique after loading persisted records. */
    public static void synchronizeNextId(java.util.List<Transaction> records) {
        int highest = 1000;
        for (Transaction transaction : records) {
            highest = Math.max(highest, transaction.id);
        }
        nextId = highest + 1;
    }

    public int getId() { return id; }
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public String getIssueDate() { return issueDate; }
    public String getDueDate() { return dueDate; }
    public String getReturnDate() { return returnDate; }
    public double getFine() { return fine; }
    public boolean isOpen() { return returnDate == null; }

    public void close(LocalDate date) {
        returnDate = date.toString();
        long overdueDays = Math.max(0,
                ChronoUnit.DAYS.between(LocalDate.parse(dueDate), date));
        fine = overdueDays * 5.0;
    }

    public String getStatus() {
        if (!isOpen()) return "CLOSED";
        return LocalDate.now().isAfter(LocalDate.parse(dueDate)) ? "OVERDUE" : "OPEN";
    }

    @Override
    public String toString() {
        return String.format("TX-%d | Book=%s | Member=%s | Issue=%s | Due=%s | Return=%s | Fine=INR %.2f",
                id, bookId, memberId, issueDate, dueDate,
                returnDate == null ? "-" : returnDate, fine);
    }
}
