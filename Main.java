/*
 * Main - Part of the DotCom game program - Amr Ayman (c) 2015.
 * Just a simple command-line cross-platform game written in Java.
 *
 * This program is licensed under the GPLv2 License, and is thus considered
 * free software, implying the right to modify/distribute/sell this program for free.
 *
 * This code is intended to be written for Java 7 or higher.
 * It uses features introduced after Java 6, and is therefore compiled
 * with JDK7, JDK8 or possibly higher if backwards-compatibilty is maintained.
 */

/*
 * TODO:
 *	1. Add color support.
 *	2. Improve UI (possibly ncurses-like functionality)
*/

// imports
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Main {
// game settings
	static String[] comNames = {"Google.com", "Facebook.com", "LifeHacker.com", "Blogger.com", "Twitter.com", "GNU.org", "GNOME.org", "KDE.org",
		"Kernel.org", "Linux.com", "Microsoft.com", "Tumblr.com", "OpenSuSE.org", "Windows.com", "Xbox.com", "WikiPedia.com", "Java.com", "Python.org",
		"Instagram.com"};
	static DotComSettings settings;
// some vars
	static Scanner sc = new Scanner(System.in);
	static int maxLives = 6;
	static int lives = maxLives;
	static int maxComs = 0;
	static int attempts = 0;
	static int correct = 0;
	static int wrong = 0;
// main
	public static void main(String[] args) {
		// game preparation
		try {
			settings = new DotComSettings(3, 1, 20, false);
		} catch (InstantiationException e) { } // this shall never happen

		configMenu();
		DotCom.config(settings);

		// game loop
		for (int level = 1; lives > 0; ++level) {

			if (level > 1) {
				System.out.println("-----------------------------------New Level-----------------------------------");
				settings.maxBorder += 10;
				settings.numHits++;
				if (level % 5 == 0) {
					int bonus = level * calculateScore();
					if (bonus > 0) {
						System.out.printf("You got a bonus %d live%s!\n", bonus, bonus > 1 ? "s" : "");
						maxLives += bonus;
					}
				}
			}

			lives = maxLives;
			System.out.printf("Coms will reside from %d to %d.\nDotComs may%s overlap.\n" +
				"Must be hit %d time%s.\nYou have %d lives to go.\nLevel %d, Start!\n",
				settings.minBorder, settings.maxBorder, settings.mayOverlap ? "" : " not", settings.numHits, settings.numHits > 1 ? "s" : "", lives, level);
			createComs();

// DEBUG ONLY
/*
			for (DotCom com : DotCom.getAllComs()) {
				System.out.printf("%s: [ ", com);
				for (Place p : com.getPlaces())
					System.out.printf("%s ", p);
				System.out.println("]");
			}
*/
// DEBUG ONLY

			while (!DotCom.isAllDead() && lives > 0) {
				int guess = getInteger("Guess a number: ");

				if (guess > settings.maxBorder || guess < settings.minBorder) {
					System.out.printf("Not a valid number! Range is from %d to %d.\n", settings.minBorder, settings.maxBorder);
					continue;
				}

				++attempts;
				DotCom[] found = DotCom.hitAny(guess);
				if (found == null) {
					System.out.println("Miss!");
					--lives;
					++wrong;
				} else {
					for (DotCom com : found)
						System.out.printf("You %s %s!\n", com.isKilled() ? "killed" : "hit", com.getDomainName());
					++correct;
				}
			}
		}
		sc.close();
		// game end -- summary
		viewGameSummary();
	}
// methods
	// creates the required dotcoms for the game. maxComs is used.
	// if maxComs is 0, then createComs will attempt to create as many coms as possible.
	static void createComs() {
		DotCom.clearAllComs();
		Random rnd = new Random();
		ArrayList<Integer> used = new ArrayList<Integer>();

		int bound = ((settings.maxBorder - settings.minBorder) / settings.numHits);
		bound = (maxComs > bound || maxComs == 0) ? bound : maxComs;

		for (int i = 0; i < bound;) {
			int tmp = rnd.nextInt(bound);

			if (!used.contains(tmp)) {
				try {
					new DotCom(comNames[tmp]);
				} catch (InstantiationException e) { }
				used.add(tmp);
				++i;
			}
		}
	}

	static int getInteger(String out) {
		int c;
		while (true) {
			System.out.print(out);
			try {
				c = Integer.parseInt(sc.nextLine());
				return c;
			} catch (NumberFormatException e) {
				System.out.println("Not a number!");
			}
		}
	}

	// creates a menu-based configuration panel for altering DotCom settings
	static void configMenu() {
		int choice;
		while (true) {
			System.out.printf("Configure each DotCom:\n\t1. Hits required [%d]\n\t2. Number of Lives [%d]\n\t3. Minimum Border [%d]" +
				"\n\t4. Maximum Border [%d]\n\t5. May Overlap with other DotComs [%s]\n\t6. Start Now!\n",
				settings.numHits, lives, settings.minBorder, settings.maxBorder, settings.mayOverlap);
			choice = getInteger("Change: ");
			if (choice < 1 || choice > 6)
				continue;
			if (choice < 5) {
				choice = getInteger("Enter a number: ");
				if (choice < 1)
					continue;
				switch (choice) {
					case 1:
						settings.numHits = choice;
						break;
					case 2:
						lives = choice;
						break;
					case 3:
						settings.minBorder = choice;
						break;
					case 4:
						settings.maxBorder = choice;
						break;
				}
			} else {
				if (choice == 6)
					return;

				System.out.print("Enter true/false: ");
				switch (sc.next().toLowerCase()) {
					case "true":
						settings.mayOverlap = true;
						break;
					case "false":
						settings.mayOverlap = false;
						break;
					default:
						System.out.println("Wrong Value!");
						continue;
				}
			}
		}
	}

	static int calculateScore() {
		return (attempts == 0 ? 0 : ((correct/attempts)*100));
	}

	static void viewGameSummary() {
		System.out.println("-----------------------------------Summary-----------------------------------");
		for (DotCom com : DotCom.getAllComs()) {
			if (com.isKilled()) {
				System.out.printf("You killed %s!\n", com);
			} else {
				int hits = settings.numHits - com.getHitsLeft();
				if (hits > 0) {
					System.out.printf("You hit %s %d time%s!\n", com, hits, hits > 1 ? "s" : "");
				} else {
					System.out.printf("You never hit %s!\n", com);
				}
			}
		}

		if (correct > 0)
			System.out.printf("You were correct %d time%s!\n", correct, correct > 1 ? "s" : "");
		else
			System.out.println("You were never correct!");

		if (wrong > 0)
			System.out.printf("You were wrong %d time%s!\n", wrong, wrong > 1 ? "s" : "");
		else
			System.out.println("You were never wrong!");

		int score = calculateScore();
		System.out.printf("Your score is %d!\nYou were %s!\n", score, lives == 0 ? (score > 100 ? "not bad" : "horrible") : (score > 500 ? "awesome" : "good"));
		System.out.println("---------------------------------End Summary---------------------------------");
	}
}
