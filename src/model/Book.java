package model;

import java.io.Serializable;

/** Represents a library book and its current circulation state. */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status { AVAILABLE, ISSUED }

    private final String id;
    private String title;
    private String author;
    private String category;
    private Status status;
    private String issuedTo;

    public Book(String id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = Status.AVAILABLE;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public Status getStatus() { return status; }
    public String getIssuedTo() { return issuedTo; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }

    public void issueTo(String memberId) {
        if (status == Status.ISSUED) {
            throw new IllegalStateException("Book is already issued.");
        }
        status = Status.ISSUED;
        issuedTo = memberId;
    }

    public void returnBook() {
        if (status == Status.AVAILABLE) {
            throw new IllegalStateException("Book is not currently issued.");
        }
        status = Status.AVAILABLE;
        issuedTo = null;
    }

    @Override
    public String toString() {
        return String.format("%-7s | %-28s | %-20s | %-18s | %-10s",
                id, title, author, category, status);
    }
}
