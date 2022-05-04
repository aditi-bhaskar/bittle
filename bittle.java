import java.util.* ;

// Written by Aditi
// spin-off of wordle
// but with bits && bytess
class Main {
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in) ;
		boolean is_play_over = false ;
		boolean is_game_over = false ;

		System.out.println("\n\n~~~~ PLAYING BITTLE ~~~~\n") ; 
		
		//other initializations
		ArrayList<Integer> bit_guess_count = new ArrayList<Integer>() ;
		ArrayList<Integer> byte_guess_count = new ArrayList<Integer>() ;

		//and, now to the game
		while(!is_play_over) {
				
			is_game_over = false ;

			// player action...
			System.out.print(".................................... \n" + 
												"Would you like to:\n" + 
												"  p - play bit mode (novice)?\n" +
												"  a - play byte mode (advanced)?\n" +
												"  s - display your stats\n" + 
												"  q - quit?\n" + 
												"\n>>") ;
			String play = s.nextLine() ;
			System.out.print(".................................... \n") ; 

			if (play.equals("q")) { // QUITTING
				is_play_over = true ;
				s.close() ;
				System.out.println("You have sucessfully quit bittle :)\n" + 
														"Have a \"binary\"-ific day!\n") ;
			} 
			else if (play.equals("s")) { // PRINT PLAYER STATS
				System.out.println("\n---Bit Stats--- \n" + statsPrinter(bit_guess_count)) ;
				System.out.println("\n---Byte Stats--- \n" + statsPrinter(byte_guess_count)) ;
			} 
			else if (play.equals("p") || play.equals("a")) { // PLAY GAME
				// P = regular (bit) mode
				// A = byte mode
				System.out.println("Starting game now.") ;
            
            // switching to/fro   
				int len ;
				String toggle = "" ;
				if (play.equals("p")) {
					len = 1 ;
					toggle = "bit mode" ;
				}
				else { // play.equals("a")
					len = 8 ;
					toggle = "byte mode" ;
				}

            //now, randomly creating the answer
				String ans = "" ;
				for (int i = 0; i < len; i++) {
					int idek = (int)(Math.random() * 2) ;
					if (idek == 0)
						ans += "0" ;
					else // idek == 1
						ans += "1" ;
				}

            // instructions on how to play
				System.out.println(".................................... \n" +
				"How to play:\n" +
				"A \"bit\" is a one or zero. bits (and bytes) represent stuff in binary.\n" +
            "Eight bits make a byte!\n" +
				"enter a starting " + konstantFinder(len) + ", then repeatedly enter \n" +
				"more " + konstantFinder(len) + "s until you guess the correct " + konstantFinder(len) + " \n" +
				" ->  * * means it's not in the " + konstantFinder(len) + "\n" +
				" ->  + + means it's in " + konstantFinder(len) + ", incorrect spot\n" +
				" ->      (by itself) means it's in " + konstantFinder(len) + ", correct spot\n" +
				"good luck!\n................................ \n" +
            "You are playing " + toggle + "\n") ;
				
				// starting the play
				ArrayList<String> past_guesses = new ArrayList<String>() ;
				int guess_count = 0 ;
				while (!is_game_over) {
					
					String guess = "" ;

					// taking a guess
					// note that len == 1 (unless you're in byte mode len == 8)
					System.out.println("\nenter your next guess............................") ;

					while (!isStringCorrectLength(guess, len) || !isStringBinary(guess)) {
						//taking a guess from user
						System.out.print("only " + len + " bits (either 0 or 1) allowed:\n>>") ;
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

					if (!incorrect) {   
						is_game_over = youWinOnceAgain(guess_count, len, ans) ;
						if (len == 1) 
							bit_guess_count.add(guess_count) ;
						else 
							byte_guess_count.add(guess_count) ;
					}
				}    
			} 
			else { //bc sometimes sentient beings aren't smart
				System.out.println("please input one of the below options.");
			}
		}  
	}

	// and now, for my little code-packages (aka methods aka functions aka procedures)
	/* "Brown paper packages tied up with strings...
			These are a few of my favorite things..."
	*/
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
			ret = "\nYour odds of being a robot are " + (maths * 30 / 100) ;
		}
      if (min == -1 || max == -1) {
         return "You haven't tried this mode yet :( " ;
      } //else {
         return " average guess count : " + Double.toString(maths) + 
         "\n minimum guess count : " + min +
         "\n maximum guess count : " + max +
         ret ;
      //}
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
		boolean ret = true ;
		if (g.length() != l) {
			ret = false ;
		}
		return ret ;
	}
	public static void printPastGuesses(ArrayList<String> past) {
		//printing
		System.out.print("Guess History\n");
		for (int i = 0; i < past.size(); i++) {
			System.out.print(past.get(i) + "\n");
		}
	}
	public static boolean youWinOnceAgain(int gc, int l, String a) {
		System.out.println("*******************************\n" + 
											"YOU WIN! \nGAME OVER!\n" + 
											"You took " + gc + " guess(es)\n" +
											"The " + konstantFinder(l) + " was: " + a + 
											"\n*******************************\n");    
		return true ;
	}
	public static String konstantFinder(int length) {
		String konstant ;
		if (length == 1) 
			konstant = "bit" ;
		else 
			konstant = "byte" ;
		return konstant ;
	}
	
}

