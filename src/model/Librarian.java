package model;

/** Represents a librarian account in the system. */
public class Librarian extends Person {
    private static final long serialVersionUID = 1L;

    private final String employeeId;
    private String department;

    public Librarian(String id, String name, String email, String phone,
                     String employeeId, String department) {
        super(id, name, email, phone);
        this.employeeId = employeeId;
        this.department = department;
    }

    public String getEmployeeId() { return employeeId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String getRole() { return "Librarian"; }

    @Override
    public String getSummary() {
        return getId() + " | " + getName() + " | " + department
                + " | Employee: " + employeeId;
    }
}
