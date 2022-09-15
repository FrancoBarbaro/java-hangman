import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

public class Hangman {
    private static Scanner inputScanner = new Scanner(System.in);
    private static String secretWord = getSecretWord();
    private static final int SECRET_WORD_LENGTH = secretWord.length();

    public static void main(String[] args) {
        ArrayList<String> guesses = new ArrayList<String>();
        char[] revealedLetters = new char[SECRET_WORD_LENGTH];
        Arrays.fill(revealedLetters, '_');

        int guessesLeft = 10;
        boolean playerWon = false;

        System.out.print("I'm thinking of a " + SECRET_WORD_LENGTH + " letter word: ");
        System.out.println(revealedLetters);

        while (guessesLeft > 0) {
            String guess = getGuess("Guess a letter or word: ", guesses);
            guesses.add(guess);

            if (guess.length() == 1) {
                char letter = guess.charAt(0);
                int[] occurrences = find(secretWord, letter);

                if (occurrences.length == 0) {
                    System.out.println("Wrong!");
                    guessesLeft--;
                } else {
                    for (int index : occurrences) {
                        revealedLetters[index] = letter;
                    }
                    System.out.println("Correct!");
                }

                System.out.println(revealedLetters);

                if (!ArrayUtils.contains(revealedLetters, '_')) {
                    playerWon = true;
                    break;
                }
            } else if (guess.equals(secretWord)) {
                playerWon = true;
                break;
            } else {
                System.out.println("Wrong!");
                guessesLeft--;
            }

            System.out.println("Guesses left: " + guessesLeft + "\n");
        }

        if (playerWon) {
            System.out.println("You win!");
        } else {
            System.out.println("You lose!");
        }

        System.out.println("The word was " + secretWord + "\n");

        inputScanner.close();
    }

    private static String getGuess(String prompt, ArrayList<String> prevGuesses) {
        String input;

        while (true) {
            System.out.print(prompt);
            input = inputScanner.nextLine();

            if (prevGuesses.contains(input)) {
                System.out.println("You already guessed that.");
            } else if (input.length() != 1 && input.length() != SECRET_WORD_LENGTH) {
                System.out.println("Please guess either one letter or a " + SECRET_WORD_LENGTH + " letter word.");
            } else if (!input.matches("[a-zA-Z]+")) {
                System.out.println("Please guess with only alphabetic characters.");
            } else {
                return input;
            }
        }
    }

    private static int[] find(String word, char guess) {
        ArrayList<Integer> indexes = new ArrayList<Integer>();

        int index = -1;
        while (true) {
            index = word.indexOf(guess, index + 1);
            if (index == -1)
                break;
            indexes.add(index);
        }

        /*
         * converts from type ArrayList<Integer> (mutable list of Integer class
         * instances) to int[] (immutable array of primative integers)
         */
        int[] arr = indexes.stream().mapToInt(Integer::intValue).toArray();

        return arr;
    }

    private static String getSecretWord() {
        String word = "";
        Path path = Paths.get("./words.txt");
        Random random = new Random();

        try {
            List<String> words = Files.readAllLines(path);
            int randIndex = random.nextInt(words.size());
            word = words.get(randIndex);
        } catch (IOException ex) {
            System.out.println("Something went wrong while fetching the secret word!");
            System.exit(1);
        }

        return word;
    }
}
