package v1;

import javax.swing.JOptionPane;

/**
 * Main driver for the Mawl Project
 * 
 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
 * @version 5/1/17
 */
public class MainDriver {

	public static void main(String[] args) 
	{
		gameStart();
	}
	
	/**
	 * Starts the game
	 */
	public static void gameStart()
	{
		int gameStart = JOptionPane.showConfirmDialog(null, " Welcome to MAWL! Do you have existing profiles and decks? \n Selecting 'Yes' will bring you to the Load Players screen \n "
				+ "Selecting 'No' will bring you to the Deck and Profile Builder \n Selecting 'Cancel' will close the program", "Game Start", JOptionPane.YES_NO_CANCEL_OPTION);

		if(gameStart == JOptionPane.YES_OPTION)
		{
			runGame();
		}
		else if(gameStart == JOptionPane.NO_OPTION)
		{
			runBuilder();
		}
		else
		{
			
		}
	}
	
	/**
	 * Creates a new PlayerLoader for use in the driver
	 */
	private static void runGame()
	{
		PlayerLoader loader = new PlayerLoader();
	}
	
	/**
	 * Creates a new ProfileAndDeckBuilder for use in the driver
	 */
	private static void runBuilder()
	{
		ProfileAndDeckBuilder builder = new ProfileAndDeckBuilder();
	}

}
