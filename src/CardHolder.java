package v1;
import java.util.List;

/**
 * @author Brendon Armstrong, Alex Lam, Sean McMillan, Craig Wert
 *
 */
public interface CardHolder {

	/**
	 * Getter for the list of cards
	 * 
	 * @return card list
	 */
	public abstract List<Card> getCards();

	/**
	 * Getter for the card list size
	 * 
	 * @return card list size
	 */
	public int getSize();
}