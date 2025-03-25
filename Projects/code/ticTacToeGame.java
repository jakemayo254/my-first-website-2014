import java.util.*;

class ticTacToeGame {
	public static void main(String[] args) {
		ticTacToeGameG game = new ticTacToeGameG();
		
		System.out.println ();
		System.out.println ("                        ,,");
		System.out.println ("   J  AAAA  K  K  EEEE  ,  SSSS");
		System.out.println ("   J  A  A  K K   E        S   ");
		System.out.println ("   J  AAAA  KK    EEEE     SSSS");
		System.out.println ("J  J  A  A  K K   E           S");
		System.out.println ("JJJJ  A  A  K  K  EEEE     SSSS");
		System.out.println ();
		System.out.println ("TTTTT  IIIII  CCCCC      TTTTT  AAAAA  CCCCC      TTTTT  OOOOO  EEEEE");
		System.out.println ("  T      I    C            T    A   A  C            T    O   O  E    ");
		System.out.println ("  T      I    C      --    T    AAAAA  C      --    T    O   O  EEEEE");
		System.out.println ("  T      I    C            T    A   A  C            T    O   O  E     ");
		System.out.println ("  T    IIIII  CCCCC        T    A   A  CCCCC        T    OOOOO  EEEEE ");
		System.out.println();
		System.out.println("Created by: Jake Mayo -> jakeryandesign.com");
		System.out.println();
		System.out.println ("Directions: Enter the number in square to place piece.");

		do {
			game.newArray(); //creates new board
			
			do {
				System.out.println ();
				game.printBoard();	//prints board 
				game.takeCommand(); //waits for command from player
				game.placePiece(); //places piece
			} while (!game.checkWin() && game.checkTurn()); //loops till win
		} while (game.playAgain()); // ask if they want to play again
	}
}

/*
     |     |     
  1  |  2  |  3  
_____|_____|_____
     |     |      
  4  |  5  |  6  
_____|_____|_____
     |     |     
  7  |  8  |  9  
     |     |     
XXX turn: 
*/

class ticTacToeGameG {
		Scanner sc = new Scanner (System.in);
		String [][] board = new String [3][3]; //String can either be a number/XXX/OOO
		String X = "XXX";
		String O = "OOO";
		String command = ""; // command is from 1 to 9 indicating which space to play
		String again = ""; // variable for if the players want to play again
		int turn = 0; // X = even or O = odd (X/O turn) base on turn value
		int count = 1; // count is used to fill spots with numbers
		int win = 0; // win value indicates what type of win either horizontal/vertical/diagonal
		int first = 0; //doesnt display wins till after first game
		int xwin = 0; //keeps track of wins
		int owin = 0;
		
		// print board with no win lines
		public void printBoard () {
			System.out.println ("     |     |     ");
			System.out.println (" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " ");
			System.out.println ("_____|_____|_____");
			System.out.println ("     |     |      ");
			System.out.println (" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " ");
			System.out.println ("_____|_____|_____");
			System.out.println ("     |     |     ");
			System.out.println (" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " ");
			System.out.println ("     |     |     ");
		}
		
		// prompt players to choose spot
		public void takeCommand () {
			if (turn % 2 == 0) {
				do {
					System.out.print ("XXX turn: ");
					command = sc.next();
				} while (!validCommand()); //loops till correct command
			} else {
				do {
					System.out.print ("OOO turn: ");
					command = sc.next();	
				} while (!validCommand()); // loops till correct command
			}
		}
		
		// checks if command is valid and spot is not taken
		public boolean validCommand () {
			if (command.length() != 1)
				return false;
			
			// checks board for spot
			for (int i = 0; i < board.length; i++)
				for (int j = 0; j < board[0].length; j++)
					if (board[i][j].equals(" " + command + " "))
						return true;
						
			return false;
		}
		
		// place piece on board
		public void placePiece () {
			for (int i = 0; i < board.length; i++)
				for (int j = 0; j < board[0].length; j++)
					if (board[i][j].equals(" " + command + " ")) {
						// place either X or O depending on turn
						if (turn % 2 == 0)
							board[i][j] = X;
						else 
							board[i][j] = O;
					}
		}
		
		// create new board and fill with numbers
		public void newArray () {
			// zero out turn and win variables
			turn = 0;
			win = 0;
			count = 1;

			// fill board with numbers
			for (int i = 0; i < board.length; i++)
				for (int j = 0; j < board[0].length; j++)
					board[i][j] = " " + count++ + " ";
					
			if (first > 0) {
				System.out.println();
				System.out.println("******************");
				System.out.println("Wins XXX: " + xwin + " OOO: " + owin);
				System.out.println("******************");
			}
			first = 1;
		}
		
		public boolean checkWin () {
			// check for win
			if (checkRow() || checkColumn() || checkDiagonal()) {
				printWinBoard();
				
				// modulos turn to see which player won 
				if (turn % 2 == 0) {
					xwin++;
					System.out.println ("XXX Wins!!!!!!");
					System.out.println ();
				} else {
					owin++;
					System.out.println ("OOO Wins!!!!!!");
					System.out.println ();
				}	
				return true;
			}
			
			// no winner and increase turn by one
			turn++;	
			return false;
		}
		
		// prints board depending on what value the variable "win" is
		public void printWinBoard () {
			// win in a row
			if (win == 1 || win == 4 || win == 7) {
				System.out.println ();
				System.out.println ("     |     |     ");
				
				if (win == 1)
					System.out.println ("-" + board[0][0] + "---" + board[0][1] + "---" + board[0][2] + "-");
				else  
					System.out.println (" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " ");
				
				System.out.println ("_____|_____|_____");
				System.out.println ("     |     |      ");
				
				if (win == 4)
					System.out.println ("-" + board[1][0] + "---" + board[1][1] + "---" + board[1][2] + "-");
				else  
					System.out.println (" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " ");
				
				System.out.println ("_____|_____|_____");
				System.out.println ("     |     |     ");
				
				if (win == 7)
					System.out.println ("-" + board[2][0] + "---" + board[2][1] + "---" + board[2][2] + "-");
				else  
					System.out.println (" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " ");
				
				System.out.println ("     |     |     ");
			// win in a column
			} else if (win == 3 || win == 9 || win == 15) {
				if (win == 3) {
					System.out.println ("  |  |     |     ");
					System.out.println (" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " ");
					System.out.println ("__|__|_____|_____");
					System.out.println ("  |  |     |      ");
					System.out.println (" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " ");
					System.out.println ("__|__|_____|_____");
					System.out.println ("  |  |     |     ");
					System.out.println (" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " ");
					System.out.println ("  |  |     |     ");
				} else if (win == 9) {
					System.out.println ("     |  |  |     ");
					System.out.println (" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " ");
					System.out.println ("_____|__|__|_____");
					System.out.println ("     |  |  |      ");
					System.out.println (" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " ");
					System.out.println ("_____|__|__|_____");
					System.out.println ("     |  |  |     ");
					System.out.println (" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " ");
					System.out.println ("     |  |  |     ");
				} else {
					System.out.println ("     |     |  |  ");
					System.out.println (" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " ");
					System.out.println ("_____|_____|__|__");
					System.out.println ("     |     |      ");
					System.out.println (" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " ");
					System.out.println ("_____|_____|__|__");
					System.out.println ("     |     |     ");
					System.out.println (" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " ");
					System.out.println ("     |     |  |  ");
				}
			// win in a diagonal
			} else if (win == 17) {
				System.out.println ();
				System.out.println ("\\    |     |     ");
				System.out.println (" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " ");
				System.out.println ("____\\|_____|_____");
				System.out.println ("     |\\    |      ");
				System.out.println (" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " ");
				System.out.println ("_____|____\\|_____");
				System.out.println ("     |     |\\    ");
				System.out.println (" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " ");
				System.out.println ("     |     |    \\ ");
				System.out.println ();
			} else {
				System.out.println ();
				System.out.println ("     |     |    /");
				System.out.println (" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " ");
				System.out.println ("_____|_____|/____");
				System.out.println ("     |    /|      ");
				System.out.println (" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " ");
				System.out.println ("_____|/____|_____");
				System.out.println ("    /|     |     ");
				System.out.println (" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " ");
				System.out.println ("/    |     |     ");
				System.out.println ();
			}
		}
		
		// check rows for win
		public boolean checkRow () {
			for (int i = 0; i < board.length; i++)
				if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
					win = (i*3)+1;
					return true;
				}
					
			return false;
		}
		
		// check columns for win
		public boolean checkColumn () {
			for (int i = 0; i < board.length; i++)
				if (board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
					win = (i*6)+3;
					return true;
				}
							
			return false;
		}
		
		// check diagnonals for win
		public boolean checkDiagonal () {
			if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
				win = 17;
				return true;
			}
			
			if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
				win = 16;
				return true;
			}		
			
			return false;
		}
		
		public boolean checkTurn () {
			// if turn less then 9 game is still in play
			if (turn < 9) {
				return true;
			}
			
			// after 9 turns the game is draw and draws C on board
			System.out.println ();
			System.out.println ("     |     |     ");
			System.out.println (" " + board[0][0] + "-|-" + board[0][1] + "-|-" + board[0][2] + "-");
			System.out.println ("__|__|_____|_____");
			System.out.println ("  |  |     |      ");
			System.out.println (" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " ");
			System.out.println ("__|__|_____|_____");
			System.out.println ("  |  |     |     ");
			System.out.println (" " + board[2][0] + "-|-" + board[2][1] + "-|-" + board[2][2] + "-");
			System.out.println ("     |     |     ");
			
			System.out.println ("Cat!!! No one Wins");
			System.out.println ();
			return false;	
		}
		
		// prompt to ask if players want to play again
		public boolean playAgain () {
			System.out.print ("Play again? Enter (y): ");
			again = sc.next();
			
			if (again.equals("y") || again.equals("Y"))
				return true;
			
			// exit statement
			System.out.println("Thanks for playing!");
			return false;
		}
}


