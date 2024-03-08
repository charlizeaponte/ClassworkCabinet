/*
 * Charlize Aponte and Anton 
 * Date: 3/7/24
 * Assignment: lws compression and decompression with DNA sequences
 */

import java.util.HashMap;
import java.util.Scanner;

public class lwz{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the letter 'C' for compression or 'D' for decompression:");
        String choice = scan.nextLine().toUpperCase();
       
         // Check user choice
        if (choice.equals("C")) {
            System.out.println("Enter the DNA sequence (string of A, C, G, T):");
            String inputString = scan.nextLine();
            String compressed = compress(inputString);
            System.out.println("Compressed sequence: " + compressed);
        } else if (choice.equals("D")) {
            System.out.println("Enter the compressed sequence (string of 0, 1, 2, 3 with spaces):");
            String inputString = scan.nextLine();
            String decompressed = decompress(inputString);
            System.out.println("Decompressed sequence: " + decompressed);
        } else {
            System.out.println("Invalid choice. Please enter 'C' for compression or 'D' for decompression.");
        }
        scan.close();
    }

     // Compression function
    public static String compress(String uncompressed) {
        HashMap<String, Integer> dictionary = new HashMap<>();
        dictionary.put("A", 0);
        dictionary.put("C", 1);
        dictionary.put("G", 2);
        dictionary.put("T", 3);

        String compressed = "";
        String current = "";
        int index = 4;

        // Compress the input string
        for (char c : uncompressed.toCharArray()) {
            String next = current + c;
            if (dictionary.containsKey(next)) {
                current = next;
            } else {
                compressed += dictionary.get(current) + " ";
                dictionary.put(next, index++);
                current = "" + c;
            }
        }
        if (!current.equals("")) {
            compressed += dictionary.get(current);
        }

        return compressed;
    }
    // Decompression function
    public static String decompress(String compressed) {
        HashMap<Integer, String> dictionary = new HashMap<>();
        dictionary.put(0, "A");
        dictionary.put(1, "C");
        dictionary.put(2, "G");
        dictionary.put(3, "T");

        String[] parts = compressed.split(" ");
        int index = 4;

        String decompressed = "";
        String previous = dictionary.get(Integer.parseInt(parts[0]));
        decompressed += previous;

        // Decompress the input string
        for (int i = 1; i < parts.length; i++) {
            String current;
            int currentIndex = Integer.parseInt(parts[i]);
            if (dictionary.containsKey(currentIndex)) {
                current = dictionary.get(currentIndex);
            } else if (currentIndex == index) {
                current = previous + previous.charAt(0);
            } else {
                return "Invalid sequence";
            }
            decompressed += current;
            dictionary.put(index++, previous + current.charAt(0));
            previous = current;
        }

        return decompressed;
    }


}
