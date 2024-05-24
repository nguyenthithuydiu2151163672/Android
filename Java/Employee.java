import java.util.ArrayList;
import java.util.Scanner;

public class Employee {
    // khai bao
    int id, age, code;
    String name, department;
    double salary_rate;

    static ArrayList<Employee> employeeArrayList = new ArrayList<>();
    //contructor tuong minh
    public Employee(int id, int age, int code, String name, String department, double salary_rate) {

        this.id = id;
        this.age = age;
        this.code = code;
        this.name = name;
        this.department = department;
        this.salary_rate = salary_rate;

    }
    //hien thi nhan vien
    static void displayEmp() {
        System.out.println("ID\t"+"Name\t"+"Age\t"+"Department\t"+"Code\t"+"Salary rate");
        for (int i = 0; i < employeeArrayList.size(); i++) {
            Employee nv = employeeArrayList.get(i);
            System.out.println(nv.id + "\t" + nv.name+  "\t" + nv.age+ "\t" + nv.department+ "\t\t" + nv.code+ "\t" + nv.salary_rate);
            
        }
    }

    //them nhan vien
    static boolean addEmp(int id, int age, int code, String name, String department, double salary_rate){
       return employeeArrayList.add(new Employee(id, age, code, name, department, salary_rate));
      
    }
    
    //xoa nhan vien
    static boolean delEmp(int id){
        return employeeArrayList.remove(employeeArrayList.get(id));
    }
    
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 10; i++) {
            Employee nv1 = new Employee(i, 18, i, "D", "A"+i, 1);
            employeeArrayList.add(nv1);
        }
        int n;
        do {
            System.out.println("1 to show Emp");
            System.out.println("2 to show add Emp");
            System.out.println("3 to show del Emp");
            n =scanner.nextInt();
            if(n ==1){
                displayEmp();
            }else if(n==2){
                System.out.print("ID: ");
                int  id = scanner.nextInt();
                System.out.print("\nName: ");
                 String name = scanner.next();
                System.out.print("\nAge: ");
                int  age = scanner.nextInt();
                System.out.print("\nDepartment: ");
                String  department = scanner.next();
                System.out.print("\nCode: ");
               int code = scanner.nextInt();
                System.out.print("\nSalary rate: ");
               double salary_rate = scanner.nextDouble();
               if(addEmp(id, age, code, name, department, salary_rate)){
                System.out.println("done!");
               }
                
            }else if(n==3){
                System.out.print("ID: ");
                int id = scanner.nextInt();
                if(delEmp(id)){
                    System.out.println("Deleted!");
                }
            }
            else{
                System.out.println("Close");
                scanner.close();
            }
        } while (n==1||n==2||n==3);

    }
}
