import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/***********************************************
 * @file Proj3.java
 * @description This class contains all sorting methods as well as a
    main method to create sorted, shuffled, and reversed lists for testing
 * @author Alex Warren
 * @date November 12, 2025
 ***********************************************/

public class Proj3 {
    // Sorting Method declarations
    // Merge Sort recursively splits list and merges sorted parts
    public static <T extends Comparable> void mergeSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }
    }

    // merge method to compare elements and merge them in sorted order
    public static <T extends Comparable> void merge(ArrayList<T> a, int left, int mid, int right) {
        ArrayList<T> temp = new ArrayList<>();
        int i = left, j = mid + 1;

        // merge two halves into temp list
        while (i <= mid && j <= right) {
            if (a.get(i).compareTo(a.get(j)) <= 0)
                temp.add(a.get(i++));
            else
                temp.add(a.get(j++));
        }

        // copy remaining elements
        while (i <= mid) temp.add(a.get(i++));
        while (j <= right) temp.add(a.get(j++));

        //copy into original array
        for (int k = 0; k < temp.size(); k++) {
            a.set(left + k, temp.get(k));
        }
    }

    // Quick Sort recursively partitions list around a pivot
    public static <T extends Comparable> void quickSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(a, left, right);
            quickSort(a, left, pivotIndex - 1);
            quickSort(a, pivotIndex + 1, right);
        }
    }

    // partition method with median of three pivot selection
    public static <T extends Comparable> int partition (ArrayList<T> a, int left, int right) {
        int mid = (left + right) / 2;

        // find first, middle, and last elements for median of three selection
        T first = a.get(left);
        T middle = a.get(mid);
        T last = a.get(right);

        int pivotIndex;
        if (first.compareTo(middle) < 0) {
            pivotIndex = (middle.compareTo(last) < 0) ? mid : (first.compareTo(last) < 0 ? right : left);
        } else {
            pivotIndex = (first.compareTo(last) < 0) ? left : (middle.compareTo(last) < 0 ? right : mid);
        }

        // Move pivot to the end
        swap(a, pivotIndex, right);
        T pivot = a.get(right);

        // partition
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (a.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(a, i, j);
            }
        }

        swap(a, i + 1, right);
        return i + 1;
    }

    // helper to swap two elements
    static <T> void swap(ArrayList<T> a, int i, int j) {
        T temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    // Heap Sort builds a max heap and repeatedly extracts the max element
    public static <T extends Comparable> void heapSort(ArrayList<T> a, int left, int right) {
        int n = right - left + 1;

        // Build max heap
        for (int i = left + (n / 2) - 1; i >= left; i--) {
            heapify(a, i, right);
        }

        // Extract elements one by one
        for (int i = right; i > left; i--) {
            swap(a, left, i);
            heapify(a, left, i - 1);
        }
    }

    // heapify method ensures heap-order property after each extraction
    public static <T extends Comparable> void heapify (ArrayList<T> a, int left, int right) {
        int largest = left;
        int leftChild = 2 * left + 1;
        int rightChild = 2 * left + 2;

        // Compare left child
        if (leftChild <= right && a.get(leftChild).compareTo(a.get(largest)) > 0) {
            largest = leftChild;
        }

        // Compare right child
        if (rightChild <= right && a.get(rightChild).compareTo(a.get(largest)) > 0) {
            largest = rightChild;
        }

        // Swap and continue heapifying if root not largest
        if (largest != left) {
            swap(a, left, largest);
            heapify(a, largest, right);
        }
    }

    // Bubble Sort repeatedly compares adjacent elements and swaps if necessary
    public static <T extends Comparable> int bubbleSort(ArrayList<T> a, int size) {
        int comparisons = 0;
        boolean swapped;

        //nested for loop to make enough passes to fully sort the list
        for (int i = 0; i < size - 1; i++) {
            swapped = false;
            for (int j = 0; j < size - i - 1; j++) {
                comparisons++;
                if (a.get(j).compareTo(a.get(j + 1)) > 0) {
                    swap(a, j, j + 1);
                    swapped = true;
                }
            }
            // if already sorted, stop early
            if (!swapped) break;
        }

        return comparisons;
    }

    // Odd-Even Transposition Sort alternates between odd and even phases until sorted
    public static <T extends Comparable> int transpositionSort(ArrayList<T> a, int size) {
        if (a == null || size <= 1) return 0;

        boolean swapped = true;
        int comparisons = 0;

        // keep going until no swaps occur in a full pass
        while (swapped) {
            swapped = false;

            // Odd phase
            for (int i = 1; i <= size - 2; i += 2) {
                comparisons++; // count each comparison
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    swapped = true;
                }
            }

            // Even phase
            for (int i = 0; i <= size - 2; i += 2) {
                comparisons++; // count each comparison
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    swapped = true;
                }
            }
        }

        return comparisons;
    }

    public static void main(String [] args)  throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java Proj3 <filename> <numLines>");
            return;
        }

        String filename = args[0];
        int numLines = Integer.parseInt(args[1]);

        // Read file from a scanner
        ArrayList<SP500> data = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filename);
        Scanner scanner = new Scanner(fis);

        // read line by line
        while (scanner.hasNextLine() && data.size() < numLines) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue; // skip blanks

            String[] parts = line.split(",");
            if (parts.length == 2) {
                String symbol = parts[0].trim();
                double price = Double.parseDouble(parts[1].trim());
                data.add(new SP500(symbol, price));
            }
        }

        scanner.close();
        fis.close();

        // troubleshooting
        if (data.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        // create sorted, shuffled, and reversed list
        ArrayList<SP500> sortedList = new ArrayList<>(data);
        Collections.sort(sortedList);

        ArrayList<SP500> shuffledList = new ArrayList<>(sortedList);
        Collections.shuffle(shuffledList);

        ArrayList<SP500> reversedList = new ArrayList<>(sortedList);
        Collections.sort(reversedList, Collections.reverseOrder());

        // Create output files analysis.txt and sorted.txt
        PrintWriter analysisWriter = new PrintWriter(new FileOutputStream("analysis.txt", true));
        PrintWriter sortedWriter = new PrintWriter(new FileOutputStream("sorted.txt", false));

        String[] listTypes = {"Sorted", "Shuffled", "Reversed"};
        ArrayList<ArrayList<SP500>> lists = new ArrayList<>(Arrays.asList(sortedList, shuffledList, reversedList));

        //print headers
        System.out.printf("%-20s %-15s %-20s %-20s %-10s\n",
                "Algorithm", "List Type", "Metric", "Value", "Lines");

        // loop over all list types
        for (int i = 0; i < lists.size(); i++) {
            String type = listTypes[i];
            ArrayList<SP500> base = lists.get(i);

            // run time test
            for (String algo : new String[]{"bubble", "merge", "quick", "heap"}) {
                ArrayList<SP500> temp = new ArrayList<>(base);
                long start = System.nanoTime();

                //
                switch (algo) {
                    case "bubble" -> bubbleSort(temp, temp.size());
                    case "merge" -> mergeSort(temp, 0, temp.size() - 1);
                    case "quick" -> quickSort(temp, 0, temp.size() - 1);
                    case "heap" -> heapSort(temp, 0, temp.size() - 1);
                }

                long end = System.nanoTime();
                long elapsed = end - start;

                System.out.printf("%-20s %-15s %-20s %-20d %-10d\n",
                        algo, type, "Time", elapsed, numLines);
                analysisWriter.printf("%s,%s,%d,Time,%d\n", algo, type, numLines, elapsed);

                //write sorted output
                for (SP500 stock : temp) sortedWriter.println(stock);
            }

            // Print number of comparisons for bubble sort and transposition sort
            ArrayList<SP500> temp1 = new ArrayList<>(base);
            int bubbleComparisons = bubbleSort(temp1, temp1.size());
            System.out.printf("%-20s %-15s %-20s %-20d %-10d\n",
                    "bubble", type, "Comparisons", bubbleComparisons, numLines);
            analysisWriter.printf("bubble,%s,%d,Comparisons,%d\n", type, numLines, bubbleComparisons);

            ArrayList<SP500> temp2 = new ArrayList<>(base);
            int transComp = transpositionSort(temp2, temp2.size());
            System.out.printf("%-20s %-15s %-20s %-20d %-10d\n",
                    "transposition", type, "Comparisons", transComp, numLines);
            analysisWriter.printf("transposition,%s,%d,Comparisons,%d\n", type, numLines, transComp);
        }

        analysisWriter.close();
        sortedWriter.close();
    }

}

