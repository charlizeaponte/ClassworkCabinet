/**
 * @author Charlize Aponte and Anton
 * Date: 02/09/2024
 *  Asssignment: Longest Common SubSequence using Dynamic Programming
 */
 import java.util.Scanner; 

public class longCommonSeq {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter First string: ");
        String input1 = scan.nextLine(); 
        System.out.println("Enter Second string: ");
        String input2 = scan.nextLine(); 
       System.out.println("Enter third string: ");
       String input3 = scan.nextLine();
        scan.close(); 

        String lcs = computeLongSeq(input1, input2,input3);
        int length = lcs.length();
        System.out.println("Longest Common Subsequence: " + lcs);
        System.out.println("Length of Longest Common Subsequence: " + length);
    }

    public static String computeLongSeq(String input1, String input2, String input3) {

        // Get lengths of the input strings
        int x = input1.length();
        int y = input2.length();
        int z = input3.length();
    
        // Create a 3D array to store the lengths of LCS
        int[][][] L = new int[x + 1][y + 1][z + 1];
    
        // Fill in the dynamic programming table
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                for (int k = 1; k <= z; k++) {
                    if (input1.charAt(i - 1) == input2.charAt(j - 1) && input1.charAt(i - 1) == input3.charAt(k - 1)) {
                        L[i][j][k] = L[i - 1][j - 1][k - 1] + 1; // Match
                    } else {
                        // Not a match, take the maximum of the three possible directions
                        int temp1 = Math.max(L[i - 1][j][k], L[i][j - 1][k]);
                        L[i][j][k] = Math.max(temp1, L[i][j][k - 1]);
                    }
                }
            }
        }

        // Build the longest common subsequence
        
        // Get the length of the LCS from the bottom right corner of the table
       int length = L[x][y][z];
       
       // Create a char array to store the LCS
       char[] lcs = new char[length];
    
        // Start from the bottom right corner of the table
       int i = x, j = y, k = z;

       while (i > 0 && j > 0 && k > 0) {
           if (input1.charAt(i - 1) == input2.charAt(j - 1) && input1.charAt(i - 1) == input3.charAt(k - 1)) {
                // Add the common character to the LCS
               lcs[length - 1] = input1.charAt(i - 1);
               i--;
               j--;
               k--;
               length--;
           } else if (L[i - 1][j][k] > L[i][j - 1][k]) {
             // Move to the cell above  
                i--;
           } else if (L[i][j - 1][k] > L[i][j][k - 1]) {
            // Move to the cell on the left 
                j--;
           } else {
            // Move to the cell behind
               k--;
           }
       }

       return new String(lcs);
    
}
  
}
