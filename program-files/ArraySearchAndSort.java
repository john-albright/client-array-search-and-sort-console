/**
 * 
 * Array Search and Sort Console Program
 *
 * Originally to fulfill the Java Data Structures Lab 04 requirement of CSC 241 at Oakton Community College
 *
 * Description: The program populates two parallel arrays 
 * with client names and respective client numbers from a database. This
 * array is used in conjunction with the database to perform CRUD operations
 * in addition to search and sort algorithms.
 *
 * The pathways of the program are:
 * - add (A): user can add a client to the database
 * - binary search (B): user can perform a binary search using the roster
 * - modify (M): user can modify a client name or number
 * - print (P): user can print out the entire contents of the clients table in the clients.db
 * - remove (R): user can remove a client name or number
 * - search (S): user can search for a client name or number
 * - exit (X): user can exit the program
 * 
 * If B is selected, the user can choose between one of three sorts:
 * - A (the Arrays.sort() method provided by Java)
 * - B (bubble sort)
 * - I (insertion sort)
 *
 * Programmer: John Albright
 * Date Created: February 20, 2021
 * Last Date Modified: October 10, 2021
 * 
 */

import java.util.Scanner;
import java.util.Date;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.Arrays;
import java.lang.Math;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ArraySearchAndSort {
    // Declare a new scanner to be used by various functions
    static Scanner sc = new Scanner(System.in);

    // Declare connection (to be made to database)
    static Connection connection = null;
    
    // Declare static arrays to store original values
    static String[] myClientsOriginal = new String[100];
    static int[] myClientNumbersOriginal = new int[100];

    // Declare static arrays to be mutated
    static String[] myClients = new String[100];
    static int[] myClientNumbers = new int[100];

    // Declare static arrays to be mutated 
    static int[] myClientNumbersCopy = new int[100];
    static String[] myClientsCopy = new String[100];

    // Keep track of pathways accessed
    static String pathwaysAccessed = "";

    public static void main(String args[]) {

        try {
            // Establish connection and initialize statement
            String databasePath = "jdbc:sqlite:clients.db";
            connection = DriverManager.getConnection(databasePath);
            
            // Get the original list of records
            Statement countStatement = connection.createStatement();
            ResultSet rsCount = countStatement.executeQuery("SELECT COUNT(*) FROM clients");
            rsCount.next();
            int recordsCount = rsCount.getInt(1);
            
            myClientNumbersOriginal = new int[recordsCount];
            myClientsOriginal = new String[recordsCount];

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM clients");
            int index = 0;

            while (rs.next()) {
                myClientsOriginal[index] = rs.getString("client_name");
                myClientNumbersOriginal[index] = Integer.parseInt(rs.getString("client_number"));
                ++index;
            }

            // Set the length of the arrays to be populated
            myClientNumbersCopy = new int[recordsCount];
            myClientsCopy = new String[recordsCount];

            // Populate the arrays with the values in the database
            populateArrays(myClients, myClientNumbers);

            // Declare flags or sentinel values
            boolean stopProgram = false; 
            boolean validYN = false;
            boolean validNaNo = false;
            boolean validSort = false;
            boolean validNumber = false; 
            
            // Declare all other variables
            char YesNoCheck, sortChoice, choiceConv;
            String firstTwoCharB, firstTwoCharM, firstTwoCharS, choice, newClientName, 
                   SQLStatement;
            int newClientNumber = 0;
        
            do {

                // Prompt user to choose a path: search or alter
                System.out.print("\nWhat would you like to do today? " +
                                 "\nEnter A to add a client to the roster." + 
                                 "\nEnter B to perform a binary search. " +
                                 "\nEnter M to modify a client name or number." +
                                 "\nEnter P to print the client roster." +
                                 "\nEnter R to remove a client from the roster. " +
                                 "\nEnter S to search the clients. " +
                                 "\nEnter X to exit the application. -->  ");
                
                // Store the string or character written
                choice = sc.next().toUpperCase();
                // Extract the first letter of the string
                choiceConv = choice.charAt(0);

                // Create six pathways for the user: 
                // add, binary search, print, modify, search, or exit

                // ADD PATHWAY
                if (choiceConv == 'A') {
                    System.out.print("\nAre you sure you would like to add a client to the database? ");

                    validYN = false;

                    do {
                        YesNoCheck = sc.next().toUpperCase().charAt(0);

                        if (YesNoCheck == 'Y') {
                            validYN = true;
                        } else if (YesNoCheck == 'N') {
                            validYN = true;
                        } else {
                            System.out.print("Please enter Y or N --> ");
                        }
                    } while (!validYN);

                    if (YesNoCheck == 'Y') {
                        pathwaysAccessed += "A ";
                        System.out.print("What is the last name of the client? ");
                        newClientName = sc.next();
                        
                        validNumber = false;

                        System.out.print("What is the client's number? ");

                        while (!validNumber) {

                            try {
                                newClientNumber = sc.nextInt();

                                if (newClientNumber == 0) {
                                    throw new Exception();
                                }

                                for (int clientNumber : myClientNumbers) {
                                    if (clientNumber == newClientNumber) {
                                        throw new Exception();
                                    }
                                }

                                validNumber = true;
                                
                            } catch (NumberFormatException e) {
                                System.out.print("Please enter a number --> ");
                            } catch (Exception e) {
                                System.out.print("Please enter a number other than 0 or one that's already in the database --> ");
                            }
                        }

                        // Initliaze SQL statement
                        Statement insertStatement = connection.createStatement();

                        // Create SQL query statement
                        SQLStatement = "INSERT INTO clients VALUES (" + 
                                       newClientNumber + ", \"" + newClientName + "\")";
                        
                        // Execute SQL query
                        insertStatement.executeUpdate(SQLStatement);
                        insertStatement.setQueryTimeout(30);

                        System.out.println("\n >> Client with name " + newClientName + " and number " +
                                            newClientNumber +" was added to the database.");

                        // Repopulate array
                        populateArrays(myClients, myClientNumbers);
                    }

                }
                // BINARY SEARCH PATHWAY
                else if (choiceConv == 'B') {
                    pathwaysAccessed += "B ";
                    
                    System.out.print("\nThe arrays must be sorted to utilize binary search.\n\n" +
                                    "How would you like to sort the arrays?\n" + 
                                    "Enter A for java's built-in array sort.\n" +
                                    "Enter B for bubble sort.\n" +
                                    "Enter I for insertion sort. --> ");

                    setArraySize();

                    // Populate new arrays to be sorted
                    populateArrays(myClientsCopy, myClientNumbersCopy);
                    // Set sentinel value for sort type
                    validSort = false;

                    do {
                        sortChoice = sc.next().charAt(0);

                        if (sortChoice == 'A') {
                            validSort = true;
                            Arrays.sort(myClientNumbersCopy);
                           Arrays.sort(myClientsCopy);
                        } else if (sortChoice == 'B') {
                            validSort = true;
                            bubbleSortInt(myClientNumbersCopy);
                            bubbleSortStr(myClientsCopy);
                        } else if (sortChoice == 'I') {
                            validSort = true;
                            insertionSortInt(myClientNumbersCopy);
                            insertionSortStr(myClientsCopy);
                        } else {
                            System.out.print("\nNot a valid sort type. ");
                            System.out.print("Enter a valid sort type (A, B, I) --> ");
                        }
                    } while (!validSort);

                    // Set sentinel value for na, no, or nu
                    validNaNo = false; 

                    while(!validNaNo) {
                        // Call method to get name or number from the user
                        firstTwoCharB = nameOrNumber("search for");

                        // Search using binary search and print out the sorted array
                        if (firstTwoCharB.equalsIgnoreCase("na")) {
                            validNaNo = true;
                            clientNameSearch(myClientsCopy, myClientNumbersCopy, 'B');
                            printArrays(myClientsCopy, myClientNumbersCopy);
                       }
                        else if (firstTwoCharB.equalsIgnoreCase("no") || firstTwoCharB.equalsIgnoreCase("nu")) {
                            validNaNo = true;
                            clientNumberSearch(myClientsCopy, myClientNumbersCopy, 'B');
                            printArrays(myClientsCopy, myClientNumbersCopy);
                       } else {
                            validNaNo = false;
                            System.out.print("\nPlease enter a valid value.");
                        }
                    }
                }
                // MODIFY PATHWAY
                else if (choiceConv == 'M') {
                    // Call method to get name or number from the user
                    firstTwoCharM = nameOrNumber("modify");

                    // Create three pathways: number, name, exit
                    if (firstTwoCharM.equals("nu") || firstTwoCharM.equals("no")) {
                        clientNumberModify(myClients, myClientNumbers);
                        pathwaysAccessed += "M ";
                    } else if (firstTwoCharM.equals("na")) {
                        clientNameModify(myClients, myClientNumbers);
                        pathwaysAccessed += "M ";
                    } else {
                        System.out.println("\nNot a valid command. Exiting...");
                    }

                    // Repopulate array to reflect modifications
                    populateArrays(myClients, myClientNumbers);
                } 
                // PRINT CLIENTS PATHWAY
                else if (choiceConv == 'P') {
                    pathwaysAccessed += "P ";
                    setArraySize();
                    populateArrays(myClientsCopy, myClientNumbersCopy);
                    // Print out all the values of both arrays
                    printArrays(myClientsCopy, myClientNumbersCopy);
                }
                // REMOVE PATHWAY
                else if (choiceConv == 'R') {
                    // Call method to get name or number from the user
                    firstTwoCharM = nameOrNumber("modify");

                    // Create three pathways: number, name, exit
                    // Repopulate array if removal made
                    if (firstTwoCharM.equals("nu") || firstTwoCharM.equals("no")) {
                        clientNumberRemove(myClients, myClientNumbers);
                        pathwaysAccessed += "M ";
                        setArraySize();
                        populateArrays(myClients, myClientNumbers);
                    } else if (firstTwoCharM.equals("na")) {
                        clientNameRemove(myClients, myClientNumbers);
                        pathwaysAccessed += "M ";
                        setArraySize();
                        populateArrays(myClients, myClientNumbers);
                    } else {
                        System.out.println("\nNot a valid command. Exiting...");
                    }
                    
                }
                // SEARCH PATHWAY
                else if (choiceConv == 'S') {

                    pathwaysAccessed += "S ";
                    
                    // Set sentinel value
                    validNaNo = false;

                    System.out.print("\nHow would you like to search? ");
                    // Search using the linear search
                    while(!validNaNo) {
                        // Call method to get name or number from the user
                        firstTwoCharS = nameOrNumber("search for");
                        
                        if (firstTwoCharS.equalsIgnoreCase("nu") || firstTwoCharS.equalsIgnoreCase("no")) {
                            validNaNo = true;
                            clientNumberSearch(myClients, myClientNumbers, 'L');
                        } else if (firstTwoCharS.equalsIgnoreCase("na")) {
                            validNaNo = true;
                            clientNameSearch(myClients, myClientNumbers, 'L');
                        } else {
                            validNaNo = false;
                            System.out.print("\nPlease enter a valid value.");
                        }
                    }
                } 
                // EXIT PROGRAM
                else if (choiceConv == 'X') {
                    System.out.print("\nAre you sure you would like to exit?  ");
                    
                    validYN = false;

                    while(!validYN) {
                        YesNoCheck = sc.next().toUpperCase().charAt(0);

                        if (YesNoCheck == 'Y') {
                            validYN = true;
                            stopProgram = true;
                        } else if (YesNoCheck == 'N') {
                            validYN = true;
                            stopProgram = false;
                        } else {
                            System.out.print("\nPlease enter Y or N --> ");
                        }
                    }
                }
                // HANDLE incorrect input
                else {
                    System.out.println("Not a valid command.\n");
                }

            } while (!stopProgram);

            // Close the scanner sc
            sc.close();

            // Declare a date object to print out the current date
            printFooter();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    /**
     * Searches for a string in an array of 
     * strings with a parallel array of integers of the 
     * same length. Prints out various statements to the
     * console. If the third parameter is 'B', the 
     * search is done via binary search. If the third
     * parameter is 'L', the search is done via linear
     * search. This method calls one of two other
     * functions: linSearchStr or binarySearchStr.
     * @param array array of strings
     * @param array array of integers of the same length
     * @param char char to select which type of search
     * 
     */
    static public void clientNameSearch(String myClients[], int myClientNumbers[], char searchType) {
        int arrayPosClientNo = -1;
        
        // Prompt the user to type the name of a client
        System.out.print("Please enter a client's name: ");

        // Scan the client name entered and store it in a variable
        String clientToLookFor = sc.next();

        // Call the linear or binary search int methods 
        if (searchType == 'B') {
            arrayPosClientNo = binarySearchStr(myClients, clientToLookFor);
        } else if (searchType == 'L') {
            arrayPosClientNo = linSearchString(myClients, clientToLookFor);
        } else {
            System.out.println("Invalid argument.");
        }
        // Print out the position of the client in the list if it was found
        if (arrayPosClientNo > 0)
        System.out.println("\n >> Client found at position " + (arrayPosClientNo + 1) + " on the roster.");

        // Try to find the associated client number
        try {
            int associatedClientNo = myClientNumbers[arrayPosClientNo];
            System.out.printf(" >> %s's client number is %d.\n\n", clientToLookFor, associatedClientNo);
        } catch (ArrayIndexOutOfBoundsException ex1) {
            System.out.println("\nNo associated client number found.\n");
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
    public static void clientNumberSearch(String myClients[], int myClientNumbers[], char searchType) {
        // Declare variables to be used in this method
        boolean inputCheck = false;
        int clientNumberToLookFor = -1;
        int arrayPosClientName = -1;

        // Try to scan and store an integer value 
        do {
            System.out.print("Please enter a client's number: ");
            
            try {
                clientNumberToLookFor = sc.nextInt();
                inputCheck = true;
            } catch (InputMismatchException error) {
                sc.next();
                System.out.println("Please enter a valid integer --> ");
            }
        } while (!inputCheck);

        // Call the linear or binary search int methods 
        if (searchType == 'B') {
            arrayPosClientName = binarySearchInt(myClientNumbers, clientNumberToLookFor);
        } else if (searchType == 'L') {
            arrayPosClientName = linSearchInt(myClientNumbers, clientNumberToLookFor);
        } else {
            System.out.println("\nInvalid argument.");
        }
        
        // Print out the position of the client in the list if found
        if (arrayPosClientName > 0)
        System.out.println("\n >> Client number found at position " + (arrayPosClientName + 1) + " on the roster.");
        
        try {
            String associatedClientName = myClients[arrayPosClientName];
            System.out.printf(" >> %d is the client number of %s.\n\n", 
                              clientNumberToLookFor, associatedClientName);
        } catch (ArrayIndexOutOfBoundsException ex3) {
            System.out.println("\nNo associated client name found.\n");
        }
    }

    /**
     * Modifies an integer in an array of integers using
     * an array of strings with the same length. 
     * @param array array of strings
     * @param array array of integers of the same length
     * 
     */
    public static void clientNumberModify(String myClients[], int myClientNumbers[]) {
        // Declare variables to be used in the following try-catch statement 
        boolean inputCheck = false;
        int clientNumberToLookFor = -1;

        // Try to scan and store an integer value 
        do {
            try {
                System.out.print("Please enter a client's number: ");
                clientNumberToLookFor = sc.nextInt();
                inputCheck = true;
            } catch (InputMismatchException e) {
                sc.next();
                System.out.print("Please enter a valid integer --> ");
            }
        } while (!inputCheck);
        

        // Invoke the linSearchInt method to find the position 
        // of the client number in the array using linear search
        int arrayPosClientName = linSearchInt(myClientNumbers, clientNumberToLookFor);

        String associatedClientName = "";

        try {
            associatedClientName = myClients[arrayPosClientName];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nNo associated client name found.\n");
            return;
        }
        
        // Declare variables to be used in the next try-catch statement 
        int newClientNumber = -1;
        boolean inputCheck2 = false;
        
        // Prompt the user to enter in a new client number
        do {
            System.out.printf("Enter the new client number to replace %d: ", clientNumberToLookFor);
            try {
                newClientNumber = sc.nextInt();

                if (newClientNumber == 0) {
                    throw new Exception();
                }

                for (int clientNumber : myClientNumbers) {
                    if (clientNumber == newClientNumber) {
                        throw new Exception();
                    }
                }

                inputCheck2 = true;

            } catch (InputMismatchException e) {
                sc.next();
                System.out.print("Please enter a valid integer --> ");
            } catch (Exception e) {
                sc.next();
                System.out.print("Please enter an integer other than 0 or one that's already in the database --> ");
            }
        } while (!inputCheck2);
        
        // Deprecated statement - the array will be repopulated in main
        // Update the value in the myClientNumbers array
        // myClientNumbers[arrayPosClientName] = newClientNumber;

        // Insert new client number in the database
        try {
            Statement updateStatement = connection.createStatement();
            updateStatement.executeUpdate("UPDATE clients SET client_number = " + newClientNumber + 
                                                  " WHERE client_number = " + clientNumberToLookFor);
            updateStatement.setQueryTimeout(30);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } 

        // Print out the user the change made
        System.out.printf("\n >> %s's client number was changed from %d to %d.\n\n", 
                          associatedClientName, clientNumberToLookFor, newClientNumber);

    }

    /**
     * Modifies a string in an array of strings using
     * an array of integers with the same length. 
     * @param array array of strings
     * @param array array of integers of the same length
     * 
     */
    public static void clientNameModify(String myClients[], int myClientNumbers[]) {
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
            System.out.println("\nNo associated client number found.\n");
            return;
        }

        // Prompt the user to enter in a new client name
        System.out.printf("Enter in the new client name to replace %s: ", clientToLookFor);
        String newClientName = sc.next();

        // Deprecated statement - the array will be repopulated in main
        // Update the value in the myClients array
        // myClients[arrayPosClientNo] = newClientName;

        // Insert new client number in the database
        try {
            Statement updateStatement = connection.createStatement();
            updateStatement.executeUpdate("UPDATE clients SET client_name = \"" + newClientName + 
                                                  "\" WHERE client_name = \"" + clientToLookFor + "\"");
            updateStatement.setQueryTimeout(30);

        } catch (SQLException e) {
            System.err.println("\n" + e.getMessage() + "\n");
        } 

        System.out.printf("\n >> Client %d's name was changed from %s to %s\n\n", 
                          associatedClientNo, clientToLookFor, newClientName);

    }

    /**
     * Removes a record from the clients table in the clients.db file
     * using the client number as the search criterion
     * @param array array of strings
     * @param array array of integers of the same length
     * 
     */
    public static void clientNumberRemove(String myClients[], int myClientNumbers[]) {
        // Declare variables to be used in the following try-catch statement 
        boolean inputCheck = false;
        int clientNumberToLookFor = -1;

        // Try to scan and store an integer value 
        do {
            try {
                System.out.print("Please enter a client's number: ");
                clientNumberToLookFor = sc.nextInt();
                inputCheck = true;
            } catch (InputMismatchException e) {
                sc.next();
                System.out.print("Please enter a valid integer --> ");
            }
        } while (!inputCheck);
        

        // Invoke the linSearchInt method to find the position 
        // of the client number in the array using linear search
        int arrayPosClientName = linSearchInt(myClientNumbers, clientNumberToLookFor);

        String associatedClientName = "";

        try {
            associatedClientName = myClients[arrayPosClientName];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nNo associated client name found.\n");
            return;
        }

                // Double check the removal with the user
        System.out.printf("Are you sure you want to remove the client %s with number %d? ", 
                          associatedClientName, clientNumberToLookFor);

        boolean validYN = false; 

        while(!validYN) {
            char yesOrNo = sc.next().toUpperCase().charAt(0);
            
            if (yesOrNo == 'Y') {
                validYN = true;
                try {
                    Statement updateStatement = connection.createStatement();
                    updateStatement.executeUpdate("DELETE FROM clients WHERE client_number = " + clientNumberToLookFor);
                    updateStatement.setQueryTimeout(30);

                } catch (SQLException e) {
                    System.err.println("\n" + e.getMessage() + "\n");
                } 

                System.out.printf("\n >> Client with id %d and name %s was removed.\n\n", 
                                clientNumberToLookFor, associatedClientName);

            } else if (yesOrNo == 'N') {
                validYN = true;
                System.out.printf("\n >> Client with id %d and name %s was NOT removed.\n\n", 
                                clientNumberToLookFor, associatedClientName);

            } else {
                validYN = false;
                System.out.print("Please enter Y or N --> ");
            }
        }

    }

    /**
     * Removes a record from the clients table in the clients.db file
     * using the name as the search criterion
     *  
     * @param array array of strings
     * @param array array of integers of the same length
     * 
     */
    public static void clientNameRemove(String myClients[], int myClientNumbers[]) {
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
            System.out.println("\nNo associated client number found.\n");
            return;
        }

        // Double check the removal with the user
        System.out.printf("Are you sure you want to remove the client %s with number %d? ", 
                          clientToLookFor, associatedClientNo);

        boolean validYN = false; 

        while(!validYN) {
            char yesOrNo = sc.next().toUpperCase().charAt(0);

            if (yesOrNo == 'Y') {
                validYN = true;
                try {
                    Statement updateStatement = connection.createStatement();
                    updateStatement.executeUpdate("DELETE FROM clients WHERE client_name = \"" + clientToLookFor + "\"");
                    updateStatement.setQueryTimeout(30);

                } catch (SQLException e) {
                    System.err.println("\n" + e.getMessage() + "\n");
                } 

                System.out.printf("\n >> Client with id %d and name %s was removed.\n\n", 
                                associatedClientNo, clientToLookFor);

            } else if (yesOrNo == 'N') {
                validYN = true;
                System.out.printf("\n >> Client with id %d and name %s was NOT removed.\n\n", 
                                associatedClientNo, clientToLookFor);

            } else {
                validYN = false;
                System.out.print("Please enter Y or N --> ");
            }
        }
    }

    /**
     * Performs linear search on an array of strings. 
     * @param array array of strings to be searched through
     * @param String string to be searched for
     * @return the index of the string if exists in array or
     *         -1 if the string is not a member of the array.
     * 
     */
    public static int linSearchString(String strArray[], String search) {
        int i = 0;
        int flag = -1;
        for(i = 0; i < strArray.length; i++) {
            /* Use the equals method of string objects to compare
            the strings of the array with the string "search" */
            if (search.equals(strArray[i])) {
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            return i;
        } else {
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
    public static int linSearchInt(int intArray[], int search) {
        int flag = -1;

        for (int i = 0; i < intArray.length; i++) {
            if(search == intArray[i]) {
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
    public static int binarySearchInt(int intArray[], int search) {
        int length = intArray.length;
        int upper = length - 1;
        int lower = 0;
        int half = ((upper + lower) / 2);
        int index = half;
        int checker = 0;

        // Check to see that the precondition is met using linear search
        // if (linSearchInt(intArray, search) == -1) return -1;

        while (index >= 0) {
            checker = index;
            if (search == intArray[index]) {
                return index;
            } else if (search < intArray[index]) {
                if (upper == index) {
                    return -1;
                }
                upper = index;
                index = (int)Math.floor((upper + lower) / 2);
            } else if (search > intArray[index]) {
                if (lower == index) {
                    return -1;
                }
                lower = index;
                index = (int)Math.ceil((upper + lower) / 2);
            }
            //index = (upper + lower) / 2;
            System.out.println(upper + ", " + lower + ", " + index);
            if (index == checker) {
                break;
            }
        }
        return -1;
    }

    /**
     * Performs binary search on an array of strings
     * 
     * Limitation: only searches up to the second letter in 
     *             a word
     * @param array array of strings to be searched through
     * @param int string to be searched for
     * @return the index of the string if exists in array or
     *         -1 if the string is not a member of the array.
     * 
     */
    public static int binarySearchStr(String strArray[], String search) {
        int thisLength, nextLength, minLength;
        
        int length = strArray.length;
        int index = ((length - 1) / 2);
        int upper = length - 1;
        int lower = 0;

        while (index >= 0) {
            if (search.equalsIgnoreCase(strArray[index])) {
                return index;
            }
            else if (search.toLowerCase().charAt(0) < strArray[index].toLowerCase().charAt(0)) {
                if (upper == index) {
                    return -1;
                }
                upper = index;
                index = (upper + lower) / 2;
            } else if (search.toLowerCase().charAt(0) > strArray[index].toLowerCase().charAt(0)) {
                if (lower == index) {
                    return -1;
                }
                lower = index;
                if (index == length - 2) index = length - 1;
                else index = (upper + lower) / 2;
            } else if (search.toLowerCase().charAt(0) == strArray[index].toLowerCase().charAt(0)) {
                thisLength = search.length();
                nextLength = strArray[index].length();
                minLength = thisLength > nextLength ? nextLength : thisLength;

                for (int z = 1; z < minLength; z++) {
                    if (search.toLowerCase().charAt(z) > strArray[index].toLowerCase().charAt(z)) {
                        lower = index;
                        break;
                    } else if (search.toLowerCase().charAt(z) == strArray[index].toLowerCase().charAt(z)) {
                        continue;
                    } else {
                        upper = index;
                        break;
                    }
                }
                index = (upper + lower) / 2;
            }
            //System.out.println(index);
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

        for (int i = 0; i < length - 1; i++) {
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

            if (equalityCounter == length - 1) {
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
    public static String[] bubbleSortStr(String[] strArray) {
        String tempStr;
        int thisLength, nextLength, minLength;
        int length = strArray.length;

        for (int i = 0; i < length - 1; i++) {
            int equalityCounter = 0;
            for (int j = 0; j < length - 1; j++) {
                if (strArray[j].charAt(0) > strArray[j+1].charAt(0)) {
                    //System.out.println("GREATER: " + strArray[j] + " versus " + strArray[j+1]);
                    tempStr = strArray[j+1];
                    strArray[j+1] = strArray[j];
                    strArray[j] = tempStr;

                } else if (strArray[j].charAt(0) == strArray[j+1].charAt(0)) {
                    //System.out.println("EQUAL: " + strArray[j] + " versus " + strArray[j+1]);
                    thisLength = strArray[j].length();
                    nextLength = strArray[j+1].length();
                    minLength = thisLength > nextLength ? nextLength : thisLength;

                    for (int z = 1; z < minLength; z++) {
                        //System.out.println(strArray[j].charAt(z) + " versus " + strArray[j+1].charAt(z));
                        if (strArray[j].charAt(z) > strArray[j+1].charAt(z)) {
                            tempStr = strArray[j+1];
                            strArray[j+1] = strArray[j];
                            strArray[j] = tempStr;
                            break;
                        } else if (strArray[j].charAt(z) == strArray[j+1].charAt(z)) {
                            continue;
                        } else if (strArray[j].charAt(z) < strArray[j+1].charAt(z)) {
                            break;
                        }
                    }
                } else {
                    equalityCounter++;
                }
            }

            // Print out the arrays at each step 
            // Show the sorting in action
            System.out.println(Arrays.toString(strArray));

            if (equalityCounter == length - 1) {
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
                         "\nEnter no or nu to " + verb + " a client number. --> ");

        // Store the string after converting it to lower case letters
        // Then, store the first two letters of that string
        String searchChoice = sc.next().toLowerCase();
        String firstTwoChar = "";

        try {
            firstTwoChar = searchChoice.substring(0,2);
        } catch (StringIndexOutOfBoundsException o) {
            System.out.println("Not a valid command.\n");
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
    public static void printArrays(String strArray[], int intArray[]) {
        // Print the header 
        System.out.println("\n---------------------------------------------");
        System.out.println("--------------- Clients Table ---------------");
        System.out.println("---------------------------------------------\n");
        System.out.printf("%20s  ", "Name");
        System.out.printf("%-20s\n", "Number");
        
        // Print all the terms of each array 
        for (int i = 0; i < strArray.length && strArray[i] != null; i++) {
            System.out.printf("%20s  ", strArray[i]);
            System.out.printf("%-20d\n", intArray[i]);
        }

        // Print an extra line
        System.out.println();
    }

    /**
     * Prints a footer with a summary of pathways obtained 
     * in the current session and the contents of the original clients table
     * and the modified clients table
     *
     * @param array array of strings
     * @param array array of integers
     * 
     */ 
    public static void printFooter() {
        Date myDate = new Date();
        String myDateFormat = "MM/dd/yyy";
        SimpleDateFormat dtToday = new SimpleDateFormat(myDateFormat);
        System.out.println("\n\n------------------------------------");
        System.out.println("---------- Session Summary ---------");
        System.out.println("------------------------------------\n");
        System.out.println("Pathways accessed: " + pathwaysAccessed + "\n");
        System.out.println("\n----- Original ------  ------ Changes ------");
        System.out.printf("%-12s", "Name");
        System.out.printf("%-12s", "Number");
        System.out.printf("%-12s", "Name");
        System.out.printf("%-12s\n", "Number");

        int originalLen = myClientsOriginal.length;
        int newLen = myClientsCopy.length;
        int endpoint = originalLen > newLen ? originalLen : newLen;

        boolean endOfOriginal = false; 
        boolean endOfCopy = false;

        for (int i = 0; i < endpoint ; i++) {
            if (i < originalLen && myClientsOriginal[i] != null) {
                System.out.printf("%-12s", myClientsOriginal[i]);
                System.out.printf("%-12d", myClientNumbersOriginal[i]);
                
                if (endOfCopy) {
                    System.out.println();
                }

            } else {
                endOfOriginal = true;
            }

            if (i < newLen && myClientsCopy[i] != null ) {
                if (endOfOriginal) {
                    System.out.printf("%-24s", " ");
                }

                System.out.printf("%-12s", myClients[i]);
                System.out.printf("%-12d\n", myClientNumbers[i]);
            } else {
                endOfCopy = true;
            }
        }

        System.out.printf("\n\nToday's date: %s\n", dtToday.format(myDate));
        System.out.printf("Programmer: John Albright\n\n");
    }

    /**
     * Instantiates two arrays to reflect the count of the records 
     * in the clients table in the clients.db file
     *
     * @param array array of strings
     * @param array array of integers
     * 
     */ 

    public static void setArraySize() {
        
        try {
            // Get the count of records in the database
            Statement countStatement = connection.createStatement();
            ResultSet rsCount = countStatement.executeQuery("SELECT COUNT(*) FROM clients");
            rsCount.next();
            int recordsCount = rsCount.getInt(1);
            //System.out.println(recordsCount);

            // Set the length of the arrays to be populated
            myClientNumbersCopy = new int[recordsCount];
            myClientsCopy = new String[recordsCount];
        } catch (SQLException e) {
                System.err.println(e.getMessage());
        }
    }

    /**
     * Populates an array of strings and numbers of equal length with 
     * values found in a database with two fields of the same type
     * @param array array of strings
     * @param array array of integers
     * 
     */ 
    public static void populateArrays(String strArray[], int intArray[]) {
            
            try {
                // Get all the users from the clients table
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM clients");

                // Initialize index to remember position in array
                int index = 0;

                // Iterate through all the records extracted from the clients table
                while (rs.next()) {
                    strArray[index] = rs.getString("client_name");
                    intArray[index] = Integer.parseInt(rs.getString("client_number"));
                    ++index;
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
    }
    
    /**
     * Performs an insertion sort on an array of integers
     * @param array array of integers to be sorted
     * @return the same array but sorted in order from
     *         lowest to highest integer
     */
    public static int[] insertionSortInt(int[] intArray) {
        int length = intArray.length;

        for (int j = 0; j < length - 1; j++) {
            if (intArray[j] > intArray[j+1]) {
                if (j == 0 || intArray[j+1] > intArray[j-1]) {   
                    int temp = intArray[j];
                    intArray[j] = intArray[j+1];
                    intArray[j+1] = temp;
                } else {
                    int tracker = j + 1;
                    int movingComp = j - 1;

                    while (movingComp > 0) {
                        if (intArray[tracker] > intArray[movingComp]) break;
                        movingComp--;
                    }

                    int tempInt;
                    if (movingComp != 0 && intArray[tracker] >= intArray[movingComp]) {
                        tempInt = intArray[movingComp+1];
                        intArray[movingComp+1] = intArray[tracker];
                    } else {
                        tempInt = intArray[movingComp];
                        intArray[movingComp] = intArray[tracker];
                    }
                    
                    while (tracker > movingComp) {
                        if (tracker == movingComp+1) {
                            if (intArray[tracker] < tempInt) {
                                intArray[tracker+1] = tempInt;
                            } else {
                                intArray[tracker] = tempInt;
                            }
                        } else {
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
    public static String[] insertionSortStr(String[] strArray) {
        int length = strArray.length;
        String tempStr, temp;

        for (int j = 0; j < length - 1; j++) {
            if (strArray[j].charAt(0) > strArray[j+1].charAt(0)) {
                if (j == 0 || strArray[j+1].charAt(0) > strArray[j-1].charAt(0)) {   
                    temp = strArray[j];
                    strArray[j] = strArray[j+1];
                    strArray[j+1] = temp;
                } else {
                    int tracker = j + 1;
                    int movingComp = j - 1;

                    while (movingComp > 0) {
                        if (strArray[tracker].charAt(0) > strArray[movingComp].charAt(0)) break;
                        movingComp--;
                    }

                    if (movingComp != 0 && strArray[tracker].charAt(0) >= strArray[movingComp].charAt(0)) {
                        tempStr = strArray[movingComp+1];
                        strArray[movingComp+1] = strArray[tracker];
                    } else {
                        tempStr = strArray[movingComp];
                        strArray[movingComp] = strArray[tracker];
                    }

                    while (tracker > movingComp) {
                        if (tracker == movingComp+1) {
                            if (strArray[tracker].charAt(0) < tempStr.charAt(0)) {
                                strArray[tracker+1] = tempStr;
                            } else {
                                strArray[tracker] = tempStr;
                            }
                        } else {
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