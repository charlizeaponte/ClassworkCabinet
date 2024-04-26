/*
 * author:Charlize Aponte & Anton 
 * Date: 04/17/24
 * Assignment: Decompression challenge 
 */
import java.util.Scanner;

public class Decompression  {

    public static void main(String[] args) {
        System.out.println("Enter the test cases:");
        Scanner scanner = new Scanner(System.in);
        int testCases = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int t = 0; t < testCases; t++) {
            String line = scanner.nextLine();
            long frequency = computeFrequency(line);
            System.out.println(frequency);
        }
    }
    private static long computeFrequency(String line) {
        long frequency = 0;
        long totalLength = 0;
        String[] parts = line.split("\\s+");
    
        for (int i = 0; i < parts.length; i += 2) {
            if (parts[i].isEmpty()) {
                continue; // Skip empty strings
            }
    
            if (parts[i].length() == 1) { // Single character
                totalLength += 1;
            } else { // Decompressed substring
                int repeat = Integer.parseInt(parts[i]);
                String substring = parts[i + 1];
                totalLength += repeat * substring.length();
                i++; // Increment i to skip the decompressed substring
            }
        }
    
        // Find the most frequent character frequency
        long[] charCounts = new long[26]; // Array to store frequency of each letter
        for (int j = 0; j < line.length(); j++) {
            char c = line.charAt(j);
            if (Character.isLetter(c)) {
                charCounts[c - 'A']++;
            }
        }
    
        for (long count : charCounts) {
            frequency = Math.max(frequency, count);
        }
    
        return frequency;
    }
}