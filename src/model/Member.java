package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Library member with a membership tier and borrowing limit. */
public class Member extends Person {
    private static final long serialVersionUID = 1L;

    public enum MembershipType {
        STUDENT(5), REGULAR(3), PREMIUM(10);

        private final int borrowingLimit;

        MembershipType(int borrowingLimit) {
            this.borrowingLimit = borrowingLimit;
        }

        public int getBorrowingLimit() { return borrowingLimit; }
    }

    private final String membershipDate;
    private MembershipType membershipType;
    private final List<String> issuedBookIds;

    public Member(String id, String name, String email, String phone,
                  String membershipDate, MembershipType membershipType) {
        super(id, name, email, phone);
        this.membershipDate = membershipDate;
        this.membershipType = membershipType;
        this.issuedBookIds = new ArrayList<>();
    }

    public String getMembershipDate() { return membershipDate; }
    public MembershipType getMembershipType() { return membershipType; }
    public List<String> getIssuedBookIds() {
        return Collections.unmodifiableList(issuedBookIds);
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public boolean canBorrow() {
        return issuedBookIds.size() < membershipType.getBorrowingLimit();
    }

    public boolean hasBook(String bookId) {
        return issuedBookIds.stream().anyMatch(id -> id.equalsIgnoreCase(bookId));
    }

    public void addBook(String bookId) {
        if (!canBorrow()) {
            throw new IllegalStateException("Borrowing limit reached for this membership.");
        }
        if (!hasBook(bookId)) issuedBookIds.add(bookId);
    }

    public void removeBook(String bookId) {
        issuedBookIds.removeIf(id -> id.equalsIgnoreCase(bookId));
    }

    @Override
    public String getRole() { return "Member"; }

    @Override
    public String getSummary() {
        return String.format("%-7s | %-22s | %-9s | Borrowed: %d/%d",
                getId(), getName(), membershipType, issuedBookIds.size(),
                membershipType.getBorrowingLimit());
    }
}
