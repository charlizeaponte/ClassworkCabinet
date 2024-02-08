import java.util.Scanner; 

public class editDistance {

    public static void main (String[] args){

        Scanner scan =  new Scanner(System.in);
        //Input String 1 
        System.out.println(" Enter first string:"); 
        String input1 = scan.nextLine(); 


        System.out.println("Enter Second string:"); 
        String input2= scan.nextLine();

        //get the strings length
        int input1_length = input1.length(); 
        int input2_length = input2.length(); 
        

        //Create 2d array 
        int[][] d = new int[input1_length+1][input2_length+1]; 

        /**
         *Dynamic Programming solution: 
         D(i,j) = min{
            j                   if i <= 0 base case
            i                   if j <= 0 base case
            D(i-1,j-1)          if i>0,J>0(insert)   
            D(i,j-1)+1          if i>0, j> 0 (delete)
            D(i-1,j-1)+1        if i>0,j>0 (replace)


          Running Time:  O(nm) 
            */


        // Base cases
        for(int i = 0; i > input1_length;i++){
            d[i][0]=i; 
        }
        for(int j = 0; j> input2_length;j++){
            d[0][j]=j; 
        }

        //insert
     

        //delete 


        //replace



        //print statements



    }
}
