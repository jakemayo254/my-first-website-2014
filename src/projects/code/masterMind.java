import java.util.*;
import java.io.*;

class masterMind {
	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		masterMindGame2 game = new masterMindGame2();
		String reset = ""; // if reset is y game starts over again
		String guessS = ""; // 4 digit guess from player string to test for Integer
		int run = 1; // keeps track of how many trys if 12 then there is a win
		int count = 1; // prints guess attempt number
		int guess = 0; // guess value
		
		System.out.println();
		System.out.println("***********************************************");
		System.out.println("*                             ,,              *");
		System.out.println("*         J  AAAA  K  K  EEEE  ,  SSSS        *");
		System.out.println("*         J  A  A  K K   E        S           *");
		System.out.println("*         J  AAAA  KK    EEEE     SSSS        *");
		System.out.println("*      J  J  A  A  K K   E           S        *");
		System.out.println("*      JJJJ  A  A  K  K  EEEE     SSSS        *");
		System.out.println("*                                             *");
		System.out.println("*    M   M  AAAA  SSSS  TTTTT  EEEE  RRRR     *");
		System.out.println("*    MM MM  A  A  S       T    E     R  R     *");
		System.out.println("*    M M M  AAAA  SSSS    T    EEEE  RRR      *");
		System.out.println("*    M   M  A  A     S    T    E     R  R     *");
		System.out.println("*    M   M  A  A  SSSS    T    EEEE  R   R    *");
		System.out.println("*                                             *");
		System.out.println("*           M   M  IIIII  N   N  DDD          *");
		System.out.println("*           MM MM    I    NN  N  D  D         *");
		System.out.println("*           M M M    I    N N N  D  D         *");
		System.out.println("*           M   M    I    N  NN  D  D         *");
		System.out.println("*           M   M  IIIII  N   N  DDD          *");
		System.out.println("*                                             *");
		System.out.println("***********************************************");
		System.out.println ();
		System.out.println("Created by: Jake Mayo -> jakeryandesign.com");
		System.out.println();
		System.out.println("----------------- Directions ------------------");
		System.out.println("Crack a 4 digit CODE with hints after each guess");
		System.out.println("Guess a 4 digit number with digits 1 - 6");
		System.out.println("Allowed 10 guesses");
		System.out.println("White = correct digit but in wrong place");
		System.out.println("Red = correct digit in right place");
		System.out.println("Example -> Code: 1234 Guess: 1354 Hints(Red: 2 White: 1)");
		System.out.println("Because 1 and 4 are in the right spot Red = 2");
		System.out.println("Because 3 is in the code but in the wrong spot White = 1");
		System.out.println("Goal is to get 4 Reds");
		System.out.println ("Options are 1,2,3,4,5,6");
		
		do {
			count = 1;
			// generates code that needs to be cracked
			game.createCode();
			System.out.println ();
		
			do {
				do {
					//prompt user to enter guess and shows which guess they are on
					System.out.print("* " + count + " * " + "Enter guess: ");
					guessS = sc.next();
				} while (!game.isInteger(guessS)); //loops till correct input
				
				// convert string into int 
				guess = Integer.parseInt(guessS);
				// test if guess is correct
				run = game.tryAgain(guess);
				System.out.println();
				count++;
			} while (run < 11); //if guess is correct then it will return 12
		
			// run is only 12 when guess is correct and did it below 10 guesses
			if (run == 12) {
				System.out.println(":) :) :) :) :)");
				System.out.println ("You Win!!!");
				System.out.println(":) :) :) :) :)");
			} else { 
				System.out.println(":( :( :( :( :(");
				System.out.println ("You Lose");
				System.out.println(":( :( :( :( :(");
			}
			
			System.out.println ();		
			System.out.print ("Do you want to try again? Enter (y) ");
			reset = sc.next();
		} while (reset.equals("y") || reset.equals("Y")); //loops till player doesn't enter y
	}
}

class masterMindGame2 {
	Random random = new Random();
	int[] codeB = new int[4]; // holds code
	int[] codeTemp = new int [4]; // makes a copy of code to compare with guess
	int[] guessB = new int[4]; //guess in array form
	int[][] bracket = new int[10][4]; //keeps record of all guesses
	int[][] clue = new int[10][2]; //keeps record of clues for each guess
    int code = 0;
    int guess;
    int redCount = 0;
	int whiteCount = 0;
    int turn = 0;
    int ten = 1;
    int temp = 0;
    int breakDown = 0;
   
    // generates random 4 digit number for code
	public void createCode() {
		code = 0;
		
		for (int i = 0; i < 4; i++) {
			do {
				temp = random.nextInt(7);
			} while (temp == 0);
			
			code += temp * ten;
			ten = ten * 10;
		}
		
		ten = 1;
		breakDown = code;
		//System.out.println("code " + code);
		
		//puts code into an array
		for (int i = 3; i >= 0; i--) {
			codeB[i] = breakDown - ((breakDown / 10) * 10);
			breakDown = breakDown / 10;
		}
	}

	// checks if guess is correct or gives clues if incorrect
	public int tryAgain (int guess) {
		if (guess == code) {
			getClues (guess);
			System.out.println ();
			System.out.println("**********");
			System.out.println ("Tries = " + turn);
			System.out.println ("Code -> " + code);
			System.out.println("**********");
			
			turn = 0;
			return 12;
		//maxed attempts and prints code
		} else if (turn == 9) {
			getClues (guess);
			System.out.println();
			System.out.println("**********");
			System.out.println ("Code -> " + code );
			System.out.println("**********");
			turn = 0;
			return 13;
		} else {
			// prints clues
			getClues (guess);
			return turn;
		}
	}

	public void getClues (int guess) {
		breakDown = guess;
		
		// convert guess to array
		for (int i = 3; i >= 0; i--) {
			bracket[turn][i] = breakDown - ((breakDown / 10) * 10);
			guessB[i] = breakDown - ((breakDown / 10) * 10);
			breakDown = breakDown / 10;
		}
		
		// copys code to temp
		for (int i = 0; i < 4; i++)
			codeTemp[i] = codeB[i];
		
		// get redCount amount
		for (int j = 0; j < 4; j++) {
			if (bracket[turn][j] == codeB[j]) {
				redCount++;
				codeTemp[j] = 0;
				guessB[j] = 9;
			}
		}
		
		// get whiteCount amount
		for (int l = 0; l < 4; l++) 
			for (int k = 0; k < 4; k++) {
				if (codeTemp[l] == guessB[k]) {
					codeTemp[l] = 0;
					guessB[k] = 8;
					whiteCount++;
				}
			}
		
		// records red/white count in clue array
		clue[turn][0] = redCount;
		clue[turn][1] = whiteCount;
		// reset red/white count and increse turn
		redCount = 0;
		whiteCount = 0;
		turn++;
		// print results
		getResults();
	}
	
	// prints all attempts and clue results
	public void getResults () {
		for (int i = 0; i < turn; i++) {
			System.out.print (Arrays.toString(bracket[i]) + "  ");
			System.out.println ("Red " + clue[i][0] + " White " + clue[i][1]);
		}
	}

	// checks if guess is an Integer and in-range
	public static boolean isInteger(String s) {
		int guess = 0;
		int digit = 0;
		int count = 0;
		
		// test if Integer
		try {
			guess = Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		
		// test if in range
		while (guess != 0) {
			digit = guess % 10;
			
			if (count == 4)
				return false;
			
			if (digit < 1 || digit > 6)
				return false;
			
			count++;	
			guess = guess / 10;
		}
		
		// checks if guess is 4 digits
		return (count == 4) ? true : false;
	}
}






