/*
* @author charlizeaponte and Anton
* Assignment: Btrees
* Date:3/26/24
*/



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


class BTreeNode {
    int[] keys;
    int degree;
    BTreeNode[] children;
    int numKeys;
    boolean isLeaf;

    // Constructor
    public BTreeNode(int degree, boolean isLeaf) {
        this.degree = degree;
        this.isLeaf = isLeaf;
        keys = new int[2 * degree - 1];
        children = new BTreeNode[2 * degree];
        numKeys = 0;
    }

    // Search for a key
    public int search(int key) {
        int blocksRead = 1;
        int i = 0;
        while (i < numKeys && key > keys[i]) {
          i++;
        }
      
        if (i < numKeys && keys[i] == key) { // Check if key found in current node
          return blocksRead;
        }
      
        if (isLeaf)
          return 0;
      
        // Check if child might contain the key even if full
        if (key > keys[i]) {
          i++;
        }
      
        // Check for null child before recursive call
        if (children[i] != null) { 
      
          // Additional check for keys in current node
          for (int j = i + 1; j < numKeys; j++) {
            if (key < keys[j]) {
              break; 
            }
          }
          
          return blocksRead + children[i].search(key); // Recursive call
        } else {
          return blocksRead; // Child doesn't exist, return current count
        }
    }

    // Insert a key into a non-full node
    public void insertNonFull(int key) {
        int i = numKeys - 1;
    
        if (isLeaf) {
           
            while (i >= 0 && keys[i] > key) {
                keys[i + 1] = keys[i];
                i--;
            }
    
            keys[i + 1] = key;
            numKeys++;
        } else {
            // Find the appropriate child to insert the key
            while (i >= 0 && keys[i] > key)
                i--;
    
            if (children[i + 1].numKeys == 2 * degree - 1) {
                
                splitChild(i + 1, children[i + 1]);
                if (keys[i + 1] < key)
                    i++;
            }
            children[i + 1].insertNonFull(key);
        }
    }
    
    // Split child node
    public void splitChild(int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(degree, y.isLeaf);

        z.numKeys = degree - 1;

        for (int j = 0; j < degree - 1; j++)
            z.keys[j] = y.keys[j + degree];

        if (!y.isLeaf) {
            for (int j = 0; j < degree; j++)
                z.children[j] = y.children[j + degree];
        }

        y.numKeys = degree - 1;

        for (int j = numKeys; j >= i + 1; j--)
            children[j + 1] = children[j];

        children[i + 1] = z;

        for (int j = numKeys - 1; j >= i; j--)
            keys[j + 1] = keys[j];

        keys[i] = y.keys[degree - 1];
        numKeys++;
    }
}

// B-tree
class BTree {
    private BTreeNode root;
    private int degree;

    // Constructor
    public BTree(int degree) {
        this.degree = degree;
        root = null;
    }

    // Search for a key
    public int search(int key) {
        if (root == null)
            return 0;

        int blocksRead = root.search(key);
        return blocksRead;
    }

    // Insert a key
    public void insert(int key) {
        if (root == null) {
            root = new BTreeNode(degree, true);
            root.keys[0] = key;
            root.numKeys = 1;
        } else {
            if (root.numKeys == 2 * degree - 1) {
                BTreeNode s = new BTreeNode(degree, false);
                s.children[0] = root;
                s.splitChild(0, root);
                int i = 0;
                if (s.keys[0] < key)
                    i++;
                s.children[i].insertNonFull(key);
                root = s;
            } else {
                root.insertNonFull(key);
            }
        }
    }
}



public class BTrees {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the degree of the B-tree:");
        int degree = scan.nextInt();
        BTree bTree = new BTree(degree);

        System.out.println("Enter the number of elements:");
        int numElements = scan.nextInt();

        // Create a list to store elements for randomized insertion
        List<Integer> elements = new ArrayList<>();

        System.out.println("Enter the elements to insert into the B-tree (separated by spaces):");
        for (int i = 0; i < numElements; i++) {
            elements.add(scan.nextInt());
        }

        // Randomize the order of elements
        Collections.shuffle(elements);

        // Insert elements in the randomized order
        for (Integer element : elements) {
            bTree.insert(element);
        }

        System.out.println("Enter the number of elements to search:");
        int numSearch = scan.nextInt();

        System.out.println("Enter the elements to search for (separated by spaces):");
        for (int i = 0; i < numSearch; i++) {
            int searchKey = scan.nextInt();
            int nodesVisited = bTree.search(searchKey);
            System.out.println(nodesVisited);
        }

        scan.close();
    }
}