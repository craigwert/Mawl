package v1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class CardPanel extends JPanel implements Serializable{
	public static final int CARD_WIDTH = 105;
	public static final int CARD_HEIGHT = 135;
	public static final int UNSELECTED_BORDER_THICKNESS = 4;
	public static final int SELECTED_BORDER_THICKNESS = 7;
	public static final int NAME_FONT_SIZE = 14;
	public static final int POWER_FONT_SIZE = 24;
	public static final Color RARE_BORDER = new Color(255,215,0);
	public static final Color COMMON_BORDER_BG = Color.BLACK;
	public static final Color TEXT_COLOR = Color.WHITE;
	
	//http://game-icons.net/skoll/originals/fist.html by Skoll
	public static final String FIST_IMG = "img/fist.png";
	//http://game-icons.net/lorc/originals/saber-tooth.html by Lorc
	public static final String TOOTH_IMG = "img/tooth.png";
	//http://game-icons.net/lorc/originals/triple-claws.html by Lorc
	public static final String CLAW_IMG = "img/claw.png";
	//http://game-icons.net/lorc/originals/thor-fist.html by Lorc
	public static final String POWER_UP_IMG = "img/powerup.png";
	//http://game-icons.net/lorc/originals/voodoo-doll.html by Lorc
	public static final String POWER_DOWN_IMG = "img/powerdown.png";
	
	private ExecutorService updateService;
	
	private JPanel northPanel;
	private JPanel centerPanel;
	private JPanel southPanel;
	private JLabel testImg;
	
	private JLabel cardName;
	private JLabel power = new JLabel();
	
	private Card card;
	
	public CardPanel(Card card) {
		this.card = card;
		this.card.setListener(new CardListener(this));
		setLayout(new BorderLayout());
		constructCard();
	}
	
	public CardPanel()
	{
		
	}
	
	
	public void constructCard() {
		this.removeAll();
		northPanel = new JPanel();
		northPanel.setBackground(COMMON_BORDER_BG);
		centerPanel = new JPanel();
		centerPanel.setBackground(COMMON_BORDER_BG);
		southPanel = new JPanel();
		southPanel.setBackground(COMMON_BORDER_BG);
		southPanel.setLayout(new GridLayout(1, 2));
		
		cardName = new JLabel();
		cardName.setText(card.getName());
		formatText(cardName, NAME_FONT_SIZE);
		northPanel.add(cardName, Alignment.LEADING);
		
		if(card.getType().equals("Creature"))
		{
			testImg = new JLabel(new ImageIcon("img/C_" + card.getName().toLowerCase() + ".png"));
		}
		else
		{
			testImg = new JLabel(new ImageIcon("img/E_" + card.getName().toLowerCase() + ".png"));
		}
		centerPanel.add(testImg);
		
		if(card.getType().equals("Creature")) {
			addCreatureType();
		} else {
			addEnhancementType();
		}
		
		update();

		JPanel powerPanel = new JPanel();
		powerPanel.setLayout(new BorderLayout());
		powerPanel.setBackground(COMMON_BORDER_BG);
		powerPanel.add(power, BorderLayout.EAST);
		southPanel.add(powerPanel);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
	}

	public void update() {
		if(card.getType().equals("Creature")) {
			power.setText(String.valueOf(((CreatureCard)card).getAttackPower()));
		} else {
			power.setText((String.valueOf(((EnhancementCard)card).getEnhancement())));
		}
		formatText(power, POWER_FONT_SIZE);
		
		int borderThickness;
		if(card.getSelected()) {
			borderThickness = SELECTED_BORDER_THICKNESS;
		} else {
			borderThickness = UNSELECTED_BORDER_THICKNESS;
		}
		
		if(card.getIsRare()) {
			this.setBorder(BorderFactory.createLineBorder(RARE_BORDER, borderThickness, true));
		} else {
			this.setBorder(BorderFactory.createLineBorder(COMMON_BORDER_BG, borderThickness, true));
		}
	}
	
	public void addEnhancementType() {
		String imageString;
		if(((EnhancementCard)card).getEnhancement() >= 0) {
			imageString = POWER_UP_IMG;
		} else {
			imageString = POWER_DOWN_IMG;
		}
		addIcon(imageString);
	}
	
	public void addCreatureType() {
		String imageString;
		switch(((CreatureCard)card).getCreatureType()) {
			case FIST: imageString = FIST_IMG;
				break;
			case TOOTH: imageString = TOOTH_IMG;
				break;
			case CLAW: imageString = CLAW_IMG;
				break;
			default:
				return;
		}
		addIcon(imageString);
	}
	
	public void addIcon(String imageString) {
		ImageIcon typeImg = new ImageIcon(imageString);
		JLabel typeImgLabel = new JLabel(typeImg);
		
		JPanel typePanelSouth = new JPanel();
		typePanelSouth.setBackground(COMMON_BORDER_BG);
		typePanelSouth.setLayout(new BorderLayout());
		JPanel typePanelWest = new JPanel();
		typePanelWest.setBackground(COMMON_BORDER_BG);
		typePanelWest.setLayout(new FlowLayout(FlowLayout.LEFT));
		typePanelSouth.add(typePanelWest, BorderLayout.SOUTH);
		typePanelWest.add(typeImgLabel);
		southPanel.add(typePanelSouth);
	}
	
	public void formatText(JLabel text, int fontSize) {
		text.setText(text.getText() + "  ");
		text.setFont(new Font("Impact", Font.ITALIC, fontSize));
		text.setForeground(TEXT_COLOR);
	}
	
	public Card getCard() {
		return this.card;
	}
}
