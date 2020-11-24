
/**
 * The Single class is used for representing a hand of single in a Big Two card game.
 * It is a subclass of the Hand class.
 * @author think
 *
 */
public class Single extends Hand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * creates and returns an instance of the Single class
	 * @param player the player who plays this hand of single
	 * @param cards a list of cards
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super (player, cards);
	}

	/* (non-Javadoc)
	 * @see Hand#isValid()
	 * checks if this is a valid hand of single
	 */
	public boolean isValid() {
		if (this.size() == 1)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see Hand#getType()
	 * returns the type Single
	 */
	public String getType() {
		return "Single";
	}

}
