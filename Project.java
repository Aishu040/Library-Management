package Library;
import java.sql.*;
import java.util.Scanner;
import java.util.Calendar;

public class Project {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter user type (admin/user):");
        String userType = scanner.nextLine();

        if (userType.equalsIgnoreCase("admin")) {
            adminMenu();
        } else if (userType.equalsIgnoreCase("user")) {
            userMenu();
        } else {
            System.out.println("Invalid user type!");
        }

        scanner.close();
    }

    public static void adminMenu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Admin Menu:");
            System.out.println("1. Add a book");
            System.out.println("2. Remove a book");
            System.out.println("3. View all books");
            System.out.println("4. View all bookstatus");            
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    insertRecords();
                    break;
                case 2:
                    deleteRecords();
                    break;
                case 3:
                    readRecords();
                    break;
                case 4:
                    bookRecords();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        scanner.close();
    }

    public static void userMenu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("User Menu:");
            System.out.println("1. View available books");
            System.out.println("2. Borrow a book");
            System.out.println("3. Return a book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    readRecords();
                    break;
                case 2:
                    borrowBook();
                    break;
                case 3:
                    returnBook();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 4);

        scanner.close();
    }
    public static void borrowBook() throws Exception {
        String url = "jdbc:mysql://localhost:3306/db";
        String userName = "root";
        String passWord = "Aishu@0409";
        Scanner s = new Scanner(System.in);  

        System.out.println("Enter book ID to borrow:");
        int book_id = s.nextInt();
        s.nextLine(); 

        System.out.println("Enter username to borrow:");
        String user_name = s.nextLine();
        String bookstatus = "borrowed";

        Connection con = DriverManager.getConnection(url, userName, passWord);
        String insertUserQuery = "INSERT INTO user1 (user_name, book_id, bookstatus) VALUES (?, ?, ?)";
        PreparedStatement pstUser = con.prepareStatement(insertUserQuery);
        pstUser.setString(1, user_name);
        pstUser.setInt(2, book_id);
        pstUser.setString(3, bookstatus);
        int userRows = pstUser.executeUpdate();
        System.out.println(userRows + " row(s) inserted into user1 table.");
        pstUser.close();

        System.out.println("Enter userid to borrow:");
        int user_id = s.nextInt();
        s.nextLine(); 
        System.out.println("Enter borrowdate to borrow (yyyy-MM-dd):");
        String borrow_dateStr = s.nextLine();
        Date borrow_date = Date.valueOf(borrow_dateStr);

        Calendar cal = Calendar.getInstance();
        cal.setTime(borrow_date);
        cal.add(Calendar.DATE, 15);
        Date return_date = new Date(cal.getTimeInMillis());

        String insertBorrowQuery = "INSERT INTO borrow_book (book_id, user_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
        PreparedStatement pstBorrow = con.prepareStatement(insertBorrowQuery);
        pstBorrow.setInt(1, book_id);
        pstBorrow.setInt(2, user_id);
        pstBorrow.setDate(3, borrow_date);
        pstBorrow.setDate(4, return_date);
        int borrowRows = pstBorrow.executeUpdate();
        System.out.println(borrowRows + " row(s) inserted into borrow_book table.");
        pstBorrow.close();

        String updateBookQuery = "UPDATE book SET quantity = quantity - 1 WHERE book_id = ?";
        PreparedStatement pstUpdate = con.prepareStatement(updateBookQuery);
        pstUpdate.setInt(1, book_id);
        int bookRows = pstUpdate.executeUpdate();
        System.out.println("successfully book borrowed");
        pstUpdate.close();

        bookRecords();
        con.close();
    }

    public static void returnBook() throws Exception {
        String url = "jdbc:mysql://localhost:3306/db";
        String userName = "root";
        String passWord = "Aishu@0409";
        Scanner s = new Scanner(System.in);    
        System.out.println("Enter borrow_id to remove in borrow_book:");
        int borrow_id = s.nextInt();
        s.nextLine();
        System.out.println("Enter user_name to remove in user table:");
        String user_name = s.nextLine();

        Connection con = DriverManager.getConnection(url, userName, passWord);
        String deleteBorrowQuery = "DELETE FROM borrow_book WHERE borrow_id = ?";
        PreparedStatement pst = con.prepareStatement(deleteBorrowQuery);
        pst.setInt(1, borrow_id);
        int borrowRowsDeleted = pst.executeUpdate();
        System.out.println("Number of borrow records deleted: " + borrowRowsDeleted);
        pst.close();

        String deleteUserQuery = "DELETE FROM user1 WHERE user_name = ?";
        PreparedStatement pst1 = con.prepareStatement(deleteUserQuery);
        pst1.setString(1, user_name);
        int userRowsDeleted = pst1.executeUpdate();
        System.out.println("Number of user records deleted: " + userRowsDeleted);
        pst1.close();

        System.out.println("Enter book_id to return:");
        int book_id = s.nextInt();

        String updateBookQuery = "UPDATE book SET quantity = quantity + 1 WHERE book_id = ?";
        PreparedStatement pstUpdate = con.prepareStatement(updateBookQuery);
        pstUpdate.setInt(1, book_id);
        int bookRowsUpdated = pstUpdate.executeUpdate();
        System.out.println("Number of book records updated: " + bookRowsUpdated);
        pstUpdate.close();
        bookRecords(); 
        con.close();  
    }

    public static void insertRecords() throws Exception{
		String url="jdbc:mysql://localhost:3306/db";
		String userName="root";
		String passWord="Aishu@0409";
        Scanner s = new Scanner(System.in);	
        System.out.println("Enter book details:");
		int book_id=s.nextInt();
		s.nextLine();
		String title=s.nextLine();
		String author=s.nextLine();
		int quantity=s.nextInt();
		s.nextLine();
		String query="insert into book values(?,?,?,?)";
		
		Connection con=DriverManager.getConnection(url,userName,passWord);
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1, book_id);
        pst.setString(2, title);
        pst.setString(3,author);
        pst.setInt(4, quantity);        
        int rows = pst.executeUpdate();
		
		System.out.println("Number of rows affected "+rows);
		readRecords();
		con.close();
	}

    public static void deleteRecords() throws Exception{
		String url="jdbc:mysql://localhost:3306/db";
		String userName="root";
		String passWord="Aishu@0409";
        System.out.println("Enter book id to remove:");		
        Scanner s = new Scanner(System.in);			
		int book_id=s.nextInt();
		String query="Delete from book where book_id=?";
		
		Connection con=DriverManager.getConnection(url,userName,passWord);
		PreparedStatement pst=con.prepareStatement(query);
		pst.setInt(1,book_id);
		int rows=pst.executeUpdate();
		
		System.out.println("Number of rows affected: "+rows);
		readRecords();
		con.close();
	}


    public static void readRecords() throws Exception {
        String url = "jdbc:mysql://localhost:3306/db";
        String userName = "root";
        String passWord = "Aishu@0409";
        String query = "SELECT * FROM book";

        Connection con = DriverManager.getConnection(url, userName, passWord);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            System.out.println("Book_Id : " + rs.getInt(1));
            System.out.println("Title of the Book : " + rs.getString(2));
            System.out.println("Author : " + rs.getString(3));
            System.out.println("Quantity : " + rs.getInt(4));
        }
        con.close();
    }
    public static void bookRecords() throws Exception {
        String url = "jdbc:mysql://localhost:3306/db";
        String userName = "root";
        String passWord = "Aishu@0409";
        String query = "select * from viewstatus;";
        Connection con = DriverManager.getConnection(url, userName, passWord);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        System.out.println("+---------+---------------------------------+---------------------------------+----------+------------------+-------------+---------------------+---------------------+");
        System.out.println("| Book_Id | Title                           | Author                          | Quantity | User name        | Book Status | Borrow_date         | return_date         |");
        System.out.println("+---------+---------------------------------+---------------------------------+----------+------------------+-------------+---------------------+---------------------+");
        while (rs.next()) {
            System.out.printf("| %-7d | %-31s | %-31s | %-8d | %-16s | %-11s | %-19s | %-19s |\n",
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getDate(7),
                    rs.getDate(8));
        }
        System.out.println("+---------+---------------------------------+---------------------------------+----------+------------------+-------------+---------------------+---------------------+");
        con.close();
    }
}
