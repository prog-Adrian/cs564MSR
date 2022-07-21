import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Main Application.
 */
public class BTreeMain {

    public static void main(String[] args) {

        /** Read the input file -- input.txt */
        Scanner scan = null;
        try {
            scan = new Scanner(new File("src/input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        /** Read the minimum degree of B+Tree first */

        int degree = scan.nextInt();
        
        BTree bTree = new BTree(degree);

        /** Reading the database student.csv into B+Tree Node*/
        List<Student> studentsDB = getStudents();

        for (Student s : studentsDB) {
            bTree.insert(s);
        }
        
//        //DELETE THIS!!!!!!!!!!!!!!
//        BTreeNode tempNode = new BTreeNode(degree, false);
//        tempNode.children[0] = new BTreeNode(degree, true);
//        tempNode.children[1] = new BTreeNode(degree, true);
//        tempNode.children[2] = new BTreeNode(degree, true);
//        tempNode.children[3] = new BTreeNode(degree, true);
//        
//        tempNode.keys[0] = 1000000;
//        tempNode.keys[1] = 200000000;
//        tempNode.keys[2] = 300000000;
//        tempNode.keys[3] = 400000000;
//        tempNode.keys[4] = 500000000;
//        tempNode.values[0] = 1;
//        tempNode.values[1] = 2;
//        tempNode.values[2] = 3;
//        tempNode.values[3] = 6;
//        tempNode.values[4] = 7;
////        Student tempStudent = studentsDB.get(0);
////        Student tempStudent2 = studentsDB.get(1);
//        for (Student s : studentsDB) {
//            bTree.insert(s);
//        }
//        System.out.println(bTree.search((long) 578875478));
//        System.out.println((long) 1234);
//        //DELETE THIS!!!!!!!!!!!!!!
        
        
        
        /** Start reading the operations now from input file*/
        try {
            while (scan.hasNextLine()) {
                Scanner s2 = new Scanner(scan.nextLine());

                while (s2.hasNext()) {

                    String operation = s2.next();

                    switch (operation) {
                        case "insert": {

                            long studentId = Long.parseLong(s2.next());
                            String studentName = s2.next() + " " + s2.next();
                            String major = s2.next();
                            String level = s2.next();
                            int age = Integer.parseInt(s2.next());
                            
                            //The record is pulled if it is in the line. Otherwise a random long is
                            //created for a new recordId to be placed in student.                            
                            long recordID;
                            if (s2.hasNext()) {
                              recordID = Long.parseLong(s2.next());
                            }
                            else {
                              Random randRecID = new Random();
                              recordID = (long) randRecID.nextInt(50000000);
                              //System.out.println(recordID);
                            }
                            Student s = new Student(studentId, age, studentName, major, level, recordID);
                            bTree.insert(s);

                            break;
                        }
                        case "delete": {
                            long studentId = Long.parseLong(s2.next());
                            boolean result = bTree.delete(studentId);
                            if (result)
                                System.out.println("Student deleted successfully.");
                            else
                                System.out.println("Student deletion failed.");

                            break;
                        }
                        case "search": {
                            long studentId = Long.parseLong(s2.next());
                            long recordID = bTree.search(studentId);
                            if (recordID != -1)
                                System.out.println("Student exists in the database at " + recordID);
                            else
                                System.out.println("Student does not exist.");
                            break;
                        }
                        case "print": {
                            List<Long> listOfRecordID = new ArrayList<>();
                            listOfRecordID = bTree.print();
                            System.out.println("List of recordIDs in B+Tree " + listOfRecordID.toString());
                        }
                        default:
                            System.out.println("Wrong Operation");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Student> getStudents() {

        /** 
         * Extracts the students information from "Students.csv"
         * returns the studentList<Students>
         */
      long studentId = 0;
      int age = 0;
      String studentName = null;
      String major= null;
      String level = null;
      long recordId = 0;
//      int i = 0;

      
      List<Student> studentList = new ArrayList<>();
      
      Scanner scanStudent = null;
      //Open the Student csv file
      try {
          scanStudent = new Scanner(new File("src/Student.csv"));
      } catch (FileNotFoundException e) {
          System.out.println("File not found.");
      }
      
      //For each line, pull in the information delimited by "," from the Student csv and add a new
      //student to the list with all saved variables.
      try {
        while (scanStudent.hasNextLine()) {
            Scanner scanStudent2 = new Scanner(scanStudent.nextLine());
            
            if (scanStudent2.hasNext()) {
              studentId = Long.parseLong(scanStudent2.useDelimiter(",").next());
            }
            
            if (scanStudent2.hasNext()) {
              studentName = scanStudent2.useDelimiter(",").next();;
            }
            
            if (scanStudent2.hasNext()) {
              major = scanStudent2.useDelimiter(",").next();
            }
            
            if (scanStudent2.hasNext()) {
              level = scanStudent2.useDelimiter(",").next();
            }
            
            if (scanStudent2.hasNext()) {
              age = Integer.parseInt(scanStudent2.useDelimiter(",").next());
            }
            
            if (scanStudent2.hasNext()) {
              recordId = Long.parseLong(scanStudent2.useDelimiter(",").next());
            }
            
            Student student = new Student(studentId, age, studentName, major, level, recordId);
            studentList.add(student);

        }
      } catch (Exception e) {
          e.printStackTrace();
      }
      
        return studentList;
        
    }
}
