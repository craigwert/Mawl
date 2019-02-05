package v1;

import javax.swing.JPanel;

public class CardCell extends JPanel {
	private CellLocation cellLoc;
	private int position;
	private CardPanel cardPanel;
	
	public CardCell(CellLocation cellLoc, int position) {
		this.cellLoc = cellLoc;
		this.position = position;
	}
	
	public void addCardPanel(CardPanel cardPanel) {
		this.cardPanel = cardPanel;
		this.add(cardPanel);
	}
	
	public void removeCardPanel() {
		if(this.cardPanel != null) {
			this.remove(cardPanel);
		}
		this.cardPanel = null;
	}
	
	public CardPanel getCardPanel() {
		return this.cardPanel;
	}
	
	public CellLocation getCellLoc() {
		return cellLoc;
	}
	
	public int getPosition() {
		return position;
	}
}
