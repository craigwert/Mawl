package v1;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A GUI for building Profiles or making new Decks
 * 
 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
 * @version 5/1/17
 */
public class ProfileAndDeckBuilder extends JFrame
{
	protected DeckBuilder deckBuilder;
	protected ProfileManager profileManager;
	protected JButton btnDeckBuilder = new JButton("Build Deck");
	protected JButton btnCreateProfile = new JButton("Create Profile");
	protected JLabel lblWelcome = new JLabel("Welcome to the Profile and Deck Builder. What would you like to do?"); 
	protected JLabel lblBanner = new JLabel("Profile and Deck Builder");
	protected CardLayout cl = new CardLayout();
	protected JPanel pnlContainer = new JPanel();
	protected JPanel pnlButtons = new JPanel();
	protected JPanel pnlStart = new JPanel();
	protected JPanel pnlProfileManager = new ProfileManager();
	protected JPanel pnlDeckBuilder = new DeckBuilder();
	
	/**
	 * Constructor for the ProfileAndDeckBuilder GUI
	 */
	public ProfileAndDeckBuilder() 
	{
		addComponents();
		addListeners();
		this.pack();
		this.setMinimumSize(new Dimension(980, 300));
		this.setMaximumSize(new Dimension(980, 300));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Adds the components to the GUI
	 */
	private void addComponents()
	{
		this.setLayout(new BorderLayout());
		pnlContainer.setLayout(cl);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnlButtons.add(btnDeckBuilder);
		pnlButtons.add(btnCreateProfile);
		
		pnlStart.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnlStart.add(lblWelcome);
		this.add(pnlButtons, BorderLayout.SOUTH);
		this.add(lblBanner, BorderLayout.NORTH);
		
		pnlContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlContainer.add(pnlStart, "START");
		pnlContainer.add(pnlProfileManager, "PROFILE");
		pnlContainer.add(pnlDeckBuilder, "DECK");
		
		this.add(pnlContainer, BorderLayout.CENTER);
		
	}
	
	/**
	 * Adds listeners to the components of the GUI
	 */
	private void addListeners()
	{
		btnCreateProfile.addActionListener(new ProfileManagerListener());
		btnDeckBuilder.addActionListener(new DeckBuilderListener());
	}
	
	/**
	 * Listener that changes the displayed panel in the JFrame's CardLayout to show the DeckBuilder panel
	 * 
	 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
	 * @version 5/1/17
	 */
	private class DeckBuilderListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{ 
			cl.show(pnlContainer, "DECK");
		}
		
	}
	
	/**
	 * Listener that changes the displayed panel in the JFrame's CardLayout to show the ProfileManager panel
	 * 
	 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
	 * @version 5/1/17
	 */
	public class ProfileManagerListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			cl.show(pnlContainer, "PROFILE");
		}
		
		
	}
	

}
