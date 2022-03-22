import java.io.*;
import java.util.*;

class Main {

  // Variables for Colours
  public static final String reset = "\033[0m";
  public static final String green_bg = "\033[42m";
  public static final String yellow_bg = "\033[0;103m";
  public static final String gray_bg = "\033[0;100m";
  public static final String white = "\033[1;37m";
  public static final String red = "\033[1;31m";
  public static final String blue = "\033[1;34m"; 
  public static final String green = "\033[1;32m";

  // Score variable
  public static int score = 0;

  // Wordle Word Bank
  public static String[] wordBank() {
    ArrayList<String> wordsList = new ArrayList<String>();
    
    try {
      File myObj = new File("wordleWords.txt");
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        //add data to the array
        wordsList.add(data);
     
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

    String[] words = new String[wordsList.size()];
    words = wordsList.toArray(words);

    return words;
  }

  // Generate Target Word
  public static char[] targetWordMethod(String[] words) {
    // generate and split word
    int generatedWordInt = (int)Math.floor(Math.random()*words.length);
    String targetWord = words[generatedWordInt];  
    char[] targetWordArr = targetWord.toCharArray();
    
    return targetWordArr;
  }

  // Generate Input Word
  public static char[] inputWordMethod() {

    Scanner sc = new Scanner(System.in);
    
    // input and split word
    String inputWord = sc.nextLine().toUpperCase();
    char[] inputWordArr = inputWord.toCharArray();
    
    return inputWordArr;
  }

  public static void introText() {
    // Intro Text
    System.out.println("\nGuess the WORDLE in six tries\n");
    System.out.println("Each guess must be a valid five-letter word. Hit the enter button to submit.\n");
    System.out.println("After each guess, the color of the tiles will change to show how close your guess was to the word. For example:\n");

    // weary
    wordBuilder("W", green_bg, "E", gray_bg, "A", gray_bg, "R", gray_bg, "Y", gray_bg);
    System.out.println("\nThe letter W is in the word and in the correct spot.\n");

    // pills
    wordBuilder("P", gray_bg, "I", yellow_bg, "L", gray_bg, "L", gray_bg, "S", gray_bg);
    System.out.println("\nThe letter I is in the word but in the wrong spot.\n");

    // vague
    wordBuilder("V", gray_bg, "A", gray_bg, "G", gray_bg, "U", gray_bg, "E", gray_bg);
    System.out.println("\nThese letters are not in the word.\n");

    // line
    for (int i = 0; i < 40; i++) {
      System.out.print("-");
    }
    System.out.println("\n");
    
  }

  // Formatting words
  public static void wordBuilder(String letter1, String bg1, String letter2, String bg2, String letter3, String bg3, String letter4, String bg4, String letter5, String bg5) {
    System.out.println(bg1 + " " + letter1 + " " + reset + " " + bg2 + " " + letter2 + " " + reset + " " + bg3 + " " + letter3 + " " + reset + " " + bg4 + " " + letter4 + " " + reset + " " + bg5 + " " + letter5 + " " + reset + " ");
  }

  // Actual Gamplay Method
  public static void guessProcessing(char[] targetWordArr) throws InterruptedException {
    int numberOfGuesses = 0; // sets the number of guesses
    boolean answeredCorrectly = false; // used for answer verification

    while (numberOfGuesses < 6 && answeredCorrectly == false) {
      char[] inputWordArr = inputWordMethod();
      String inputWord = String.valueOf(inputWordArr).toLowerCase();
      String targetWord = String.valueOf(targetWordArr);
      inputClearing();

      // check if input word is of length 5
      if (inputWordArr.length != 5) {
        System.out.println(red + "Please input a 5 letter word" + reset);
        Thread.sleep(500);
        inputClearing();
      } 

      // check if input word is in word bank
      else if (!Arrays.stream(wordBank()).anyMatch(String.valueOf(inputWordArr).toLowerCase()::equals)) {
        System.out.println(red + "Please input a valid word" + reset);
        Thread.sleep(500);
        inputClearing();
        
      }
  
      // proceed with valid input
      else {
        numberOfGuesses++;
        wordChecker(targetWordArr, inputWordArr);
        if (inputWord.equals(targetWord)) {
          answeredCorrectly = true;
          break;
        }
      }
    }

    if (answeredCorrectly == false) {
      System.out.println(gray_bg + "Unfortunately you couldn't guess the word. The word was " + red + String.valueOf(targetWordArr) + reset + "\n");
    } else {
      score++;
      if (numberOfGuesses == 1) {
        System.out.println(gray_bg + "Congratulations! You guessed the word in " + numberOfGuesses + " try" + reset + "\n");
      } else {
        System.out.println(gray_bg + "Congratulations! You guessed the word in " + (numberOfGuesses) + " tries" + reset + "\n");
      }
    }

    System.out.println(blue + "Your score is " + score + "\n" + reset);
  }

  // check and grade word
  public static void wordChecker(char[] targetWordArr, char[] inputWordArr) {

    // copy target word array
    char[] targetWordArrCopy = new char[5];
    for (int i = 0; i < 5; i++) {
      targetWordArrCopy[i] = targetWordArr[i];
    }
    
    // create an array for the background colour of each letter
    String[] bgs = {gray_bg, gray_bg, gray_bg, gray_bg, gray_bg, gray_bg};
    
    for (int i = 0; i < 5; i++) {
      if (Character.toUpperCase(targetWordArr[i]) == inputWordArr[i]) {
        bgs[i] = green_bg;
        targetWordArrCopy[i] = '*';
      }
    }

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if ((Character.toUpperCase(targetWordArrCopy[j]) == inputWordArr[i])) {
          if (bgs[i] != green_bg) {
            bgs[i] = yellow_bg;
            targetWordArrCopy[j] = '*';
          }
        }
      }
    }
    
    // build the word
    wordBuilder(Character.toString(inputWordArr[0]), bgs[0], Character.toString(inputWordArr[1]), bgs[1], Character.toString(inputWordArr[2]), bgs[2], Character.toString(inputWordArr[3]), bgs[3], Character.toString(inputWordArr[4]), bgs[4]);
    System.out.println();
    
  }
  
  // clearing the input
  public static void inputClearing() {
    System.out.print(String.format("\033[%dA",1));
      System.out.print("\033[2K");
  }

  // play again loop
  public static void gameplayLoop(String[] words) throws InterruptedException {
    
    // scanner + variable initialization
    Scanner sc = new Scanner(System.in);
    String playAgain = "";
    
    do {
      // generate target word
      char[] targetWordArr = targetWordMethod(words);
      
      if (playAgain.equals("yes")) {
        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println("");
      }
      
      // processing guesses
      guessProcessing(targetWordArr);
      System.out.println("Would you like to play again? (" + green + "yes" + reset + " or " + red + "no" + reset + ")");

      // user input
      do {
        if (!playAgain.equals("yes") && !playAgain.equals("no")) {
          System.out.println(red + "Please input a valid answer" + reset);
          Thread.sleep(500);
          inputClearing();
        }
        
        playAgain = sc.nextLine();
        inputClearing();
      } while (!playAgain.equals("yes") && !playAgain.equals("no"));
      
    } while (playAgain.equals("yes"));

    System.out.println("");
    System.out.println("----------------------------------------");
    System.out.println("\nThanks for playing!\n");
  }


  // main method
  public static void main(String[] args) throws InterruptedException {
    
    // word bank + target word generation
    String[] words = wordBank();

    // intro text
    introText();

    // gameplay loop - initializes the wordle gameplay with play again functionality
    gameplayLoop(words);
    
  }
}