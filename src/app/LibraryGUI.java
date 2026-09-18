package app;

import model.Book;
import model.Member;
import model.Transaction;
import model.Member.MembershipType;
import service.Library;
import service.ReportService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Professional desktop dashboard for LibraX.
 * Uses only Java Swing from the standard JDK; the CLI remains fully available.
 */
public final class LibraryGUI {
    private static final Color NAV = new Color(31, 42, 55);
    private static final Color NAV_HOVER = new Color(44, 58, 74);
    private static final Color ACCENT = new Color(38, 103, 145);
    private static final Color ACCENT_DARK = new Color(29, 82, 116);
    private static final Color PAGE = new Color(244, 247, 250);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(35, 45, 58);
    private static final Color MUTED = new Color(105, 116, 128);
    private static final Color BORDER = new Color(220, 226, 233);
    private static final Font TITLE = new Font("SansSerif", Font.BOLD, 28);
    private static final Font PAGE_TITLE = new Font("SansSerif", Font.BOLD, 21);
    private static final Font SECTION = new Font("SansSerif", Font.BOLD, 15);
    private static final Font BODY = new Font("SansSerif", Font.PLAIN, 13);

    private LibraryGUI() { }

    public static void launch(Library library) {
        SwingUtilities.invokeLater(() -> create(library));
    }

    private static void create(Library library) {
        JFrame frame = new JFrame("LibraX | Smart Library Management System");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1180, 720);
        frame.setMinimumSize(new Dimension(1050, 650));
        frame.setMinimumSize(new Dimension(1100, 700));
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE);
        root.add(header(), BorderLayout.NORTH);

        JPanel pages = new JPanel(new CardLayout());
        pages.setBackground(PAGE);
        pages.setBorder(new EmptyBorder(20, 22, 20, 22));

        JPanel dashboard = dashboardPanel(library, pages);
        JPanel books = booksPanel(library);
        JPanel members = membersPanel(library);
        JPanel circulation = circulationPanel(library);
        JPanel reports = reportPanel(library);

        pages.add(dashboard, "Dashboard");
        pages.add(books, "Books");
        pages.add(members, "Members");
        pages.add(circulation, "Circulation");
        pages.add(reports, "Reports");

        root.add(navigation(pages), BorderLayout.WEST);
        root.add(pages, BorderLayout.CENTER);
        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private static JPanel header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(15, 24, 14, 24)));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("LIBRAX");
        title.setFont(TITLE);
        title.setForeground(TEXT);
        JLabel subtitle = new JLabel("Smart Library Management System  •  Core Java");
        subtitle.setFont(BODY);
        subtitle.setForeground(MUTED);
        left.add(title);
        left.add(Box.createVerticalStrut(2));
        left.add(subtitle);
        header.add(left, BorderLayout.WEST);

        JLabel tech = new JLabel("JAVA SWING  |  FILE-BASED STORAGE");
        tech.setHorizontalAlignment(SwingConstants.RIGHT);
        tech.setFont(new Font("SansSerif", Font.BOLD, 11));
        tech.setForeground(ACCENT);
        header.add(tech, BorderLayout.EAST);
        return header;
    }

    private static JPanel navigation(JPanel pages) {
        String[] labels = {"Dashboard", "Books", "Members", "Circulation", "Reports"};
        JPanel nav = new JPanel();
        nav.setPreferredSize(new Dimension(205, 0));
        nav.setBackground(NAV);
        nav.setBorder(new EmptyBorder(22, 12, 18, 12));
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("LIBRAX");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 20));
        brand.setBorder(new EmptyBorder(0, 10, 5, 0));
        nav.add(brand);

        JLabel descriptor = new JLabel("Library Operations");
        descriptor.setForeground(new Color(175, 186, 198));
        descriptor.setFont(new Font("SansSerif", Font.PLAIN, 11));
        descriptor.setBorder(new EmptyBorder(0, 10, 22, 0));
        nav.add(descriptor);

        CardLayout cards = (CardLayout) pages.getLayout();
        for (String label : labels) {
            JButton button = navButton(label);
            button.addActionListener(e -> cards.show(pages, label));
            nav.add(button);
            nav.add(Box.createVerticalStrut(6));
        }

        nav.add(Box.createVerticalGlue());
        JLabel footer = new JLabel("Core Java  •  v1.0");
        footer.setForeground(new Color(160, 171, 183));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setBorder(new EmptyBorder(0, 10, 3, 0));
        nav.add(footer);
        return nav;
    }

    private static JButton navButton(String text) {
        JButton b = new JButton(text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(NAV);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(11, 14, 11, 10));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setOpaque(true);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(NAV_HOVER); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(NAV); }
        });
        return b;
    }

    private static JPanel dashboardPanel(Library library, JPanel pages) {
        JPanel panel = pagePanel();
        panel.add(pageHeading("Dashboard", "Real-time overview of library operations"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);

        JPanel cards = new JPanel(new GridLayout(2, 2, 14, 14));
        cards.setOpaque(false);
        cards.add(statCard("TOTAL BOOKS", String.valueOf(library.getBooks().size()), "Library inventory"));
        cards.add(statCard("AVAILABLE", String.valueOf(library.availableBookCount()), "Ready to issue"));
        cards.add(statCard("ISSUED", String.valueOf(library.issuedBookCount()), "Active circulation"));
        cards.add(statCard("MEMBERS", String.valueOf(library.getMembers().size()), "Registered users"));

        JPanel top = new JPanel(new BorderLayout(14, 14));
        top.setOpaque(false);
        top.add(cards, BorderLayout.CENTER);
        JPanel overview = overviewCard(library);
        overview.setPreferredSize(new Dimension(220, 0));
        top.add(overview, BorderLayout.EAST);
        body.add(top, BorderLayout.NORTH);

        JPanel activityCard = titledPanel("Recent Activity", "Latest circulation records");
        activityCard.add(new JScrollPane(activityTable(library)), BorderLayout.CENTER);
        body.add(activityCard, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.setBorder(new EmptyBorder(0, 0, 2, 0));
        actions.add(sectionLabel("Quick Actions"));
        actions.add(actionButton("+ Add Book", () -> showPage(pages, "Books")));
        actions.add(actionButton("+ Register Member", () -> showPage(pages, "Members")));
        actions.add(actionButton("Issue Book", () -> showPage(pages, "Circulation")));
        actions.add(actionButton("Return Book", () -> showPage(pages, "Circulation")));
        body.add(actions, BorderLayout.SOUTH);

        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel overviewCard(Library library) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(220, 0));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(15, 17, 15, 17)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Library Overview");
        title.setFont(SECTION);
        title.setForeground(TEXT);
        card.add(title);
        card.add(Box.createVerticalStrut(12));
        int total = library.getBooks().size();
        double availability = total == 0 ? 0 : (library.availableBookCount() * 100.0 / total);
        addMetric(card, "Availability", String.format("%.0f%%", availability));
        addMetric(card, "Transactions", String.valueOf(library.getTransactions().size()));
        addMetric(card, "Overdue loans", String.valueOf(library.getOverdueTransactions().size()));
        addMetric(card, "Collected fines", String.format("INR %.2f", library.totalCollectedFines()));
        return card;
    }

    private static void addMetric(JPanel parent, String name, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        JLabel n = new JLabel(name);
        n.setFont(BODY);
        n.setForeground(MUTED);
        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 14));
        v.setForeground(TEXT);
        row.add(n, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        parent.add(row);
    }

    private static JPanel statCard(String label, String value, String hint) {
        JPanel card = new JPanel(new BorderLayout(6, 4));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(14, 17, 14, 17)));
        JLabel name = new JLabel(label);
        name.setFont(new Font("SansSerif", Font.BOLD, 11));
        name.setForeground(ACCENT);
        JLabel number = new JLabel(value);
        number.setFont(new Font("SansSerif", Font.BOLD, 31));
        number.setForeground(TEXT);
        JLabel small = new JLabel(hint);
        small.setFont(new Font("SansSerif", Font.PLAIN, 11));
        small.setForeground(MUTED);
        card.add(name, BorderLayout.NORTH);
        card.add(number, BorderLayout.CENTER);
        card.add(small, BorderLayout.SOUTH);
        return card;
    }

    private static JTable activityTable(Library library) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"TRANSACTION ID", "BOOK", "MEMBER", "ISSUE DATE", "DUE DATE", "STATUS"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        library.getTransactions().stream().limit(8).forEach(t -> model.addRow(new Object[]{
                "TX-" + t.getId(), t.getBookId(), t.getMemberId(), t.getIssueDate(), t.getDueDate(), t.getStatus()
        }));
        if (model.getRowCount() == 0) model.addRow(new Object[]{"-", "-", "-", "-", "-", "No activity"});
        JTable table = new JTable(model);
        styleTable(table);
        return table;
    }

    private static JPanel booksPanel(Library library) {
        JPanel panel = pagePanel();
        panel.add(pageHeading("Book Management", "Manage inventory, availability and catalogue search"), BorderLayout.NORTH);

        DefaultTableModel model = nonEditableModel("ID", "TITLE", "AUTHOR", "CATEGORY", "STATUS");
        JTable table = new JTable(model);
        styleTable(table);

        JTextField searchField = textField(250);
        JButton search = primaryButton("Search");
        JButton refresh = secondaryButton("Refresh");
        JButton add = primaryButton("+ Add Book");
        JButton update = secondaryButton("Update");
        JButton remove = secondaryButton("Remove");

        Runnable load = () -> loadBooks(model, library.getBooks());
        search.addActionListener(e -> loadBooks(model, library.searchBooks(searchField.getText())));
        searchField.addActionListener(e -> loadBooks(model, library.searchBooks(searchField.getText())));
        refresh.addActionListener(e -> { searchField.setText(""); load.run(); });
        add.addActionListener(e -> addBookDialog(panel, library, load));
        update.addActionListener(e -> updateSelectedBook(panel, library, table, load));
        remove.addActionListener(e -> removeSelectedBook(panel, library, table, load));

        JPanel toolbar = toolbar();
        toolbar.add(new JLabel("Search")); toolbar.add(searchField); toolbar.add(search); toolbar.add(refresh);
        toolbar.add(Box.createHorizontalStrut(12)); toolbar.add(add); toolbar.add(update); toolbar.add(remove);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setOpaque(false);
        content.add(toolbar, BorderLayout.NORTH);
        JPanel card = titledPanel("Catalogue", "40+ records with sortable columns");
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        content.add(card, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        load.run();
        return panel;
    }

    private static void addBookDialog(Component parent, Library library, Runnable refresh) {
        JTextField id = textField(0), title = textField(0), author = textField(0), category = textField(0);
        JPanel form = formPanel(new String[]{"Book ID", "Title", "Author", "Category"}, new JComponent[]{id, title, author, category});
        if (JOptionPane.showConfirmDialog(parent, form, "Add Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                library.addBook(new Book(id.getText().trim(), title.getText().trim(), author.getText().trim(), category.getText().trim()));
                refresh.run(); message(parent, "Book added successfully.");
            } catch (Exception ex) { error(parent, ex.getMessage()); }
        }
    }

    private static void updateSelectedBook(Component parent, Library library, JTable table, Runnable refresh) {
        int row = table.getSelectedRow();
        if (row < 0) { error(parent, "Select a book from the table first."); return; }
        int modelRow = table.convertRowIndexToModel(row);
        String id = String.valueOf(table.getModel().getValueAt(modelRow, 0));
        Book book = library.findBook(id);
        if (book == null) { error(parent, "Book not found."); return; }
        JTextField title = textField(0); title.setText(book.getTitle());
        JTextField author = textField(0); author.setText(book.getAuthor());
        JTextField category = textField(0); category.setText(book.getCategory());
        JPanel form = formPanel(new String[]{"Title", "Author", "Category"}, new JComponent[]{title, author, category});
        if (JOptionPane.showConfirmDialog(parent, form, "Update Book • " + id, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try { library.updateBook(id, title.getText().trim(), author.getText().trim(), category.getText().trim()); refresh.run(); message(parent, "Book updated successfully."); }
            catch (Exception ex) { error(parent, ex.getMessage()); }
        }
    }

    private static void removeSelectedBook(Component parent, Library library, JTable table, Runnable refresh) {
        int row = table.getSelectedRow();
        if (row < 0) { error(parent, "Select a book from the table first."); return; }
        int modelRow = table.convertRowIndexToModel(row);
        String id = String.valueOf(table.getModel().getValueAt(modelRow, 0));
        int choice = JOptionPane.showConfirmDialog(parent, "Remove book " + id + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (library.removeBook(id)) { refresh.run(); message(parent, "Book removed successfully."); }
            else error(parent, "Book could not be removed. It may be currently issued.");
        }
    }

    private static JPanel membersPanel(Library library) {
        JPanel panel = pagePanel();
        panel.add(pageHeading("Member Management", "Registered users, membership tiers and borrowing limits"), BorderLayout.NORTH);
        DefaultTableModel model = nonEditableModel("ID", "NAME", "EMAIL", "TYPE", "BORROWED", "JOINED");
        JTable table = new JTable(model);
        styleTable(table);
        JTextField search = textField(250);
        JButton find = primaryButton("Search");
        JButton refresh = secondaryButton("Refresh");
        JButton add = primaryButton("+ Register Member");
        Runnable load = () -> loadMembers(model, library.getMembers());
        find.addActionListener(e -> loadMembers(model, library.getMembers().stream().filter(m ->
                contains(m.getId(), search.getText()) || contains(m.getName(), search.getText()) || contains(m.getEmail(), search.getText())).toList()));
        search.addActionListener(e -> find.doClick());
        refresh.addActionListener(e -> { search.setText(""); load.run(); });
        add.addActionListener(e -> addMemberDialog(panel, library, load));
        JPanel toolbar = toolbar();
        toolbar.add(new JLabel("Search")); toolbar.add(search); toolbar.add(find); toolbar.add(refresh); toolbar.add(Box.createHorizontalStrut(12)); toolbar.add(add);
        JPanel card = titledPanel("Registered Members", "Membership and current borrowing status");
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel content = new JPanel(new BorderLayout(10, 10)); content.setOpaque(false);
        content.add(toolbar, BorderLayout.NORTH); content.add(card, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER); load.run();
        return panel;
    }

    private static void addMemberDialog(Component parent, Library library, Runnable refresh) {
        JTextField id = textField(0), name = textField(0), email = textField(0), phone = textField(0);
        JComboBox<MembershipType> type = new JComboBox<>(MembershipType.values());
        JPanel form = formPanel(new String[]{"Member ID", "Name", "Email", "Phone", "Membership"}, new JComponent[]{id, name, email, phone, type});
        if (JOptionPane.showConfirmDialog(parent, form, "Register Member", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                library.addMember(new Member(id.getText().trim(), name.getText().trim(), email.getText().trim(), phone.getText().trim(),
                        LocalDate.now().toString(), (MembershipType) type.getSelectedItem()));
                refresh.run(); message(parent, "Member registered successfully.");
            } catch (Exception ex) { error(parent, ex.getMessage()); }
        }
    }

    private static JPanel circulationPanel(Library library) {
        JPanel panel = pagePanel();
        panel.add(pageHeading("Circulation", "Issue and return books with automatic due dates and fines"), BorderLayout.NORTH);

        JPanel actions = toolbar();
        JButton issue = primaryButton("Issue Book");
        JButton returned = secondaryButton("Return Book");
        JButton refreshButton = secondaryButton("Refresh");
        actions.add(issue); actions.add(returned); actions.add(refreshButton);

        DefaultTableModel model = nonEditableModel("TRANSACTION", "BOOK", "MEMBER", "ISSUE DATE", "DUE DATE", "STATUS");
        JTable table = new JTable(model); styleTable(table);
        Runnable refresh = () -> loadActiveLoans(model, library);
        refreshButton.addActionListener(e -> refresh.run());
        issue.addActionListener(e -> issueDialog(panel, library, refresh));
        returned.addActionListener(e -> returnDialog(panel, library, refresh));

        JPanel content = new JPanel(new BorderLayout(10, 10)); content.setOpaque(false);
        content.add(actions, BorderLayout.NORTH);
        JPanel card = titledPanel("Active Loans", "Current open and overdue circulation records");
        card.add(new JScrollPane(table), BorderLayout.CENTER); content.add(card, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER); refresh.run();
        return panel;
    }

    private static void issueDialog(Component parent, Library library, Runnable refresh) {
        JTextField book = textField(0), member = textField(0);
        JPanel form = formPanel(new String[]{"Book ID", "Member ID"}, new JComponent[]{book, member});
        if (JOptionPane.showConfirmDialog(parent, form, "Issue Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try { library.issueBook(book.getText().trim(), member.getText().trim()); refresh.run(); message(parent, "Book issued successfully."); }
            catch (Exception ex) { error(parent, ex.getMessage()); }
        }
    }

    private static void returnDialog(Component parent, Library library, Runnable refresh) {
        JTextField book = textField(0), member = textField(0);
        JPanel form = formPanel(new String[]{"Book ID", "Member ID"}, new JComponent[]{book, member});
        if (JOptionPane.showConfirmDialog(parent, form, "Return Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try { double fine = library.returnBook(book.getText().trim(), member.getText().trim()); refresh.run(); message(parent, String.format("Book returned successfully. Fine: INR %.2f", fine)); }
            catch (Exception ex) { error(parent, ex.getMessage()); }
        }
    }

    private static JPanel reportPanel(Library library) {
        JPanel panel = pagePanel();
        panel.add(pageHeading("Reports & Analytics", "Operational summary, categories, activity and overdue analysis"), BorderLayout.NORTH);
        JTextArea area = createOutputArea();
        area.setText(library.buildReport().replace("₹", "INR "));
        JPanel toolbar = toolbar();
        JButton generate = primaryButton("Generate & Export Report");
        JButton refresh = secondaryButton("Refresh");
        toolbar.add(generate); toolbar.add(refresh);
        generate.addActionListener(e -> {
            area.setText(library.buildReport().replace("₹", "INR ") +
                    "\nReport generation is running on a dedicated Java thread...\n");
            new ReportService(library, true).start();
        });
        refresh.addActionListener(e -> area.setText(library.buildReport().replace("₹", "INR ")));
        JPanel content = new JPanel(new BorderLayout(10, 10)); content.setOpaque(false);
        content.add(toolbar, BorderLayout.NORTH);
        JPanel card = titledPanel("Analytics Report", "Generated from the persisted library records");
        card.add(new JScrollPane(area), BorderLayout.CENTER); content.add(card, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private static void showPage(JPanel pages, String page) { ((CardLayout) pages.getLayout()).show(pages, page); }

    private static JPanel pagePanel() {
        JPanel p = new JPanel(new BorderLayout(16, 16)); p.setBackground(PAGE); return p;
    }

    private static JPanel pageHeading(String title, String subtitle) {
        JPanel p = new JPanel(); p.setOpaque(false); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel main = new JLabel(title); main.setFont(PAGE_TITLE); main.setForeground(TEXT);
        JLabel sub = new JLabel(subtitle); sub.setFont(BODY); sub.setForeground(MUTED);
        p.add(main); p.add(Box.createVerticalStrut(4)); p.add(sub); return p;
    }

    private static JLabel sectionLabel(String text) { JLabel l = new JLabel(text); l.setFont(SECTION); l.setForeground(TEXT); return l; }

    private static JPanel toolbar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 7)); p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(2, 5, 2, 5))); return p;
    }

    private static JPanel titledPanel(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(6, 8)); p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(10, 11, 11, 11)));
        JPanel heading = new JPanel(); heading.setOpaque(false); heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title); t.setFont(SECTION); t.setForeground(TEXT);
        JLabel s = new JLabel(subtitle); s.setFont(new Font("SansSerif", Font.PLAIN, 11)); s.setForeground(MUTED);
        heading.add(t); heading.add(Box.createVerticalStrut(2)); heading.add(s); p.add(heading, BorderLayout.NORTH); return p;
    }

    private static JButton actionButton(String text, Runnable action) { JButton b = secondaryButton(text); b.addActionListener(e -> action.run()); return b; }

    private static JButton primaryButton(String text) { JButton b = new JButton(text); b.setFont(new Font("SansSerif", Font.BOLD, 12)); b.setBackground(ACCENT); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setBorder(BorderFactory.createEmptyBorder(8, 13, 8, 13)); return b; }
    private static JButton secondaryButton(String text) { JButton b = new JButton(text); b.setFont(new Font("SansSerif", Font.BOLD, 12)); b.setFocusPainted(false); b.setBorder(BorderFactory.createEmptyBorder(8, 13, 8, 13)); return b; }
    private static JTextField textField(int width) { JTextField f = new JTextField(); if (width > 0) f.setPreferredSize(new Dimension(width, 32)); f.setFont(BODY); return f; }

    private static DefaultTableModel nonEditableModel(String... columns) {
        return new DefaultTableModel(columns, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    }

    private static void styleTable(JTable table) {
        table.setRowHeight(30); table.setFont(BODY); table.setForeground(TEXT); table.setSelectionBackground(new Color(221, 235, 245)); table.setSelectionForeground(TEXT);
        table.setShowGrid(true); table.setGridColor(new Color(232, 236, 240)); table.setAutoCreateRowSorter(true); table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11)); table.getTableHeader().setForeground(TEXT); table.getTableHeader().setBackground(new Color(238, 242, 246)); table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer(); renderer.setBorder(new EmptyBorder(0, 8, 0, 8));
        table.setDefaultRenderer(Object.class, renderer);
    }

    private static void loadBooks(DefaultTableModel model, List<Book> books) {
        model.setRowCount(0); for (Book b : books) model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getStatus()});
    }

    private static void loadMembers(DefaultTableModel model, List<Member> members) {
        model.setRowCount(0); for (Member m : members) model.addRow(new Object[]{m.getId(), m.getName(), m.getEmail(), m.getMembershipType(), m.getIssuedBookIds().size() + "/" + m.getMembershipType().getBorrowingLimit(), m.getMembershipDate()});
    }

    private static void loadActiveLoans(DefaultTableModel model, Library library) {
        model.setRowCount(0);
        for (Transaction t : library.getTransactions()) if (t.isOpen()) model.addRow(new Object[]{"TX-" + t.getId(), t.getBookId(), t.getMemberId(), t.getIssueDate(), t.getDueDate(), t.getStatus()});
        if (model.getRowCount() == 0) model.addRow(new Object[]{"-", "-", "-", "-", "-", "No active loans"});
    }

    private static JPanel formPanel(String[] labels, JComponent[] components) {
        JPanel form = new JPanel(new GridLayout(labels.length, 2, 9, 9));
        for (int i = 0; i < labels.length; i++) { JLabel label = new JLabel(labels[i] + ":"); label.setFont(BODY); form.add(label); form.add(components[i]); }
        return form;
    }

    private static boolean contains(String value, String query) { return value != null && query != null && value.toLowerCase().contains(query.trim().toLowerCase()); }
    private static void message(Component parent, String text) { JOptionPane.showMessageDialog(parent, text, "LibraX", JOptionPane.INFORMATION_MESSAGE); }
    private static void error(Component parent, String text) { JOptionPane.showMessageDialog(parent, text == null ? "Operation failed." : text, "Operation Failed", JOptionPane.ERROR_MESSAGE); }

private static JTextArea createOutputArea() {
    JTextArea a = new JTextArea();
    a.setEditable(false);
    a.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
    a.setMargin(new Insets(10, 10, 10, 10));
    return a;
}

public static void main(String[] args) {
    Library library = new Library();
    launch(library);
}
}
