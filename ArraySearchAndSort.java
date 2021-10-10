/**
 * 
 * Java Data Structures Lab 04
 * Description: The program populates two parallel arrays 
 * with client names and respective client numbers. The program
 * begins by prompting the user to pick one of three pathways:
 * S for search, M for modify, and B for binary search. Within 
 * binary search, the user can choose how to sort the arrays:
 * A for java's Array.sort() method, B for bubble sort, or I for
 * insertion sort. The user can choose to search or modify 
 * client names or client numbers within each pathway. 
 * 
 * 
 * Programmer: John Albright
 * Date Created: February 20, 2021
 * 
 */

import java.util.Scanner;
import java.util.Date;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.lang.Math;

public class ArraySearch2 
{
    // Declare a new scanner to be used by various functions
    static Scanner sc = new Scanner(System.in);

    public static void main(String args[]) 
    {
        // Declare an array of strings
        String[] myClients = new String[10];

        // Populate the array of strings with client names
        myClients[0] = "Butler";
        myClients[1] = "Samuels";
        myClients[2] = "Bond";
        myClients[3] = "Chang";
        myClients[4] = "Baker";
        myClients[5] = "Davis";
        myClients[6] = "Zheng";
        myClients[7] = "Joe";

        // Declare an array of integers
        int[] myClientNumbers = new int[10];

        // Populate the array of integers with client numbers
        myClientNumbers[0] = 108;
        myClientNumbers[1] = 121;
        myClientNumbers[2] = 188;
        myClientNumbers[3] = 107;
        myClientNumbers[4] = 122;
        myClientNumbers[5] = 111;
        myClientNumbers[6] = 203;
        myClientNumbers[7] = 135;

        // Declare new arrays to be sorted in the binary search pathway ('B')
        int[] myClientNumbersSorted = Arrays.copyOfRange(myClientNumbers, 0, 8);
        String[] myClientsSorted = Arrays.copyOfRange(myClients, 0, 8);
        
        // Prompt user to choose a path: search or alter
        System.out.print("\nWhat would you like to do today? " +
                            "\nEnter S to search the clients. " +
                            "\nEnter B to perform a binary search. " +
                            "\nEnter M to modify a client name or number: ");
        
        // Store the string or character written
        String choice = sc.next().toUpperCase();
        // Extract the first letter of the string
        char choiceConv = choice.charAt(0);

        // Create four pathways for the user: 
        // search, modify, binary search or exit
        // SEARCH PATHWAY
        if (choiceConv == 'S')
        {
            System.out.print("\nHow would you like to search? ");
            // Call method to get name or number from the user
            String firstTwoCharS = nameOrNumber("search for");

            // Search using the linear search
            if (firstTwoCharS.equals("nu") || firstTwoCharS.equals("no"))
            {
                clientNumberSearch(myClients, myClientNumbers, 'L');
            }
            else if (firstTwoCharS.equals("na"))
            {
                clientNameSearch(myClients, myClientNumbers, 'L');
            }
            else
            {
                System.out.println("Not a valid command.\n");
                System.exit(2);
            }
        } 
        // MODIFY PATHWAY
        else if (choiceConv == 'M') 
        {
            // Print out all the values of both arrays
            printArrays(myClients, myClientNumbers);

            // Call method to get name or number from the user
            String firstTwoCharM = nameOrNumber("modify");

            // Create three pathways: number, name, exit
            if (firstTwoCharM.equals("nu") || firstTwoCharM.equals("no"))
            {
                clientNumberModify(myClients, myClientNumbers);
            }
            else if (firstTwoCharM.equals("na"))
            {
                clientNameModify(myClients, myClientNumbers);
            }
            else
            {
                System.out.println("Not a valid command.\n");
                System.exit(3);
            }
            
            // Print out arrays after changes made 
            printArrays(myClients, myClientNumbers);
        } 
        // BINARY SEARCH PATHWAY
        else if (choiceConv == 'B')
        {
            System.out.print("\nThe arrays must be sorted to utilize binary search.\n" +
                               "How would you like to sort the arrays?\n" + 
                               "Enter A for java's built-in array sort.\n" +
                               "Enter B for bubble sort.\n" +
                               "Enter I for insertion sort: ");

            char sortChoice = sc.next().charAt(0);

            if (sortChoice == 'A')
            {
                Arrays.sort(myClientNumbersSorted);
                Arrays.sort(myClientsSorted);
            }
            // While bubble sort is in action, print the array
            else if (sortChoice == 'B')
            {
                bubbleSortInt(myClientNumbersSorted);
                bubbleSortStr(myClientsSorted);
            }
            else if (sortChoice == 'I')
            {
                insertionSortInt(myClientNumbersSorted);
                insertionSortStr(myClientsSorted);
            }
            else
            {
                System.out.println("Not a valid sort type.\n");
                System.exit(7);
            }

            // Call method to get name or number from the user
            String firstTwoCharB = nameOrNumber("search for");

            // Search using binary search and print out the sorted array
            if (firstTwoCharB.equals("na"))
            {
                clientNameSearch(myClientsSorted, myClientNumbersSorted, 'B');
                printArrays(myClientsSorted, myClientNumbersSorted);
            }
            else if (firstTwoCharB.equals("no") || firstTwoCharB.equals("nu"))
            {
                clientNumberSearch(myClientsSorted, myClientNumbersSorted, 'B');
                printArrays(myClientsSorted, myClientNumbersSorted);
            }    
        }
        else 
        {
            System.out.println("Not a valid command.\n");
            System.exit(4);
        }

        // Close the scanner sc
        sc.close();

        // Declare a date object to print out the current date
        Date myDate = new Date();
        String myDateFormat = "MM/dd/yyy";
        SimpleDateFormat dtToday = new SimpleDateFormat(myDateFormat);
        System.out.println("-------------------------");
        System.out.printf("Today's date: %s\n", dtToday.format(myDate));
        System.out.printf("Programmer: John Albright\n\n");

    }
    
    /**
     * Searches for a string in an array of 
     * strings with a parallel array of integers of the 
     * same length. Prints out various statements to the
     * console. If the third parameter is 'B', the 
     * search is done via binary search. If the third
     * parameter is 'L', the search is done via linear
     * search. This method calls one of two other
     * functions: linSearchString or binarySearchString.
     * @param array array of strings
     * @param array array of integers of the same length
     * @param char char to select which type of search
     * 
     */
    static public void clientNameSearch(String myClients[], int myClientNumbers[], char searchType)
    {
        int arrayPosClientNo = -1;
        
        // Prompt the user to type the name of a client
        System.out.print("Please enter a client's name: ");

        // Scan the client name entered and store it in a variable
        String clientToLookFor = sc.next();

        // Call the linear or binary search int methods 
        if (searchType == 'B')
        {
            arrayPosClientNo = binarySearchStr(myClients, clientToLookFor);
        }
        else if (searchType == 'L')
        {
            arrayPosClientNo = linSearchString(myClients, clientToLookFor);
        }
        else
        {
            System.out.println("Invalid argument.");
        }
        // Print out the position of the client in the list if it was found
        if (arrayPosClientNo > 0)
        System.out.println("Client found at position " + (arrayPosClientNo + 1) + " on the roster.");

        // Try to find the associated client number
        try {
            int associatedClientNo = myClientNumbers[arrayPosClientNo];
            System.out.printf("%s's client number is %d.\n\n", clientToLookFor, associatedClientNo);
        } catch (ArrayIndexOutOfBoundsException ex1) {
            System.out.println("No associated client number found.\n");
        }
    }


    /**
     * Searches for an integer in an array of 
     * integers with a parallel array of strings of the 
     * same length. Prints out various statements to the
     * console. If the third parameter is 'B', the 
     * search is done via binary search. If the third
     * parameter is 'L', the search is done via linear
     * search. This method calls one of two other
     * functions: linSearchInt or binarySearchInt.
     * @param array array of strings
     * @param array array of integers of the same length
     * @param char char to select which type of search
     * 
     */
    public static void clientNumberSearch(String myClients[], int myClientNumbers[], char searchType)
    {
        // Declare variables to be used in this method
        boolean inputCheck = false;
        int clientNumberToLookFor = -1;
        int arrayPosClientName = -1;

        // Try to scan and store an integer value 
        do {
            try {
                System.out.print("Please enter a client's number: ");
                clientNumberToLookFor = sc.nextInt();
                inputCheck = true;
            } catch (InputMismatchException ex2) {
                sc.next();
                System.out.println("Please enter a valid integer!");
            }
        } while (!inputCheck);

        // Call the linear or binary search int methods 
        if (searchType == 'B')
        {
            arrayPosClientName = binarySearchInt(myClientNumbers, clientNumberToLookFor);
        }
        else if (searchType == 'L')
        {
            arrayPosClientName = linSearchInt(myClientNumbers, clientNumberToLookFor);
        }
        else
        {
            System.out.println("Invalid argument.");
        }
        
        // Print out the position of the client in the list if found
        if (arrayPosClientName > 0)
        System.out.println("Client number found at position " + (arrayPosClientName + 1) + " on the roster.");
        
        try {
            String associatedClientName = myClients[arrayPosClientName];
            System.out.printf("Client %d is %s.\n\n", 
                              clientNumberToLookFor, associatedClientName);
        } catch (ArrayIndexOutOfBoundsException ex3) {
            System.out.println("No associated client name found.\n");
        }
    }

    /**
     * Modifies an integer in an array of integers using
     * an array of strings with the same length. 
     * @param array array of strings
     * @param array array of integers of the same length
     * 
     */
    public static void clientNumberModify(String myClients[], int myClientNumbers[])
    {
        // Declare variables to be used in the following try-catch statement 
        boolean inputCheck = false;
        int clientNumberToLookFor = -1;

        // Try to scan and store an integer value 
        do {
            try {
                System.out.print("Please enter a client's number: ");
                clientNumberToLookFor = sc.nextInt();
                inputCheck = true;
            } catch (InputMismatchException ex2) {
                sc.next();
                System.out.println("Please enter a valid integer!");
            }
        } while (!inputCheck);
        

        // Invoke the linSearchInt method to find the position 
        // of the client number in the array using linear search
        int arrayPosClientName = linSearchInt(myClientNumbers, clientNumberToLookFor);

        String associatedClientName = "";

        try {
            associatedClientName = myClients[arrayPosClientName];
        } catch (ArrayIndexOutOfBoundsException ex3) {
            System.out.println("No associated client name found.\n");
        }
        
        // Declare variables to be used in the next try-catch statement 
        int newClientNumber = -1;
        boolean inputCheck2 = false;
        
        // Prompt the user to enter in a new client number
        do {
            try {
                System.out.printf("Enter in the new client number to replace %d: ", clientNumberToLookFor);
                newClientNumber = sc.nextInt();
                inputCheck2 = true;
            } catch (InputMismatchException ex2) {
                sc.next();
                System.out.println("Please enter a valid integer!");
            }
        } while (!inputCheck2);
        
        // Update the value in the myClientNumbers array
        myClientNumbers[arrayPosClientName] = newClientNumber;
        // Print out the user the change made
        System.out.printf("%s's client number was changed from %d to %d.\n\n", 
                          associatedClientName, clientNumberToLookFor, newClientNumber);

    }

    /**
     * Modifies a string in an array of strings using
     * an array of integers with the same length. 
     * @param array array of strings
     * @param array array of integers of the same length
     * 
     */
    public static void clientNameModify(String myClients[], int myClientNumbers[])
    {
        // Prompt the user to type the name of a client
        System.out.print("Please enter a client's name: ");

        // Scan the client name entered and store it in a variable
        String clientToLookFor = sc.next();

        // Find the position of the client in the array using linear or sequential search
        int arrayPosClientNo = linSearchString(myClients, clientToLookFor);

        int associatedClientNo = -1;

        // Try to find the associated client number
        try {
            associatedClientNo = myClientNumbers[arrayPosClientNo];
        } catch (ArrayIndexOutOfBoundsException ex1) {
            System.out.println("No associated client number found.\n");
        }

        // Prompt the user to enter in a new client name
        System.out.printf("Enter in the new client name to replace %s: ", clientToLookFor);
        String newClientName = sc.next();

        // Update the value in the myClients array
        myClients[arrayPosClientNo] = newClientName;
        System.out.printf("Client %d's name was changed from %s to %s\n\n", 
                          associatedClientNo, clientToLookFor, newClientName);


    }

    /**
     * Performs linear search on an array of strings. 
     * @param array array of strings to be searched through
     * @param String string to be searched for
     * @return the index of the string if exists in array or
     *         -1 if the string is not a member of the array.
     * 
     */
    public static int linSearchString(String strArray[], String search)
    {
        int i = 0;
        int flag = -1;
        for(i = 0; i < strArray.length; i++)
        {
            /* Use the equals method of string objects to compare
            the strings of the array with the string "search" */
            if (search.equals(strArray[i]))
            {
                flag = 1;
                break;
            }
        }
        if (flag == 1)
        {
            return i;
        }
        else
        {
            return flag;
        }
    }

    /**
     * Performs linear search on an array of integers
     * @param array array of integers to be searched through
     * @param int int to be searched for
     * @return the index of the integer if exists in array or
     *         -1 if the string is not a member of the array.
     * 
     */
    public static int linSearchInt(int intArray[], int search)
    {
        int flag = -1;
        for(int i = 0; i < intArray.length; i++)
        {
            if(search == intArray[i])
            {
                flag = 1;
                return i;
            }
        }
        return flag;
    }

    /**
     * Performs binary search on an array of integers
     * Precondition: searched integer must be in the array
     * @param array array of integers to be searched through
     * @param int int to be searched for
     * @return the index of the integer if exists in array or
     *         -1 if the string is not a member of the array.
     * 
     */
    public static int binarySearchInt(int intArray[], int search)
    {
        int length = intArray.length;
        int upper = length - 1;
        int lower = 0;
        int half = ((upper + lower) / 2);
        int index = half;
        int checker = 0;

        // Check to see that the precondition is met using linear search
        if (linSearchInt(intArray, search) == -1) return -1;

        while (index >= 0)
        {
            checker = index;
            if (search == intArray[index])
            {
                return index;
            }
            else if (search < intArray[index])
            {
                upper = index;
                index = (int)Math.floor((upper + lower) / 2);
            }
            else if (search > intArray[index])
            {
                lower = index;
                index = (int)Math.ceil((upper + lower) / 2);
                //if (index == length - 3) index = length - 1;
                //else index = (upper + lower - 1) / 2;
            }
            //index = (upper + lower) / 2;
            System.out.println(upper + ", " + lower + ", " + index);
            if (index == checker)
            {
                break;
            }
        }
        return -1;
    }

    /**
     * Performs binary search on an array of strings
     * Precondition: searched string must be in the array
     * Limitation: only sorts up to the second letter in 
     *             a word
     * @param array array of strings to be searched through
     * @param int string to be searched for
     * @return the index of the string if exists in array or
     *         -1 if the string is not a member of the array.
     * 
     */
    public static int binarySearchStr(String strArray[], String search)
    {
        int length = strArray.length;
        int index = ((length - 1) / 2);
        int upper = length - 1;
        int lower = 0;
        
        // Check to see that the precondition is met
        if (!Arrays.asList(strArray).contains(search)) return -1;

        while (index >= 0)
        {
            if (search.equals(strArray[index]))
            {
                return index;
            }
            else if (search.toLowerCase().charAt(0) < strArray[index].toLowerCase().charAt(0))
            {
                upper = index;
                index = (upper + lower) / 2;
            }
            else if (search.toLowerCase().charAt(0) > strArray[index].toLowerCase().charAt(0))
            {
                lower = index;
                if (index == length - 2) index = length - 1;
                else index = (upper + lower) / 2;
            }
            else if (search.toLowerCase().charAt(0) == strArray[index].toLowerCase().charAt(0) 
                     && search.toLowerCase().charAt(1) != strArray[index].toLowerCase().charAt(1))
            {
                if (search.toLowerCase().charAt(1) > strArray[index].toLowerCase().charAt(1))
                {
                    lower = index;
                }
                else upper = index;
                index = (upper + lower) / 2;
            }
        }
        return -1;
    }

    /**
     * Performs a bubble sort on an array of integers
     * @param array array of integers to be sorted
     * @return the same array but sorted in order from
     *         lowest to highest integer
     */
    public static int[] bubbleSortInt(int[] intArray) 
    {
        int length = intArray.length;

        for (int i = 0; i < length - 1; i++)
        {
            int equalityCounter = 0;
            for (int j = 0; j < length - 1; j++)
            {
                /* Swap the integers if the first one is 
                greater than the second */
                if (intArray[j] > intArray[j+1])
                {
                    int tempInt = intArray[j+1];
                    intArray[j+1] = intArray[j];
                    intArray[j] = tempInt;
                }
                else 
                {
                    equalityCounter++;
                }
            }

            // Print out the arrays at each step 
            // Show the sorting in action
            System.out.println(Arrays.toString(intArray));

            if (equalityCounter == length - 1)
            {
                break;
            }
        }
        return intArray;
    }

    /**
     * Performs a bubble sort on an array of strings
     * Can sort a list completely alphabetically as 
     * it works past the first letter to the end of 
     * each word
     * @param array array of strings to be sorted
     * @return the same array but sorted alphabetically
     */
    public static String[] bubbleSortStr(String[] strArray) 
    {
        int length = strArray.length;

        for (int i = 0; i < length - 1; i++)
        {
            int equalityCounter = 0;
            for (int j = 0; j < length - 1; j++)
            {
                // Swap the strings if the first one starts with a letter
                if (strArray[j].charAt(0) > strArray[j+1].charAt(0))
                {
                    String tempStr = strArray[j+1];
                    strArray[j+1] = strArray[j];
                    strArray[j] = tempStr;
                }
                else if (strArray[j].charAt(0) == strArray[j].charAt(0))
                {
                    int jLength = strArray[j].length();
                    int nextLength = strArray[j+1].length();
                    
                    int minLength = jLength > nextLength ? nextLength : jLength;
                    for (int z = 1; z < minLength; z++)
                    {
                        if (strArray[j].charAt(z) > strArray[j+1].charAt(z))
                        {
                            String tempStr = strArray[j+1];
                            strArray[j+1] = strArray[j];
                            strArray[j] = tempStr;
                        }
                        else if (strArray[j].charAt(z) == strArray[j+1].charAt(z))
                        {
                            continue;
                        }
                        else if (strArray[j].charAt(z) < strArray[j+1].charAt(z))
                        {
                            break;
                        }
                    }
                }
                else 
                {
                    equalityCounter++;
                }
            }

            // Print out the arrays at each step 
            // Show the sorting in action
            System.out.println(Arrays.toString(strArray));

            if (equalityCounter == length - 1)
            {
                break;
            }
        }
        return strArray;
    }


    /**
     * Determines whether a user wants to ______ a
     * client name or client number. The main method
     * uses the verbs "modify" and "search for."
     * @param String string of variable length
     * @return a string of two characters:
     *         either "na", "no", or "nu"
     * 
     */ 
    public static String nameOrNumber(String verb)
    {
        System.out.print("\nEnter na to " + verb + " a client name. " +
                         "\nEnter no or nu to " + verb + " a client number: ");

        // Store the string after converting it to lower case letters
        // Then, store the first two letters of that string
        String searchChoice = sc.next().toLowerCase();
        String firstTwoChar = "";

        try {
            firstTwoChar = searchChoice.substring(0,2);
        } catch (StringIndexOutOfBoundsException o) {
            System.out.println("Not a valid command.\n");
            System.exit(1);
        }
        return firstTwoChar;

    }

    /**
     * Prints all the values of an array of strings and numbers 
     * of equal length 
     * @param array array of strings
     * @param array array of integers
     * 
     */ 
    public static void printArrays(String strArray[], int intArray[])
    {
        // Print the header 
        System.out.println("Name\tNumber\n---------------");
        
        // Print all the terms of each array 
        for (int i =0; i < strArray.length && strArray[i] != null; i++)
        {
            System.out.printf("%s\t%d\n", strArray[i], intArray[i]);
        }

        // Print an extra line
        System.out.println();
    }
    
    /**
     * Performs an insertion sort on an array of integers
     * @param array array of integers to be sorted
     * @return the same array but sorted in order from
     *         lowest to highest integer
     */
    public static int[] insertionSortInt(int[] intArray)
    {
        int length = intArray.length;

        for (int j = 0; j < length - 1; j++)
        {
            if (intArray[j] > intArray[j+1])
            {
                if (j == 0 || intArray[j+1] > intArray[j-1])
                {   
                    int temp = intArray[j];
                    intArray[j] = intArray[j+1];
                    intArray[j+1] = temp;
                }
                else
                {
                    int tracker = j + 1;
                    int movingComp = j - 1;

                    while (movingComp > 0)
                    {
                        if (intArray[tracker] > intArray[movingComp]) break;
                        movingComp--;
                    }

                    int tempInt;
                    if (movingComp != 0 && intArray[tracker] >= intArray[movingComp])
                    {
                        tempInt = intArray[movingComp+1];
                        intArray[movingComp+1] = intArray[tracker];
                    }
                    else
                    {
                        tempInt = intArray[movingComp];
                        intArray[movingComp] = intArray[tracker];
                    }
                    while (tracker > movingComp)
                    {
                        if (tracker == movingComp+1)
                        {
                            if (intArray[tracker] < tempInt)
                            {
                                intArray[tracker+1] = tempInt;
                            }
                            else 
                            {
                                intArray[tracker] = tempInt;
                            }
                        } 
                        else
                        {
                            intArray[tracker] = intArray[tracker-1];
                        }
                        tracker--;
                    }
                }
            }
            // Print out the arrays at each step 
            // Show the sorting in action
            System.out.println(Arrays.toString(intArray));
        }
        return intArray;
    }    

    /**
    * Performs an insertion sort on an array of strings
    * Note: the method only takes into account the first
    * letter of each string (not the other letters).
    * @param array array of strings to be sorted
    * @return the same array but sorted alphabetically
    */ 
    public static String[] insertionSortStr(String[] strArray)
    {
        int length = strArray.length;

        for (int j = 0; j < length - 1; j++)
        {
            if (strArray[j].charAt(0) > strArray[j+1].charAt(0))
            {
                if (j == 0 || strArray[j+1].charAt(0) > strArray[j-1].charAt(0))
                {   
                    String temp = strArray[j];
                    strArray[j] = strArray[j+1];
                    strArray[j+1] = temp;
                }
                else
                {
                    int tracker = j + 1;
                    int movingComp = j - 1;

                    while (movingComp > 0)
                    {
                        if (strArray[tracker].charAt(0) > strArray[movingComp].charAt(0)) break;
                        movingComp--;
                    }

                    String tempStr;
                    if (movingComp != 0 && strArray[tracker].charAt(0) >= strArray[movingComp].charAt(0))
                    {
                        tempStr = strArray[movingComp+1];
                        strArray[movingComp+1] = strArray[tracker];
                    }
                    else
                    {
                        tempStr = strArray[movingComp];
                        strArray[movingComp] = strArray[tracker];
                    }
                    while (tracker > movingComp)
                    {
                        if (tracker == movingComp+1)
                        {
                            if (strArray[tracker].charAt(0) < tempStr.charAt(0))
                            {
                                strArray[tracker+1] = tempStr;
                            }
                            else 
                            {
                                strArray[tracker] = tempStr;
                            }
                        } 
                        else
                        {
                            strArray[tracker] = strArray[tracker-1];
                        }
                        tracker--;
                    }
                }
            }
            // Print out the arrays at each step 
            // Show the sorting in action
            System.out.println(Arrays.toString(strArray));
        }
        return strArray;
    }    

}