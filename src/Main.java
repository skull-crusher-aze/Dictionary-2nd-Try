import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static DictionaryManager dictionaryManager = new DictionaryManager();
    private static Dictionary currDictionary = null;

    public static void main(String[] args) {
        boolean done = false;
        while (!done) {
            showMainMenu();
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    createNewDictionary();
                    break;
                case 2:
                    loadDictionary();
                    break;
                case 3:
                    deleteDictionary();
                    break;
                case 4:
                    listAllDictionaries();
                    break;
                case 0:
                    done = true;
                    System.out.println("Exiting");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n == Welcome to the Dictionary Manager == ");
        System.out.println(" 1. Create a new dictionary");
        System.out.println(" 2. Load dictionary");
        System.out.println(" 3. Delete dictionary");
        System.out.println(" 4. List all dictionaries");
        System.out.println(" 0. Exit");
        System.out.print(" Enter your choice: ");
    }

    private static void createNewDictionary() {
        System.out.print("Enter the name of the dictionary: ");
        String name = scanner.nextLine();

        System.out.print("Enter the source language of the dictionary: ");
        String sourceLanguage = scanner.nextLine().toUpperCase();

        System.out.print("Enter the target language of the dictionary: ");
        String targetLanguage = scanner.nextLine().toUpperCase();

        try {
            boolean created = dictionaryManager.createDictionary(name, sourceLanguage, targetLanguage);
            if (created) {
                System.out.println("Dictionary Created Successfully");
            } else {
                System.out.println("Dictionary Already Exists");
            }
        } catch (IOException e) {
            System.out.println("Error while creating the dictionary: " + e.getMessage());
        }
    }

    private static void loadDictionary() {
        System.out.print("Enter the name of the dictionary: ");
        String name = scanner.nextLine();

        try {
            currDictionary = dictionaryManager.loadDictionary(name);
            if (currDictionary == null) {
                System.out.println("Dictionary Not Found");
            } else {
                System.out.println("Dictionary Loaded Successfully");
                wordOperationsMenu();
            }
        } catch (IOException e) {
            System.out.println("Error while loading the dictionary: " + e.getMessage());
        }
    }

    private static void deleteDictionary() {
        System.out.print("Enter the name of the dictionary: ");
        String name = scanner.nextLine();

        boolean deleted = dictionaryManager.deleteDictionary(name);
        if (deleted) {
            System.out.println("Dictionary Deleted Successfully");
            if (currDictionary != null && currDictionary.getName().equals(name)) {
                currDictionary = null;
            }
        } else {
            System.out.println("Dictionary Not Found");
        }
    }

    private static void listAllDictionaries() {
        List<String> dictionaries = dictionaryManager.listAllDictionaries();

        if (dictionaries.isEmpty()) {
            System.out.println("No dictionaries found");
        } else {
            System.out.println("\n=== Available Dictionaries ===");
            for (int i = 0; i < dictionaries.size(); i++) {
                String dictName = dictionaries.get(i);
                try {
                    Dictionary dict = dictionaryManager.loadDictionary(dictName);
                    System.out.println((i + 1) + ". " + dictName + " (" +
                            dict.getSourceLanguage() + "-" +
                            dict.getTargetLanguage() + ")");
                } catch (IOException e) {
                    System.out.println((i + 1) + ". " + dictName + " (Error loading info)");
                }
            }
            System.out.println("============================");
        }
    }

    private static void wordOperationsMenu() {
        boolean inDictionary = true;

        while (inDictionary && currDictionary != null) {
            showWordMenu();
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addWord();
                    break;
                case 2:
                    deleteWord();
                    break;
                case 3:
                    searchWord();
                    break;
                case 4:
                    showAllWords();
                    break;
                case 0:
                    inDictionary = false;
                    System.out.println("Going To Main Menu");
                    break;
                default:
                    System.out.println("Wrong choice");
            }
        }
    }

    private static void showWordMenu() {
        System.out.println("\n=== WORD OPERATIONS MENU ===");
        System.out.println("Current Dictionary: " + currDictionary.getName() + " (" +
                currDictionary.getSourceLanguage() + "-" +
                currDictionary.getTargetLanguage() + ")");
        System.out.println("1. Add new word");
        System.out.println("2. Delete word");
        System.out.println("3. Search for word");
        System.out.println("4. Show all words");
        System.out.println("0. Go to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addWord() {
        System.out.print("Enter word in " + currDictionary.getSourceLanguage() + ": ");
        String word = scanner.nextLine();

        System.out.print("Enter translation in " + currDictionary.getTargetLanguage() + ": ");
        String translation = scanner.nextLine();

        try {
            currDictionary.addWord(word, translation);
            System.out.println("Word Added Successfully");
        } catch (IOException e) {
            System.out.println("Error while adding word: " + e.getMessage());
        }
    }

    private static void deleteWord() {
        System.out.print("Enter the word you want to delete: ");
        String word = scanner.nextLine();

        try {
            boolean deleted = currDictionary.deleteWord(word);
            if (deleted) {
                System.out.println("Word Deleted Successfully");
            } else {
                System.out.println("Word Not Found");
            }
        } catch (IOException e) {
            System.out.println("Error while deleting word: " + e.getMessage());
        }
    }

    private static void searchWord() {
        System.out.print("Enter the word you want to search: ");
        String word = scanner.nextLine();

        String translation = currDictionary.searchWord(word);
        if (translation != null) {
            System.out.println("Found: " + word + " -> " + translation);
        } else {
            System.out.println("Word Not Found");
        }
    }

    private static void showAllWords() {
        Map<String, String> allWords = currDictionary.getAllWords();

        if (allWords.isEmpty()) {
            System.out.println("No words found in the dictionary");
        } else {
            System.out.println("\n=== Dictionary Contents ===");
            System.out.println("Dictionary Name: " + currDictionary.getName() +
                    " (" + currDictionary.getSourceLanguage() +
                    "-" + currDictionary.getTargetLanguage() + ")");
            System.out.println("Word Count: " + allWords.size());
            System.out.println("===================================");

            int count = 1;
            for (Map.Entry<String, String> entry : allWords.entrySet()) {
                System.out.println(count + ". " + entry.getKey() + " -> " + entry.getValue());
                count++;
            }
        }
    }
}