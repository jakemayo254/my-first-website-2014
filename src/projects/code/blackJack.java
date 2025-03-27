import java.util.*;

class blackJack {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Table table = new Table(); //holds all the players and dealer hand
        Dealer dealer = new Dealer(); //controls cards and payments
        Deck deck; 
        String deckCountS, repeat;
        String endS = "";
        String choiceS = "";
        int deckCount = 1; //how mand decks are in stack of cards
        int handCount = 1;
        int bust = 0;
        int end, skip, choice;

        System.out.println();
        System.out.println("***************************************");
        System.out.println("*                           ,,        *");
        System.out.println("*      J  AAAA  K  K  EEEE  ,  SSSS   *");
        System.out.println("*      J  A  A  K K   E        S      *");
        System.out.println("*      J  AAAA  KK    EEEE     SSSS   *");
        System.out.println("*   J  J  A  A  K K   E           S   *");
        System.out.println("*   JJJJ  A  A  K  K  EEEE     SSSS   *");
        System.out.println("*                                     *");
        System.out.println("*    BBB   L     AAAA  CCCC  K  K     *");
        System.out.println("*    B  B  L     A  A  C     K K      *");
        System.out.println("*    BBB   L     AAAA  C     KK       *");
        System.out.println("*    B  B  L     A  A  C     K K      *");
        System.out.println("*    BBB   LLLL  A  A  CCCC  K  K     *");
        System.out.println("*                                     *");
        System.out.println("*         J  AAAA  CCCC  K  K         *");
        System.out.println("*         J  A  A  C     K K          *");
        System.out.println("*         J  AAAA  C     KK           *");
        System.out.println("*         J  A  A  C     K K          *");
        System.out.println("*      JJJJ  A  A  CCCC  K  K         *");
        System.out.println("*                                     *");
        System.out.println("***************************************");
        System.out.println();
        System.out.println("Created by: Jake Mayo -> jakeryandesign.com");
        System.out.println();
        System.out.println("* type no if you have NO INPUT for add/delete player");
        System.out.println("* Dealer hits on soft 17");
        System.out.println("* up to 5 players at a time");
        System.out.println("* Black Jack pays 3 to 2");
        System.out.println("* enter 0 if player wants to skip turn during bet");
        System.out.println();

        do {
            System.out.print("How many decks do you want? Enter 1 - 9 default 1: ");
            deckCountS = sc.next();
        } while (!table.isInteger(deckCountS)); //loops till valid entry between 1-9

        deckCount = Integer.parseInt(deckCountS); //convert string to Integer
        deck = new Deck(deckCount); //creates deck
        System.out.println();

        table.addPlayer(); //function adds players

        while (!endS.equals("no")) { //loops till no more players
            deck.shuffle(); //shuffle deck twice
            deck.shuffle();
            table.printNameMoney(); //print players name and money

            System.out.println("* Enter even amount or 0 for player to skip round *");
            System.out.println();

            dealer.placeBets(table); //players place bets
            dealer.firstDeal(table, deck); //dealer does first deal of cards to players
            table.printHidden(); //prints player hands but hides one dealer card
            dealer.insurance(table, table.dealer.hand.get(0), dealer); //checks if dealer should offer insurance

            if (table.dealer.hand.size() != 0)
                dealer.blackJack(table); //checks if players have black jack

			//loops through each player for turn
            for (int i = 0; i < table.players.size(); i++) {
                if (!table.players.get(i).hand.isEmpty()) {
                    System.out.println("+++++++++++++++");
                    System.out.println();
                }

			    //splits hand if player can
                dealer.splitHand(table.players.get(i), deck);

                for (Hand item: table.players.get(i).hand) {
                    choice = 0;

                    if (item.bJ && item.value1 != 1) {
                        item.gain(item.bet);
                        item.gain((item.bet * 3) / 2);
                    }

                    while (!item.bJ && (choice < 1 || choice > 4)) {
                        System.out.println(">>> " + table.players.get(i).name + " <<<");
                        System.out.println("*** Hand " + handCount + " ***");
                        item.printHand();
                        System.out.println();

                        if (table.players.get(i).cash >= item.bet) {
                            do {
                                do {
                                    System.out.print("Enter (1) Hit, (2) Stand, (3) Surrender, (4) Double Down ");
                                    choiceS = sc.next();
                                } while (!table.isInteger(choiceS));

                                choice = Integer.parseInt(choiceS);
                            } while (choice < 1 || choice > 4);
                        } else {
                            do {
                                do {
                                    System.out.print("Enter (1) Hit, (2) Stand, (3) Surrender ");
                                    choiceS = sc.next();
                                } while (!table.isInteger(choiceS));

                                choice = Integer.parseInt(choiceS);
                            } while (choice < 1 || choice > 3);
                        }
                    }

                    System.out.println();

                    while (!item.bJ && (choice == 1 && bust == 0) && (item.value1 != 21 && item.value2 != 21)) {
                        dealer.hit(item, deck);
                        System.out.println(">>> " + table.players.get(i).name + " <<<");
                        System.out.println("*** Hand " + handCount + " ***");
                        item.printHand();

                        if (item.value1 > 21) {
                            System.out.println("!!! Busted !!!");
                            bust = 1;
                        }

                        if (bust == 0 && (item.value1 != 21 && item.value2 != 21)) {
                            System.out.println();

                            do {
                                do {
                                    System.out.print("Enter (1) Hit, (2) Stand ");
                                    choiceS = sc.next();
                                } while (!table.isInteger(choiceS));

                                choice = Integer.parseInt(choiceS);
                            } while (choice != 1 && choice != 2);

                            System.out.println();
                        } else
                            System.out.println();
                    }

                    handCount++;
                    bust = 0;

                    if (choice == 3) {
                        System.out.println("SSS Surrender SSS");
                        System.out.println();
                        table.players.get(i).gain(item.bet / 2);
                        item.value1 = 2;
                    }

                    if (choice == 4) {
                        System.out.println("DDD DoubleDown DDD");
                        item.bet += table.players.get(i).bet(item.bet);
                        dealer.doubleDown(item, deck);
                        System.out.println();
                    }
                }
            }

            dealer.dealerTurn(table.dealer, table, deck); //dealer deals own hand
            dealer.compareHands(table.dealer, table); //compares dealer hand to player's hands
            table.postPrintTable();

            System.out.println("Win/Loss");

            //prints win/loss stats
            for (Player item: table.players) {
                System.out.print(item.name + " ");
                System.out.println(item.inPlayMoney);
            }

            System.out.println();

            table.printNameMoney();
            endS = table.addDelete();
            dealer.clearTable(table);

            if (endS.equals("no")) {
                for (Player x: table.players) {
                    table.deletePlayerEnd(x.name);
                }
            }

            System.out.println();
        }
    }
}

class Card {
    int deckOrder = 0; //decides which card this card will be
    int suit = 0;
    int rank = 0;
    int points = 2;
    String[] suits = {
        "Spades", "Hearts", "Clubs", "Diamonds"
    };
    String[] ranks = {
        "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"
    };

    Card(int deckOrder) {
        this.deckOrder = deckOrder % 52;
        suit = this.deckOrder / 13;
        rank = this.deckOrder % 13;

        if (rank >= 8 && rank <= 11)
            points = 10;
        else if (rank == 12)
            points = 1;
        else
            points += rank;
    }

    public void printCard() {
        System.out.println(ranks[rank] + " of " + suits[suit]);
    }

	//test if card is equal to card x
    public boolean equal(Card x) {
        return (this.rank == x.rank) ? true : false;
    }

	//test if card isAce
    public boolean isAce() {
        return (rank == 12) ? true : false;
    }
}

class Deck {
    Card[] deck; //deck is an array of type Cards
    int cardCount = 52; //each deck holds 52 cards
    int count, dealCard;
    int numberOfDecks = 1; 

	//default constructor with one deck of cards
    Deck() {
        deck = new Card[cardCount];

        for (int i = 0; i < cardCount; i++)
            deck[i] = new Card(i);
    }

	//if you want more then 1 deck of cards
    Deck(int numberOfDecks) {
        if (numberOfDecks < 1 || numberOfDecks > 10)
            numberOfDecks = 1;

        this.numberOfDecks = numberOfDecks;
        cardCount = cardCount * numberOfDecks;
        deck = new Card[cardCount];

        for (int i = 0; i < cardCount; i++)
            deck[i] = new Card(i);
    }

	//prints deck
    public void printDeck() {
        for (int i = 0; i < deck.length; i++)
            deck[i].printCard();
    }

	//shuffle cards
    public void shuffle() {
        Card temp;
        int y; 

        for (int i = 0; i < deck.length; i++) {
            y = (int)(Math.random() * 52 * numberOfDecks);

            temp = deck[i];
            deck[i] = deck[y];
            deck[y] = temp;
        }
    }

	//deal card on top of deck
    public Card dealCard() {
        return deck[dealCard++];
    }
}

class Hand extends Player {
    ArrayList < Card > hand = new ArrayList(); //hand is a list of cards
    int value1, value2; //hand can have two values if one card is an Ace
	int bet, insurance; //how much is betted on hand if hand has insurance
    boolean bJ = false; //black jack is true if initial hand is 21

    Hand() {}

	//add one card
    Hand(Card card) {
        addPoints(card);
        hand.add(card);
    }

	//add two cards
    Hand(Card card1, Card card2) {
        hand.add(card1);
        hand.add(card2);
        addPoints(card1);
        addPoints(card2);

        if (value2 == 21) //checks if black jack
            bJ = true;
    }

    public void addCard(Card card) {
        hand.add(card);
        addPoints(card);

			//if hand size is 2 check for black jack
        if (hand.size() == 2)
            if (value2 == 21)
                bJ = true;
    }

	//helper method for addCard
    public void addPoints(Card x) {
        if (x.isAce()) {
            if (value2 < 11) {
                value1 += 1;
                value2 += 11;
            } else {
                value1 += 1;
                value2 += 1;
            }
        } else {
            value1 += x.points;
            value2 += x.points;
        }
    }

	//print hand
    public void printHand() {
        for (Card item: hand)
            item.printCard();

        if (bJ)
            System.out.println("<<< BLACK JACK >>>");
        else if (value1 > 0) {
            System.out.print("<<< " + value1);

            if (value2 > 0 && value1 != value2 && value2 < 22)
                System.out.print(" or " + value2);

            System.out.println(" >>>");
        }
    }

	//splits hand by removing one card and returning that card
    public Card split() {
        Card temp = hand.get(1);
        value1 -= temp.points;
        value2 -= temp.points;
        hand.remove(1);

        return temp;
    }

	//erases hand values and cards
    public void clearHand() {
        hand.clear();
        value1 = 0;
        value2 = 0;
        bJ = false;
    }

	//test if you can split hand
    public boolean canSplit() {
        return (hand.size() == 2 && hand.get(0).equal(hand.get(1)));
    }

	//test if hand is bust
    public boolean bust() {
        if (value1 > 21)
            return true;

        return false;
    }
}

//player
class Player {
    ArrayList < Hand > hand = new ArrayList(); //list that holds all hands player can have
    String name; //player name
    int cash, initialCash, inPlayMoney;

    Player() {}

    Player(String name, int cash) {
        this.name = name;
        this.cash = cash;
        initialCash = cash; //records how much player came to table with
    }

	//when player makes bet it tak
    public int bet(int amount) {
        inPlayMoney -= amount;
        cash -= amount;
        return amount;
    }

    public void gain(int money) {
        inPlayMoney += money;
        cash += money;
    }

    public boolean hasFunds(int withdraw) {
        return (0 <= cash - withdraw) ? true : false;
    }

	//print name and cash
    public void print() {
        System.out.println(name + " " + cash);
    }

	//prints all hands for player
    public void printHand() {
        for (Hand item: hand)
            item.printHand();
    }

	//clears hands of player
    public void clearHand() {
        hand.clear();
        inPlayMoney = 0;
    }
}

//dealer controls table
class Dealer {
    Scanner sc = new Scanner(System.in);
    String split;
	 int outRound = 0;

    //clears table hands
    public void clearTable(Table table) {
        for (Player item: table.players)
            item.clearHand();

        table.dealer.clearHand();
    }

    //compares dealer's hand to player's hand
    public void compareHands(Hand dealer, Table table) {
        int dealerBestValue = 0;
        int playerBestValue = 0;
        int dtemp1 = 0;
        int dtemp2 = 0;
        int temp1 = 0;
        int temp2 = 0;

        if (!dealer.bJ && (dealer.value1 <= 21 || dealer.value2 <= 21)) {
            if (dealer.value1 <= 21)
                dtemp1 = dealer.value1;
            if (dealer.value2 <= 21)
                dtemp2 = dealer.value2;

            dealerBestValue = (dealer.value1 > dealer.value2) ? dealer.value1 : dealer.value2;
        }

        for (Player item: table.players) {
            for (Hand hand: item.hand) {
                if (hand.value1 <= 21)
                    temp1 = hand.value1;
                if (hand.value2 <= 21)
                    temp2 = hand.value2;

                playerBestValue = (temp1 > temp2) ? temp1 : temp2;

                if (!hand.bJ && playerBestValue > dealerBestValue && hand.value1 <= 21)
                    item.gain(hand.bet * 2);
                else if (!hand.bJ && (playerBestValue == dealerBestValue) && hand.value1 <= 21)
                    item.gain(hand.bet);
            }

            temp1 = 0;
            temp2 = 0;
        }

        System.out.println("playerBest " + playerBestValue);
        System.out.println("dealerBest " + dealerBestValue);
        System.out.println();
    }

    //cycles through players to place bets
    public void placeBets(Table table) {
        int bet, cash;
        int loop = 0;
        String cashS, betS;

        System.out.println("$$$$$$$$$$$$$");
        System.out.println();

        while (loop == 0) {
            for (Player item: table.players) {
                if (item.cash == 0) {
                    do {
                        do {
                            System.out.print(item.name + " add cash: ");
                            cashS = sc.next();
                        } while (!table.isInteger(cashS));

                        cash = Integer.parseInt(cashS);
                    } while (cash <= 0);

                    item.cash += cash;
                }

                do {
                    do {
                        System.out.print(item.name + " enter bet: ");
                        betS = sc.next();
                    } while (!table.isInteger(betS));

                    bet = Integer.parseInt(betS);
                } while (!item.hasFunds(bet) || bet % 2 != 0 || bet < 0);

					 if (bet == 0)
						outRound++;

                if (bet > 0) {
                    item.hand.add(new Hand());
                    item.bet(bet);
                    item.hand.get(0).bet = bet;
                }
            }

            for (Player item: table.players)
                if (!item.hand.isEmpty())
                    loop = 1;
        }

        System.out.println();
        System.out.println("$$$$$$$$$$$$$");
        System.out.println();
    }

    //initial deal after bets
    public void firstDeal(Table table, Deck deck) {
        for (Player item: table.players)
            if (!item.hand.isEmpty())
                item.hand.get(0).addCard(deck.dealCard());

        table.dealer.addCard(deck.dealCard());

        for (Player item: table.players)
            if (!item.hand.isEmpty())
                item.hand.get(0).addCard(deck.dealCard());

        table.dealer.addCard(deck.dealCard());
    }

    //scans table for black jacks and pays out
    public void blackJack(Table table) {
        for (Player item: table.players)
            if (!item.hand.isEmpty())
                if (item.hand.get(0).bJ) {
                    item.gain(item.hand.get(0).bet);
                    item.gain((item.hand.get(0).bet * 3) / 2);
                    item.hand.get(0).value1 = 1;
                    System.out.println("~~~ BLACK JACK ~~~");
                    System.out.println("~~~ BLACK JACK ~~~");
                    System.out.println();
                    System.out.println(">>> " + item.name + " <<<");

                    for (Card card: item.hand.get(0).hand)
                        card.printCard();

                    System.out.println();
                    System.out.println("~~~ BLACK JACK ~~~");
                    System.out.println("~~~ BLACK JACK ~~~");
                    System.out.println();
                }
    }

    //action if player wants to doubleDown
    public void doubleDown(Hand hand, Deck deck) {
        hand.addCard(deck.dealCard());

        if (hand.value1 > 21) {
            hand.printHand();

            System.out.println("!!! Busted !!!");
            System.out.println();
        }
    }

    //when player wants to hit
    public void hit(Hand hand, Deck deck) {
        hand.addCard(deck.dealCard());
    }

    //when every player is done dealer deals own hand till it reachers above 17 or when it beats all hands
    public void dealerTurn(Hand dealer, Table table, Deck deck) {
			outRound = 0;
        int highestHand = 0;
        boolean availablePlayers = false;

        for (Player item: table.players)
            for (Hand hand: item.hand)
                if ((hand.value1 <= 21 || hand.value2 <= 21) && (hand.value1 != 0)) {
                    availablePlayers = true;

                    if (hand.value1 <= 21 && hand.value1 > highestHand)
                        highestHand = hand.value1;
                    if (hand.value2 <= 21 && hand.value2 > highestHand)
                        highestHand = hand.value2;
                }

        if (availablePlayers && dealer.value2 != 21 && (dealer.value1 < highestHand || dealer.value2 <= 21 && dealer.value2 < highestHand)) {

            if (dealer.value2 <= 17 && dealer.value1 != dealer.value2)
                dealer.addCard(deck.dealCard());

            while (dealer.value1 <= highestHand && (dealer.value1 < 17 && !(dealer.value2 >= 17 && dealer.value2 <= 21))) {
                dealer.addCard(deck.dealCard());
            }
        }

        System.out.println(">>> Dealer <<<");
        dealer.printHand();
    }

    //if players want to buy insurance if dealer has either ace or 10 value card showing
    public void insurance(Table table, Card dealer, Dealer x) {
        String name = "";
		  int insurC = 0;

        if (dealer.points == 1 || dealer.points == 10) {
            do {
                System.out.print("Insurance? Enter name or no: ");
                name = sc.next();

                for (Player item: table.players)
                    if (item.name.toLowerCase().equals(name.toLowerCase()))
                        if (!item.hand.isEmpty()) {
                            item.hand.get(0).insurance = item.bet(item.hand.get(0).bet / 2);
									  insurC++;
								 }
            } while (!name.equals("no") && insurC < (table.players.size() - x.outRound));

				insurC = 0;
		
            // if dealer has black jack
            if (table.dealer.value2 == 21) {
					  System.out.println();
                System.out.println("~ Dealer HAS Black Jack ~");
                System.out.println();
					 table.dealer.printHand();
					 System.out.println();

                //pay out insurance buyers
                for (Player item: table.players) {
                    if (item.hand.get(0).insurance > 0)
                        item.gain(item.hand.get(0).insurance * 2);
                    if (item.hand.get(0).bJ)
                        item.gain(item.hand.get(0).bet);
                }

                clearTable(table);
            } else //dealer doesn't have black jack
                System.out.println("Dealer DOES NOT have Black Jack");

            System.out.println();
        }
    }

    //if player wants to split hand
    //recursive function
    public void splitHand(Player player, Deck deck) {
        int handCount = 1;

        for (int i = 0; i < player.hand.size(); i++) {

            if (player.hand.get(i).canSplit() && player.hasFunds(player.hand.get(i).bet)) {
                System.out.println(">>> " + player.name + " <<<");
                System.out.println("*** Hand " + (i + 1) + " ***");
                player.hand.get(i).printHand();

                System.out.println();
                do {
                    System.out.print("Split hand? Enter yes or no: ");
                    split = sc.next();
                } while (!split.equals("no") && !split.equals("yes"));

                if (split.equals("yes")) {
                    Card temp = player.hand.get(i).split();

                    player.hand.get(i).addCard(deck.dealCard());
                    player.hand.add(new Hand(temp, deck.dealCard()));
                    player.hand.get(player.hand.size() - 1).bet = player.bet(player.hand.get(i).bet);

                    // prints the split and results
                    System.out.println();
                    System.out.println(">< >< >< >< ><");
                    System.out.println(">< >< >< >< ><");
                    System.out.println();
                    System.out.println("--- " + player.name + " ---");

                    for (Hand hand: player.hand) {
                        System.out.println("*** Hand " + handCount++ + " ***");

                        hand.printHand();

                        System.out.println();
                    }

                    System.out.println(">< >< >< >< ><");
                    System.out.println(">< >< >< >< ><");
                    System.out.println();

                    // recursion call to check new hands for possible splits
                    splitHand(player, deck);
                }
            }
        }
    }
}

//Table holds the players and dealer hand
class Table {
    Scanner sc = new Scanner(System.in);
    ArrayList < Player > players = new ArrayList(); //list of players
    Hand dealer = new Hand(); //dealer hand

    Table() {}

    //adds and deletes players
    public String addDelete() {
        String answer; //holds answer to if they want to add/delete/continue/show table 

        do {
            if (players.size() < 5 && !players.isEmpty()) //if table has room for more players
                do {
                System.out.print("Enter d for delete, a for add, s to show table, Continue yes/no: ");
                answer = sc.next();
            } while (!answer.equals("d") && !answer.equals("a") && !answer.equals("no") && !answer.equals("yes") && !answer.equals("s"));
            else if (!players.isEmpty()) //if table is full
                do {
                System.out.print("Enter d for delete, s to show table, Continue yes/no: ");
                answer = sc.next();
            } while (!answer.equals("d") && !answer.equals("no") && !answer.equals("yes") && !answer.equals("s"));
            else //if table is empty and want to add players
                do {
                System.out.print("Enter a for add, s to show table, Continue yes/no: ");
                answer = sc.next();
            } while (!answer.equals("a") && !answer.equals("no") && !answer.equals("yes") && !answer.equals("s"));

            if (answer.equals("d")) //delete player
                deletePlayer();
            if (answer.equals("a")) //add player
                addPlayer();
            if (answer.equals("s")) { //print table players and money they have
                System.out.println();
                printNameMoney();
                System.out.println();
            }

            //return response if players want to continue or quit game
            if (answer.equals("no") || answer.equals("yes"))
                return answer;

        } while (!answer.equals("no") && !answer.equals("yes")); //loops till they are done add/delete players

        System.out.println();
        return answer;
    }

    //add player to game
    public void addPlayer() {
        Player temp;
        int amount;
        String name;
        String amountS = "";

        //only adds when table is less then or equal to 5 players
        while (players.size() < 5) {
            do {
                System.out.print("Enter Name: ");
                name = sc.next();
            } while (isInteger(name) && name == ""); //loops till name is valid

            if (name.equals("no")) //if they are done adding players break out of loop
                break;

            do {
                do { //enter amount of money 
                    System.out.print("Enter Amount (Even number): ");
                    amountS = sc.next();
                } while (!isInteger(amountS)); //loops till entry is an Integer

                amount = Integer.parseInt(amountS);
            } while (amount % 2 != 0); //loops till int is an even number

            temp = new Player(name, amount); //create player
            addPlayer(temp); //add player
        }

        System.out.println();
    }

    //helper method for addPlayer() adds player to list
    public void addPlayer(Player newName) {
        int test = 0; //flag if player already exists

        for (Player item: players) {
            //checks table to make sure not adding same player
            if (item.name.toLowerCase().equals(newName.name.toLowerCase())) {
                System.out.println("Can't add same player");
                test = 1;
                break;
            }
        }

        //if player is unique add them to the table
        if (test == 0)
            players.add(newName);
    }


    //delete players
    public void deletePlayer() {
        String name; // holds name that needs to be deleted

        while (!players.isEmpty()) { //loops while there are players to delete
            System.out.print("Delete Player, Enter name: ");
            name = sc.next();

            if (name.equals("no")) //break out of loop when done deleting
                break;

            deletePlayer(name); //delete player
        }
    }

    //prints info of player leaving table
    public void deletePlayer(String name) {
        for (int i = 0; i < players.size(); i++)
            if (players.get(i).name.toLowerCase().equals(name.toLowerCase())) {
                System.out.println();
                System.out.println("<<< " + players.get(i).name + " >>>");
                System.out.println("Initial Cash: " + players.get(i).initialCash);
                System.out.println("Leaving Cash: " + players.get(i).cash);
                System.out.println("Gain/Loss of: " + (players.get(i).cash - players.get(i).initialCash));
                System.out.println();
                players.remove(i);
            }
    }

    //prints all players info if everyone leaves game
    public void deletePlayerEnd(String name) {
        for (int i = 0; i < players.size(); i++)
            if (players.get(i).name.toLowerCase().equals(name.toLowerCase())) {
                System.out.println();
                System.out.println("<<< " + players.get(i).name + " >>>");
                System.out.println("Initial Cash: " + players.get(i).initialCash);
                System.out.println("Leaving Cash: " + players.get(i).cash);
                System.out.println("Gain/Loss of: " + (players.get(i).cash - players.get(i).initialCash));
                System.out.println();
                //players.remove(i);
            }
    }

    //print cards on table
    public void printTable() {
        System.out.println("--- Dealer ---");

        //dealer's hand
        for (Card item: dealer.hand)
            item.printCard();

        //player's hands
        for (Player item: players) {
            System.out.println("--- " + item.name + "---");
            item.printHand();
        }
    }

    //end game prints all cards on table. dealer hand not hidden
    public void postPrintTable() {

        System.out.println("****************");
        System.out.println("****************");
        System.out.println();
        System.out.println("--- Dealer ---");

        dealer.printHand();

        //print if dealer busted
        if (dealer.value1 > 21)
            System.out.println("!!! Busted !!!");

        System.out.println();

        //print player's hands
        for (Player item: players) {
            System.out.println("--- " + item.name + " ---");

            for (Hand hand: item.hand) {
                hand.printHand();

                if (hand.value1 > 21) //if busted
                    System.out.println("!!! Busted !!!");

                if (hand.value1 == 2) { //print if player surrendered
                    System.out.println("SSS Surrender SSS");
                    System.out.println();
                }
            }
            System.out.println();
        }

        //end seperator
        System.out.println("****************");
        System.out.println("****************");
        System.out.println();
    }

    //prints cards with dealer card hidden
    public void printHidden() {
        int count = 1;

        System.out.println("****************");
        System.out.println("****************");
        System.out.println();
        System.out.println("--- Dealer ---");

        dealer.hand.get(0).printCard();

        System.out.println("XXXXXXXXXXXXXX"); //gestures hidden card
        System.out.print("<<< " + dealer.hand.get(0).points);

        if (dealer.hand.get(0).isAce())
            System.out.print(" or 11 ");

        System.out.println(" >>>");
        System.out.println();

        for (Player item: players) {
            System.out.println("--- " + item.name + " ---");

            for (Hand hands: item.hand) {
                count++;
                hands.printHand();
            }
            count = 1;
            System.out.println();
        }

        System.out.println("****************");
        System.out.println("****************");
        System.out.println();
    }

    //prints player name and amount of money player has
    public void printNameMoney() {
        for (Player item: players)
            System.out.println(item.name + " $" + item.cash);

        System.out.println();
    }

    //checks if string is Integer
    public static boolean isInteger(String s) {
        try {
        Integer.parseInt(s);
			}
        catch (Exception e) {
        		return false;
			}
        return true;
    }
}