
/**
 * The FullHouse class is used for representing a hand of full house.
 * It is a subclass of the Hand class.
 * 
 * @author think
 *
 */
public class FullHouse extends Hand{

	/**
	 * 
	 */
	private static final long serialVersionUID = -597613473172882159L;
	/**
	 * creates and returns an instance of the FullHouse class
	 * @param player a player who plays this hand of full house
	 * @param cards a list of cards
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 * checks if this is a valid hand of full house
	 */
	public boolean isValid() {
		if (this.size()==5){
			this.sort();
			int rank1 = this.getCard(0).getRank();
			int rank2 = this.getCard(4).getRank();
			int c1=0, c2=0;
			for (int i=0; i<5; i++) {
				if (this.getCard(i).getRank()==rank1)
					c1++;
				else if (this.getCard(i).getRank()==rank2)
					c2++;
			}
			if (c1==2 && c2==3)
				return true;
			else if (c1==3 && c2==2)
				return true;
			else
				return false;
		}
		return false;
	}
	
			
	/* (non-Javadoc)
	 * @see Hand#getType()
	 * returns the type FullHouse
	 */
	public String getType(){return "FullHouse";}
	
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 * retrieves the top card of the hand of full house
	 */
	public Card getTopCard() {
		this.sort();
		if (this.getCard(2).getRank()==this.getCard(0).getRank()){
			return this.getCard(2);
		}
		else {
			return this.getCard(4);
		}
	}
	/* (non-Javadoc)
	 * @see Hand#beats(Hand)
	 * checks if this hand beats a specified hand
	 */
	public boolean beats(Hand hand) {
		if (hand.size()!=5)
			return false;
		else {
			if (hand.getType()== "StraightFlush")
				return false;
			else if (hand.getType()=="Quad")
				return false;
			else if (hand.getType()=="FullHouse"){
				if (this.getTopCard().compareTo(hand.getTopCard())>0)
					return true;
				return false;	
			}
			else
				return true;
		}
	}
}
