
/**
 * The Straight class is used for representing a hand of straight in the Big Two card game.
 * It is a subclass of the Hand class.
 * 
 * @author think
 *
 */
public class Straight extends Hand{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4735239255262917363L;
	/**
	 * creates and returns an instance of the Straight class
	 * @param player a player who plays this hand of straight
	 * @param cards a list of cards
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 * checks if this is a valid card of straight
	 */
	public boolean isValid(){
		if (this.size()==5) {
			this.sort();
			CardList temp = new CardList();
			for (int i=0; i<5; i++) {
				Card c = this.getCard(i);
				Card temp2;
				if (c.getRank()==1 || c.getRank()==0) 
					temp2 = new Card(c.suit, c.rank+13);
				else
					temp2 = new Card(c.suit, c.rank);
				temp.addCard(temp2);
					
			}
			if ((temp.getCard(0)).getRank()+1== (temp.getCard(1)).getRank()) {
				if ((temp.getCard(1)).getRank()+1== (temp.getCard(2)).getRank()) {
					if((temp.getCard(2)).getRank()+1== (temp.getCard(3)).getRank()){
						if ((temp.getCard(3)).getRank()+1== temp.getCard(4).getRank())
							return true;
						return false;
					}
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 * returns the type Straight
	 */
	public String getType(){return "Straight";}
	
	/* (non-Javadoc)
	 * @see Hand#beats(Hand)
	 * checks if this hand beats a specified hand
	 */
	public boolean beats(Hand hand) {
		if (hand.size()!=5)
			return false;
		else {
			if (hand.getType()=="Straight"){
				if (this.getTopCard().compareTo(hand.getTopCard())>0)
					return true;
				return false;	
			}
			else
				return false;
		}
	}
}
