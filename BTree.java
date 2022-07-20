import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * B+Tree Structure
 * Key - StudentId
 * Leaf Node should contain [ key,recordId ]
 */
class BTree {

    /**
     * Pointer to the root node.
     */
    private BTreeNode root;
    /**
     * Number of key-value pairs allowed in the tree/the minimum degree of B+Tree
     **/
    private int t;

    BTree(int t) {
        this.root = null;
        this.t = t;
    }
    
    //Check if the node is full and return a boolean value
    private boolean checkIfFull(BTreeNode currentNode) {
      int count = 0;
      for (int i = 0; i < currentNode.keys.length; i++) {
        if ((currentNode.keys[i] == 0 && i == 0) || (currentNode.keys[i] > 0 && currentNode.keys[i] != 0)) {
          count ++;
        }
      }
      if (count == 2*this.t) {
        return true;
      }
      return false;
    }
    
    //Create an array of 3 new nodes, create an array of entries size t+1 add all in the leaf 
    //node to the entry and add the new entry in order, put the first t/2 entries in the first
    //node and the second t/2 +1 entries in the second node. Move the first entry in the second
    //node to the parent in order. Set this.next for first node to second and for second node to
    //the next node for the original. Return the array in the order of parent node, left node and
    //right node.
    private BTreeNode[] split(BTreeNode currentNode, BTreeNode parentNode, Student student) {
      //Set up new array of nodes to be passed later
      BTreeNode [] splitNodes = new BTreeNode[3];
      //Set up the next nodes for the new leaves
      splitNodes[2].next = currentNode.next;
      splitNodes[1].next = splitNodes[2];
      //Set new nodes to be leaf nodes and n and t values
      splitNodes[1].leaf = true;
      splitNodes[2].leaf = true;
      splitNodes[1].t = this.t;
      splitNodes[2].t = this.t;
      splitNodes[1].n = this.t;
      splitNodes[2].n = this.t + 1;
      //create temp array to add the new key and value into along with original keys and values
      long [] keys = Arrays.copyOf(splitNodes[1].keys, 2*this.t + 1);
      long [] vals = Arrays.copyOf(splitNodes[1].values, 2*this.t + 1);
      keys[2*t] = student.studentId;
      vals[2*t] = student.recordId;
      //sort the array
      
      Arrays.sort(keys);
      Arrays.sort(vals);
      //Check if parent has size == t, recursively manage parent of parent and split, sort, and redistribute children as needed.
      
      return splitNodes;
    }
    
    BTreeNode insertIntoLeaf(Student student, BTreeNode leafNode) {
      BTreeNode toRet = leafNode;
      int indexToInsert = -1;

      if(checkIfFull(leafNode) == true) {
          return null;
      }

      for(int i = 0; i < leafNode.values.length; i++) {
          if(student.recordId < leafNode.values[i]) {
              indexToInsert = i;
              break;
          }
      }

      long[] newValArr = Arrays.copyOf(leafNode.values, 2*leafNode.t);
      newValArr[indexToInsert] = student.recordId;

      if(indexToInsert != -1) {
          for(int k = indexToInsert+1; k < leafNode.values.length; k++) {
              newValArr[k] = leafNode.values[k-1];
          }
      }

      return toRet;
  }

    long search(long studentId) {
        /** \
         * TODO:
         * Implement this function to search in the B+Tree.
         * Return recordID for the given StudentID.
         * Otherwise, print out a message that the given studentId has not been found in the table and return -1.
         */
      BTreeNode current = this.root;
      boolean found = false;
      while (!found) {
        if (current.leaf == false) {
          for (int i = 0; i < current.keys.length; i++) {
            if (studentId < current.keys[i] && i == 0) {
              current = current.children[i];
              break;
            }
            else if (studentId > current.keys[i] && i == current.keys.length) {
              current = current.children[i+1];
              break;
            }
            else if (studentId > current.keys[i] && studentId < current.keys[i+1]) {
              current = current.children[i+1];
              break;
            }
          }
        }
        else {
          for (int i = 0; i < current.keys.length; i++) {
            if (studentId == current.keys[i]) {
              return current.values[i];
            }
          }
          return -1;
        }
      }
        return -1;
    }

    BTree insert(Student student) {
        /**
         * TODO:
         * Implement this function to insert in the B+Tree.
         * Also, insert in student.csv after inserting in B+Tree.
         */
        
        BTreeNode current = currentNode != null ? currentNode : this.root ;
        int currKeyArrLength = current.keys.length;
        BTreeNode insertInto;
        // Looking for the correct child node to insert into
        // Checks the max key for the children node of the current node (either root or passed through param)
        // Assumption: Children nodes are sorted in order
        for (BTreeNode x : current.children) {
            // Checking if children is a leaf node or not 
            if(x.leaf == true) {
              //checking that the leaf node is full or not
              if(checkIfFull(x)) {
                //add in split function
              }
              else {
                long[] currKeys = x.keys;
                // Checking if key can be inserted in to the node by evaluating the current max key in the child node
                // If true then student.studentID has to reside within this node; Otherwise move on to another child node
                if(currKeys[currKeys.length-1] > student.studentId) {
                    insertInto = x;
                    break;
                }
              }
            }           
        }
        if(current.keys.length <= this.t) {
            current.keys[currKeyArrLength] = student.studentId;
            return null;
        }
        // Split current node
        else {

        }
        // Otherwise split the node in the middle
        return this;
    }

    boolean delete(long studentId) {
        /**
         * TODO:
         * Implement this function to delete in the B+Tree.
         * Also, delete in student.csv after deleting in B+Tree, if it exists.
         * Return true if the student is deleted successfully otherwise, return false.
         */
        return true;
    }

    List<Long> print() {

      List<Long> listOfRecordID = new ArrayList<>();

      /**
       * TODO:
       * Implement this function to print the B+Tree.
       * Return a list of recordIDs from left to right of leaf nodes.
       *
       */
      BTreeNode currentNode = this.root;
      while(currentNode.leaf == false) {
          // Loop until leaf node is hit
          // First child in the children array should be the smallest value since it's sorted 
          currentNode = currentNode.children[0];
      }
      while(currentNode.next != null) {
          for(long val: currentNode.values) {
              listOfRecordID.add(val);
          }
          currentNode = currentNode.next;
      }
      return listOfRecordID;
  }
    
    boolean writeToCSV(Student student) {
        return false;
    }
}
