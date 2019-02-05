package v1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

@SuppressWarnings("serial")
public class FieldGui extends JFrame{
	
	private GameManager manager;
	private JPanel buttonPanel;
	JScrollPane logScrollPane;
	private JTextArea logTextBox;
	
	public final int width = 1250;
	public final int height = 1000;
	public final int numLayers = 6;
	public final int cellWidth = 500;
	public final int cellThickness = 2;

	private ArrayList<JPanel> northCellList = new ArrayList<JPanel>(5);
	private ArrayList<JPanel> enhancementCellList = new ArrayList<JPanel>(4);
	private ArrayList<JPanel> southCellList = new ArrayList<JPanel>(5);
	private ArrayList<JPanel> layers = new ArrayList<JPanel>(numLayers);
	
	private JButton newGameBtn;
	private JButton restartWithNewPlayersBtn;
	
	private JPanel[] handPanels = new JPanel[2];
	
	private Color borderColor = Color.BLACK;
	private Color cellColor = Color.LIGHT_GRAY;
	private Color outOfPlayAreaColor = Color.GRAY;
	private Color handColor = new Color(110, 70, 0);
	
	public final static String GAME_NAME = "MAWL: The World's Best Entirely Animal-Themed Trading Card Game";
	public final static String LOG_TITLE = "Game Log";
	public final static String NEW_GAME_BTN_TXT = "Start New Game";
	public final static String RESTART_WITH_NEW_PLAYERS_TXT = "Return To Start Menu";
	
	public FieldGui(GameManager manager) {
		super(GAME_NAME);
		this.manager = manager;
		buildWindow();
	}
	
	private void buildWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setPreferredSize(new Dimension(width, height));
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		addLayers(numLayers);
		addCells(layers.get(1), 5, northCellList, CellLocation.P1);
		addCells(layers.get(2), 4, enhancementCellList, CellLocation.ENHANCEMENT);
		addCells(layers.get(3), 5, southCellList, CellLocation.P2);
		
		setResizable(false);
		pack();
		setVisible(true);
	}


	private void addLayers(int numLayers) {
		for(int i = 0; i < numLayers; i++) {
			JPanel layer = new JPanel();
			layer.setPreferredSize(new Dimension(width, height / numLayers));
			layer.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
			//If the layer is for the field, set it as a box layout to accomodate the cells.
			if(i > 0 && i < 4) {
				layer.setLayout(new GridLayout(1, 5));
				layer.setBackground(outOfPlayAreaColor);
			}
			//If the layer is the last layer, add the textbox for the log as well as any other important buttons.
			else if(i > numLayers - 2){
				layer.setLayout(new BoxLayout(layer, BoxLayout.X_AXIS));
				logTextBox = new JTextArea();
				logTextBox.setEditable(false);
				logScrollPane = new JScrollPane(logTextBox);
				logScrollPane.setPreferredSize(new Dimension(width / 2, height / numLayers));
				logScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 25, 1, Color.LIGHT_GRAY), LOG_TITLE));
				layer.add(logScrollPane, Alignment.LEADING);
				buttonPanel = new JPanel(new GridLayout(1, 0));
				buttonPanel.setPreferredSize(new Dimension(width / 2, height / numLayers));
				newGameBtn = new JButton(NEW_GAME_BTN_TXT);
				restartWithNewPlayersBtn = new JButton(RESTART_WITH_NEW_PLAYERS_TXT);
				for(JButton button : new JButton[] {newGameBtn, restartWithNewPlayersBtn}) {
					button.addActionListener(new ButtonListener());
					buttonPanel.add(button);
				}
				buttonPanel.add(newGameBtn);
				buttonPanel.add(restartWithNewPlayersBtn);
				layer.add(buttonPanel);
			}
			//If the layer is for a player's hand, color it and add it to the array of hand panels.
			else {
				layer.setBackground(handColor);
				if(i == 0) {
					handPanels[0] = layer;
				} else {
					handPanels[1] = layer;
				}
			}
			Dimension layerSize = new Dimension(width, height / numLayers);
			layer.setPreferredSize(layerSize);
			layer.setMaximumSize(layerSize);
			layer.setMinimumSize(layerSize);
			layers.add(layer);
			this.add(layer);
		}
	}

	private void addCells(JPanel layer, int numCells, ArrayList<JPanel> list, CellLocation cellLoc) {
		layer.add(Box.createHorizontalGlue());
		for(int i = 0; i < numCells; i++) {
			CardCell cell = new CardCell(cellLoc, i);
			cell.setLayout(new FlowLayout(FlowLayout.CENTER));
			cell.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, borderColor));
			cell.setBackground(cellColor);
			cell.addMouseListener(new CardCellListener(cell));
			layer.add(cell);
			list.add(cell);
		}
		layer.add(Box.createHorizontalGlue());
	}
	
	//Be sure to pass in a 1 or 2 here, not a 0 or 1.
	public void updateHand(int player, Hand hand) {
		handPanels[player - 1].removeAll();
		for(Card card : hand.getCards()) {
			handPanels[player - 1].add(createCardPanel(card));
		}
		this.setVisible(true);
	}
	
	public CardPanel createCardPanel(Card card) {
		CardPanel panel = new CardPanel(card);
		panel.addMouseListener(new CardPanelListener(panel));
		return panel;
	}
	
	public void addCard(Card card, CellLocation loc, int index) {
		ArrayList<JPanel> row;
		switch(loc) {
			case P1: row = northCellList;
					 break;
			case P2: row = southCellList;
					 break;
			default: row = enhancementCellList;
		}
		((CardCell)row.get(index)).addCardPanel(createCardPanel(card));
		this.setVisible(true);
	}
	
	public void removeCard(CellLocation loc, int index) {
		ArrayList<JPanel> row;
		switch(loc) {
			case P1: row = northCellList;
					 break;
			case P2: row = southCellList;
					 break;
			default: row = enhancementCellList;
		}
		((CardCell)row.get(index)).removeCardPanel();
		this.setVisible(true);
	}
	
	public void clearField() {
		for(JPanel panel : northCellList) {
			panel.removeAll();
		}
		for(JPanel panel : southCellList) {
			panel.removeAll();
		}
		for(JPanel panel : enhancementCellList) {
			panel.removeAll();
		}
	}
	
	public void savePlayers() {
		manager.savePlayers();
		manager.shutDownPlayExecutor();
		dispose();
	}
	
	public JTextArea getLogTextBox() {
		return logTextBox;
	}
	
	public void setLogTextBox(JTextArea logTextBox) {
		logScrollPane.getViewport().remove(this.logTextBox);
		this.logTextBox = logTextBox;
		logScrollPane.getViewport().add(logTextBox);
		JScrollBar logScrollBar = logScrollPane.getVerticalScrollBar();
		logScrollBar.setValue(logScrollBar.getMaximum());
	}
	
	public ArrayList<JPanel> getNorthCellList() {
		return northCellList;
	}

	public ArrayList<JPanel> getEnhancementCellList() {
		return enhancementCellList;
	}

	public ArrayList<JPanel> getSouthCellList() {
		return southCellList;
	}
	
	public class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == newGameBtn) {
				manager.resetGame(true);
			} else if(e.getSource() == restartWithNewPlayersBtn) {
				dispose();
				MainDriver.gameStart();
			}
		}
	}
	
	public class CardCellListener implements MouseListener {
		private CardCell cell;
		
		public CardCellListener(CardCell cell) {
			this.cell = cell;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
				Card[] cellLoc;
				switch(cell.getCellLoc()) {
					case P1: cellLoc = manager.getCurrentField().getP1Creatures();
							 break;
					case P2: cellLoc = manager.getCurrentField().getP2Creatures();
					 		 break;
					default: cellLoc = manager.getCurrentField().getEnhancements();
				}
				manager.selectFieldLocation(cellLoc, cell.getPosition());
		}



		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}

		@Override
		public void mouseEntered(MouseEvent arg0) {}
		
	}
	
	public class CardPanelListener implements MouseListener {
		
		private CardPanel cardPanel;
		
		public CardPanelListener(CardPanel cardPanel) {
			this.cardPanel = cardPanel;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(cardPanel.getParent() == (handPanels[manager.getCurrentPlayerInt() - 1])) {
				manager.selectCard(cardPanel.getCard());
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}
		
		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
	}
}
