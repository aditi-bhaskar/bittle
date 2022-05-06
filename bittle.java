import java.util.* ;

// Written by Aditi 
// spin-off of wordle, but with bits && nibbles && bytes
// **Command Line Game**

// Hello world! 
// If you're reading this code, beware! it's really messy
//   and I intend on cleaning it up at some point but that
//   point may come any time within the next millenium

class Main {
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in) ;

		// so, the user commands are here so I can switch them easily
		final String quit = "q" ;
		final String play_bit_mode = "bit" ;
		final String play_nibble_mode = "nibble" ;
		final String play_byte_mode = "byte" ;
		final String get_instructions = "howto" ;
		final String display_stats = "stats" ;
		final ArrayList<String> commands = new ArrayList<String>() ;
		commands.add(quit) ; 				// 0
		commands.add(play_bit_mode) ; 		// 1
		commands.add(play_nibble_mode) ;    // 2
		commands.add(play_byte_mode) ; 		// 3
		commands.add(get_instructions) ; 	// 4
		commands.add(display_stats) ;		// 5

		// in case you didn't realize what you were playing: 
		System.out.println("\n\n~~~~ PLAYING BITTLE ~~~~\n") ; 
		
		// other initializations
		ArrayList<Integer> rubbish = new ArrayList<Integer>() ;
		ArrayList<ArrayList<Integer>> condensed_guess_counts = new ArrayList<ArrayList<Integer>>() ;
		condensed_guess_counts.add(0, rubbish) ;
		condensed_guess_counts.add(1, new ArrayList<Integer>()) ; // bits
		condensed_guess_counts.add(2, rubbish) ;
		condensed_guess_counts.add(3, rubbish) ;
		condensed_guess_counts.add(4, new ArrayList<Integer>()) ; // nibbles
		condensed_guess_counts.add(5, rubbish) ;
		condensed_guess_counts.add(6, rubbish) ;
		condensed_guess_counts.add(7, rubbish) ;
		condensed_guess_counts.add(8, new ArrayList<Integer>()) ; // bytes

		//and, now to the game
		boolean is_play_over = false ; // for the "play"
		boolean is_game_over = false ; // for each specific sub-game of bittle
		boolean isPrintingInstructions = true ;
		while(!is_play_over) {
					
			is_game_over = false ;

			// player action...
			String play = getPlayerAction(s, commands) ;

			if (play.equals(quit)) { // QUITTING
				is_play_over = true ;
				s.close() ;
				System.out.println("You have sucessfully quit bittle :)\n" + 
									"Have a \"binary\"-ific day!\n") ;
			} 
			else if (play.equals(display_stats)) { // PRINT PLAYER STATS
				System.out.println(
					"\n---Bit Stats--- \n" + 
					statsPrinter(condensed_guess_counts.get(1)) +
					"\n\n---Nibble Stats--- \n" + 
					statsPrinter(condensed_guess_counts.get(4)) +
					"\n\n---Byte Stats--- \n" + 
					statsPrinter(condensed_guess_counts.get(8))
				);
			} 
			else if (play.equals(get_instructions)) { // INSTRUCTIONS on HOW TO PLAY
				isPrintingInstructions = !isPrintingInstructions ;
				System.out.println(
					" -- \"how to play\" instructions switched " +
					(isPrintingInstructions ? "on" : "off")) ;
			}
			else if (play.equals(play_bit_mode) // PLAY GAME, bit-mode (len = 1) 
					|| play.equals(play_nibble_mode) // nibble mode (len = 4)
					|| play.equals(play_byte_mode)) { // advanced / byte mode (len = 8)
							
				int len = konstantFinderInverse(play, commands) ;

				//now, randomly creating the answer
				String ans = "" ;
				for (int i = 0; i < len; i++) {
					//the value can either be 0 or 1; randomly chosen - 
					ans += ((int)(Math.random() * 2) == 0) ? "0" : "1" ;
				}

				if (isPrintingInstructions) {
					// instructions on how to play
					System.out.println(instructionsToPlay(len, isPrintingInstructions)) ;
				}

				// starting the game
				ArrayList<String> past_guesses = new ArrayList<String>() ;
				int guess_count = 0 ;
				while (!is_game_over) {

					// taking a guess
					// note that len == 1 (unless you're in byte mode len == 8)
					System.out.println("\nenter your next guess............................") ;

					String guess = "" ;
					while (!isStringCorrectLength(guess, len) || !isStringBinary(guess)) {
						//taking a guess from user
						System.out.print("only " + len + " bits (either 0 or 1) allowed:\n>> ") ;
						guess = s.nextLine() ;
					}

					//incrementing guess count and complimenting player
					System.out.println("nice guess!") ;
					guess_count += 1 ;

					//comparing Strings and printing
					String printing = "" ;
					boolean incorrect = false ;
					for(int i = 0; i < guess.length(); i++) {
						String ans_letter = ans.substring(i, i+1) ;
						String guess_letter = guess.substring(i, i+1) ;
						if (ans_letter.equals(guess_letter)) { // right bit, right pos
							printing += " " + guess_letter + "   " ;
						}
						else { // (ans.indexOf(guess_letter) != -1) // wrong bit, wrong pos
							printing += "*" + guess_letter + "*  " ;
							incorrect = true ;
						}
					}
				
					//printing
					past_guesses.add(printing) ;
					printPastGuesses(past_guesses);

					// conditions for exiting the game
					// and, I use 42 as a max for obvious reasons here.
					// if you don't understand, then, so long...
					if (!incorrect || past_guesses.size() >= 42) {
						condensed_guess_counts.get(len).add(guess_count) ;

						is_game_over = true ;
						if (!incorrect) 
							winOrLose(guess_count, len, ans, "win");
						else if (past_guesses.size() >= 42) {
							winOrLose(guess_count, len, ans, "lose"); 
						}
					}
				}    
			} 
			else { //bc sometimes sentient beings aren't the most brilliant
				System.out.println("please have some sense and input one of the below options.");
			}
		}  
	}

	// and now, for my little code-packages (aka methods aka functions aka procedures)
	// they are in alphabetical order
	/* "Brown paper packages tied up with strings...
			These are a few of my favorite things..."
	*/
	
	public static String getPlayerAction(Scanner s, ArrayList<String> commands) {
		System.out.print(".................................... \n" + 
						"Would you like to:\n" + 
						"  " + commands.get(1) + "\t\t - play bit mode (novice)?\n" +
						// todo: add the \t back to nibble mode command
						"  " + commands.get(2) + "\t - play nibble mode (intermediate)?\n" +
						"  " + commands.get(3) + "\t\t - play byte mode (advanced)?\n" +
						"  " + commands.get(4) + "\t\t - turn on/off instructions?\n" + 
						"  " + commands.get(5) + "\t\t - display your stats?\n" + 
						"  " + commands.get(0) + "\t\t - quit?\n" + 
						"\n>> ") ;
		String play = s.nextLine() ;
		System.out.print(".................................... \n") ;
		return play ;
	}
	public static String instructionsToPlay(int len, boolean isPrintingInstructions) {
		return isPrintingInstructions ?
		".................................... \n" +
		"How to play:\n" +
		"A \"bit\" is a one or zero. bits (and nibbles, and bytes) represent stuff in binary.\n" +
		"Eight bits make a byte!\n" +
		"enter a starting " + konstantFinder(len) + ", then repeatedly enter more " +
		konstantFinder(len) + "s until you guess the correct " + konstantFinder(len) + " \n" +
		" ->  * * means it's not in the " + konstantFinder(len) + "\n" +
		" ->  + + means it's in " + konstantFinder(len) + ", incorrect spot\n" +
		" ->      (by itself) means it's in " + konstantFinder(len) + ", correct spot\n" +
		"good luck!\n................................ \n" +
		"You are playing " + konstantFinder(len) + " mode\n" 
		:
		"" ;
	}
	public static boolean isStringBinary(String g) {
		boolean ret = true ;
		//checking if the string is in binary (0 or 1)
		for (int i = 0; i < g.length(); i++) {
			if (!(g.substring(i, i+1).equals("1")) && !(g.substring(i, i+1).equals("0"))) {
				ret = false ;
			}
		}
		return ret ;
	}
	public static boolean isStringCorrectLength(String g, int l) {
	  //if the length isn't the length, then it probably isn't the correct length
		return (g.length() != l) ? false : true ;
	}
	public static void printPastGuesses(ArrayList<String> past) {
		//printing
		System.out.print("Guess History\n");
		for (int i = 0; i < past.size(); i++) {
			System.out.print(past.get(i) + "\n");
		}
	}
	// konstantFinder is for easy translation between answer's length to Strings
	public static String konstantFinder(int len) {
		// if the length = 1, then choose "bit"
		// otherwise choose "byte"
		// if the length = 4, then choose "nibble", else leave as is
		// return chosen string
		String ret = (len == 1) ? "bit" : "byte" ;
		ret = (len == 4) ? "nibble" : ret ;
		return ret ;
	}
	public static int konstantFinderInverse(String play, ArrayList<String> commands) {
		// switching to && fro modes! 
		//  1 if it's in bit mode, otherwise
		//  4 if it's in nibble mode, otherwie
		//  8 (default -> byte mode)
		//								byte						     nibble   	bit	
		return play.equals(commands.get(3)) ? 8 : (play.equals(commands.get(2)) ? 4 : 1);
		
	}
	public static String statsPrinter(ArrayList<Integer> guess_history) {
		// calculating stats
		double maths = 0.0 ;
	  int min = -1 ;
	  int max = -1 ;
	  if (guess_history.size() != 0) {
		 min = guess_history.get(0) ;
		 max = guess_history.get(0) ;
	  }
		for (Integer g : guess_history) {
			maths += g ;
			if (g < min)
				min = g ;
			if (g > max)
				max = g ;
		}
		if (guess_history.size() != 0)
			maths /= guess_history.size() ;
			// some random stuff
			String ret = "" ;
			if (maths >= 10) {
				ret = "\nYour odds of being a robot are " + (maths * 30 / 10) + "%" ;
			}
		// and, a lengthy return statement, starring my new obsession with ?:
		return (guess_history.size() == 0) ? 
				// if the mode hasn't been played yet
				"You haven't tried this mode yet :( " 
				: 
				// if mode has been played and there is data to show
				" games played : " + guess_history.size() + 
				"\n average guess count : " + Double.toString(maths) + 
				"\n minimum guess count : " + min +
				"\n maximum guess count : " + max +
				ret ;
	  
	}
	public static void winOrLose(int guesscnt, int len, String ans, String isSuccess) {
		System.out.println("*******************************\n" + 
							"GAME OVER!\nYou " + isSuccess + 
							" with " + guesscnt + " guess(es)\n" +
							"The " + konstantFinder(len) + " was: " + ans + 
							"\n*******************************\n");    
	}
	
}

