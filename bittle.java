import java.util.* ;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
// Written by Aditi B
// spin-off of wordle
// but with bits
class Main {
  public static void main(String[] args) {
    
    Scanner s = new Scanner(System.in) ;
    boolean is_play_over = false ;
    boolean is_game_over = false ;

    System.out.println("\n\n~~~~ PLAYING BITTLE ~~~~\n") ; 

    System.out.println(".................................... \n" +
                        "How to play:\n" +
                        "A \"bit\" is a one or zero. bits represent stuffs in binary." + 
                        "enter a starting BIT," +
                        "then repeatedly enter more bits\n" +
                        "until you guess the correct bit \n" +
                        " >> *letter* means it's not in the bit\n" +
                        " >> +letter+ means it's in bit, incorrect spot\n" +
                        " >>  letter  (by itself) means it's in bit, correct spot\n" +
                        "good luck!\n ................................ \n") ;
    
    //other initializations
    ArrayList<String> past_guesses = new ArrayList<String>() ;
    int guess_count = 0 ;
    boolean byte_mode = false ;
    int len = 1 ;

    //and, now to the game
    while(!is_play_over) {
        
      is_game_over = false ;

      // player action...
      System.out.print(".................................... \n" + 
                        "Would you like to:\n" + 
                        "  e - enable/disable byte mode?\n" +
                        "  p - play the game?\n" +
                        "  q - quit?\n" + 
                        "enter the mode's corresponding letter \n>>") ;
      String play = s.nextLine() ;
      System.out.print(".................................... \n") ; 

      if (play.equals("q")) { // QUITTING
        is_play_over = true ;
        System.out.println("You have sucessfully quit :)") ;
      } 
      else if (play.equals("e")) { // BYTE MODE
       String toggle = "" ;
       if (byte_mode) {
         byte_mode = false ;
         len = 1 ;
         toggle = "disabled" ;
       }
       else {
         byte_mode = true ;
         len = 8 ;
         toggle = "enabled" ;
        }
        System.out.println("Eight bits make a byte!\n" + 
        "You have successfully " + toggle + " byte mode\n" + 
        "................................................") ;
      }
      else if (play.equals("p")) {// -- PLAY GAME
        System.out.println("Starting game now.") ;
        guess_count = 0 ;
        
        String ans = "" ;
        for (int i = 0; i < len; i++) {
          int idek = (int)(Math.random() * 2) ;
          if (idek == 0){
            ans += "0" ;
          }
          else { // idek == 1
            ans += "1" ;
          }
        }
        
        while (!is_game_over) {
          String guess = "" ;
          boolean isBinary = false ;
          // taking a guess
          // note that len usually = 1, unless you're in byte mode (len == 8)
          System.out.println("enter your next guess..............") ;
          while (guess.length() != len && !isBinary) {
            isBinary = true ;
            System.out.print("only " + len + " bits (either 0 or 1) allowed:\n>>") ;
            guess = s.nextLine() ;
            for (int i = 0; i < guess.length(); i++) {
              if (guess.substring(i, i+1) != "1" && guess.substring(i, i+1) != "0") {
                isBinary = false ;
              }
            }
          }
          guess_count += 1 ;

          //comparing
          String printing = "" ;
          boolean incorrect = false ;
          for(int i = 0; i < len; i++) {
            String ans_letter = ans.substring(i, i+1) ;
            String guess_letter = guess.substring(i, i+1) ;
            if (ans_letter.equals(guess_letter)) { // right bit, right pos
              printing += guess_letter + "    " ;
            }
            else { // (ans.indexOf(guess_letter) != -1) // wrong bit, wrong pos
              printing += "*" + guess_letter + "*  " ;
              incorrect = true ;
            }
          }

          //printing
          past_guesses.add(printing) ;
          if (!incorrect) {
            System.out.println("*******************************\n" + 
                               "YOU WIN! \nGAME OVER!\n" + 
                               "You took " + guess_count + " guesses\n" +
                               "The bit was: " + ans + 
                               "\n*******************************\n");    
            is_game_over = true ;  
          }
        }    
      } else { //bc sometimes people be dumb and don't follow instructions
        System.out.println("please input one of the options.");
      }
  
    }
  }
}

