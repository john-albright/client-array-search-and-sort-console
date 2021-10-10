/**
 * Description: This program determines whether an integer is in an array of integers
 * using binary search.
 * 
 */


import java.util.Arrays;

public class BinarySearchRecursive {

    public static void main(String args[])
    {
        int[] sampleArray = {1, 5, 6, 10, 20, 35, 71, 72};
        int test1 = 2;
        String verdict1 = (binarySearchInt(sampleArray, test1)) ? "Yes" : "No";

        int test2 = 10;
        String verdict2 = (binarySearchInt(sampleArray, test2)) ? "Yes" : "No";

        System.out.printf("Is %d in %s?\t%s\n\n", test1, Arrays.toString(sampleArray), verdict1);
        System.out.printf("Is %d in %s?\t%s\n\n", test2, Arrays.toString(sampleArray), verdict2);
        
    }

    public static boolean binarySearchInt(int[] intArray, int search)
    {
        int length = intArray.length;

        int index = (length - 1) / 2;

        // Attempt at making this function recursive 
        if (length == 0) return false;
        else if (search == intArray[index])
        {
            System.out.println("original array: " + Arrays.toString(intArray));
            int[] intArrayMod = Arrays.copyOfRange(intArray, 0, index);
            System.out.println("new array: " + Arrays.toString(intArrayMod));
            return true;
        }
        else if (search < intArray[index])
        {
            System.out.println("original array: " + Arrays.toString(intArray));
            int[] intArrayMod = Arrays.copyOfRange(intArray, 0, index);
            System.out.println("new array: " + Arrays.toString(intArrayMod));
            return binarySearchInt(intArrayMod, search);
        }
        else
        {
            System.out.println("original array: " + Arrays.toString(intArray));
            int[] intArrayMod = Arrays.copyOfRange(intArray, index + 1, length);
            System.out.println("new array: " + Arrays.toString(intArrayMod));
            return binarySearchInt(intArrayMod, search);
        }
    }
}

