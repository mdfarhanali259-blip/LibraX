package app;

import exception.InvalidInputException;
import model.Book;
import model.Member;
import model.Librarian;
import model.Member.MembershipType;
import model.Transaction;
import service.Library;
import service.ReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line entry point for LibraX.
 * The application is fully executable without a GUI.
 */
public class LibraryManagementApp {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Library LIBRARY = new Library();

    public static void main(String[] args) {
        seedDemoData();
        printWelcome();
        mainMenu();
    }

    private static void printWelcome() {
        System.out.println("""

                ============================================================
                              LIBRAX
                     SMART LIBRARY MANAGEMENT SYSTEM
                ============================================================
                 Core Java | OOP | Collections | File I/O | Exceptions
                 Multithreading | Swing
                ============================================================
                """);
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("""

                    -------------------- MAIN MENU --------------------
                    1. Book Management
                    2. Member Management
                    3. Issue / Return
                    4. Search Library
                    5. Reports & Analytics
                    6. Open Swing Dashboard
                    7. Save Data
                    0. Exit
                    ----------------------------------------------------
                    """);

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> bookMenu();
                    case 2 -> memberMenu();
                    case 3 -> circulationMenu();
                    case 4 -> searchBooks();
                    case 5 -> generateReport();
                    case 6 -> LibraryGUI.launch(LIBRARY);
                    case 7 -> saveData();
                    case 0 -> {
                        LIBRARY.save();
                        System.out.println("Data saved successfully. Thank you for using LibraX!");
                        return;
                    }
                    default -> System.out.println("Please choose a valid menu option.");
                }
            } catch (Exception ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            }
        }
    }

    private static void bookMenu() {
        while (true) {
            System.out.println("""

                    ================= BOOK MANAGEMENT =================
                    1. Add book
                    2. Update book
                    3. Remove book
                    4. List all books
                    5. Search books
                    0. Back
                    ====================================================
                    """);

            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> updateBook();
                    case 3 -> removeBook();
                    case 4 -> printBooks(LIBRARY.getBooks());
                    case 5 -> searchBooks();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            }
        }
    }

    private static void addBook() throws Exception {
        String id = read("Book ID: ");
        String title = read("Title: ");
        String author = read("Author: ");
        String category = read("Category: ");

        LIBRARY.addBook(new Book(id, title, author, category));
        System.out.println("✓ Book added successfully.");
    }

    private static void updateBook() throws Exception {
        String id = read("Book ID to update: ");
        String title = read("New title: ");
        String author = read("New author: ");
        String category = read("New category: ");

        LIBRARY.updateBook(id, title, author, category);
        System.out.println("✓ Book details updated.");
    }

    private static void removeBook() {
        String id = read("Book ID to remove: ");
        if (LIBRARY.removeBook(id)) {
            System.out.println("✓ Book removed successfully.");
        } else {
            System.out.println("Book was not found or is currently issued.");
        }
    }

    private static void memberMenu() {
        while (true) {
            System.out.println("""

                    ================= MEMBER MANAGEMENT ==============
                    1. Register member
                    2. List members
                    3. View member
                    0. Back
                    ====================================================
                    """);

            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1 -> registerMember();
                    case 2 -> listMembers();
                    case 3 -> viewMember();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            }
        }
    }

    private static void registerMember() throws Exception {
        String id = read("Member ID: ");
        String name = read("Name: ");
        String email = read("Email: ");
        String phone = read("Phone: ");

        System.out.println("Membership types: 1=Student (5 books), 2=Regular (3), 3=Premium (10)");
        int typeChoice = readInt("Membership: ");

        MembershipType type = switch (typeChoice) {
            case 1 -> MembershipType.STUDENT;
            case 2 -> MembershipType.REGULAR;
            case 3 -> MembershipType.PREMIUM;
            default -> throw new InvalidInputException("Invalid membership type.");
        };

        LIBRARY.addMember(new Member(id, name, email, phone,
                LocalDate.now().toString(), type));
        System.out.println("✓ Member registered successfully.");
    }

    private static void listMembers() {
        System.out.println("\nID      | NAME                   | TYPE      | BORROWED");
        System.out.println("-".repeat(70));
        LIBRARY.getMembers().forEach(m -> System.out.println(m.getSummary()));
        System.out.println("Total members: " + LIBRARY.getMembers().size());
    }

    private static void viewMember() {
        String id = read("Member ID: ");
        Member member = LIBRARY.findMember(id);

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        System.out.println("\n" + member);
        System.out.println("Membership: " + member.getMembershipType());
        System.out.println("Joined: " + member.getMembershipDate());
        System.out.println("Borrowed books: " +
                (member.getIssuedBookIds().isEmpty() ? "None" : member.getIssuedBookIds()));
    }

    private static void circulationMenu() {
        while (true) {
            System.out.println("""

                    ================= CIRCULATION =====================
                    1. Issue book
                    2. Return book
                    3. View active loans
                    4. View overdue loans
                    0. Back
                    ====================================================
                    """);

            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1 -> issueBook();
                    case 2 -> returnBook();
                    case 3 -> printTransactions(LIBRARY.getTransactions().stream()
                            .filter(Transaction::isOpen).toList());
                    case 4 -> printTransactions(LIBRARY.getOverdueTransactions());
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            }
        }
    }

    private static void issueBook() throws Exception {
        String bookId = read("Book ID: ");
        String memberId = read("Member ID: ");

        LIBRARY.issueBook(bookId, memberId);
        System.out.println("✓ Book issued successfully.");
        System.out.println("Due date: " + LocalDate.now().plusDays(14));
    }

    private static void returnBook() throws Exception {
        String bookId = read("Book ID: ");
        String memberId = read("Member ID: ");

        double fine = LIBRARY.returnBook(bookId, memberId);
        System.out.printf("✓ Book returned. Fine due: INR %.2f%n", fine);
    }

    private static void searchBooks() {
        String query = read("Search by ID, title, author or category: ");
        List<Book> results = LIBRARY.searchBooks(query);

        if (results.isEmpty()) {
            System.out.println("No matching books found.");
        } else {
            printBooks(results);
        }
    }

    private static void generateReport() {
        System.out.println("Generating analytics in a background thread...");
        Thread reportThread = new ReportService(LIBRARY, true);
        reportThread.start();
    }

    private static void saveData() {
        LIBRARY.save();
        System.out.println("✓ All library data saved.");
    }

    private static void printBooks(List<Book> books) {
        System.out.println("\nID      | TITLE                        | AUTHOR               | CATEGORY           | STATUS");
        System.out.println("-".repeat(100));
        books.forEach(System.out::println);
        System.out.println("Records shown: " + books.size());
    }

    private static void printTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println();
        transactions.forEach(System.out::println);
        System.out.println("Records: " + transactions.size());
    }

    private static String read(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(read(prompt));
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static void seedDemoData() {
        // The bundled dataset is intentionally rich enough to demonstrate the dashboard and reports.
        if (LIBRARY.getBooks().size() >= 40) return;

        try {
            String[][] books = {
                {"B101","Java: The Complete Reference","Herbert Schildt","Programming"},
                {"B102","Clean Code","Robert C. Martin","Software Engineering"},
                {"B103","Data Structures","Mark Allen Weiss","Computer Science"},
                {"B104","Effective Java","Joshua Bloch","Programming"},
                {"B105","Head First Java","Kathy Sierra","Programming"},
                {"B106","Introduction to Algorithms","Thomas H. Cormen","Algorithms"},
                {"B107","Design Patterns","Erich Gamma","Software Engineering"},
                {"B108","The Pragmatic Programmer","David Thomas","Software Engineering"},
                {"B109","Computer Networks","Andrew S. Tanenbaum","Computer Science"},
                {"B110","Operating System Concepts","Abraham Silberschatz","Computer Science"},
                {"B111","Artificial Intelligence: A Modern Approach","Stuart Russell","Artificial Intelligence"},
                {"B112","Hands-On Machine Learning","Aurélien Géron","Artificial Intelligence"},
                {"B113","Python for Data Analysis","Wes McKinney","Data Science"},
                {"B114","Pattern Recognition and Machine Learning","Christopher Bishop","Data Science"},
                {"B115","Database System Concepts","Abraham Silberschatz","Database"},
                {"B116","Computer Organization","Carl Hamacher","Computer Architecture"},
                {"B117","Discrete Mathematics and Its Applications","Kenneth Rosen","Mathematics"},
                {"B118","Engineering Mathematics","B. S. Grewal","Mathematics"},
                {"B119","The Alchemist","Paulo Coelho","Literature"},
                {"B120","1984","George Orwell","Literature"},
                {"B121","To Kill a Mockingbird","Harper Lee","Literature"},
                {"B122","Atomic Habits","James Clear","Self Development"},
                {"B123","Deep Work","Cal Newport","Self Development"},
                {"B124","Zero to One","Peter Thiel","Business"},
                {"B125","The Lean Startup","Eric Ries","Business"},
                {"B126","Clean Architecture","Robert C. Martin","Software Engineering"},
                {"B127","Refactoring","Martin Fowler","Software Engineering"},
                {"B128","Cracking the Coding Interview","Gayle Laakmann McDowell","Algorithms"},
                {"B129","Competitive Programming","Steven Halim","Algorithms"},
                {"B130","Artificial Neural Networks","Charu C. Aggarwal","Artificial Intelligence"},
                {"B131","Deep Learning","Ian Goodfellow","Artificial Intelligence"},
                {"B132","Practical Statistics for Data Scientists","Peter Bruce","Data Science"},
                {"B133","Data Mining Concepts","Jiawei Han","Data Science"},
                {"B134","Modern Operating Systems","Andrew S. Tanenbaum","Computer Science"},
                {"B135","Computer Graphics with OpenGL","Donald D. Hearn","Computer Graphics"},
                {"B136","Digital Logic Design","M. Morris Mano","Digital Electronics"},
                {"B137","Signals and Systems","Alan V. Oppenheim","Electronics"},
                {"B138","Calculus","James Stewart","Mathematics"},
                {"B139","The Design of Everyday Things","Don Norman","Design"},
                {"B140","The Psychology of Money","Morgan Housel","Finance"}
            };
            for (String[] b : books) {
                if (LIBRARY.findBook(b[0]) == null)
                    LIBRARY.addBook(new Book(b[0], b[1], b[2], b[3]));
            }

            String[][] members = {
                {"M101","Aarav Sharma","aarav@example.com","9000000001","STUDENT"},
                {"M102","Diya Verma","diya@example.com","9000000002","PREMIUM"},
                {"M103","Rohan Mehta","rohan@example.com","9000000003","REGULAR"},
                {"M104","Ananya Singh","ananya@example.com","9000000004","STUDENT"},
                {"M105","Kabir Khan","kabir@example.com","9000000005","PREMIUM"},
                {"M106","Meera Nair","meera@example.com","9000000006","STUDENT"},
                {"M107","Arjun Patel","arjun@example.com","9000000007","REGULAR"},
                {"M108","Ishita Rao","ishita@example.com","9000000008","PREMIUM"},
                {"M109","Vivaan Gupta","vivaan@example.com","9000000009","STUDENT"},
                {"M110","Sara Khan","sara@example.com","9000000010","REGULAR"},
                {"M111","Aditya Joshi","aditya@example.com","9000000011","STUDENT"},
                {"M112","Nisha Kapoor","nisha@example.com","9000000012","PREMIUM"},
                {"M113","Karan Malhotra","karan@example.com","9000000013","REGULAR"},
                {"M114","Priya Shah","priya@example.com","9000000014","STUDENT"},
                {"M115","Dev Agarwal","dev@example.com","9000000015","PREMIUM"},
                {"M116","Aditi Mishra","aditi@example.com","9000000016","STUDENT"},
                {"M117","Yash Verma","yash@example.com","9000000017","REGULAR"},
                {"M118","Neha Bansal","neha@example.com","9000000018","PREMIUM"},
                {"M119","Rahul Sethi","rahul@example.com","9000000019","STUDENT"},
                {"M120","Tanya Roy","tanya@example.com","9000000020","REGULAR"}
            };
            for (String[] m : members) {
                if (LIBRARY.findMember(m[0]) == null) {
                    MembershipType type = MembershipType.valueOf(m[4]);
                    LIBRARY.addMember(new Member(m[0], m[1], m[2], m[3],
                            LocalDate.now().minusDays(30 + Integer.parseInt(m[0].substring(2))).toString(), type));
                }
            }

            String[][] librarians = {
                {"L001","Neha Kapoor","neha.librarian@example.com","9111111111","EMP001","Circulation"},
                {"L002","Vikram Joshi","vikram.librarian@example.com","9222222222","EMP002","Acquisitions"},
                {"L003","Pooja Nair","pooja.librarian@example.com","9333333333","EMP003","Digital Services"}
            };
            for (String[] l : librarians) {
                if (LIBRARY.getLibrarians().stream().noneMatch(x -> x.getId().equalsIgnoreCase(l[0])))
                    LIBRARY.addLibrarian(new Librarian(l[0], l[1], l[2], l[3], l[4], l[5]));
            }

            // Historical closed transactions for analytics.
            String[] historyBooks = {"B101","B103","B101","B104","B105","B103","B106","B101","B107","B104","B109","B111","B105","B112","B114","B101","B104"};
            String[] historyMembers = {"M101","M102","M101","M103","M104","M102","M105","M101","M106","M103","M107","M108","M104","M109","M110","M101","M103"};
            for (int i = 0; i < historyBooks.length; i++) {
                LocalDate issue = LocalDate.now().minusDays(45 + i * 2L);
                LocalDate due = issue.plusDays(14);
                LocalDate returned = due.plusDays(i % 4 == 0 ? 3 : 0);
                LIBRARY.addDemoTransaction(historyBooks[i], historyMembers[i], issue, due, returned);
            }

            // Current loans: three overdue and five active within the due date.
            String[][] active = {
                {"B116","M118","OVERDUE"}, {"B120","M119","OVERDUE"}, {"B123","M120","OVERDUE"},
                {"B124","M101","ACTIVE"}, {"B127","M102","ACTIVE"}, {"B131","M103","ACTIVE"},
                {"B133","M104","ACTIVE"}, {"B136","M105","ACTIVE"}
            };
            for (int i = 0; i < active.length; i++) {
                LocalDate issue = active[i][2].equals("OVERDUE")
                        ? LocalDate.now().minusDays(22 + i)
                        : LocalDate.now().minusDays(4 + i);
                LocalDate due = active[i][2].equals("OVERDUE")
                        ? LocalDate.now().minusDays(8 + i)
                        : LocalDate.now().plusDays(10 - i);
                LIBRARY.addDemoTransaction(active[i][0], active[i][1], issue, due, null);
            }

            LIBRARY.save();
        } catch (Exception ex) {
            System.err.println("Demo data setup warning: " + ex.getMessage());
        }
    }
}
