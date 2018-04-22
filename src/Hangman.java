import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.json.JSONTokener;

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
	private static boolean twoPlayer = false;
	private static String category = "unknown";
	private static String API_KEY = System.getenv("NEWSAPI_ORG_API_KEY");
	
	// check each space
	// is it available?
	// if so, how many ways can it help me win?
	// if it's the best so far, keep track of that space

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		twoPlayer = getOneOrTwoPlayer() == 2;
		if (!twoPlayer) {
			category = getCategory();
			System.out.println("The category is " + category + " from newsapi.org");
		}
		play();
	}
	
	private static String getCategory() {
		String[] categories = {
				"business",
				"entertainment",
				"health",
				"science",
				"sports",
				"technology",
				"headlines"
		};
		int pick = ThreadLocalRandom.current().nextInt(0, categories.length);
		return categories[pick];
	}
	
	/**
	 * Get headlines from urls like https://newsapi.org/v2/sources?apiKey=1b4b5629da734218baac04aa07e2d478&category=entertainment&language=en&country=us
	 * @return
	 */
	private static String getAHeadline() {
		String getHeadlinesUrl = "https://newsapi.org/v2/top-headlines?apiKey=" + API_KEY;
		if(category.equals("headlines")) {
			getHeadlinesUrl += "&country=us";
		} else {
			String sources = null;
			if (category.equalsIgnoreCase("business"))
				sources="bloomberg,business-insider,cnbc,fortune,the-wall-street-journal";
			else if (category.equals("entertainment"))
				sources="buzzfeed,entertainment-weekly,ign,mashable,mtv-news,polygon";
			else if (category.equals("health"))
				sources="medical-news-today";
			else if (category.equals("science"))
				sources = "national-geographic,new-scientist,next-big-future";
			else if (category.equals("sports"))
				sources = "bleacher-report,espn,espn-cric-info,fox-sports,nfl-news,nhl-news";
			else if (category.equals("technology"))
				sources = "ars-technica,crypto-coins-news,engadget,hacker-news,recode,techcrunch,techradar,the-next-web,the-verge,wired";
			if (sources == null)
				throw new RuntimeException("Unknown category: " + category);
			getHeadlinesUrl += "&sources=" + sources;
		}
		try {
			URL getHeadlinesURL = new URL(getHeadlinesUrl);
			BufferedReader in = new BufferedReader(new InputStreamReader(getHeadlinesURL.openStream()));
			JSONTokener jsonTokener = new JSONTokener(in);
			JSONObject jsonObject = new JSONObject(jsonTokener);
			in.close();
			int pick = ThreadLocalRandom.current().nextInt(0, jsonObject.getJSONArray("articles").length());
			return jsonObject.getJSONArray("articles").getJSONObject(pick).getString("title");
		} catch (MalformedURLException e) {
			throw new AssertionError("Expected my url to be correct: " + e.getLocalizedMessage(), e);
		} catch (IOException e) {
			throw new AssertionError("Unable to read " + getHeadlinesUrl + " right now: " + e.getLocalizedMessage(), e);
		}
	}

	private static int getOneOrTwoPlayer() {
		int answer = 0;
		while (answer < 1 || answer > 2) {
			System.out.print("One player or two?  (please enter '1' or '2') > ");
			try {
				answer = scanner.nextInt();
			} catch (Exception e) {
				System.out.print("Please enter '1' or '2' > ");
			}
		}
		return answer;
	}

	/** 
	 * Get the phrase and do the loop to play the game
	 */
	private static void play() {
		if (twoPlayer) {
			System.out.println("Enter the secret phrase:");
			phrase = getPhrase();
			clearTheScreen();
		} else {
			phrase = getAHeadline();
		}
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
		if (failures >= 6 || !twoPlayer && failures >= 1)
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
