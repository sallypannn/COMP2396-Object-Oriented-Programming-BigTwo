
/**
 * The BigTwoCard class is used for representing a card used in a Big Two card game. 
 * It is a subclass of the Card class.
 * 
 * @author think
 *
 */
public class BigTwoCard extends Card{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5822018182891811204L;

	/**
	 * Creates and returns an instance of the BigTwoCard class
	 * @param suit the suit of the card
	 * @param rank the rank of the card
	 */
	public BigTwoCard(int suit, int rank){
		super(suit, rank);
	}
	/* (non-Javadoc)
	 * @see Card#compareTo(Card)
	 * compares this card with the specified card for order.
	 * returns a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card.
	 */
	public int compareTo(Card card) {
		Card temp1;
		Card temp2;
		if (this.rank==1 || this.rank==0)
			temp1 = new Card(this.suit, this.rank+13);
		else
			temp1= new Card(this.suit, this.rank);
		if (card.rank==1 || card.rank==0)
			temp2 = new Card(card.suit, card.rank+13);
		else
			temp2 = new Card(card.suit,card.rank);
		return (temp1.compareTo(temp2));

	}
}
