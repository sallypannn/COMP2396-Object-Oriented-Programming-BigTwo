
/**
 * The Pair class is used for representing a hand of pair in the Big Two card game
 * It is a subclass of the Hand class
 * @author think
 *
 */
public class Pair extends Hand{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7735787877198652522L;
	/**
	 * creates and returns an instance of the Pair class
	 * @param player a player who plays this hand of pair
	 * @param cards a list of cards
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 * checks if this is a valid hand of pair
	 */
	public boolean isValid(){
		if (this.size()==2) {
			if ((this.getCard(0)).getRank() == (this.getCard(1)).getRank())
				return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 * returns the type "Pair"
	 */
	public String getType(){return "Pair";}
	

}
