package Hospital.demo.model;

class Library {
    String bookName;
    int bookPages;
    int totalFine;

    void addBook(String name, int pages) {
        bookName = name;
        bookPages = pages;
        System.out.println("Book added: " + bookName + " (" + bookPages + " pages)");
    }

    void issueBook(int daysTaken) {
        if (daysTaken > 7) {
            totalFine = (daysTaken - 7) * 2;
        } else {
            totalFine = 0;
        }
        System.out.println("Book issued for " + daysTaken + " days. Fine: ₹" + totalFine);
    }

    void showBook() {
        System.out.println("Book: " + bookName + ", Pages: " + bookPages + ", Fine Due: ₹" + totalFine);
    }

    public static void main(String[] args) {
        Library l = new Library();
        l.addBook("Java Basics", 350);
        l.issueBook(10);
        l.showBook();
    }
}
