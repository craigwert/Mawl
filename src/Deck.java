package v1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Brendon Armstrong, Alex Lam, Sean McMillan, Craig Wert
 *
 */
public class Deck implements CardHolder, Serializable{

	private LinkedList<Card> cards = new LinkedList<Card>();
	
	public Deck() 
	{
		
	}
	/**
	 * Constructor
	 * 
	 * @param cards
	 */
	public Deck(LinkedList<Card> cards) {
		this.cards = cards;
	}
	
	public LinkedList<Card> getCards(){
		return cards;
	}

	public int getSize(){
		return cards.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean setCards(LinkedList<Card> cards) {
		if(cards != null){
			this.cards = cards;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Will shuffle the deck
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}

	/**
	 * Will return a card drawn from top of deck
	 * 
	 * @return drawnCard
	 */
	public Card draw() {
		return cards.remove();
	}
	
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
	
	public static Deck deserialize(String fileName)
	{
		
		Deck d = new Deck();
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
		{
			d = (Deck) in.readObject();
			
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
		
			return d;
	}
}