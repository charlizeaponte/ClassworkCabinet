/*
 * @charlizeaponte
 * Date: 4/27/24 
 * Assignment: Overlapping Intervals 
 *Running Time:O(n log n)
 */


 import java.util.*;

 class ClosedInterval {
     long start;
     long end;
     
     public ClosedInterval(long start, long end) {
         this.start = start;
         this.end = end;
     }
 }
 
 public class overlappingIntervals {
     
     public static void main(String[] args) {
        
         System.out.println("Enter your test cases: (Copy and Paste into terminal)");
         Scanner scanner = new Scanner(System.in);
         int N = scanner.nextInt();
         List<ClosedInterval> intervals = new ArrayList<>();
         for (int i = 0; i < N; i++) {
            
             long a = scanner.nextLong();
             long b = scanner.nextLong();
           
             intervals.add(new ClosedInterval(a, b));
         }
         
         // Merge sort the intervals O(n log n)
         mergeSort(intervals, 0, intervals.size() - 1);
         
         // Count overlaps O(n log n)
         int overlaps = countOverlaps(intervals);
         
         System.out.println("Results:");
         System.out.println(overlaps);
     }
     
     // Merge sort algorithm
     private static void mergeSort(List<ClosedInterval> intervals, int low, int high) {
         if (low < high) {
             int mid = (low + high) / 2;
             // Recursively sort left half
             mergeSort(intervals, low, mid);
             // Recursively sort right half
             mergeSort(intervals, mid + 1, high);
             // Merge two sorted halves
             merge(intervals, low, mid, high);
         }
     }
     
     // Merge two sorted subarrays
     private static void merge(List<ClosedInterval> intervals, int low, int mid, int high) {
         List<ClosedInterval> merged = new ArrayList<>();
         int i = low;
         int j = mid + 1;
         
         // Merge the two sorted subarrays O(n)
         while (i <= mid && j <= high) {
             if (intervals.get(i).start <= intervals.get(j).start) {
                 merged.add(intervals.get(i++));
             } else {
                 merged.add(intervals.get(j++));
             }
         }
         
         //  elements from left subarray O(n)
         while (i <= mid) {
             merged.add(intervals.get(i++));
         }
         
         //  elements from right subarray  O(n)
         while (j <= high) {
             merged.add(intervals.get(j++));
         }
         
         //  merged elements back to original list O(n)
         for (int k = 0; k < merged.size(); k++) {
             intervals.set(low + k, merged.get(k));
         }
     }
     
     // Count overlapping intervals
     private static int countOverlaps(List<ClosedInterval> intervals) {
         int overlaps = 0;
         TreeSet<ClosedInterval> endPoints = new TreeSet<>((a, b) -> {
             // sort intervals by end points
             if (a.end != b.end) {
                 return Long.compare(a.end, b.end);
             } else {
                 return Long.compare(a.start, b.start);
             }
         });
 
         // Iterate through intervals
         for (ClosedInterval interval : intervals) {
             long start = interval.start;
             long end = interval.end;
 
             // Remove intervals that end before the current interval starts O(log n)
             endPoints.headSet(new ClosedInterval(Long.MIN_VALUE, start), false).clear();
 
             // Count overlaps with remaining intervals O(log n)
             overlaps += endPoints.size();
 
             // Add current interval O(log n)
             endPoints.add(interval);
         }
 
         return overlaps;
     }
 }
 