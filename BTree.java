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
	
	/**
	 * Searches through the B+Tree using key values to find a passed studentId key.
	 * If found the recordID value is returned. Otherwise -1 is returned and key not
	 * found is printed.
	 * @param studentId
	 * @return -1 for not found or the value associated with the key
	 */
	long search(long studentId) {
	  
	  //Set the current node to root and check through each key if child is less than the left most, between
	  //two keys or greater than the rightmost. Repeat until a leaf is found and check through it's keys.
	  //If the key is found return it. Otherwise print that it wasn't found and return -1.
      BTreeNode current = this.root;
      boolean found = false;
      while (!found) {
        //The current node is not a leaf node and each is checked until the child at the correct position
        //is found.
        if (current.leaf == false) {
          for (int i = 0; i < current.keys.length; i++) {
            //Correct child is left most child
            if (studentId < current.keys[i] && i == 0) {
              current = current.children[i];
              break;
            }
            //Correct child is between two identified keys
            else if (studentId > current.keys[i] && i == current.keys.length) {
              current = current.children[i+1];
              break;
            }
            //Correct child is after the last key
            else if (studentId > current.keys[i] && i < current.keys.length - 1 && studentId < current.keys[i+1]) {
              current = current.children[i+1];
              break;
            }
          }
        }
        //Its a leaf node and all keys are checked for a match.
        else {
          for (int i = 0; i < current.keys.length; i++) {
            if (studentId == current.keys[i]) {
              return current.values[i];
            }
          }
          System.out.println("The given studentId has not been found in the table.");
          return -1;
        }
      }
      System.out.println("The given studentId has not been found in the table.");
      return -1;
  }

    /**
     * //Called after a split on the original node. Sets the passed nodes n value to
     * the number of keys in the node. If full, sets the leaf to false.
     * @param currentNode
     */	
	  void countCurrentNode(BTreeNode currentNode) {
		int count = 0;
		for (int i = 0; i < currentNode.keys.length; i++) {
			if (currentNode.keys[i] > 0) {
				count++;
			}
		}
		currentNode.n = count;
		if(currentNode.n == 0) {
			for(int i = 0; i < currentNode.keys.length;i++) {
				currentNode.keys[i] = -1;
			}
		}
		if(currentNode.n == this.t*2) {
			currentNode.leaf = false;
		}
	}

  /**
   * Create an array of 2 new nodes. Add the student key and value into a key and value array along with
   * the original keys and values from the leaf node sorted by the keys. Split those arrays and place the
   * first t values in the left leaf node and the second t+1 values in the right leaf node. Set the left
   * leaf node to point to the right leaf node and the right leaf node to point to the same node as the
   * original leaf node. Return the array of the split leaf nodes.
   * @param student
   * @param currentNode
   * @return array of new leaves
   */
	private BTreeNode[] splitLeaf(BTreeNode currentNode, Student student) {
	      //Set up new array of nodes to be passed later (parent, left child, right child)
	      BTreeNode [] splitNodes = new BTreeNode[2];
	      int indexToInsert = -1;
	      //Initialize and set up the new nodes for the leaf split
	      splitNodes[1] = new BTreeNode(this.t, true);
	      splitNodes[0] = new BTreeNode(this.t, true);
	      splitNodes[1].next = currentNode.next;
	      splitNodes[0].next = splitNodes[1];
	      //Set the n for the new nodes to the number of keys and values about to be added.
	      splitNodes[0].n = this.t;
	      splitNodes[1].n = this.t + 1;
	      //create temp array to find the new key and value position along with original keys and values
	      long [] keys = Arrays.copyOf(currentNode.keys, 2*this.t + 1);
	      long [] vals = Arrays.copyOf(currentNode.values, 2*this.t + 1);
	      keys[2*this.t] = student.studentId;
	      vals[2*this.t] = student.recordId;

	      for(int i = 0; i < keys.length; i++) {
	        if(student.studentId < keys[i]) {
	            indexToInsert = i;
	            break;
	        }
	        else if (i == keys.length - 1) {
	          indexToInsert = i;
	          break;
	        }
	        indexToInsert = keys.length - 1;
	      }

	    //Split and place the new arrays into the children keys and values
	    for (int i = 0; i < this.t; i++) {
	      splitNodes[0].keys[i] = keys[i];
	      splitNodes[0].values[i] = vals[i];
	    }
	    splitNodes[1].keys[0] = keys[this.t];
	    splitNodes[1].values[0] = vals[this.t];
	    for (int i = 1; i < this.t+1; i++) {
	      splitNodes[1].keys[i] = keys[i+this.t];
	      splitNodes[1].values[i] = vals[i+this.t];
	    }
	    //Return the array of new leaf nodes after the split.
	      return splitNodes;
	    }
	
	/**
	 * Performs a simple insert into a leaf when the leaf is not full and doesn't require a split.
	 * Adds the original keys and values along with the passed student keys and values into arrays,
	 * sorting them by key values from least to greatest, then updates the passed nodes keys and values
	 * with the arrays and returns the node.
	 * @param Student, BTreeNode
	 * @return BTreeNode of the leaf
	 */
	BTreeNode insertIntoLeaf(Student student, BTreeNode leafNode) {
	      BTreeNode toRet = leafNode;
	      int indexToInsert = -1;
//	      countCurrentNode(leafNode);

	      if(leafNode.n >= this.t*2) {
	          return null;
	      }
	      
	      //Check if the new key is less than any already there. If so, set the insert index to that
	      //position. Otherwise set the index to the last element position.
	      for(int i = 0; i < leafNode.keys.length; i++) {
	          if(student.studentId < leafNode.keys[i]) {
	              indexToInsert = i;
	              break;
	          }
	          else if(leafNode.n == 0) {
	        	  indexToInsert = 0;
	          }
	          else if (leafNode.n == (2*this.t)) {
	        	  indexToInsert = leafNode.n - 1;
	            break;
	          }
	          else {
	        	  indexToInsert = leafNode.n;
	          }
	      }
	      
	      //A new array is created that copies the original leaf node keys and values and places the
	      //new key and value of student in the index where student belongs
	      long[] newKeyArr = Arrays.copyOf(leafNode.keys, 2*leafNode.t);
	      newKeyArr[indexToInsert] = student.studentId;

	      long[] newValArr = Arrays.copyOf(leafNode.values, 2*leafNode.t);
	      newValArr[indexToInsert] = student.recordId;
	      
	      //The second half of the original leaf node keys and values is then shifted over by one and placed 
	      //to the right of the student key and value
	      if(indexToInsert != -1) {
	          for(int k = indexToInsert+1; k < leafNode.keys.length; k++) {
	              newKeyArr[k] = leafNode.keys[k-1];
	          }
	          for(int k = indexToInsert+1; k < leafNode.values.length; k++) {
	              newValArr[k] = leafNode.values[k-1];
	          }
	      }
	      
	      //Set the leafNode keys and values to the new arrays
	      toRet.keys = newKeyArr;
	      toRet.values = newValArr;
	      toRet.n ++;
	      //Testing purposes. Remove!!!
//	      countCurrentNode(leafNode);
	      return toRet;
	  }
	
	/**
	 * 
	 * @param parent
	 * @param newChildren
	 * @return
	 */
//	BTreeNode parentInsertChildren(BTreeNode parent, BTreeNode[] newChildren) {
//		// Base case
//		if(this.root == parent) {
//			if(parent.n < this.t*2) {
//				for(BTreeNode x: newChildren) {
//					int indexToInsert = -1;
//					long childKey = -1;
//					int childPos = -1;
//					for(int i = 0; i < parent.keys.length;i++) {
//						if(x.keys[0] < parent.keys[i]) {
//							indexToInsert = i;
//							childKey = x.keys[0];
//							break;
//						}
//						else if(parent.n == this.t*2) {
//							indexToInsert = parent.n - 1;
//							childPos = this.t*2;
//							childKey = x.keys[0];
//							break;
//						}
//						else {
//							indexToInsert = parent.n;
//							childKey = x.keys[0];
//						}
//						childPos = i;
//					}
//					long[] newKeyArr = parent.keys;
//					BTreeNode[] newChildrenArr = parent.children;
//					
//					newKeyArr[indexToInsert] = childKey;
//					newChildrenArr[childPos] = x;
//					
//					for(int l = indexToInsert + 1; l < parent.keys.length;l++) {
//						newKeyArr[l] = parent.keys[l-1];
//					}
//					for(int p = 0; p < parent.keys.length;p++) {
//						for(int u = 0; u < parent.children.length;u++) {
//							if(x.keys[0] == parent.keys[p]) {
//								newChildArr[]
//							}
//							
//						}
//					}
//					
//					
//				}
//			}
//			else {
//				// Parent is a root and is full
//				// Split the root and set the children
//			}
//		}
//		return null;
//	}

	/**
	 * This function finds the place for the new node to be inserted into
	 * @param student
	 * @param currentNode
	 * @return
	 */
	BTreeNode findCorrectSubtree(Student student, BTreeNode currentNode) {
		BTreeNode current = currentNode;
		// If leaf node search for position in values for student record ID
		if(current.leaf == false && current.n < this.t*2) {
			// Position is in terms of the children nodes < k will result in the i th children
			// k <= x < v will result in the i+1 th children
			// x >= v will result in the i+1th children at the end of the list
			int position = -1;
			for(int i = 0; i < current.keys.length; i++) {
				if(student.studentId < current.keys[i]) {
					position = i;
					break;
				}
				// at the end of the list use the last pointer
				else if ((i + 1) > current.keys.length - 1) {
					position = i + 1;
					break;
				}
				// in between the current key (i) and next key (i+1)
				else if(current.keys[i] <= student.studentId && current.keys[i+1] > student.studentId) {
					position = i + 1;
					break;
				}
			}
			if(position != -1) {
				BTreeNode childPointer = current.children[position];
				findCorrectSubtree(student, childPointer);
			}
		}
		else {
			if(currentNode.n >= this.t*2) {
				BTreeNode parentNode = currentNode;
				BTreeNode[] tempSplit = splitLeaf(currentNode, student);
			}
			insertIntoLeaf(student, current);
		}
		return null;
	}

	

	BTree insert(Student student) {
		/**
		 * TODO:
		 * Implement this function to insert in the B+Tree.
		 * Also, insert in student.csv after inserting in B+Tree.
		 * 
		 */
		BTreeNode destNode = null;
		// Checking if root exists
		if(this.root == null) {
			this.root = new BTreeNode(this.t, true);
		}
		
		// If root is not a leaf node then find the correct subtree
		if(this.root.leaf == false) {
			destNode = findCorrectSubtree(student, this.root);
		}
		// If root is a leaf then we only can insert into the root
		else if(this.root.leaf == true) {
			this.root = insertIntoLeaf(student, this.root);
		}		
		
		// If destination node is found insert the new student values in to the node
		if(destNode != null) {
			// Insert into leaf node
			BTreeNode temp = insertIntoLeaf(student, destNode);
			System.out.println(temp.toString());
		}
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
	
	/**
	 * Iterates from root until the left most child is found. Then the recordID values of all
	 * keys from left to right are added to a list. Then the next leaf node will be selected
	 * and the process will repeat until all leaf node recordIDs have been added. Finally,
	 * the list will be returned.
	 * @return list of recordIDs from left to right of leaf nodes.
	 */
	List<Long> print() {

		List<Long> listOfRecordID = new ArrayList<>();
		BTreeNode currentNode = this.root;
		
		while(currentNode.leaf == false) {
			// Loop until leaf node is hit
			// First child in the children array should be the smallest value since it's sorted 
			currentNode = currentNode.children[0];
		}
		//iterates through values in first node and adds them to the list
		for(long val: currentNode.values) {
          listOfRecordID.add(val);
        }
		//checks if there are any additional nodes, and if so, adds their values to the list
		while(currentNode.next != null) {
		  currentNode = currentNode.next;
			for(long val: currentNode.values) {
				listOfRecordID.add(val);
			}
		}
		return listOfRecordID;
	}

	boolean writeToCSV(Student student) {
		return false;
	}
}
