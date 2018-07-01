import java.util.Scanner;

/**
 * Plays the game, hangman!
 * 
 * Copyright 2018 Joshua Richardson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * 
 * @author jric
 *
 */
public class Hangman {
	static int counter;
	static int bodyparts;
	static int letters;
	static String phrase;
	static String guessed;
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		play();
	}

	/** 
	 * Get the phrase and do the loop to play the game
	 * 
	 * TODO: Alex (use while loop)
	 */
	private static void play() {
	}

	/**
	 * Print out a bunch of newlines to clear the secret phrase off the screen.  Or better yet, print the terminal
	 * control characters:  \033c  (for clear screen)
	 * 
	 * TODO: Andrew
	 */
	private static void clearTheScreen() {
		int linecounter = 0;
		while (linecounter < 20) {
			System.out.println("");
			linecounter++;
		}
	}

	/**
	 * @return true iff the user has guessed all the letters in the phrase
	 * 
	 * TODO: Ben
	 */
	private static boolean isPhraseComplete() {
		return true;
	}

	/**
	 * @return true iff the number of failures is >= 6
	 * 
	 * TODO: Alex
	 */
	private static boolean RIP() {
		return false;
	}

	/**
	 * Show the user the letters they have already guessed
	 * 
	 * TODO: Andrew
	 */
	private static void showGuesses() {
		System.out.print(guessed);
	}

	/** 
	 * Gets the next guess and updates the value of failures
	 * 
	 * TODO: Ben (use while loop)
	 */
	private static void getNextGuess() {
	}

	/**
	 * Gets a letter from the user; it must not have already been guessed before
	 * @return that new letter
	 * 
	 * TODO: Alex (use while loop)
	 */
	private static char getNextValidGuess() {
		return 0;
	}

	/**
	 * Draws the phrase with blanks for the letters that haven't been guessed yet.
	 * 
	 * TODO: Andrew
	 */
	private static void drawPhrase() {
		for (int counter = 0; counter < phrase.length(); counter++) {
			char check = phrase.charAt(counter);
			// if the character at index #(counter #) in string "phrase" is in guessed list
			if (-1 != guessed.lastIndexOf(check)) {
				System.out.print(check);
			}
			else if(Character.isAlphabetic(check)) {
				System.out.print("_");
			}
			else {
				System.out.print(check);
			} 
		}
	}

	/**
	 * Draws the hangman's platform and any parts of the body that have already been hung.
	 * 
	 * TODO: Ben
	 */
	private static void drawHangman() {
	}

	/**
	 * Collects a phrase of at least 10 letters.
	 * @return the phrase
	 * 
	 * TODO: Andrew (use a while loop)
	 */
	private static String getPhrase() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("What will your phrase be?");
		String phrase = scanner.nextLine();
		int letters = phrase.length();
		if (letters > 9) {
			return phrase;
		}
		else {
			System.out.println("Sorry, it has to be at least 10 letters.  Try again!");
			return getPhrase();
		}

	}

}
