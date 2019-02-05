package v1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A Player with a name, a deck, a hand, and a record for wins and losses
 * 
 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
 * @version 5/1/17
 */
public class Player implements Serializable{

	private Deck myDeck = new Deck();
	private Hand myHand = new Hand();
	private String name = "";
	private int wins;
	private int games;

	public Player() {
		this.wins = 0;
		this.games = 0;
	}

	/**
	 * Constructor for all objects of Player class
	 * 
	 * @param myDeck the player's deck
	 * @param myHand the player's hand
	 */
	public Player(Deck myDeck, String name) {
		this.myDeck = myDeck;
		this.name = name;
		this.wins = 0;
		this.games = 0;
	}

	/**
	 * Sets the player's name
	 * 
	 * @param name desired name for the player
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the player's deck
	 * 
	 * @param myDeck the desired deck for the player
	 */
	public void setMyDeck(Deck myDeck) {
		this.myDeck = myDeck;
	}

	/**
	 * Will draw a full hand(seven cards)
	 * 
	 */
	public void drawToFullHand() {
		while(myHand.getSize() < 7 && (myDeck.getSize() >= 0)){
			myHand.addCard(myDeck.draw());
		}
	}

	/**
	 * Discards the specific card in the list.
	 * 
	 * @param card card to be discarded 
	 */
	public void discard(Card card) {
		myHand.remove(card);
	}

	/**
	 * Increments games and wins/losses depending on the outcome
	 * 
	 * @param win whether or not the player won
	 */
	public void addGame(boolean win) {
		if(win) {
			wins++;
		}
		games++;
	}

	/**
	 * Clears the player's hand
	 */
	public void clearHand() {
		myHand = new Hand();
	}
	
	/**
	 * Gets the player's deck
	 * 
	 * @return the player's deck
	 */
	public Deck getMyDeck() {
		return myDeck;
	}

	/**
	 * Gets the player's hand
	 * 
	 * @return the player's hand
	 */
	public Hand getMyHand() {
		return myHand;
	}
	
	/**
	 * Gets the player's wins
	 * 
	 * @return the player's number of wins
	 */
	public int getWins() {
		return wins;
	}
	
	/**
	 * Gets the number of games the player has played
	 * 
	 * @return the number of games the player has played
	 */
	public int getGames() {
		return games;
	}
	
	/**
	 * Gets the player's name
	 * 
	 * @return the player's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Saves the player to a file
	 * 
	 * @param fileName the filename to save the player to
	 */
	public void serialize(String fileName)
	{
		try (ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(fileName));)
		{
			out.writeObject(this);
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}	
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets a saved player from a file
	 * 
	 * @param fileName the file in which the saved player is located
	 * @return the saved player
	 */
	public static Player deserialize(String fileName)
	{
		
		Player p = new Player();
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
		{
			p = (Player) in.readObject();
			
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
		
			return p;
	}
	
}