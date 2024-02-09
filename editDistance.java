/*
 * @author charlizeaponte and Anton
 * Assignment: Edit Distance using Dynamic Programming
 * Date: 02/7/24
 * 
 */


import java.util.Arrays;
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
        String[][] edits = new String[input1_length+1][input2_length+1];

        /**
         *Dynamic Programming solution:
         Define: D(i, j) as the edit distance between the first i characters of input1 (S) and the first j characters of input2 (T)
         D(i,j) = min {
            j                   if i <= 0 (base case)
            i                   if j <= 0 (base case)
            D(i-1, j-1)         if i > 0, j > 0, and Si = Tj
            D(i-1, j) + 1       if i > 0, j > 0 (insert) $2
            D(i, j-1) + 1       if i > 0, j > 0 (delete) $2
            D(i-1, j-1) + 1     if i > 0, j > 0 (substitute) $3
         }

          Running Time:  O(nm)

        */

        // Base cases (where indexes are at a 0)
        for(int i = 0; i <= input1_length;i++){
            d[i][0]=i;
            if (i == 0){
                edits[i][0] = "Nothing";
            }
            else if (i < input1_length){
                edits[i][0] = String.format("Delete %s", input1.charAt(i));
            }
        }
        for(int j = 0; j <= input2_length;j++){
            d[0][j]=j; 
            if (j == 0){
                edits[0][j] = "Nothing";
            }
            else if (j < input2_length){
                edits[0][j] = String.format("Insert %s", input2.charAt(j));
            }
        }

        // Filling in the rest of the table after the base case
        for(int i = 1; i <= input1_length; i++){
            for(int j = 1; j <= input2_length; j++){

                // Find the min value between all of the cases
                int minValue = 1000;
                // Keep track of action
                String action = "";

                // D(i-1, j-1)         if i > 0, j > 0, and Si = Ti
                // (if letters are the same at this point)
                if(input1.charAt(i-1) == input2.charAt(j-1)){
                    minValue = d[i-1][j-1];
                    action = "Nothing";
                }

                // D(i-1, j) + 2       if i > 0, j > 0 (insert) $2
                // (if you must add a letter)
                else if (input1.substring(0, i-1).length() < input2.substring(0, j-1).length()){
                    int val = d[i][j-1] + 1;
                    if(minValue > val){
                        minValue = val;
                    }
                    action = String.format("Insert %s", input2.charAt(j-1));
                }

                // D(i, j-1) + 1       if i > 0, j > 0 (delete) $2
                // (if you must subtract a letter)
                else if(input1.substring(0, i-1).length() > input2.substring(0, j-1).length()){
                    int val = d[i-1][j] + 1;
                    if(minValue > val){
                        minValue = val;
                    }
                    action = String.format("Delete %s", input1.charAt(i-1));
                }

                // D(i-1, j-1) + 1     if i > 0, j > 0 (substitute) $3
                // (if you must swap letter)
                else {
                    int val = d[i-1][j-1] + 1;
                    if(minValue > val){
                        minValue = val;
                    }
                    action = String.format("Replace %s with %s", input1.charAt(i-1), input2.charAt(j-1));
                }

                d[i][j] = minValue;
                edits[i][j] = action;
                
            }
        }

        // DEBUG - array view
        // for (int[] is : d) {
        //     System.out.println(Arrays.toString(is));
        // }
        
        //print statements
        System.out.println();
        System.out.println(String.format("Edit Distance is %d", d[input1_length][input2_length]));

        // reconstructing actions
        int i = input1_length;
        int j = input2_length;
        while(i >= 0 && j >= 0){
            String act = edits[i][j];
            if(act.charAt(0) == 'N'){
                i--;
                j--;
            }
            else if(act.charAt(0) == 'I'){
                System.out.println(act);
                j--;
            }
            else if(act.charAt(0) == 'D'){
                System.out.println(act);
                i--;
            }
            else if(act.charAt(0) == 'R'){
                System.out.println(act);
                i--;
                j--;
            }
        }
        
    }
}
