import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

// Node class for the Huffman Tree
class HuffmanTreeNode implements Comparable<HuffmanTreeNode> {
    int frequency;
    char character;
    HuffmanTreeNode leftChild, rightChild;

    public HuffmanTreeNode(int frequency, char character, HuffmanTreeNode leftChild, HuffmanTreeNode rightChild) {
        this.frequency = frequency;
        this.character = character;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public int compareTo(HuffmanTreeNode other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}

// Class representing a prioritized item for the priority queue
class PriorityQueueItem implements Comparable<PriorityQueueItem> {
    int priority;
    HuffmanTreeNode node;

    public PriorityQueueItem(int priority, HuffmanTreeNode node) {
        this.priority = priority;
        this.node = node;
    }

    @Override
    public int compareTo(PriorityQueueItem other) {
        return Integer.compare(this.priority, other.priority);
    }
}

public class HuffmanEncoding {

    public static void main(String[] args) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("string.txt"));
        String text = fileReader.readLine(); // Read the string file
        fileReader.close();

        // Create frequency map
        Map<Character, Integer> characterFrequencyMap = new HashMap<>();
        text.chars().forEach(ch -> characterFrequencyMap.put((char) ch, characterFrequencyMap.getOrDefault((char) ch, 0) + 1));

        // Initialize priority queue
        Queue<PriorityQueueItem> priorityQueue = new PriorityQueue<>();
        characterFrequencyMap.forEach((ch, freq) -> priorityQueue.add(new PriorityQueueItem(freq, new HuffmanTreeNode(freq, ch, null, null))));

        // Build Huffman Tree
        while (priorityQueue.size() > 1) {
            PriorityQueueItem left = priorityQueue.poll();
            PriorityQueueItem right = priorityQueue.poll();
            HuffmanTreeNode mergedNode = new HuffmanTreeNode(left.priority + right.priority, '\0', left.node, right.node);
            priorityQueue.add(new PriorityQueueItem(mergedNode.frequency, mergedNode));
        }

        // Access root of the tree
        HuffmanTreeNode rootNode = priorityQueue.poll().node;

        // Generate Huffman codes
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateHuffmanCodes(rootNode, "", huffmanCodes);

        // Encode and write to new file
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("encode.txt"));
        StringBuilder encodedText = new StringBuilder();
        text.chars().forEach(ch -> encodedText.append(huffmanCodes.get((char) ch)));
        fileWriter.write(rootNode.frequency + "\n" + encodedText);
        fileWriter.close();
    }

    // Generate Huffman codes recursively
    private static void generateHuffmanCodes(HuffmanTreeNode node, String code, Map<Character, String> codes) {
        if (node != null) {
            if (node.leftChild == null && node.rightChild == null) {
                codes.put(node.character, code);
            } else {
                generateHuffmanCodes(node.leftChild, code + "0", codes);
                generateHuffmanCodes(node.rightChild, code + "1", codes);
            }
        }
    }
}
