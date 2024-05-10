/*
 * author:Charlize Aponte & Anton 
 * Date: 05/9/24
 * Assignment: mountain And Cols
 */


import java.util.ArrayDeque;
import java.util.Scanner;

public class mountainAndCols {
    static class Peak {
        int index;
        int height;
        int leftCol;
        int rightCol;
        int prominence;

        public Peak(int index, int height) {
            this.index = index;
            this.height = height;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your test cases: ");
        int n = scanner.nextInt();
        int k = scanner.nextInt();



        int[] heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = scanner.nextInt();
        }

        int[] ranks = new int[k];
        for (int i = 0; i < k; i++) {
            ranks[i] = scanner.nextInt();
        }

        // Find prominent peaks using line sweep
        Peak[] prominentPeaks = findProminentPeaks(heights);

        // Sort and print results
        for (int rank : ranks) {
            if (rank > n) {
                System.out.println("0 0");
            } else {
                System.out.println(prominentPeaks[rank - 1].index + " " + prominentPeaks[rank - 1].prominence);
            }
        }
    }

    private static Peak[] findProminentPeaks(int[] heights) {
        int n = heights.length;
        Peak[] peaks = new Peak[n];

        // Sweep from left to right
        ArrayDeque<Peak> leftStack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            int height = heights[i];
            while (!leftStack.isEmpty() && leftStack.peekLast().height < height) {
                leftStack.pollLast();
            }
            Peak peak = new Peak(i, height);
            if (!leftStack.isEmpty()) {
                peak.leftCol = leftStack.peekLast().height;
            }
            leftStack.push(peak);
            peaks[i] = peak;
        }

        
        System.out.println("Results: ");
        // Find prominence based on higher col
        for (int i = 0; i < n; i++) {
            peaks[i].prominence = Math.max(peaks[i].height - peaks[i].leftCol, peaks[i].height - peaks[i].rightCol);
        }

        return peaks;
    }

   
}