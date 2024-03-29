/*
* @author charlizeaponte and Anton
* Assignment: Btrees
* Date:3/26/24
*/





import java.util.Scanner;

class BTreeNode {
    int[] keys; // Array to store keys
    int degree; // Degree of the B-tree
    BTreeNode[] children; // Array to store child nodes
    int numKeys; // Number of keys currently in the node
    boolean isLeaf; // Indicates whether the node is a leaf

    // Constructor
    public BTreeNode(int degree, boolean isLeaf) {
        this.degree = degree;
        this.isLeaf = isLeaf;
        keys = new int[2 * degree - 1];
        children = new BTreeNode[2 * degree];
        numKeys = 0;
    }

    // search method for a key in the B-tree
    public int search(int key) {
        int blocks= 1;
        int i = 0;
        while (i < numKeys && key > keys[i]) {
            i++;
            blocks++;
        }

        if (i < numKeys && keys[i] == key)
            return blocks;

        if (isLeaf)
            return 0;

        return blocks + children[i].search(key);
    }

    // insert a key into a non-full node method
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

    //  split a child node method 
    public void splitChild(int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(y.degree, y.isLeaf);
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

    // remove a key from the node method 
    public BTreeNode remove(int key) {
        int idx = findIndex(key);

        if (idx < numKeys && keys[idx] == key) {
            if (isLeaf)
                removeFromLeaf(idx);
            else
                removeFromNonLeaf(idx);
        } else {
            if (isLeaf) {
                return null;
            }

            boolean flag = (idx == numKeys);

            if (children[idx].numKeys < degree)
                fill(idx);

            if (flag && idx > numKeys)
                children[idx - 1].remove(key);
            else
                children[idx].remove(key);
        }

        return this;
    }

    // find the index of a key method
    private int findIndex(int key) {
        int idx = 0;
        while (idx < numKeys && keys[idx] < key)
            ++idx;
        return idx;
    }

    //  remove a key from a leaf node method 
    private void removeFromLeaf(int idx) {
        for (int i = idx + 1; i < numKeys; ++i)
            keys[i - 1] = keys[i];
        numKeys--;
    }

    //  remove a key from a non-leaf node method
    private void removeFromNonLeaf(int idx) {
        int k = keys[idx];

        if (children[idx].numKeys >= degree) {
            int predecessor = getPredecessor(idx);
            keys[idx] = predecessor;
            children[idx].remove(predecessor);
        } else if (children[idx + 1].numKeys >= degree) {
            int successor = getSuccessor(idx);
            keys[idx] = successor;
            children[idx + 1].remove(successor);
        } else {
            merge(idx);
            children[idx].remove(k);
        }
    }

    // Predecessor Method 
    private int getPredecessor(int idx) {
        BTreeNode cur = children[idx];
        while (!cur.isLeaf)
            cur = cur.children[cur.numKeys];
        return cur.keys[cur.numKeys - 1];
    }

    // successor Method 
    private int getSuccessor(int idx) {
        BTreeNode cur = children[idx + 1];
        while (!cur.isLeaf)
            cur = cur.children[0];
        return cur.keys[0];
    }

    // fill a child node that has less than degree keys method 
    private void fill(int idx) {
        if (idx != 0 && children[idx - 1].numKeys >= degree)
            borrowFromPrev(idx);
        else if (idx != numKeys && children[idx + 1].numKeys >= degree)
            borrowFromNext(idx);
        else {
            if (idx != numKeys)
                merge(idx);
            else
                merge(idx - 1);
        }
    }

    // borrow a key from the previous child node method 
    private void borrowFromPrev(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx - 1];

        for (int i = child.numKeys - 1; i >= 0; --i)
            child.keys[i + 1] = child.keys[i];

        if (!child.isLeaf) {
            for (int i = child.numKeys; i >= 0; --i)
                child.children[i + 1] = child.children[i];
        }

        child.keys[0] = keys[idx - 1];

        if (!isLeaf)
            child.children[0] = sibling.children[sibling.numKeys];

        keys[idx - 1] = sibling.keys[sibling.numKeys - 1];

        child.numKeys += 1;
        sibling.numKeys -= 1;
    }

    // borrow a key from the next child node Method 
    private void borrowFromNext(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];

        child.keys[(child.numKeys)] = keys[idx];

        if (!child.isLeaf)
            child.children[(child.numKeys) + 1] = sibling.children[0];

        keys[idx] = sibling.keys[0];

        for (int i = 1; i < sibling.numKeys; ++i)
            sibling.keys[i - 1] = sibling.keys[i];

        if (!sibling.isLeaf) {
            for (int i = 1; i <= sibling.numKeys; ++i)
                sibling.children[i - 1] = sibling.children[i];
        }

        child.numKeys += 1;
        sibling.numKeys -= 1;
    }

    //  merge a child node with its sibling Method
    private void merge(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];

        child.keys[degree - 1] = keys[idx];

        for (int i = 0; i < sibling.numKeys; ++i)
            child.keys[i + degree] = sibling.keys[i];

        if (!child.isLeaf) {
            for (int i = 0; i <= sibling.numKeys; ++i)
                child.children[i + degree] = sibling.children[i];
        }

        for (int i = idx + 1; i < numKeys; ++i)
            keys[i - 1] = keys[i];

        for (int i = idx + 2; i <= numKeys; ++i)
            children[i - 1] = children[i];

        child.numKeys += sibling.numKeys + 1;
        numKeys--;
    }
}


class BTree {
    private BTreeNode root;
    private int degree;

    // Constructor 
    public BTree(int degree) {
        this.degree = degree;
        root = null;
    }

    // search  BTREE method
    public int search(int key) {
        if (root == null)
            return 0; // Tree is empty

        int blocksRead = root.search(key);
        return blocksRead;
    }

    // insert btree method 
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

    // remove btree method 
    public void remove(int key) {
        if (root == null) {
            System.out.println("The tree is empty");
            return;
        }

        root.remove(key);

        if (root.numKeys == 0) {
            if (root.isLeaf)
                root = null;
            else
                root = root.children[0];
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

        System.out.println("Enter the elements to insert into the B-tree: (ADD A SPACE IN BETWEEN EACH ELEMENT)");
        for (int i = 0; i < numElements; i++) {
            int num = scan.nextInt();
            bTree.insert(num);
        }

        System.out.println("Enter the number of elements to search and possibly delete:");
        int numSearches = scan.nextInt();

        System.out.println("Enter the elements to search and possibly delete:");
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < numSearches; i++) {
            int num = scan.nextInt();
            int blocksRead = bTree.search(num);
            output.append("Search \"").append(num).append("\": ").append(blocksRead).append(" nodes visited");
            if (i != numSearches - 1) {
                output.append(", ");
            } else {
                output.append("\n");
            }
            bTree.remove(num);
        }
        System.out.print(output);
        scan.close();
    }
}

