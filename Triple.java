
/**
 * The Triple class is used for representing a hand of triple in a Big Two card game
 * It is a subclass of the Hand class
 * 
 * @author think
 *
 */
public class Triple extends Hand{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2109395258622002051L;
	/**
	 * creates and returns an instance of the Triple class
	 * @param player a player who plays this hand of triple
	 * @param cards a list of cards
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 * checks if this is a valid hand of triple
	 */
	public boolean isValid(){
		if (this.size()==3) {
			if ((this.getCard(0)).getRank() == (this.getCard(1)).getRank()) {
				if ((this.getCard(0)).getRank() == (this.getCard(2)).getRank()) {
					return true;
				}
			}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 * returns the type Triple
	 */
	public String getType(){return "Triple";}

}
