/*
 * @author charlizeaponte and Anton
 * Assignment: Edit Distance using Dynamic Programming
 * Date: 02/7/24
 * 
 */



import java.util.Scanner; 

public class editDistance {

    public static void main (String[] args){

        Scanner scan =  new Scanner(System.in);
        //Input String 1 
        System.out.println("Enter first string:"); 
        String input1 = scan.nextLine();
        input1 = input1.toUpperCase();

        System.out.println("Enter Second string:"); 
        String input2 = scan.nextLine();
        input2 = input2.toUpperCase();
        scan.close();

        //get the strings length
        int input1_length = input1.length(); 
        int input2_length = input2.length(); 

        //Create 2d array for distance tracking
        int[][] d = new int[input1_length+1][input2_length+1];
        //Create 2d array for tracking which edit you are doing
        String[][] methods = new String[input1_length+1][input2_length+1];


        /**
         *Dynamic Programming solution: 
         D(i,j) = min{
            j*2                   if i <= 0 base case
            i*2                   if j <= 0 base case
            D(i-1,j-1)          if i>0,J>0(insert)   
            D(i,j-1)+1          if i>0, j> 0 (delete)
            D(i-1,j-1)+1        if i>0,j>0 (replace)


          Running Time:  O(nm) 
            */
    // Base cases
        for (int i = 0; i <= input1_length; i++) {
         d[i][0] = i * 2; // Cost of deletion
    }
    for (int j = 0; j <= input2_length; j++) {
        d[0][j] = j * 2; // Cost of insertion
    }

  // Fill the D table
  // Loop through each character of input1 and input2
for (int i = 1; i <= input1_length; i++) {
    for (int j = 1; j <= input2_length; j++) {
        // If characters at current positions are the same, no operation is needed
        if (input1.charAt(i - 1) == input2.charAt(j - 1)) {
            d[i][j] = d[i - 1][j - 1]; // no operation needed
        } else {
            // Calculate costs for each operation
            int replace = d[i - 1][j - 1] + 3; // Cost of substitution
            int delete = d[i - 1][j] + 2;      // Cost of deletion
            int insert = d[i][j - 1] + 2;      // Cost of insertion
            
            // Choose the minimum of the three methods
            if (replace <= delete && replace <= insert) {
                // Replace operation
                d[i][j] = replace;
                methods[i][j] = "Replace " + input1.charAt(i - 1) + " with " + input2.charAt(j - 1);
            } else if (delete <= replace && delete <= insert) {
                // Delete operation
                d[i][j] = delete;
                methods[i][j] = "Delete " + input1.charAt(i - 1);
            } else {
                // Insert operation
                d[i][j] = insert;
                methods[i][j] = "Insert " + input2.charAt(j - 1);
            }
        }
    }
}

    // Print the edit distance
    int editDistance = d[input1_length][input2_length];
    System.out.println("Edit Distance is " + editDistance);
    
    // Print the methods performed and string after each step
    String result = input1;
    for (int i = input1_length, j = input2_length; i > 0 || j > 0;) {
        if (i > 0 && j > 0 && input1.charAt(i - 1) == input2.charAt(j - 1)) {
            i--;
            j--;
            continue;
        }
        String op = methods[i][j];
        if (op != null) {
            if (op.startsWith("Replace")) {
                System.out.println(result + ": " + op);
                result = result.substring(0, i - 1) + input2.charAt(j - 1) + result.substring(i);
                i--;
                j--;
            } else if (op.startsWith("Delete")) {
                System.out.println(result + ": " + op);
                result = result.substring(0, i - 1) + result.substring(i);
                i--;
            } else if (op.startsWith("Insert")) {
                System.out.println(result + input2.charAt(j - 1) + ": " + op);
                result = result.substring(0, i) + input2.charAt(j - 1) + result.substring(i);
                j--;
            }
        }
    }
    System.out.println(result);
    }

 }