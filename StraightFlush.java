
/**
 * The StraightFlush class is used for representing a hand of straight flush in a Big Two card game.
 * It is a subclass of the Hand class.
 * 
 * @author think
 *
 */
public class StraightFlush extends Hand{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7319311705716526303L;
	/**
	 * creates and returns an instance of the StraightFlush class
	 * @param player a player who plays this hand of straight flush
	 * @param cards a list of cards
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 * checks if this is a valid hand of straight flush
	 */
	public boolean isValid(){
		CardList temp = new CardList();
		for (int i=0; i<this.size(); i++) 
			temp.addCard(this.getCard(i));
		Straight s= new Straight(this.getPlayer(),temp);
		Flush f = new Flush(this.getPlayer(),temp);
		if (s.isValid()&&f.isValid())
			return true;
		return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 * returns the type StraightFlush
	 */
	public String getType(){return "StraightFlush";}
	/* (non-Javadoc)
	 * @see Hand#beats(Hand)
	 * checks if this hand beats a specified hand
	 */
	public boolean beats(Hand hand) {
		if (hand.size()!=5)
			return false;
		else {
			if (hand.getType()!= "StraightFlush")
				return true;
			else {
				if (this.getTopCard().compareTo(hand.getTopCard())>0)
					return true;
				return false;
							
			}
		}
	}
}
