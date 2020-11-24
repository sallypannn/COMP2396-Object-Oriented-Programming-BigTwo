import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

/**
 * 
 * The BigTwoTable class implements the CardGameTable interface. 
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * @author think
 *
 */
public class BigTwoTable implements CardGameTable {
	/**
	 * This is a constructor for creating a BigTwoTable. 
	 * This will initialize all the instance variables of BigTwoTable class and set the layout of the  whole frame.
	 * @param game a reference to a card game associated with this table
	 */
	public BigTwoTable(CardGame game) {
		this.game = game;
		mouseEnabled = true;
		selected = new boolean[13];
		setActivePlayer(game.getCurrentIdx());
		
		cardImages = new Image[4][13];
		String[] suits = { "d", "c", "h", "s" }; // {Diamond, Club, Heart, Spade}
		char[] ranks = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' };
		for (int i=0; i<4; i++) {
			for (int j=0; j<13; j++) {
				String name =ranks[j]+suits[i]+".gif";
				cardImages[i][j]= new ImageIcon(name).getImage();
			}
		}
		
		cardBackImage = new ImageIcon("back.gif").getImage();
		
		avatars = new Image[4];
		avatars[0] = new ImageIcon("Potter.png").getImage();
		avatars[1] = new ImageIcon("Granger.png").getImage();
		avatars[2] = new ImageIcon("Weasley.png").getImage();
		avatars[3] = new ImageIcon("Voldemort.png").getImage();
		
		backgroundImage = new ImageIcon("Hogwarts.jpg").getImage();
		
		frame = new JFrame();
		frame.setTitle("Big Two ("+((BigTwoClient)game).getPlayerName()+")");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(750, 600);
		
		bar = new JPanel();
		bar.setOpaque(false);
		
	    playButton = new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		bar.add(playButton);
		
		passButton = new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		bar.add(passButton);
		
		message = new JLabel("Message");
		bar.add(message);
		
		inputBox = new JTextField(20);
		inputBox.setEditable(true);
		inputBox.setVisible(true);
		inputBox.setAutoscrolls(true);
		bar.add(inputBox);
		
		
		bigTwoPanel = new BigTwoPanel();
		
		msgArea = new JTextArea();
		msgArea.setLineWrap(true);
		msgArea.setEditable(false);
		JScrollPane scroller = new JScrollPane(msgArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
		
		chatArea = new JTextArea();
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		inputBox.addActionListener(new InputBoxListener());
		JScrollPane scroller2 = new JScrollPane(chatArea);
		scroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
		
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(scroller);
		rightPanel.add(scroller2);
		
		
		frame.add(bigTwoPanel, BorderLayout.CENTER);
		frame.add(rightPanel, BorderLayout.EAST);
		rightPanel.setPreferredSize(new Dimension(frame.getWidth()/3, rightPanel.getHeight()));
		frame.add(bar, BorderLayout.SOUTH);
		
		
		menuBar = new JMenuBar();
		menu = new JMenu("Game");
		restart = new JMenuItem("Connect");
		restart.addActionListener(new RestartMenuItemListener());
		menu.add(restart);
		quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitMenuItemListener());
		menu.add(quit);
		menuBar.add(menu);
		
		msgMenu = new JMenu("Message");
		clear = new JMenuItem("Clear");
		clear.addActionListener(new ClearMenuItemListener());
		msgMenu.add(clear);
		menuBar.add(msgMenu);
		
		
		frame.setJMenuBar(menuBar);
		
		scroller.setPreferredSize(new Dimension(scroller.getWidth(), rightPanel.getHeight()/2));
		scroller.setMinimumSize(new Dimension(scroller.getWidth(), 250));
		frame.setVisible(true);
	}
	private CardGame game;
	private boolean mouseEnabled;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea chatArea;
	private JTextField inputBox;
	private JLabel message;
	private JPanel bar;
	private JPanel rightPanel;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenu msgMenu;
	private JMenuItem clear;
	private JMenuItem restart;
	private JMenuItem quit;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	private Image backgroundImage;
	/**
	 * @see CardGameTable#setActivePlayer(int)
	 * sets the index of the active player, i.e. the current player
	 * @param activePlayer the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
		resetSelected();
	}
	
	/**
	 * @see CardGameTable#getSelected()
	 * gets an array of indices of the cards selected
	 * @return an integer array of indices of the cards selected
	 */
	public int[] getSelected() {
		int count =0;
		for (int i=0; i<13; i++) {
			if (selected[i]==true)
				count++;
		}
		int[] selectedcards = new int[count];
		count = 0;
		for (int i=0; i<13; i++) {
			if (selected[i]==true) {
				selectedcards[count] = i;
				count++;
			}
		}
		return selectedcards;
	}
	
	/**
	 * @see CardGameTable#resetSelected()
	 * resets the list of selected cards, i.e. removes all selected cards
	 */
	public void resetSelected() {
		for (int i=0; i<13; i++) {
			selected[i] = false;
		}
	}
	
	/**
	 * @see CardGameTable#repaint()
	 * repaints the GUI
	 */
	public void repaint() {
		if (((BigTwoClient)game).getPlayerID()==activePlayer)
			enable();
		else
			disable();
		frame.repaint();
	}
	/**
	 * @see CardGameTable#printMsg(java.lang.String)
	 * prints the specified string to the message area of the GUI
	 * @param msg the specified string to be printed
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	/**
	 * @see CardGameTable#clearMsgArea()
	 * clears the message area of the GUI
	 */
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	
	/**
	 * prints the specified string to the chat message area of the GUI
	 * @param msg the specified string to be printed
	 */
	public void printChat(String msg) {
		chatArea.append(msg);
	}
	
	/**
	 * clears the chat message area
	 */
	public void clearChatArea() {
		chatArea.setText("");
	}
	
	/**
	 * @see CardGameTable#reset()
	 * resets the GUI:
	 * resets the list of selected cards,
	 * clears the message area,
	 * enables user interactions
	 */
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}
	/**
	 * @see CardGameTable#enable()
	 * enables user interactions with the GUI:
	 * enables the "Play" button,
	 * enables the "Pass" button,
	 * enables the BigTwoPanel for selection of cards through mouse clicks
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		mouseEnabled = true;
	}

	/**
	 * @see CardGameTable#disable()
	 * disables user interactions with the GUI:
	 * disables the "Play" button,
	 * disables the "Pass" button,
	 * disables the BigTwoPanel for selection of cards through mouse clicks
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		mouseEnabled = false;
	}
	
	
	/**
	 * disables the "Play" button, "Pass" button, selection of cards through mouse clicks, and the input box
	 * before the client connects with the server.
	 */
	public void disableBeforeConnect() {
		disable();
		inputBox.setEnabled(false);
	}
	
	/**
	 * enables the input box after the client connects with the server
	 */
	public void enableAfterConnect() {
		inputBox.setEnabled(true);
	}
	
	/**
	 * The BigTwoPanel class extends the JPanel class and implements the MouseListener interface.
	 * @author think
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1558682788460482099L;
		private JLabel[] labels;	
			
		/**
		 * This is a constructor for creating a BigTwoPanel.
		 * adds the labels showing the player's name to the BigTwoPanel.
		 * adds labels showing the player name for the last Hand on table to the BigtwoPanel.
		 * registers MouseListener with the BigTwoPanel object.
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
			labels = new JLabel[5];
			for (int i=0; i<4; i++) {
				String playername = "Player " + i;
				labels[i] =  new JLabel(playername);
				this.add(labels[i]);
			}
			labels[4] = new JLabel("No Hand On Table");
			labels[4].setForeground(Color.orange);
			this.add(labels[4]);
		}
		
		/**
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 * Implements the mouseClicked() method from the ActionListener interface to handle the mouse click events.
		 * It will get the coordinates of the mouse click, and check whether a card is selected by the mouse click. 
		 * Once a card is selected, the selected card will be drawn in a "raised" position with respect to the rest of the cards.
		 * @param event an object of MouseEvent class
		 */
		public void mouseClicked(MouseEvent event) {
			if (mouseEnabled) {
				int x = event.getX();
				int y = event.getY();
				CardGamePlayer p = game.getPlayerList().get(activePlayer);
				int width = this.getWidth();
				int height = (int)(this.getHeight()*0.95);
				int cardwidth, cardheight;
				if (height*3/4>width) {
					cardwidth = width/8;
					cardheight = cardwidth*4/3;
				}else {
					cardheight = height/5;
					cardwidth = cardheight*3/4;
				}
				int dx = cardwidth/5;
				int upper = cardheight*activePlayer;
				int lower = cardheight*(activePlayer+1);
				boolean notfound = true;
				if (y>upper && y<lower) {
					int nCards = p.getNumOfCards();
					if (x>cardwidth*2 && x<cardwidth*3+dx*((nCards-1))) {
						for (int i=nCards-1; notfound && i>=0; i--) {
							if (x>cardwidth*2+i*dx) {
								notfound = false;
								if (selected[i]==false) {
									selected[i]=true;
								}
								else {
									selected[i]=false;
								}
								frame.repaint();
							}
						}
					}
				}
			}	
		}
		/**
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 * draws the card game table:
		 * Sets the position of the labels; 
		 * Adds background, players' avatars, separation lines and cards held by every player to the bigTwoPanel; 
		 * Adds the player name and the cards of the last Hand on table to the bigTwoPanel
		 * @param g an object of Graphics class
		 */
		public void paintComponent(Graphics g) {
			g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
			int width = this.getWidth();
			int height = (int)(this.getHeight()*0.95);
			int cardwidth, cardheight;
			if (height*3/4>width) {
				cardwidth = width/8;
				cardheight = cardwidth*4/3;
			}else {
				cardheight = height/5;
				cardwidth = cardheight*3/4;
			}
			
			int dx = cardwidth/5;
			
			for (int i=0; i<4; i++) {
				String pn = game.getPlayerList().get(i).getName();
				if (pn==null || pn =="") {
					labels[i].setText("Player "+i);
					labels[i].setBounds(0, i*cardheight, labels[i].getWidth(), labels[i].getHeight());
					g.drawLine(0, (i+1)*cardheight, width, (i+1)*cardheight);
				}
					
				else {
					labels[i].setText(pn);
					labels[i].setBounds(0, i*cardheight, labels[i].getWidth(), labels[i].getHeight());
					int y = i*cardheight +labels[i].getHeight();
					int h = cardheight-labels[i].getHeight();
					int dy = labels[i].getHeight()/2;
					g.drawImage(avatars[i], 0, y, cardwidth, h, this);
					g.drawLine(0, (i+1)*cardheight, width, (i+1)*cardheight);
					CardGamePlayer p = game.getPlayerList().get(i);
					CardList temp = p.getCardsInHand();
					int nCards = p.getNumOfCards();
					if (i == ((BigTwoClient)game).getPlayerID()) {
						for (int j=0; j<nCards; j++) {
							int s = temp.getCard(j).getSuit();
							int r = temp.getCard(j).getRank();
							if (selected[j])
								g.drawImage(cardImages[s][r], cardwidth*2+j*dx, y-dy, cardwidth, h, this);
							else
								g.drawImage(cardImages[s][r], cardwidth*2+j*dx, y, cardwidth, h, this);
						}
					}
					else {
						for (int j=0; j<nCards; j++) {
							g.drawImage(cardBackImage, cardwidth*2+j*dx, y, cardwidth, h, this);
						}
					}
				}
				
			}
			labels[4].setBounds(0, 4*cardheight, labels[4].getWidth(), labels[4].getHeight());
			ArrayList<Hand> handsOnTable = game.getHandsOnTable();
			Hand lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable
					.get(handsOnTable.size() - 1);
			if (lastHandOnTable != null)  {
				String name = lastHandOnTable.getPlayer().getName();
				for (int k=0; k<lastHandOnTable.size(); k++) {
					int s = lastHandOnTable.getCard(k).getSuit();
					int r = lastHandOnTable.getCard(k).getRank();
					g.drawImage(cardImages[s][r], k*dx, 4*cardheight +labels[4].getHeight(), cardwidth, cardheight-labels[4].getHeight(), this);
				}
				labels[4].setText("Played by "+name);
			}
			else
				labels[4].setText("No Hand on Table");
		}
			
		/**
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 * This method is not used in this application.
		 * @param e an object of the MouseEvent class
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 * This method is not used in this application.
		 * @param e an object of the MouseEvent class
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 * This method is not used in this application.
		 * @param e an object of the MouseEvent class
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 * This method is not used in this application.
		 * @param e an object of the MouseEvent class
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}  
	}

	/**
	 * The PlayButtonListener class is an inner class of BigTwoTable class which implements the ActionListener interface.
	 * @author think
	 *
	 */
	class PlayButtonListener implements ActionListener{
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the "Play" button. 
		 * When the "Play" button is clicked, if more than one card is selected, make a move with the selected cards.
		 * @param event an object of the ActionEvent class
		 */
		public void actionPerformed(ActionEvent event)
		{
			if (getSelected().length>0)
				game.makeMove(activePlayer, getSelected());
		}
	}
	/**
	 * The PassButtonListener class is an inner class of BigTwoTable class which implements the ActionListener interface.
	 * @author think
	 *
	 */
	class PassButtonListener implements ActionListener{
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the "Pass" button. 
		 * When the "Pass" button is clicked, make a move with no selected card.
		 * @param event an object of the ActionEvent class
		 */
		public void actionPerformed(ActionEvent event) {
			game.makeMove(activePlayer, null);
		}
	}
	/**
	 * The RestartMenuItemListener class is an inner class of BigTwoTable class which implements the ActionListener interface.
	 * @author think
	 *
	 */
	class RestartMenuItemListener implements ActionListener{
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the "Restart" menu item.
		 * When the "Restart" menu item is selected, a new BigTwoDeck object will be created and shuffled, the game will be restarted.
		 * @param event an object of the ActionEvent class
		 */
		public void actionPerformed(ActionEvent event) {
			if (((BigTwoClient)game).getServerPort()==-1)
				((BigTwoClient)game).makeConnection();
			
		}
	}
	/**
	 * The QuitMenuItemListener class is an inner class of BigTwoTable class which implements the ActionListener interface.
	 * @author think
	 *
	 */
	class QuitMenuItemListener implements ActionListener{
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the "Quit" menu item. 
		 * When the "Quit" menu item is selected, the application will be terminated.
		 * @param event an object of the ActionEvent class
		 */
		public void actionPerformed(ActionEvent event) {
			if (((BigTwoClient)game).getServerPort()!=-1)
				((BigTwoClient)game).closeSocket();
			System.exit(0);
		}
	}
	
	/**
	 * The ClearMenuItemListener class is an inner class of the BigTwoTable class which implements the ActionListener interface.
	 * @author think
	 *
	 */
	class ClearMenuItemListener implements ActionListener{
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * When the "Clear" menu item is selected, the chat message area will be cleared.
		 */
		public void actionPerformed(ActionEvent event) {
			clearChatArea();
		}
	}
	
	/**
	 * The InputBoxListener class is an inner class of the BigTwoTable class which implements the ActionListener interface.
	 * @author think
	 *
	 */
	class InputBoxListener implements ActionListener{
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * When the user clicks the "Enter" key, the message in the input box will be sent to the server, 
		 * and the input box will be cleared.
		 */
		public void actionPerformed(ActionEvent e) {
			((BigTwoClient)game).sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, inputBox.getText()));				
			inputBox.setText("");
			inputBox.requestFocus();
		}
		
	}
}	

