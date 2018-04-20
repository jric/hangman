import java.util.Scanner;

/**
 * Plays the game, hangman!
 * 
 * @author jric
 *
 */
public class Hangman {
	static Scanner scanner = new Scanner(System.in);
	private static int failures = 0;
	private static String phrase;
	private static String guesses = "";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		play();
	}

	/** 
	 * Get the phrase and do the loop to play the game
	 */
	private static void play() {
		System.out.println("Enter the secret phrase:");
		phrase = getPhrase();
		clearTheScreen();
		while (true) {
			drawHangman();
			System.out.println("\n");
			showGuesses();
			System.out.println("\n");
			drawPhrase();
			System.out.println("\n");
			if (RIP()) {
				System.out.println("You're dead.  The phrase was " + phrase);
				return;
			}
			if (isPhraseComplete()) {
				System.out.println("You got it, you smarty-pants!");
				return;
			}
			getNextGuess();
			System.out.println("\n");
		}
	}

	/**
	 * Print out a bunch of newlines to clear the secret off the screen.
	 */
	private static void clearTheScreen() {
		for (int i = 0; i < 1000; i++)
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

	/**
	 * @return true iff the user has guessed all the letters in the phrase
	 */
	private static boolean isPhraseComplete() {
		for (int i = 0; i < phrase.length(); i++) {
			char letter = phrase.toLowerCase().charAt(i);
			if (letter >= 'a' && letter <= 'z' && guesses.indexOf(letter) < 0)
				return false;
		}
		return true;
	}

	/**
	 * @return true iff the number of failures is >= 6
	 */
	private static boolean RIP() {
		if (failures >= 6)
			return true;
		return false;
	}

	/**
	 * Show the user the letters they have already guessed
	 */
	private static void showGuesses() {
		System.out.print("Guesses so far: ");
		for (int i = 0; i < guesses.length(); i++) {
			System.out.print(guesses.toUpperCase().charAt(i));
			System.out.print(' ');
		}
		System.out.println();
	}

	/** 
	 * Gets the next guess and updates the value of failures
	 */
	private static void getNextGuess() {
		System.out.println("What letter would you like to guess next?");
		guesses = guesses + getNextValidGuess();
		failures = 0;
		for (int i = 0; i < guesses.length(); i++)
			if (phrase.toLowerCase().indexOf(guesses.charAt(i)) < 0)
				failures++;
	}

	/**
	 * Gets a letter from the user; it must not have already been guessed before
	 * @return that new letter
	 */
	private static char getNextValidGuess() {
		while (true) {
			String nextGuess = scanner.nextLine();
			if (nextGuess.length() < 1) {
				System.out.println("Please enter a letter");
			} else if (nextGuess.length() > 1) {
				System.out.println("Please enter only a single letter");
			} else {
				char nextGuessChar = nextGuess.toLowerCase().charAt(0);
				if (guesses.indexOf(nextGuessChar) >= 0)
					System.out.println("The letter " + nextGuessChar + " is already guessed!");
				else if (nextGuessChar < 'a' || nextGuessChar > 'z')
					System.out.println("You must enter a letter");
				else
					return nextGuessChar;
			}
		}
	}

	/**
	 * Draws the phrase with blanks for the letters that haven't been guessed yet.
	 */
	private static void drawPhrase() {
		for (int i = 0; i < phrase.length(); i++) {
			char nextChar = phrase.toLowerCase().charAt(i);
			if (nextChar == ' ')
				System.out.print("   ");
			else if (nextChar < 'a' || nextChar > 'z')
				System.out.print(nextChar);
			else if (guesses.indexOf(nextChar) >= 0)
				System.out.print(phrase.charAt(i));
			else
				System.out.print("_");
			System.out.print(" ");
		}
		System.out.println();
	}

	/**
	 * Draws the hangman's platform and any parts of the body that have already been hung.
	 */
	private static void drawHangman() {
		System.out.print("______\n");
		System.out.print("|    |\n");
		System.out.print("|    ");
		if (failures  >= 1)
			System.out.print("O");
		System.out.println();
		System.out.print("|  ");
		if (failures >= 3)
			System.out.print("--");
		else
			System.out.print("  ");
		if (failures >= 2)
			System.out.print("|");
		if (failures >= 4)
			System.out.print("--");
		System.out.println();
		System.out.print("|   ");
		if (failures >= 5)
			System.out.print("/");
		System.out.print(' ');
		if (failures >= 6)
			System.out.print("\\");
		System.out.println();
		System.out.println("-");
	}

	/**
	 * Collects a phrase of at least 10 letters.
	 * @return the phrase
	 */
	private static String getPhrase() {
		while (true) {
			String phrase = scanner.nextLine();
			int numLetters = 0;
			for (int i = 0; i < phrase.length(); i++)
				if (phrase.toLowerCase().charAt(i) >= 'a' && phrase.toLowerCase().charAt(i) <= 'z')
					numLetters++;
			if (numLetters >= 10)
				return phrase;
			System.out.println("You're phrase must contain 10 letters");
		}
	}

}
