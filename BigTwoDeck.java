
/**
 * The BigTwoDeck class is used for representing a deck of cards used in a Big Two card game.
 * It is a subclass of the Deck class.
 * 
 * @author think
 *
 */
public class BigTwoDeck extends Deck{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8254481884293570270L;


	/* (non-Javadoc)
	 * @see Deck#initialize()
	 * initializes a deck of Big Two cards.
	 * removes all cards from the deck
	 * creates 52 Big Two cards and adds them to the deck
	 */
	public void initialize() {
		this.removeAllCards();
		for (int i = 0; i<4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				this.addCard(card);
			}
		}
	}
}
