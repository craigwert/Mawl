package v1;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Hand with a collection of Cards
 * 
 * @author Brendon Armstrong, Alex Lam, Sean McMillan, Craig Wert
 * @version 5/1/2017
 *
 */
public class Hand implements CardHolder, Serializable {
	private ArrayList<Card> cards = new ArrayList<Card>();

	/**
	 * Constructor for objects of the Hand class
	 * 
	 */
	public Hand() {
		
	}
	
	/**
	 * Gets the list of cards
	 * 
	 * @return the list of cards
	 */
	public List<Card> getCards(){
		return cards;
	}
	
	/**
	 * Gets the size of the hand
	 * 
	 * @return the size of the hand
	 */
	public int getSize(){
		return cards.size();
	}
	/**
	 * Adds a card to the hand
	 * 
	 * @param card the card to be added to the hand
	 */
	public void addCard(Card card) {
		cards.add(card);
	}

	/**
	 * Removes a card from the hand
	 * 
	 * @param card card to be removed from the hand
	 */
	public void remove(Card card) {
		cards.remove(card);
	}
	
	/**
	 * Saves the hand to a file
	 * 
	 * @param fileName the name of the file to save the hand to
	 */
	public void serialize(String fileName)
	{
		try (ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(fileName));)
		{
			
			out.writeObject(this);
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets a saved hand from a file
	 * 
	 * @param fileName the file to get the hand from
	 * @return the hand saved in the file
	 */
	public static Hand deserialize(String fileName)
	{
		
		Hand h = new Hand();
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
		{
			h = (Hand) in.readObject();
			
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
			return h;
	}

}