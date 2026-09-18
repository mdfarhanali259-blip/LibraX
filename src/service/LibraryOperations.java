package service;

import exception.InvalidInputException;
import model.Book;
import model.Librarian;
import model.Member;

public interface LibraryOperations {
    void addBook(Book book) throws InvalidInputException;
    void addMember(Member member) throws InvalidInputException;
    void addLibrarian(Librarian librarian) throws InvalidInputException;
    void issueBook(String bookId, String memberId) throws Exception;
    double returnBook(String bookId, String memberId) throws Exception;
    void save();
}
