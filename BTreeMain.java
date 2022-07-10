import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
        int j = 0;
        long currStudentRecordID = 0;
        long maxStudentRecordID = 0;
        
        BTree bTree = new BTree(degree);

        /** Reading the database student.csv into B+Tree Node*/
        List<Student> studentsDB = getStudents();
        
        /**
         * Search through students for the largest recordID and keep the max.
         */        
        while (j < studentsDB.size()) {
          currStudentRecordID = studentsDB.get(j).recordId;
          
          if (currStudentRecordID > maxStudentRecordID) {
            maxStudentRecordID = currStudentRecordID;
          }
          j++;
        }

        for (Student s : studentsDB) {
            bTree.insert(s);
        }
        
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
                            /** 
                             * The maxRecordID gained early is incremented by 1 for the first new insert
                             * and again for every new insert, so as to have no overlap between sessions.
                             * */
                            maxStudentRecordID += 1;
                            long recordID = maxStudentRecordID;
                            //System.out.println(recordID);

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

        /** TODO:
         * Extract the students information from "Students.csv"
         * return the list<Students>
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
      try {
          scanStudent = new Scanner(new File("src/Student.csv"));
      } catch (FileNotFoundException e) {
          System.out.println("File not found.");
      }
      
      try {
        while (scanStudent.hasNextLine()) {
          //System.out.println(scanStudent.nextLine());
            Scanner scanStudent2 = new Scanner(scanStudent.nextLine());
            
            if (scanStudent2.hasNext()) {
              studentId = Long.parseLong(scanStudent2.useDelimiter(",").next());
              //System.out.println(studentName);
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
            
            //Student(long studentId, int age, String studentName, String major, String level, long recordId)
            Student student = new Student(studentId, age, studentName, major, level, recordId);
            studentList.add(student);
//            System.out.print(studentList.get(i).studentId + ",");
//            System.out.print(studentList.get(i).studentName + ",");
//            System.out.print(studentList.get(i).major + ",");
//            System.out.print(studentList.get(i).level + ",");
//            System.out.print(studentList.get(i).age + ",");
//            System.out.println(studentList.get(i).recordId);
//            i++;
        }
      } catch (Exception e) {
          e.printStackTrace();
      }
      
        return studentList;
        
    }
}
