import java.util.Arrays;

class LongestSubsequence3 {
    
    public static void main(String[] args) {
        
        String X = args[0].toUpperCase();
        String Y = args[1].toUpperCase();
        String Z = args[2].toUpperCase();

        int[][][] L = new int[X.length()+1][Y.length()+1][Z.length()+1];

        for (int i = 0; i <= X.length(); i++){
            for(int j = 0; j <= Y.length(); j++){
                for(int k = 0; k <= Z.length(); k++){
                    
                    if(i == 0 || j == 0 || k == 0){
                        L[i][j][k] = 0;
                    } else {
                        if(X.charAt(i-1) == Y.charAt(j-1) && Y.charAt(j-1) == Z.charAt(k-1)){
                            L[i][j][k] = L[i-1][j-1][k-1] + 1;
                        }
                        else {
                            L[i][j][k] = Math.max(Math.max(L[i-1][j][k], L[i][j-1][k]), L[i][j][k-1]);
                        }
                    }

                }
            }
        }

        // DEBUG - array view
        // for (int[][] is : L) {
        //     System.out.println(Arrays.deepToString(is));
        // }

        int i = X.length();
        int j = Y.length();
        int k = Z.length();
        System.out.print("Longest Subsequence is " + L[i][j][k] + ": ");
        StringBuilder result = new StringBuilder();
        // rebuild
        while(true){
            if(i == 0 || j == 0 || k == 0){
                break;
            }
            if(X.charAt(i-1) == Y.charAt(j-1) && Y.charAt(j-1) == Z.charAt(k-1)){
                result.append(X.charAt(i-1));
                i--;
                j--;
                k--;
            } else {
                // If Left is biggest, go left
                if(L[i-1][j][k] < L[i][j-1][k] && L[i][j-1][k] > L[i][j][k-1]){
                    j--;
                }
                // If Left (internal) is biggest, go left
                else if (L[i][j][k-1] > L[i][j-1][k] && L[i][j][k-1] > L[i-1][j][k]){
                    k--;
                }
                else {
                // otherwise, go up
                    i--;
                }
            }

        }
        System.out.println(result.reverse().toString());

    }

}
