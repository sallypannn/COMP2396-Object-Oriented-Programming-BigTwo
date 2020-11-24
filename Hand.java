
/**
 * The Hand class is used for representing a hand of cards.
 * It is a subclass of the CardList class.
 * 
 * @author think
 *
 */
public abstract class Hand extends CardList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3708415018664975012L;
	/**
	 * builds a hand with the specified player and list of cards
	 * @param player the player who plays this hand
	 * @param cards a list of cards
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i=0; i<cards.size(); i++) {
			BigTwoCard temp = (BigTwoCard) cards.getCard(i);
			this.addCard(temp);
		}
		this.sort();
	}
	private CardGamePlayer player;
	/**
	 * retrieves the player of this hand
	 * @return the player of this hand
	 */
	public CardGamePlayer getPlayer() {return player;}
	/**
	 * retrieves the top card of this hand
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		Card temp = this.getCard(this.size()-1);
		return temp;
	}
	/**
	 * checks if this hand beats a specified hand
	 * @param hand a specified hand
	 * @return true if this hand beats the specified hand, false otherwise
	 */
	public boolean beats(Hand hand) {
		if (this.size()==1 && hand.size()==1) {
			if (this.getTopCard().compareTo(hand.getTopCard())>0)
				return true;
			return false;
		}
		else if (this.size()==2 && hand.size()==2) {
			if (this.getTopCard().compareTo(hand.getTopCard())>0)
				return true;
			return false;
		}
		if (this.size()==3 && hand.size()==3) {
			if (this.getTopCard().compareTo(hand.getTopCard())>0)
				return true;
			return false;
		}
		return false;
	}
	/**
	 * checks if this is a valid hand
	 * @return true if this is a valid hand, false otherwise
	 */
	public abstract boolean isValid();
	/**
	 * returns a string specifying the type of this hand
	 * @return a string specifying the type of this hand
	 */
	public abstract String getType();
}
