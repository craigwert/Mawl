package v1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel with all things related to profile management; meant for use in the ProfileAndDeckBuilder GUI
 * 
 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
 * @version 5/1/17
 */
public class ProfileManager extends JPanel
{
	protected JButton btnNewProfile = new JButton("Create New Profile");
	protected JButton btnAddDeck = new JButton("Add Deck to Profile");
	protected JTextField txtNewName = new JTextField(10);
	protected JTextField txtProfileName = new JTextField(10);
	protected JTextField txtDeck = new JTextField(10);
	
	/**
	 * Constructor for the Profile Manager JPanel
	 */
	public ProfileManager() 
	{
		this.setLayout(new BorderLayout());
		addComponents();
		addListeners();
		this.setVisible(true);
	}
	
	/**
	 * Adds the components to the JPanel
	 */
	private void addComponents()
	{
		JLabel lblNewName = new JLabel("Name for new profile: ");
		JLabel lblProfileName = new JLabel("Name of existing profile: ");
		JLabel lblDeck = new JLabel("Name of deck to be added: ");
		JPanel newProfile = new JPanel();
		JPanel existingProfile = new JPanel();
		JPanel existingProfileText = new JPanel();
		JPanel existingProfileButton = new JPanel();
		
		newProfile.setLayout(new FlowLayout(FlowLayout.CENTER));
		existingProfile.setLayout(new BorderLayout());
		existingProfileText.setLayout(new FlowLayout(FlowLayout.CENTER));
		existingProfileButton.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		newProfile.add(lblNewName);
		newProfile.add(txtNewName);
		newProfile.add(btnNewProfile);
		newProfile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		existingProfileText.add(lblProfileName);
		existingProfileText.add(txtProfileName);
		existingProfileText.add(lblDeck);
		existingProfileText.add(txtDeck);
		
		existingProfileButton.add(btnAddDeck);
		
		existingProfile.add(existingProfileText, BorderLayout.NORTH);
		existingProfile.add(existingProfileButton, BorderLayout.SOUTH);
		existingProfile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		this.add(newProfile, BorderLayout.WEST);
		this.add(existingProfile, BorderLayout.EAST);
	}
	
	/**
	 * Adds the listeners to the components of the JPanel
	 */
	private void addListeners()
	{
		btnNewProfile.addActionListener(new NewProfileListener());
		btnAddDeck.addActionListener(new AddDeckListener());
	}
	
	/**
	 * Inner class for the new profile button listener
	 * 
	 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
	 * @version 5/1/17
	 */
	private class NewProfileListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			Player p = new Player();
			p.setName(txtNewName.getText());
			Deck d = new Deck();
			d = Deck.deserialize("default.ser");
			p.setMyDeck(d);
			p.serialize(txtNewName.getText() + ".ser");
			
		}
		
	}
	
	/**
	 * Inner class for the add deck button listener
	 * 
	 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
	 * @version 5/1/17
	 */
	private class AddDeckListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Player p = Player.deserialize(txtProfileName.getText() + ".ser");
			Deck d = Deck.deserialize(txtDeck.getText() + ".ser");
			p.setMyDeck(d);
			p.serialize(txtProfileName.getText() + ".ser");
			
		}
		
	}

}
