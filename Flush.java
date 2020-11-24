
/**
 * The Flush class is used for representing a hand of flush in the Big Two card game.
 * It is a subclass of the Hand class.
 * 
 * @author think
 *
 */
public class Flush extends Hand{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1176872676059568463L;
	/**
	 * creates and returns an instance of the Flush class
	 * @param player a player who plays this hand of flush
	 * @param cards a list of cards
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 * checks if this is a valid hand of Straight
	 */
	public boolean isValid() {
		if (this.size()==5) {
			if ((this.getCard(0)).getSuit()== (this.getCard(1)).getSuit()) {
				if ((this.getCard(1)).getSuit()== (this.getCard(2)).getSuit()) {
					if((this.getCard(2)).getSuit()== (this.getCard(3)).getSuit()){
						if ((this.getCard(3)).getSuit()== this.getCard(4).getSuit())
							return true;
					}
				}
			}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 * returns the type Flush
	 */
	public String getType(){return "Flush";}
	
	/* (non-Javadoc)
	 * @see Hand#beats(Hand)
	 * checks if this hand beats a specified hand
	 */
	public boolean beats(Hand hand) {
		if (hand.size()!=5)
			return false;
		else {
			if (hand.getType()== "Straight")
				return true;
			else if (hand.getType()=="Flush"){
				if (this.getTopCard().getSuit()>hand.getTopCard().getSuit())
					return true;
				else if (this.getTopCard().getSuit()==hand.getTopCard().getSuit())
				{if(this.getTopCard().compareTo(hand.getTopCard())>0)
					return true;
				}
				return false;	
			}
			else
				return false;
		}
	}
}
