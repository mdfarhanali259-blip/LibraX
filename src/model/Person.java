package model;

import java.io.Serializable;

/**
 * Common base class for people who interact with the library.
 * Demonstrates abstraction and inheritance.
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private String email;
    private String phone;

    protected Person(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    public abstract String getRole();
    public abstract String getSummary();

    @Override
    public String toString() {
        return String.format("%-7s | %-22s | %-10s | %-25s | %s",
                id, name, getRole(), email, phone);
    }
}
