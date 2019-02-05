package v1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DeckBuilder extends JPanel 
{
	protected Deck allCards = Deck.deserialize("allcards.ser");
	protected ArrayList<String> allCardNames = populateNames();
	protected LinkedList<String> cardsInDeck = new LinkedList<String>();
	protected HashMap<String, Card> hashAllCards = new HashMap<String, Card>();
	protected LinkedHashMap<String, Card> hashNewDeck = new LinkedHashMap<String, Card>();
	protected JList lstAllCards = new JList(new Vector(allCardNames));
	protected JList lstCardsInDeck = new JList(new Vector(cardsInDeck));
	protected JButton btnAddCard = new JButton("Add Card");
	protected JButton btnRemoveCard = new JButton("Remove Card");
	protected HashMap<String, Integer> copies = new HashMap<String, Integer>();
	protected JButton btnSaveDeck = new JButton("Save Deck");
	protected JTextField txtDeckName = new JTextField(9);
	protected DefaultListModel modelDeck = new DefaultListModel();
	protected JPanel pnlCardDisplay = new JPanel();
	protected CardPanel cardPanel = new CardPanel(new CreatureCard(false, "", 0, CreatureType.FIST));
	protected JScrollPane pnlAllCards = new JScrollPane(lstAllCards);
	protected JScrollPane pnlCardsInDeck = new JScrollPane(lstCardsInDeck);
	
	
	public DeckBuilder() 
	{
		this.setLayout(new GridLayout(1,3));
		addComponents();
		addListeners();
		populateHashMap();
		this.setVisible(true);
	}
	
	private void addComponents()
	{
		JPanel pnlCenter = new JPanel();
		JPanel pnlWest = new JPanel();
		JPanel pnlEast = new JPanel();
		JPanel pnlDivisionCenter = new JPanel();
		JPanel pnlDivisionEast = new JPanel();
		
		lstCardsInDeck.setModel(modelDeck);
		lstAllCards.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstCardsInDeck.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		
		pnlEast.setLayout(new BorderLayout());
		
		pnlAllCards.setLayout(new ScrollPaneLayout());
		
		pnlDivisionCenter.setLayout(new FlowLayout());
		
		pnlDivisionEast.setLayout(new FlowLayout());
		
		pnlCardsInDeck.setSize(new Dimension(300, 300));
		
		lstCardsInDeck.setSize(new Dimension(300, 300));
		
		pnlCardDisplay.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnlCardDisplay.setMinimumSize(new Dimension(500, 300));
		cardPanel.setVisible(false);
		pnlCardDisplay.add(cardPanel);
		
		pnlWest.setLayout(new BorderLayout());
		
		pnlCenter.setLayout(new BoxLayout(pnlAllCards, BoxLayout.Y_AXIS));
		
		pnlCardsInDeck.setLayout(new ScrollPaneLayout());
		pnlCardsInDeck.setVisible(true);
		
		pnlAllCards.setMaximumSize(new Dimension(300, 300));
 
		pnlDivisionCenter.add(btnAddCard); 
		pnlDivisionCenter.add(btnRemoveCard);
		pnlDivisionEast.add(txtDeckName);
		pnlDivisionEast.add(btnSaveDeck);
		pnlWest.add(pnlCardsInDeck, BorderLayout.CENTER);
		pnlWest.add(pnlDivisionEast, BorderLayout.SOUTH);
		pnlEast.add(pnlAllCards, BorderLayout.CENTER);
		pnlEast.add(pnlDivisionCenter, BorderLayout.SOUTH);
		
		
		this.add(pnlCardDisplay);
		this.add(pnlEast);
		this.add(pnlWest);
	}
	
	private void addListeners()
	{
		btnAddCard.addActionListener(new AddToDeckListener());
		btnRemoveCard.addActionListener(new RemoveFromDeckListener());
		btnSaveDeck.addActionListener(new SaveDeckListener());
		lstAllCards.getSelectionModel().addListSelectionListener(new SharedListSelectionHandlerForAllCards());
		lstCardsInDeck.getSelectionModel().addListSelectionListener(new SharedListSelectionHandlerForDeck());
	}
	
	public ArrayList<String> populateNames()
	{
		ArrayList<String> a = new ArrayList<String>();
		
		for(Card card : allCards.getCards())
		{
			a.add(card.getName());
		}
		
		return a;
	}
	
	public void populateHashMap()
	{
		for(Card card : allCards.getCards())
		{
			hashAllCards.put(card.getName(), card);
		}
	}
	
	public void addCardToDeck(String name) throws DeckFullException
	{
		String s;
		Card n = hashAllCards.get(name);
		Card c;
		
		if(hashNewDeck.values().size() == 19)
		{
			throw new DeckFullException();
		}
		
		if(n.getType().equals("Creature"))
		{
			c = new CreatureCard(n.getIsRare(), n.getName(), ((CreatureCard) n).getAttackPower(), ((CreatureCard) n).getCreatureType());
		}
		else
		{
			c = new EnhancementCard(n.getIsRare(), n.getName(), ((EnhancementCard) n).getEnhancement());
		}
		
		if(!(copies.containsKey(name)))
		{
			copies.put(name, 1);
		}
		
		if(hashNewDeck.containsKey(name) || hashNewDeck.containsKey(name + " copy " + copies.get(name).intValue()))
		{
			int i = copies.get(name).intValue();
			copies.put(name, i + 1);
			hashNewDeck.put(name + " copy " + copies.get(name), c);
			s = name + " copy " + copies.get(name).intValue();
		}
		else
		{
			hashNewDeck.put(name, c);
			s = name;
		}
		
		modelDeck.addElement(s);
		
	}
	
	public void removeCardFromDeck(String name)
	{
		hashNewDeck.remove(name);
		
		modelDeck.remove(lstCardsInDeck.getSelectedIndex());
	}
	
	public void saveDeck(String fileName)
	{
		Deck d = new Deck();
		LinkedList<Card> l = new LinkedList<Card>();
		
		for(Card card : hashNewDeck.values())
		{
			l.add(card);
		}
		
		d.setCards(l);
		
		d.serialize(fileName + ".ser");
	}
	
	
	private class AddToDeckListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			try 
			{
				addCardToDeck((String) lstAllCards.getSelectedValue());
			} 
			catch (DeckFullException e) 
			{
				System.err.println("Cannot add any more cards to the deck! Remove a card to make more space. @ " + e.getTimeStamp().toString());
			}
			
			lstCardsInDeck.updateUI();
			
		}
		
	}
	
	private class RemoveFromDeckListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			removeCardFromDeck((String) lstCardsInDeck.getSelectedValue());
			cardPanel.setVisible(false);
			
			lstCardsInDeck.updateUI();
		}
		
	}
	
	private class SaveDeckListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			saveDeck(txtDeckName.getText());
		}
		
	}
	
	private class SharedListSelectionHandlerForDeck implements ListSelectionListener 
	{
		public void valueChanged(ListSelectionEvent e) 
		{
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			
			if (!lsm.isSelectionEmpty() && !lsm.getValueIsAdjusting()) 
			{
				int i = lsm.getMinSelectionIndex();
				String s = (String) modelDeck.get(i);
				cardPanel.setVisible(false);
				pnlCardDisplay.remove(cardPanel);
				cardPanel = new CardPanel(hashNewDeck.get(s));
				pnlCardDisplay.add(cardPanel);
				cardPanel.setVisible(true);
				pnlCardDisplay.updateUI();
			} 
			
		}
	}
	
	private class SharedListSelectionHandlerForAllCards implements ListSelectionListener 
	{
		public void valueChanged(ListSelectionEvent e) 
		{
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			
			if (!lsm.isSelectionEmpty() && !lsm.getValueIsAdjusting()) 
			{
				String s = (String) lstAllCards.getSelectedValue();
				cardPanel.setVisible(false);
				pnlCardDisplay.remove(cardPanel);
				cardPanel = new CardPanel(hashAllCards.get(s));
				pnlCardDisplay.add(cardPanel);
				cardPanel.setVisible(true);
				pnlCardDisplay.updateUI();
			} 
			
		}
	}
}
