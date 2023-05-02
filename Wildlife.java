import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
class Admin{
    public int selectChoice(){
        Scanner Sin = new Scanner(System.in);
        System.out.println("1. Insert Animal Details");
        System.out.println("2. View Customer");
        System.out.println("3. Exit");
        System.out.println("Enter your choice:");
        int ch = Sin.nextInt();
        return ch;
    }
    public void addAnimal(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wildlife","root","root");
            Statement st = con.createStatement();		
            int animalId,age;
            String food,habitat,country,animalName;
            LocalDate date; 
            Scanner sin = new Scanner(System.in);
            System.out.println("Enter Animal Name:");
            animalName = sin.nextLine();
            System.out.println("Enter Animal Age:");
            age = sin.nextInt();
            sin.nextLine();
            System.out.println("Enter Animal Food:");
            food = sin.nextLine();
            System.out.println("Enter Animal Habitat:");
            habitat = sin.nextLine();
            System.out.println("Enter animal's origin country");
            country  = sin.nextLine();
            date = LocalDate.now();
            System.out.println("Adding Animal Details.......");
            int x= st.executeUpdate("insert into animaldetails(animalName,age,food,habitat,country,date) values('"+animalName+"','"+age+"','"+food+"','"+habitat+"','"+country+"','"+date+"')");
            if(x>0){
                System.out.println("Inserted Successfully");
            }
            else{
                System.out.println("Unsuccessful");
            }
        }
        catch(Exception ex){
           ex.printStackTrace();
        }
    }
    public void viewCustomer(){
        String name,phone,gender;
        int age,customerid,count=0;
        Date date=null;
        Scanner sin = new Scanner(System.in);
        System.out.println("Enter Customer name:");
        name = sin.nextLine();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wildlife","root","root");
            Statement st = con.createStatement();
            Statement st1 = con.createStatement();	
            ResultSet rst = st1.executeQuery("Select * from ticket where name = '"+name+"'");
            if(rst.next()){
                count = rst.getInt("persons");
                date = rst.getDate("date");
            }
            ResultSet rs=st.executeQuery("Select * from customer where name ='"+name+"' ");
            System.out.println("CustomerId\tName\t\tAge\tPhone\t\tGender\tCount\tdate");
            while(rs.next()){
                customerid = rs.getInt("customerid");
                phone  = rs.getString("phone");
                gender = rs.getString("gender");
                age = rs.getInt("age");

                System.out.println(customerid+"\t\t"+name+"\t"+age+"\t"+phone+"\t"+gender+"\t"+count+"\t"+date);
            }
            System.out.println();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
class Customer{
    public int selectChoice(){
        Scanner Sin = new Scanner(System.in);
        System.out.println("1. View Animal Details");
        System.out.println("2. Book Ticket");
        System.out.println("3. Exit");
        System.out.println("Enter your choice:");
        int ch = Sin.nextInt();
        return ch;
    }
    public void viewAnimal(){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wildlife","root","root");
                Statement st = con.createStatement();	
                Scanner sin = new Scanner(System.in);
                System.out.println("Enter Animal Name:");
                String animal = sin.nextLine();
                ResultSet rs=st.executeQuery("Select * from animaldetails where animalName ='"+animal+"' ");
                if(rs.next()) {
                    int aId = rs.getInt("animalId");
                    String aName = rs.getString("animalName");
                    int age = rs.getInt("age");
                    String afood = rs.getString("food");
                    String ahabitat = rs.getString("habitat");
                    String country = rs.getString("country");
                    System.out.println();
                    System.out.println("AnimalId:"+aId);
                    System.out.println("AnimalName:"+aName);
                    System.out.println("Age:"+age);
                    System.out.println("Food:"+afood);
                    System.out.println("Habitat:"+ahabitat);
                    System.out.println("Country:"+country);
                    System.out.println();
                }
                else{
                    System.out.println("Animal Details not found");
                } 
                st.close();
                con.close(); 
            }

            catch(Exception ex){
                ex.printStackTrace();
            }
    }
}   
class Ticket{
    public void bookTicket(){
        try{
            String name,id,gender,phone;
            int persons,age,ticketid;
            LocalDate date;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wildlife","root","root");
            Statement st = con.createStatement();
            Scanner sin = new Scanner(System.in);
            System.out.println("Enter Name:");
            name = sin.nextLine();
            System.out.println("Enter Age");
            age = sin.nextInt();
            sin.nextLine();
            System.out.println("Enter Number of Persons:");
            persons = sin.nextInt();
            sin.nextLine();
            System.out.println("Enter Gender:");
            gender = sin.nextLine();
            System.out.println("Enter Phone Number:");
            phone = sin.nextLine();
            date = LocalDate.now();
            int amount = persons * 250;
            int y = st.executeUpdate("insert into customer(name,age,phone,gender) values('"+name+"','"+age+"','"+phone+"','"+gender+"')");
            int x= st.executeUpdate("insert into ticket(name,persons,amount,date) values('"+name+"','"+persons+"','"+amount+"','"+date+"')");
            if(x>0){
                ResultSet rs=st.executeQuery("Select ticketId from ticket where name ='"+name+"' ");
                if(rs.next()){
                ticketid = rs.getInt("ticketId");
                    Payment pt = new Payment();
                    pt.makePayment(ticketid, amount);
                    System.out.println("Ticket booked Successfully");
                }
                else{
                    System.out.println("Ticketid not found");
                }
            }
            else{
                System.out.println("Ticket cannot be booked");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
class Payment{
    public void makePayment(int ticketid,int amount){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wildlife","root","root");
            Statement st = con.createStatement(); 
            String mode;
            LocalDateTime ldt;
            Scanner sin = new Scanner(System.in);
            System.out.println("Total amount is Rs."+amount);
            System.out.println("Payment mode:");
            mode = sin.nextLine();
            if(mode.equals("card")){
                cardPayment(amount);
            }
            ldt = LocalDateTime.now();
            int x= st.executeUpdate("insert into payment(ticketId,mode,amount,time) values('"+ticketid+"','"+mode+"','"+amount+"','"+ldt+"')");
            if(x>0){
                System.out.println("payment made Successfully");
            }
            else{
                System.out.println("payment cannot be made");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        } 
    }
    private void cardPayment(int amount){
        try{
            Scanner sin = new Scanner(System.in);
            String customer = "customer";
            System.out.println("Enter Card number:");
            String card = sin.nextLine();
            int fetchAmount=0;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wildlife","root","root");
            Statement st = con.createStatement();
            ResultSet rs =  st.executeQuery("Select * from card where role ='"+customer+"' ");
            while(rs.next()){

                fetchAmount = rs.getInt("amount");
            }
            if(fetchAmount>amount){
                int custAmount = fetchAmount - amount;
                int x = st.executeUpdate("Update card set amount = '"+custAmount+"' where role = 'customer'");
                if(x>0){
                    int y = st.executeUpdate("Update card set amount = amount + '"+amount+"' where role = 'admin' ");   
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
public class Wildlife{
    public static void main(String[] args){
        int ch;
        Scanner sin = new Scanner(System.in);
        while(true){
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");
            System.out.println("      Indian Wildlife Management System      ");
            System.out.println("---------------------------------------------");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");
            System.out.println("Enter your choice");
            ch = sin.nextInt();
            if(ch==1){
                Admin ad = new Admin();
                while(true){
                    int choice = ad.selectChoice();
                    if(choice==1){
                        ad.addAnimal();
                    }
                    else if(choice==2){
                        ad.viewCustomer();
                    }
                    else if(choice==3){
                        break;
                    }
                }
            }
            else if(ch==2){
                while(true){
                    Customer cust = new Customer();
                    int flag = cust.selectChoice();
                    if(flag==1){
                        cust.viewAnimal();
                    }
                    else if(flag==2){
                        Ticket t = new Ticket();
                        t.bookTicket();
                    }
                    else if(flag==3){
                        break;
                    }
                }
            }
            else if(ch==3){
                System.out.println("Thank you visit again");
                break;
            }
            else {
                System.out.println("Enter correct option");
            }
        }
    }
}