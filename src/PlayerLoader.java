package v1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class responsible for loading players from their save files into the game
 * 
 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
 * @version 5/1/17
 */
public class PlayerLoader extends JFrame
{
	protected JButton btnLoad = new JButton("Load Players");
	protected JTextField txtPlayer1 = new JTextField(9);
	protected JTextField txtPlayer2 = new JTextField(9);
	protected JLabel lblPlayer1 = new JLabel("Player 1: ");
	protected JLabel lblPlayer2 = new JLabel("Player 2: ");
	
	/**
	 * Constructor for the PlayerLoaderGUI
	 */
	public PlayerLoader()
	{
		this.setLayout(new BorderLayout());
		addComponents();
		addListeners();
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Adds components to the GUI
	 */
	private void addComponents()
	{
		JPanel pnlCenter = new JPanel();
		JPanel pnlSouth = new JPanel();
		pnlCenter.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		pnlCenter.add(lblPlayer1);
		pnlCenter.add(txtPlayer1);
		pnlCenter.add(lblPlayer2);
		pnlCenter.add(txtPlayer2);
		
		
		
		pnlSouth.add(btnLoad);
		
		this.add(pnlCenter, BorderLayout.CENTER);
		this.add(pnlSouth, BorderLayout.SOUTH);
	}
	
	/**
	 * Loads the players from their saved files
	 * 
	 * @param player1 first player being loaded
	 * @param player2 second player being loaded
	 * @return a collection containing both loaded players
	 */
	public ArrayList<Player> loadPlayers(String player1, String player2)
	{
		Player p1 = Player.deserialize(player1 + ".ser");
		Player p2 = Player.deserialize(player2 + ".ser");
		
		System.out.println(p1.getName() + " vs. " + p2.getName());
		
		ArrayList<Player> a = new ArrayList<Player>();
		a.add(p1);
		a.add(p2);
		
		return a;
	}
	
	/**
	 * Adds listeners to the components of the GUI
	 */
	private void addListeners()
	{
		btnLoad.addActionListener(new LoadPlayersListener());
	}
	
	/**
	 * Listener for the load players button
	 * 
	 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
	 * @version 5/1/17
	 */
	private class LoadPlayersListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			ArrayList<Player> players = new ArrayList<Player>();
			players = loadPlayers(txtPlayer1.getText(), txtPlayer2.getText());
			
			GameManager game = new GameManager(players.get(0), players.get(1));
			
			dispose();
		}
		
	}
}
