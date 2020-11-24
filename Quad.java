
/**
 * The Quad class is used for representing a hand of quad.
 * It is a subclass of the Quad class.
 * 
 * @author think
 *
 */
public class Quad extends Hand{

	/**
	 * 
	 */
	private static final long serialVersionUID = -576715153845113194L;
	/**
	 * creates and returns an instance of the Quad class
	 * @param player a player who plays this hand of quad
	 * @param cards a list of cards
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 * checks if this is a valid hand of quad
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
			if (c1==1 && c2==4)
				return true;
			else if (c1==4 && c2==1)
				return true;
			else
				return false;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 * returns the type Quad
	 */
	public String getType(){return "Quad";}
	
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 * retrieves the top card of the hand
	 */
	public Card getTopCard() {
		this.sort();
		if (this.getCard(1).getRank()==this.getCard(0).getRank()){
			return this.getCard(3);
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
			else if (hand.getType()=="Quad"){
				if (this.getTopCard().compareTo(hand.getTopCard())>0)
					return true;
				return false;
							
			}
			else
				return true;
		}
	}
}
