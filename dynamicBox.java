/*
 * author:Charlize Aponte & Anton 
 * Date: 04/17/24
 * Assignment: Dynamic Bounding Boxes
 */

import java.util.Scanner;

// nodes in the binary search tree
class TreeNode {
    String label;
    int x, y, z;
    TreeNode left, right;

    // initialize the node with label and coordinates
    public TreeNode(String label, int x, int y, int z) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.z = z;
        this.left = null;
        this.right = null;
    }
}

// a binary search tree
class BST {
    private TreeNode root;

    // initialize an empty tree
    public BST() {
        this.root = null;
    }

    // insert a node into the tree
    public void insert(String label, int x, int y, int z) {
        if (root == null) {
            // If the tree is empty, create a new root node
            root = new TreeNode(label, x, y, z);
            System.out.println("ADDED " + label);
        } else {
            // Otherwise, insert recursively
            insertRecursive(root, label, x, y, z);
        }
    }

    // recursively insert a node into the tree
    private void insertRecursive(TreeNode node, String label, int x, int y, int z) {
        if (label.equals(node.label)) {
            // If the label already exists, update the node's coordinates
            node.x = x;
            node.y = y;
            node.z = z;
            System.out.println("UPDATED " + label);
        } else if (label.compareTo(node.label) < 0) {
            // If the label is less than the current node's label, insert into the left subtree
            if (node.left != null) {
                insertRecursive(node.left, label, x, y, z);
            } else {
                node.left = new TreeNode(label, x, y, z);
                System.out.println("ADDED " + label);
            }
        } else {
            // If the label is greater than the current node's label, insert into the right subtree
            if (node.right != null) {
                insertRecursive(node.right, label, x, y, z);
            } else {
                node.right = new TreeNode(label, x, y, z);
                System.out.println("ADDED " + label);
            }
        }
    }

    //  delete a node from the tree
    public void delete(String label) {
        root = deleteRecursive(root, label);
    }

    // recursively delete a node from the tree
    private TreeNode deleteRecursive(TreeNode node, String label) {
        if (node == null) {
            // If the node is null, the label does not exist in the tree
            System.out.println("POINT DOES NOT EXIST");
            return null;
        }

        if (label.equals(node.label)) {
            // If the label matches the current node's label
            System.out.println("DELETED " + label);
            if (node.left == null) {
                // If the node has no left child, return its right child (or null)
                return node.right;
            }
            if (node.right == null) {
                // If the node has no right child, return its left child
                return node.left;
            }

            // Find the inorder successor (minimum value in right subtree)
            TreeNode successor = findMin(node.right);
            // Delete the inorder successor from its original position
            node.right = deleteRecursive(node.right, successor.label);
            // Replace node data with successor data
            node.label = successor.label;
            node.x = successor.x;
            node.y = successor.y;
            node.z = successor.z;
        } else if (label.compareTo(node.label) < 0) {
            // If the label is less than the current node's label, delete from the left subtree
            node.left = deleteRecursive(node.left, label);
        } else {
            // If the label is greater than the current node's label, delete from the right subtree
            node.right = deleteRecursive(node.right, label);
        }
        return node;
    }

    // find the minimum node in a subtree
    private TreeNode findMin(TreeNode node) {
        TreeNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    //  get the root node of the tree
    public TreeNode getRoot() {
        return this.root;
    }
}

//  the bounding box of the points in the tree
class BoundingBox {
    int minX, minY, minZ, maxX, maxY, maxZ;

    //  initialize the bounding box with extreme values
    public BoundingBox() {
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.minZ = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
    }

    // update the bounding box based on new coordinates
    public void update(int x, int y, int z) {
        minX = Math.min(minX, x);
        minY = Math.min(minY, y);
        minZ = Math.min(minZ, z);
        maxX = Math.max(maxX, x);
        maxY = Math.max(maxY, y);
        maxZ = Math.max(maxZ, z);
    }

    // calculate the volume of the bounding box
    public int area() {
        return (maxX - minX) * (maxY - minY) * (maxZ - minZ);
    }
}

public class dynamicBox {
    public static void main(String[] args) {
        BST bst = new BST();
        BoundingBox boundingBox = new BoundingBox();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter input test cases (PASTE THE ENTIRE INPUT): ");
        int n = Integer.parseInt(scanner.nextLine());

        // Process each input test case
        for (int i = 0; i < n; i++) {
            String[] instruction = scanner.nextLine().split(" ");
            try {
                Thread.sleep(2000); // Wait for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Results:");

            if (instruction[0].equals("INSERT")) {
                String label = instruction[1];
                int x = Integer.parseInt(instruction[2]);
                int y = Integer.parseInt(instruction[3]);
                int z = Integer.parseInt(instruction[4]);
                bst.insert(label, x, y, z);
                boundingBox.update(x, y, z);
            } else if (instruction[0].equals("DELETE")) {
                String label = instruction[1];
                bst.delete(label);
                boundingBox = updateBoundingBox(bst);
            } else if (instruction[0].equals("BBX")) {
                
                System.out.println(boundingBox.area());
            }
        }

     
        scanner.close();
    }

    //  update the bounding box after deleting a node
    private static BoundingBox updateBoundingBox(BST bst) {
        BoundingBox newBoundingBox = new BoundingBox();
        updateBoundingBoxHelper(bst.getRoot(), newBoundingBox);
        return newBoundingBox;
    }

    // Recursive update the bounding box
    private static void updateBoundingBoxHelper(TreeNode node, BoundingBox boundingBox) {
        if (node != null) {
            boundingBox.update(node.x, node.y, node.z);
            updateBoundingBoxHelper(node.left, boundingBox);
            updateBoundingBoxHelper(node.right, boundingBox);
        }
    }
}
