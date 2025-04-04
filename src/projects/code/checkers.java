import java.util.*;

class checkers {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		checkersGame2 game = new checkersGame2();
		String guess, command, playAgain;
	
		System.out.println ();
		System.out.println ("                        ,,");
		System.out.println ("   J  AAAA  K  K  EEEE  ,  SSSS");
		System.out.println ("   J  A  A  K K   E        S   ");
		System.out.println ("   J  AAAA  KK    EEEE     SSSS");
		System.out.println ("J  J  A  A  K K   E           S");
		System.out.println ("JJJJ  A  A  K  K  EEEE     SSSS");
		System.out.println ();
		System.out.println ("CCCC  H  H  EEEE  CCCC  K  K  EEEE  RRRR  SSSS");
		System.out.println ("C     H  H  E     C     K K   E     R  R  S   ");
		System.out.println ("C     HHHH  EEEE  C     KK    EEEE  RRRR  SSSS");
		System.out.println ("C     H  H  E     C     K K   E     R R      S");
		System.out.println ("CCCC  H  H  EEEE  CCCC  K  K  EEEE  R  R  SSSS");
		System.out.println ();
		System.out.println("Created by: Jake Mayo -> jakeryandesign.com");
		System.out.println ();
		System.out.println ("Directions: type the character in the middle follewed by direciton.");
		System.out.println("* Moves: (1) NorthWest (2) NorthEast (3) SouthWest (4) SouthEast");
		System.out.println ("* Example: move red piece (1) up right. Command will be: 12");
		System.out.println(" -- type Redo to take back move --");
		System.out.println ();

		do {
			playAgain = ""; //clear repeat command
			game.newGame(); //creates new game
			game.placePieces(); //puts pieces on board
			game.printBoard(); //prints board
			System.out.println("We will flip a coin to see who goes first.");
			
			do {
				System.out.print("Black please choose (h)Heads or (t)Tails? ");
				guess = sc.next();
			} while (!game.validFlipCoin(guess)); //loops till correct command

			game.flipCoin(guess); //flips coin

			do {
				do {
					game.whoNext();
					command = sc.next();
				} while (!(game.validCommand(command))); //loops till correct command

				game.makeMove(); //moves piece
				game.printBoard(); //prints board after move
				game.switchTurn(); //switch player turn
			} while (!game.endGame() && game.noMoves()); //loops till end of game or if there are no more moves

			System.out.print("Do you want to play again? Press (y): ");
			playAgain = sc.next();
		} while (playAgain.equals("y") || playAgain.equals("Y")); //loops if players want to play again
	}
}

/*
Checkers Board

 _______________________________
|___|BaB|___|BbB|___|BcB|___|BdB|
|BeB|___|BfB|___|BgB|___|BhB|___|
|___|BiB|___|BjB|___|BkB|___|BlB|
|||||___|||||___|||||___|||||___|
|___|||||___|||||___|||||___|||||
|r1r|___|r2r|___|r3r|___|r4r|___|
|___|r5r|___|r6r|___|r7r|___|r8r|
|r9r|___|r0r|___|r-r|___|r=r|___|
`````````````````````````````````
*/

class checkersGame2 {
	Scanner sc = new Scanner(System.in);
	Random random = new Random();
	// default piece names
	final String[] blackPiecesF = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"};
	final String[] redPiecesF = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "="};
	String[] blackPieces = new String[12]; //keeps record of pieces and whether they are captured or are kinged
	String[] redPieces = new String[12];
	String[][] board = new String[8][8]; //board whold black/red pieces and black/white squares
	String[][] boardCopy = new String[8][8]; //makes copy of board if player rewinds move
	String movePiece, command; //holds command
	String whiteSquare = "x"; //whitesquare marked x
	String blackSquare = "y"; //blacksquare marked y
	boolean blackTurn; //turn based on true/false
	int direction, marker;
	int column, newColumn;
	int jump, makeJump;
	int redCount, blackCount;
	int stopMove, noChange; //stopmove happens when a piece becomes a king
	int row, newRow;
	int flipCoinResult; //stores coin result
	int left = 1; //command directions. second part of command indicates which direction 
	int right = 2; // they want piece to move to 1-4
	int downLeft = 3;
	int downRight = 4;
	int firstMove = 0; //used for Redo command

	// resets pieces in arrays
	public void newGame() {
		for (int i = 0; i < redPieces.length; i++) {
			blackPieces[i] = blackPiecesF[i];
			redPieces[i] = redPiecesF[i];
		}
	}

	// creates board
	public void placePieces() {
		//lays out whiteSquares
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++){
				if (marker % 2 == 0)
					board[i][j] = whiteSquare;
			
					marker++;
			}
			
			marker++;
		}

		marker = 0;

		// place player pieces
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) 
				if (board[i][j] != whiteSquare) {
					if (i < 3) {
						board[i][j] = blackPieces[marker];
						marker++;
					} else if (i > 2 && i < 5) {
						marker = 0;
						board[i][j] = blackSquare;
					} else {
						board[i][j] = redPieces[marker];
						marker++;
					}
				}
		}
	}

	// prints board
	public void printBoard() {
		System.out.println(" _______________________________");
	
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == whiteSquare)
					System.out.print("|___");
				else if (board[i][j] == blackSquare)
					System.out.print("||||");
				else {
					if (pieceSearch(board[i][j], blackPieces) && !isKing(board[i][j])) 
						System.out.print("|B" + board[i][j] + "B");
					else if (isKing(board[i][j])) {
						if (pieceSearch(board[i][j], blackPieces)) 
							System.out.print("|B" + board[i][j].substring(0, 1) + "K");
						if (pieceSearch(board[i][j], redPieces)) 
							System.out.print("|r" + board[i][j].substring(0, 1) + "K");
					} else 
						System.out.print("|r" + board[i][j] + "r");
				}
			}
			
			System.out.println("|");
		}
		
		System.out.println("`````````````````````````````````");
	}

	//flip coin to decide who goes first
	public void flipCoin(String guess) {
		char guessChar = guess.charAt(0);
		//random number between 0-100
		flipCoinResult = random.nextInt(100);

		System.out.println();
		System.out.println("**********");

		//even = heads odd = tails
		if (flipCoinResult % 2 == 0)
			System.out.println("Coin = Heads");
		else
			System.out.println("Coin = Tails");

		//who goes first based on flip coin results
		if ((flipCoinResult % 2 == 0 && (guessChar == 'h' || guessChar == 'H')) || (flipCoinResult % 2 != 0 && (guessChar == 't' || guessChar == 'T'))) {
			blackTurn = true;
			System.out.println("Black goes first");
		} else {
			blackTurn = false;
			System.out.println("Red goes first");
		}
		
		System.out.println("**********");
		System.out.println();
	}

	//test if coin guess is correct input
	public boolean validFlipCoin(String guess) {
		char guessChar = guess.charAt(0);
		
		if (guess.length() > 1 || guess.length() < 0)
			return false;
		
		//checks if string is h or t upper or lower case	
		switch (guessChar) {
			case 'h': ;
			case 'H': ;
			case 'T': ;
			case 't': return true;
		}

		return false;
	}

	//kings piece that reaches to other side
	public void kingPiece() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < blackPiecesF.length; j++)
				if (board[7][i].equals(blackPieces[j]) && blackPieces[j].length() == 1) {
					stopMove = 1;
					blackPieces[j] += "K";
				}
	            
			for (int j = 0; j < redPiecesF.length; j++)
				if (board[0][i].equals(redPieces[j]) && redPieces[j].length() == 1) {
					stopMove = 1;
					redPieces[j] += "K";
				}
		}
	}

	//encodes command to two parts - 1: piece and 2: direction
	public void getCoordinates(String command) {
		movePiece = command.substring(0, 1);
		direction = Integer.parseInt(command.substring(1, 2));
	}

	//restores board to before last move
	public void redoMove () {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++) {
				if (!board[i][j].equals(boardCopy[i][j]))
					noChange++;
					
				board[i][j] = boardCopy[i][j];
			}
			
		if (noChange != 0) {
			printBoard();
			switchTurn();
			endGame();
		} else 
			System.out.println ("No more Redo's");
			
		noChange = 0;
	}

	//tests if command is valid
	public boolean validCommand(String commandCopy) {
		//redo board to last move
		if (commandCopy.equals("Redo") && firstMove != 0) {
			redoMove();
			return false;
		}
		
		firstMove = 1;
		command = commandCopy;

		// first test to see if it is valid in length and range
		if (command.length() != 2)
			return false;
		if (command.charAt(1) < 49 || command.charAt(1) > 52)
			return false;

		//decodes code to piece and direction		
		getCoordinates(commandCopy);

		//edge cases and if other pieces are in the way
		if (direction > 4 || direction < 0)
			return false;
		if (!moveBack() && stopMove == 0)
			return false;
		if (!validPiece(movePiece))
			return false;
		if (!edgeMove(movePiece, direction, board))
			return false;
		if (!rimMove(movePiece, direction, board))
			return false;
		if (sameTeam(row, column, direction))
			return false;
		if (jump == 0 && !blackSpace(row, column, direction) && !jumpOver(row, column, direction))
			return false;

		return true;
	}

	//if King piece can move again
	public void moveAgain() {
		row = newRow;
		column = newColumn;

		if (stopMove == 0)
			for (int i = 1; i <= 4; i++) {
				direction = i;
				command = command.substring(0, 1) + i;
				
				if (jumpOver(row, column, i) && moveBack()) {
					askToJump();
					
					if (makeJump == 1)
						makeMove();
				}
			}
		
		stopMove = 0;
		makeJump = 0;
		jump = 0;
	}

	//ask player if they want to jump again
	public void askToJump() {
		printBoard();
		System.out.print("Do you want to jump " + movePiece + " in direction " + direction + "? Press (1): ");
		makeJump = sc.nextInt();
	}

	// test if there are no more moves or if there is a win
	public boolean endGame() {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++) {
				if (pieceSearch(board[i][j], blackPieces))
					blackCount++;
				if (pieceSearch(board[i][j], redPieces))
					redCount++;
			}

		System.out.println("Remaining pieces. Black: " + blackCount + " Red: " + redCount);

		if (blackCount > 0 && redCount > 0) {
			blackCount = 0;
			redCount = 0;
			return false;
		}
		if (blackCount == 0) {
			System.out.println("Red Wins!!!");
			return true;
		}

		System.out.println("Black Wins!!!");
		return true;
	}

	//if player cannot move then they lose
	public boolean noMoves() {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++) 
				if (validPiece(board[i][j])) 
					for (int k = 1; k <= 4; k++) 
						if (validCommand(board[i][j] + k)) 
							return true;

		if (blackTurn) 
			System.out.println("Red Wins!!!");
		else 
			System.out.println("Black Wins!!!");
			
		return false;
	}

	//test if piece can move backwards
	public boolean moveBack() {
		if ((direction == downLeft || direction == downRight) && pieceSearch(movePiece, blackPieces)) 
			return true;
		if ((direction == left || direction == right) && pieceSearch(movePiece, redPieces) && !isKing(movePiece)) 
			return true;
		if (isKing(movePiece)) 
			return true;
	      
		return false;
	}

	// test if piece is King
	public boolean isKing(String movePiece) {
		for (int i = 0; i < redPieces.length; i++) {
			if (redPieces[i].substring(0, 1).equals(movePiece) && redPieces[i].length() == 2)
				return true;
			if (blackPieces[i].substring(0, 1).equals(movePiece) && blackPieces[i].length() == 2)
				return true;
		}
		
		return false;
	}

	//copys board
	public void copyBoard () {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				boardCopy[i][j] = board[i][j];
	}

	//moves piece
	public void makeMove() {
		copyBoard();
		
		if (blackSpace(row, column, direction)) {
			if (direction == left) {
				board[row - 1][column - 1] = movePiece;
				newRow = row - 1;
				newColumn = column - 1;
			} else if (direction == right) {
				board[row - 1][column + 1] = movePiece;
				newRow = row - 1;
				newColumn = column + 1;
			} else if (direction == downLeft) {
				board[row + 1][column - 1] = movePiece;
				newRow = row + 1;
				newColumn = column - 1;
			} else if (direction == downRight) {
				board[row + 1][column + 1] = movePiece;
				newRow = row + 1;
				newColumn = column + 1;
			}
		} else {
			if (direction == left) {
				board[row - 1][column - 1] = blackSquare;
				board[row - 2][column - 2] = movePiece;
				newRow = row - 2;
				newColumn = column - 2;
			}
			if (direction == right) {
				board[row - 1][column + 1] = blackSquare;
				board[row - 2][column + 2] = movePiece;
				newRow = row - 2;
				newColumn = column + 2;
			}
			if (direction == downLeft) {
				board[row + 1][column - 1] = blackSquare;
				board[row + 2][column - 2] = movePiece;
				newRow = row + 2;
				newColumn = column - 2;
			}
			if (direction == downRight) {
				board[row + 1][column + 1] = blackSquare;
				board[row + 2][column + 2] = movePiece;
				newRow = row + 2;
				newColumn = column + 2;
			}
	            
			jump = 1;
		}

		board[row][column] = blackSquare;
		kingPiece();

		if (jump == 1)
			moveAgain();

		stopMove = 0;
		jump = 0;
	}

	//switches turn
	public void switchTurn() {
		blackTurn = !blackTurn;
	}

	//prints who's next
	public void whoNext() {
		if (blackTurn)
			System.out.print("Black's Turn: ");
		else
			System.out.print("Red's Turn: ");
	}

	//can piece jumpOver?
	public boolean jumpOver(int row, int column, int direction) {
		if (direction == left && (row >= 2 && column >= 2) && !blackSpace(row, column, direction) && !sameTeam(row, column, direction) && blackSpace(row - 1, column - 1, direction))
			return true;
		if (direction == right && (row >= 2 && column <= 5) && !blackSpace(row, column, direction) && !sameTeam(row, column, direction) && blackSpace(row - 1, column + 1, direction))
			return true;
		if (direction == downLeft && (row <= 5 && column >= 2) && !blackSpace(row, column, direction) && !sameTeam(row, column, direction) && blackSpace(row + 1, column - 1, direction))
			return true;
		if (direction == downRight && (row <= 5 && column <= 5) && !blackSpace(row, column, direction) && !sameTeam(row, column, direction) && blackSpace(row + 1, column + 1, direction))
			return true;

		return false;
	}

	// test if piece is sameTeam
	public boolean sameTeam(int row, int column, int direction) {
		if (direction == left && !blackSpace(row, column, direction) && ((pieceSearch(movePiece, redPieces) == pieceSearch(board[row - 1][column - 1], redPieces)) || (pieceSearch(movePiece, blackPieces) == pieceSearch(board[row - 1][column - 1], blackPieces))))
			return true;
		if (direction == right && !blackSpace(row, column, direction) && ((pieceSearch(movePiece, redPieces) == pieceSearch(board[row - 1][column + 1], redPieces)) || (pieceSearch(movePiece, blackPieces) == pieceSearch(board[row - 1][column + 1], blackPieces))))
			return true;
		if (direction == downLeft && !blackSpace(row, column, direction) && ((pieceSearch(movePiece, redPieces) == pieceSearch(board[row + 1][column - 1], redPieces)) || (pieceSearch(movePiece, blackPieces) == pieceSearch(board[row + 1][column - 1], blackPieces))))
			return true;
		if (direction == downRight && !blackSpace(row, column, direction) && ((pieceSearch(movePiece, redPieces) == pieceSearch(board[row + 1][column + 1], redPieces)) || (pieceSearch(movePiece, blackPieces) == pieceSearch(board[row + 1][column + 1], blackPieces))))
			return true;

		return false;
	}

	//test if there is a valid piece to be moved
	public boolean validPiece(String movePiece) {
		if (blackTurn && pieceSearch(movePiece, blackPieces))
			return true;
		if (!blackTurn && pieceSearch(movePiece, redPieces))
			return true;
		
		return false;
	}

	//finds piece location
	public boolean pieceSearch(String movePiece, String[] group) {
		for (int i = 0; i < group.length; i++)
			if (group[i].substring(0, 1).equals(movePiece))
				return true;

		return false;
	}

	//test if edgemove
	public boolean edgeMove(String movePiece, int direction, String[][] board) {
		findLocation(movePiece);

		if (direction == left && (row == 0 || column == 0))
			return false;
		if (direction == right && (row == 0 || column == 7))
			return false;
		if (direction == downLeft && (row == 7 || column == 0))
			return false;
		if (direction == downRight && (row == 7 || column == 7))
			return false;

		return true;
	}

	//finds where piece is located
	public void findLocation(String movePiece) {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				if (board[i][j].substring(0, 1).equals(movePiece)) {
					row = i;
					column = j;
				}
	}

	//pieces on edge of board
	public boolean rimMove(String movePiece, int direction, String[][] board) {
		if (direction == left && ((row == 1 || column == 1) && !blackSpace(row, column, direction)))
			return false;
		if (direction == right && ((row == 1 || column == 6) && !blackSpace(row, column, direction)))
			return false;
		if (direction == downLeft && ((row == 6 || column == 1) && !blackSpace(row, column, direction)))
			return false;
		if (direction == downRight && ((row == 6 || column == 6) && !blackSpace(row, column, direction)))
			return false;

		return true;
	}

	//checks for blackSpace
	public boolean blackSpace(int row, int column, int direction) {
		if (direction == left && board[row - 1][column - 1].equals(blackSquare)) 
			return true;
		if (direction == right && board[row - 1][column + 1].equals(blackSquare)) 
			return true;
		if (direction == downLeft && board[row + 1][column - 1].equals(blackSquare)) 
			return true;
		if (direction == downRight && board[row + 1][column + 1].equals(blackSquare)) 
			return true;

		return false;
	}

	//prints pieces on board and count of pieces left
	public void printArray() {
		for (int i = 0; i < board.length; i++)
			System.out.println(Arrays.toString(board[i]));

		System.out.println(Arrays.toString(redPieces));
		System.out.println(Arrays.toString(blackPieces));
	}
}