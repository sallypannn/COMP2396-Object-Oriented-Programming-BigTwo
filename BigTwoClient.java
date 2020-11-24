import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;


/**
 * BigTwoClient class models a client for the Big Two card game. 
 * This class,  implements the CardGame interface and models the Big Two card game logics. 
 * Besides, it also implements the NetworkGame interface and handles the communication with other clients 
 * by sending and receiving messages to and from a game server.
 * @author think
 *
 */
public class BigTwoClient implements CardGame, NetworkGame{
	
	/**
	 * This is the constructor for creating a BigTwoClient.
	 * (i) create 4 players and add them to the list of players; 
	 * (ii) create a Big Two table which builds the GUI for the game and handles user actions; 
	 * (iii) make a connection to the game server.
	 * 
	 */
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		deck = null;
		currentIdx = -1;
		handsOnTable = new ArrayList<Hand>();
		for (int i = 0; i < 4; i++) {
			CardGamePlayer p = new CardGamePlayer();
			playerList.add(p);
		}
		
		serverIP = "";
		serverPort = -1;
		playerName = "";
		String input = JOptionPane.showInputDialog("Please enter your name:\n");
		if (input == null) {
			System.exit(0);
		}else {
			while ("".equals(input)) {
				input = JOptionPane.showInputDialog("Please enter your name:\n");
			}
			playerName = input;
		}
		table = new BigTwoTable(this);	
		table.disableBeforeConnect();
		makeConnection();
	}
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
	private BigTwoTable table;
	private boolean change;
	
	/**
	 * @see CardGame#getNumOfPlayers()
	 * retrieves the number of players
	 * @return the number of players
	 */
	@Override
	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	/**
	 * @see CardGame#getDeck()
	 * retrieves the deck of cards being used
	 * @return the deck of cards being used
	 */
	@Override
	public Deck getDeck() {
		return deck;
	}

	/**
	 * @see CardGame#getPlayerList()
	 * retrieves the list of players
	 * @return the list of players
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}

	/**
	 * @see CardGame#getHandsOnTable()
	 * retrieves the list of hands played on the table
	 * @return the list of hands played on the table
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}

	/**
	 * @see CardGame#getCurrentIdx()
	 * retrieves the index of the current player
	 * @return the index of the current player
	 */
	@Override
	public int getCurrentIdx() {
		return currentIdx;
	}

	/**
	 * @see CardGame#start(Deck)
	 * starts/restarts the game with a given shuffled deck of cards.
	 * (i) remove all the cards from the players as well as from the table; 
	 * (ii) distribute the cards to the players; 
	 * (iii) identify the player who holds the 3 of Diamonds; 
	 * (iv) set the currentIdx of the BigTwoClient instance to the playerID (i.e., index) of the player who holds the 3 of Diamonds; 
	 * (v) set the activePlayer of the BigTwoTable instance to the playerID (i.e., index) of the local player 
	 * (i.e., only shows the cards of the local player and the local player can only select cards from his/her own hand).
	 * @param deck a (shuffled) deck of cards
	 */
	@Override
	public void start(Deck deck) {
		for (int i=0; i<4; i++) {
			playerList.get(i).removeAllCards();
		}
		handsOnTable.clear();
		this.deck = (BigTwoDeck)deck;
		for (int k=0; k<13; k++)
			playerList.get(0).addCard(deck.getCard(k));
		playerList.get(0).getCardsInHand().sort();
		for (int k=13; k<26; k++)
			playerList.get(1).addCard(deck.getCard(k));
		playerList.get(1).getCardsInHand().sort();
	    for (int k=26; k<39; k++)
			playerList.get(2).addCard(deck.getCard(k));
	    playerList.get(2).getCardsInHand().sort();
		for (int k=39; k<52; k++)
			playerList.get(3).addCard(deck.getCard(k));
		playerList.get(3).getCardsInHand().sort();
		boolean notfound = true; Card c = new Card(0,2);
		for (int i=0; i<4 && notfound; i++) {
			if (playerList.get(i).getCardsInHand().contains(c))
			{
				currentIdx = i;
				notfound = false;
			}
		}
		change = true;
		table.reset();
		table.setActivePlayer(currentIdx);
		table.printMsg("All players are ready. Game starts.\n");
		table.printMsg(playerList.get(currentIdx).getName()+"'s turn\n");
		table.repaint();
	}

	/**
	 * @see CardGame#makeMove(int, int[])
	 * sends the message of a move made by a player with the specified playerID using the cards specified by the list of indices to the server
	 * @param playerID the specified playerId representing the player
	 * @param cardIdx the specified list of indices representing the cards
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
	}

	/**
	 * @see CardGame#checkMove(int, int[])
	 * i.e. prints game message, decides the next player, rejects the move, ends the game, etc
	 * @param playerID the specified playerId representing the player
	 * @param cardIdx the specified list of indices representing the cards
	 */
	@Override
public void checkMove(int playerID, int[] cardIdx) {
		
		String msg = "";
		Card c = new Card(0,2);
		CardList list = null; 
		list = playerList.get(playerID).play(cardIdx);
		Hand lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable
				.get(handsOnTable.size() - 1);
		if (list==null) {
			if (lastHandOnTable==null) {
				msg = "{Pass} <== Not a legal move!!!";
				change = false;
			}
			else {
				if (lastHandOnTable.getPlayer().getName()==playerList.get(currentIdx).getName()){
					msg = "{Pass} <== Not a legal move!!!";
					change = false;
				}
				else {
					msg = "{Pass}";
					currentIdx = (currentIdx+1)%4;
					change = true;
				}
			}
		}
		else {
			String selectedcards = "";
			ArrayList<Card> temp = new ArrayList<Card>();
			for (int i=0; i<list.size();i++) {
				temp.add(list.getCard(i));
				selectedcards = selectedcards + "[" + temp.get(i) + "]";
			}
				
			Hand h = composeHand(playerList.get(playerID), list);
			
			if (h!=null) {
				if (lastHandOnTable==null) {
					if (list.contains(c)) {
						handsOnTable.add(h);
						msg = "{" + h.getType() + "} "+selectedcards;
						playerList.get(currentIdx).removeCards(list);
						currentIdx = (currentIdx+1)%4;
						change = true;
					}
					else {
						msg = "{" + h.getType() + "} "+selectedcards+" <== Not a legal move!!!";
						change = false;
					}
				}
				else {
					if (lastHandOnTable.getPlayer().getName()==playerList.get(currentIdx).getName()) {
						handsOnTable.add(h);
						msg="{" + h.getType() + "} "+selectedcards;
						playerList.get(currentIdx).removeCards(list);
						currentIdx = (currentIdx+1)%4;
						change = true;
					}
					else {
						if(h.beats(lastHandOnTable)) {
							handsOnTable.add(h);
							msg="{" + h.getType() + "} "+selectedcards;
							playerList.get(currentIdx).removeCards(list);
							currentIdx = (currentIdx+1)%4;
							change = true;
						}
						else {
							msg = "{" + h.getType() + "} "+selectedcards+" <== Not a legal move!!!";
							change = false;
						}
					}
				}
			}
			else {
				msg = selectedcards+" <== Not a legal move!!!";
				change = false;
			}
		}
		msg+="\n";
		table.printMsg(msg);
		
		if (change) {
			table.setActivePlayer(currentIdx);
		}
		
		table.resetSelected();
		table.repaint();
		
		if (endOfGame()) {
			String [] temp = new String[4];
			for (int i=0; i<4; i++) {
				CardGamePlayer temp2 = playerList.get(i);
				if (temp2.getNumOfCards()==0)
					temp[i]=temp2.getName()+" wins the game.\n";
				else
					temp[i]=temp2.getName()+" has "+temp2.getNumOfCards()+" in hand.\n";
			}
			
			int input = JOptionPane.showOptionDialog(null, "Game ends\n"+temp[0]+temp[1]+temp[2]+temp[3], "Result", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

			if(input == JOptionPane.OK_OPTION)
			{
			    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			
		}
		else {
			if (change) {
				table.printMsg(playerList.get(currentIdx).getName()+"'s turn\n");
			}
		}
	}

	@Override
	/**
	 * @see CardGame#endOfGame()
	 * checks if the game ends:
	 * If any of the players has no card in hand, the game should end.
	 * @return a boolean variable showing whether the game ends
	 */
	public boolean endOfGame() {
		int s = playerList.size();
		for (int i=0; i<s; i++) {
			if (playerList.get(i).getNumOfCards()==0) {
				table.disable();
				return true;
			}
		}
		return false;
		
	}

	
	
	/**
	 * @see NetworkGame#getPlayerID()
	 * retrieves the player ID of the local player
	 * @return an integer variable representing the player ID of the local player
	 */
	@Override
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * @see NetworkGame#setPlayerID(int)
	 * sets the player ID of the local player
	 * @param playerID a specified player ID
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
		
	}

	/**
	 * @see NetworkGame#getPlayerName()
	 * retrieves the name of the local player
	 * @return a string variable representing the name of the local player
	 */
	@Override
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @see NetworkGame#setPlayerName(java.lang.String)
	 * sets the name of the local player
	 * @param playerName a specified player name
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		
	}

	/**
	 * @see NetworkGame#getServerIP()
	 * retrieves the server IP
	 * @return a string variable representing the server IP
	 */
	@Override
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * @see NetworkGame#setServerIP(java.lang.String)
	 * sets the server IP
	 * @param serverIP a specified server IP
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
		
	}

	/**
	 * @see NetworkGame#getServerPort()
	 * retrieves the server port
	 * @return an integer variable representing the server port
	 */
	@Override
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @see NetworkGame#setServerPort(int)
	 * sets the server port
	 * @param serverPort a specified server port
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @see NetworkGame#makeConnection()
	 * makes a socket connection with the game server. 
	 * Upon successful connection, 
	 * (i) creates an ObjectOutputStream for sending messages to the game server; 
	 * (ii) creates a thread for receiving messages from the game server; 
	 * (iii) sends a message of the type JOIN to the game server; 
	 * (iv) sends a message of the type READY to the game server
	 */
	@Override
	public void makeConnection() {
		try {
			setServerIP("127.0.0.1");
			setServerPort(2396);
			sock = new Socket("127.0.0.1", 2396);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			Thread readerThread = new Thread(new ServerHandler());
			readerThread.start();
			sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		}catch (Exception ex) {
			ex.printStackTrace();
		}	
	}


	/**
	 * @see NetworkGame#parseMessage(GameMessage)
	 * parses the messages received from the game server. 
	 * Based on the message type, different actions will be carried out
	 * @param message a specified game message
	 */
	@Override
	public void parseMessage(GameMessage message) {
		int x = message.getType();
		if (x==CardGameMessage.PLAYER_LIST) {
			setPlayerID(message.getPlayerID());
			String [] temp = (String [])(message.getData());
			for (int i=0; i<temp.length; i++) {
				playerList.get(i).setName(temp[i]);
			}
			table.enableAfterConnect();
		}
		else if(x==CardGameMessage.JOIN) {
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.repaint();
		}
		else if(x==CardGameMessage.FULL) {
			table.printMsg("The server is full and you cannot join the game.\n");
			setServerIP("");
			setServerPort(-1);
		}
		else if(x==CardGameMessage.QUIT) {
			playerList.get(message.getPlayerID()).setName("");
			for (int i=0; i<4; i++) {
				playerList.get(i).removeAllCards();
			}
			handsOnTable.clear();
			table.clearMsgArea();
			table.repaint();
			table.disable();
			if (this.playerID != message.getPlayerID())
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		}
		else if(x==CardGameMessage.READY) {
			table.printMsg("Player "+ message.getPlayerID() + " is ready\n");
		}
		else if(x==CardGameMessage.START) {
			start((BigTwoDeck)message.getData());
		}
		else if(x==CardGameMessage.MSG) {
			table.printChat((String)message.getData()+"\n");
		}
		else if(x==CardGameMessage.MOVE) {
			checkMove(message.getPlayerID(), (int[])message.getData());
		}
	}

	/**
	 * @see NetworkGame#sendMessage(GameMessage)
	 * sends the specified message to the game server
	 * @param message a specified game message
	 */
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * closes the socket
	 */
	public void closeSocket() {
		try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The ServerHandler class is an inner class of the BigTwoClient class that implements the Runnable interface.
	 * @author think
	 *
	 */
	class ServerHandler implements Runnable{
		private ObjectInputStream ois;
		private CardGameMessage message;
		
		/**
		 * @see java.lang.Runnable#run()
		 * Upon receiving a message, the message should be parsed accordingly.
		 */
		public void run() {
			try {
				ois = new ObjectInputStream(sock.getInputStream());
				while ((message = (CardGameMessage)ois.readObject()) != null) {parseMessage(message);}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * creates an instance of BigTwoClient.
	 * @param args not used in this application
	 */
	public static void main(String[] args) {
		BigTwoClient client = new BigTwoClient();
	}
	
	/**
	 * returns a valid hand from the specified list of cards of the player;
	 * returns null if no valid hand can be composed from the specified list of cards
	 * @param player a card game player
	 * @param cards a specified list of cards
	 * @return a valid hand or null
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		StraightFlush straightflush = new StraightFlush(player, cards); 
		if (straightflush.isValid()) 
			return straightflush; 
		Single single = new Single(player, cards);	
		if (single.isValid())
			return single;
		Pair pair = new Pair(player, cards); 
		if (pair.isValid()) 
			return pair; 
		Triple triple = new Triple(player, cards); 
		if (triple.isValid()) 
			return triple;
		Straight straight = new Straight(player, cards); 
		if (straight.isValid())
			return straight; 
		Flush flush = new Flush(player, cards); 
		if (flush.isValid())
			return flush; 
		FullHouse fullhouse = new FullHouse(player, cards); 
		if (fullhouse.isValid()) 
			return fullhouse; 
		Quad quad = new Quad(player, cards);
		if (quad.isValid()) 
			return quad; 
		return null;
	}
}


	