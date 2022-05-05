import java.util.* ;

// Written by Aditi
// spin-off of wordle, but with bits && bytes
// **Command Line Game**
class Main {
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in) ;

		System.out.println("\n\n~~~~ PLAYING BITTLE ~~~~\n") ; 
		
		//other initializations
		ArrayList<Integer> bit_guess_count = new ArrayList<Integer>() ;
		ArrayList<Integer> byte_guess_count = new ArrayList<Integer>() ;

		//and, now to the game
		boolean is_play_over = false ; // for the "play"
		boolean is_game_over = false ; // for each specific sub-game of bittle
      boolean isPrintingInstructions = true ;
		while(!is_play_over) {
				
			is_game_over = false ;

			// player action...
			System.out.print(".................................... \n" + 
								"Would you like to:\n" + 
								"  p - play bit mode (novice)?\n" +
								"  a - play byte mode (advanced)?\n" +
                        "  i - turn on/off instructions?\n" + 
								"  s - display your stats?\n" + 
								"  q - quit?\n" + 
								"\n>> ") ;
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
         else if (play.equals("i")) { // INSTRUCTIONS on HOW TO PLAY
            isPrintingInstructions = !isPrintingInstructions ;
            System.out.println(" -- \"how to play\" switched " +
                              (isPrintingInstructions ? "on" : "off")) ;
         }
			else if (play.equals("p") || play.equals("a")) { // PLAY GAME
				// P = regular (bit) mode
				// A = byte mode
				System.out.println("Starting game now.") ;
			
			// switching to/fro   
				int len ;
            // len = 1 if entered letter was "p", and len = 8 otherwise
            len = play.equals("p") ? 1 : 8 ;
				

			   //now, randomly creating the answer
				String ans = "" ;
				for (int i = 0; i < len; i++) {
               //the value can either be 0 or 1; randomly chosen - 
               ans += ((int)(Math.random() * 2) == 0) ? "0" : "1" ;
				}

            if (isPrintingInstructions) {
               // instructions on how to play
               System.out.println(instructionsToPlay(len)) ;
            }

				// starting the game
				ArrayList<String> past_guesses = new ArrayList<String>() ;
				int guess_count = 0 ;
				while (!is_game_over) {
					
					String guess = "" ;

					// taking a guess
					// note that len == 1 (unless you're in byte mode len == 8)
					System.out.println("\nenter your next guess............................") ;

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
               // if you don't understand, then, so long.
               if (!incorrect || past_guesses.size() >= 42) {
                  if (len == 1)  //bit mode
                     bit_guess_count.add(guess_count) ;
                  else           //byte mode
                     byte_guess_count.add(guess_count) ;

                  is_game_over = true ;
                  if (!incorrect) 
                     youWinOnceAgain(guess_count, len, ans, "win");
                  else if (past_guesses.size() >= 42) {
                     youWinOnceAgain(guess_count, len, ans, "lose"); 
                  }
               }
				}    
			} 
			else { //bc sometimes sentient beings aren't smart
				System.out.println("please have some sense and input one of the below options.");
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
		return   " games played : " + guess_history.size() + 
               "\n average guess count : " + Double.toString(maths) + 
		 		   "\n minimum guess count : " + min +
		 		   "\n maximum guess count : " + max +
		         ret ;
	  //}
	}
	// I know it's spelled 'toggle', but the extra e makes it exciting
	public static String instructionsToPlay(int binaryLength) {
		return (".................................... \n" +
		"How to play:\n" +
		"A \"bit\" is a one or zero. bits (and bytes) represent stuff in binary.\n" +
		"Eight bits make a byte!\n" +
		"enter a starting " + konstantFinder(binaryLength) + ", then repeatedly enter more " +
      konstantFinder(binaryLength) + "s until you guess the correct " + konstantFinder(binaryLength) + " \n" +
		" ->  * * means it's not in the " + konstantFinder(binaryLength) + "\n" +
		" ->  + + means it's in " + konstantFinder(binaryLength) + ", incorrect spot\n" +
		" ->      (by itself) means it's in " + konstantFinder(binaryLength) + ", correct spot\n" +
		"good luck!\n................................ \n" +
		"You are playing " + konstantFinder(binaryLength) + " mode\n") ;
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
	public static void youWinOnceAgain(int guesscnt, int len, String ans, String isSuccess) {
		System.out.println("*******************************\n" + 
							"GAME OVER!\nYou " + isSuccess + 
							" with " + guesscnt + " guess(es)\n" +
							"The " + konstantFinder(len) + " was: " + ans + 
							"\n*******************************\n");    
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

