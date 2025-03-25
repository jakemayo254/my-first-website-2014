import java.util.*;

class connectFour {
	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		connectFourG game = new connectFourG ();
		String command = ""; // handles command where player wants to place piece
		
		System.out.println ();
		System.out.println ("                        ,,");
		System.out.println ("   J  AAAA  K  K  EEEE  ,  SSSS");
		System.out.println ("   J  A  A  K K   E        S   ");
		System.out.println ("   J  AAAA  KK    EEEE     SSSS");
		System.out.println ("J  J  A  A  K K   E           S");
		System.out.println ("JJJJ  A  A  K  K  EEEE     SSSS");
		System.out.println ();
		System.out.println ("CCCCC  OOOOO  N   N  N   N  EEEEE  CCCCC  TTTTT      4   4");
		System.out.println ("C      O   O  NN  N  NN  N  E      C        T        4   4");
		System.out.println ("C      O   O  N N N  N N N  EEEEE  C        T    --  44444");
		System.out.println ("C      O   O  N  NN  N  NN  E      C        T            4");
		System.out.println ("CCCCC  OOOOO  N   N  N   N  EEEEE  CCCCCT   T            4");
		System.out.println ();
		System.out.println("Created by: Jake Mayo -> jakeryandesign.com");
		System.out.println();
		System.out.println("---------------- Directions ----------------"); 
		System.out.println("* Drop pieces in column");
		System.out.println("* Players switch turns till 4 pieces of either");
		System.out.println("  plaer are connected in a line");
		System.out.println("* Connected line can either be horizontal/vertical/diagonal");
		System.out.println();
		
		// prompts players to enter names
		game.getNames();
		
		do {
			System.out.println();
			System.out.println ("Directions: enter number column you want to drop piece");
			System.out.println ();
		
			game.createBoard();
			game.printBoard();
		
			do {
				System.out.println ();
				game.switchUser();
			
				do {
					game.printPrompt();
					command = sc.next();	
				} while (!game.validCommand(command)); //loops till valid command is entered
		
				game.placePiece();	
				System.out.println();
				game.printBoard();
			} while (!game.checkWin() && !game.noWin()); //checks for win or filled board with no win
			
			game.whoWins(); //prints who wins
		} while (game.repeat()); //asks if players want to play again
	}
}

/*
   0  1  2  3  4  5  6
||                     ||
||               *A*   ||
||         !X!   !X!*A*||
||      *A*!X!   !X!!X!||
||   *A*!X!*A**A*!X!*A*||
||!X!!X!*A**A*!X!*A**A*||0
*/

class connectFourG {
	Scanner sc = new Scanner (System.in);
	String [][] board = new String [6][7]; // board spots saved in array
	String player1 = "!X!"; //piece representation for players
	String player2 = "*A*";
	String player1Name = ""; //holds players names
	String player2Name = "";
	boolean player1Turn = false; //indicates which player's turn
	int columnCommand; // holds which column player wants piece
	int row = 0;
	int count = 0; //used to count when checking for 4 connected pieces
	int temp = 0; //used to check for win
	int full = 0;
	
	//prints boards and spaces that have pieces
	public void printBoard () {
		System.out.println ("   0  1  2  3  4  5  6");
		
		for (int i = 0; i < board.length; i++) {
			System.out.print ("||");
			
			for (int j = 0; j < board[0].length; j++) 
				System.out.print (board[i][j]);
			
			System.out.println ("||");
		}
		
		System.out.println ("||`````````````````````||");
	}
	
	// clears board
	public void createBoard () {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				board[i][j] = "   ";
	}
	
	// gets names for players
	public void getNames () {
		System.out.print ("Enter Player Name 1: ");
		player1Name = sc.next();
		System.out.print ("Enter Player Name 2: ");
		player2Name = sc.next();
	}
	
	//place piece on top of stack in column chosen
	//goes down column till first open space on stack
	public void placePiece () {
		for (int i = board.length-1; i >= 0; i--) 
			if (board[i][columnCommand].equals("   ")) {
				board[i][columnCommand] = (player1Turn) ? player1 : player2;
				row = i;
				break;
			}
	}
	
	// checks if command is valid
	public boolean validCommand (String number) {
		if (number.length() != 1)
			return false;
		if (number.charAt(0) < 48 || number.charAt(0) > 54)
			return false;
		if (fullColumn(Integer.parseInt(number)))
			return false;
		//if passed through tests convert string to int
		columnCommand = Integer.parseInt(number);
		return true;
	}
	
	// switch user after turn
	public void switchUser () {
		player1Turn = !player1Turn;
	}
	
	// tests if top spot in column is occupied with piece
	public boolean fullColumn (int number) {
		if (!board[0][number].equals("   "))
			return true;
		
		return false;
	}
	
	// prints which player to go next
	public void printPrompt () {
		if (player1Turn)
			System.out.print (player1Name + " " + player1 + ": ");
		else  
			System.out.print (player2Name + " " + player2 + ": ");
	}
	
	// checks for wins
	public boolean checkWin () {
		count = 0;
		
		// Up
		for (int i = row; i >= 0; i--) 
			if (board[i][columnCommand].equals(board[row][columnCommand]))
				count++;
			else  
				break;
		
		// Down
		for (int i = row; i < board.length; i++) 
			if (board[i][columnCommand].equals(board[row][columnCommand]))
				count++;
			else  
				break;
		
		if (count > 4) {
			//System.out.println ("count " + count + " down");
			return true;
		}	
				
		// Left
		count = 0;
		for (int i = columnCommand; i >= 0; i--) 
			if (board[row][i].equals(board[row][columnCommand]))
				count++;
			else  
				break;
		
		// Right
		for (int i = columnCommand; i < board[0].length; i++) 
			if (board[row][i].equals(board[row][columnCommand]))
				count++;
			else  
				break;
		
		if (count > 4) {
			//System.out.println ("count " + count + " right");
			return true;
		}	
				
		// Up Left
		count = 0;						
		temp = columnCommand;
		for (int i = row; i >= 0; i--) 
				if (temp >= 0 && board[i][temp].equals(board[row][columnCommand])) {
					count++;
					temp--;
				} else  
					break;
			
		// Down Right	
		temp = columnCommand;	
		for (int i = row; i < board.length; i++) 
			if (temp < board[0].length && board[i][temp].equals(board[row][columnCommand])) {
				count++;
				temp++;
			} else  
				break;
										
		if (count > 4) {
			//System.out.println ("count " + count + " down right");
			return true;
		}				
					
		// Up Right		
		count = 0;			
		temp = columnCommand;	
		for (int i = row; i >= 0; i--) 
				if (temp < board[0].length && board[i][temp].equals(board[row][columnCommand])) {
					count++;
					temp++;
				} else  
					break;
		
		// Down Left
		temp = columnCommand;
		for (int i = row; i < board.length; i++) 
				if (temp >= 0 && board[i][temp].equals(board[row][columnCommand])) {
					count++;
					temp--;
				} else  
					break;
						
		if (count > 4) {
			//System.out.println ("count " + count + " down left");
			return true;
		}					
						
		count = 0;	
		return false;
	}
	
	//checks if there are still plays
	public boolean noWin () {
		for (int i = 0; i < board[0].length; i++)
			if (board[0][i].equals("   "))
				return false;
		
		full = 1;		
		return true;
	}
	
	//asks players if they want to play again
	public boolean repeat () {
		System.out.print ("Play Again? Enter (y): ");
		String repeat = sc.next();
		
		if (repeat.equals("y") || repeat.equals("Y")) {
			System.out.println ();
			return true;
		}
		return false;
	}
	
	//prints who won
	public void whoWins () {
		System.out.println();
		System.out.println("******************");
		
		if (full == 1)
			System.out.println("No One Wins");
		else if (player1Turn)
			System.out.println (player1Name + " " + player1 + " Wins!!!");
		else
			System.out.println (player2Name + " " + player2 + " Wins!!!");
			
		System.out.println("******************");
		System.out.println();
		
		full = 0;
		player1Turn = !player1Turn;
	}	
}

