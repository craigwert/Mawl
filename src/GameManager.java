package v1;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GameManager {
	//Learned a bit about loggers using http://stackoverflow.com/questions/5950557/good-examples-using-java-util-logging
	private static final Logger LOGGER = Logger.getLogger(GameManager.class.getName());
	private static final long PHASE_DELAY = 1000;
	private static final long EFFECT_DELAY = 500;
	
	private Handler infoHandler;
	private FieldModel currentField;
	private Random rng = new Random();
	private Player player1;
	private Player player2;
	private LinkedList<Card> player1BaseDeck;
	private LinkedList<Card> player2BaseDeck;
	private Map<Integer, Player> playerMap = new HashMap<Integer, Player>();
	
	private FieldGui gui;
	
	private Card selectedCard;
	private Card[] selectedFieldSection;
	private int selectedFieldIndex = -1;
	private int currentPlayer;
	private boolean fieldIsFull;
	
	//Was originally using a normal ExecutorService. Inspired to use ScheduledExecutorService by:
	//http://stackoverflow.com/questions/24104313/how-to-delay-in-java
	ScheduledExecutorService playExecutor = Executors.newSingleThreadScheduledExecutor();
	
	private Function<Integer, List<Card>> getCurrHand = p -> playerMap.get(p).getMyHand().getCards();
	
	public GameManager(Player player1, Player player2){
		this.player1 = player1;
		this.player2 = player2;
		cloneDecks(player1.getMyDeck(), player2.getMyDeck());
		playerMap.put(1, player1);
		playerMap.put(2, player2);
		gui = new FieldGui(this);
	}
	
	public void newGame() {
		currentField = new FieldModel();
		clearField();
		currentPlayer = rng.nextInt(2)+1;
		player1.getMyDeck().shuffle();
		player2.getMyDeck().shuffle();
		forceDraw(1);
		forceDraw(2);
		gameLog(MessageFormat.format("{0} goes first!", new Object[]{playerMap.get(currentPlayer).getName()}));
		playPhase();
	}
	
	public void resetGame(boolean playAgain) {
			player1.clearHand();
			player1.setMyDeck(new Deck(player1BaseDeck));
			player2.clearHand();
			player2.setMyDeck(new Deck(player2BaseDeck));
			if(playAgain) {
				cloneDecks(player1.getMyDeck(), player2.getMyDeck());
				newGame();
			}
	}
	
	public void cloneDecks(Deck player1Deck, Deck player2Deck) {
		player1BaseDeck = (LinkedList<Card>)player1Deck.getCards().clone();
		player2BaseDeck = (LinkedList<Card>)player2Deck.getCards().clone();
	}
	
	public void playPhase(){
		gameLog("Play phase, START!");
		fieldIsFull = false;
		playExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				play();
			}
		}, 0, 50, TimeUnit.MILLISECONDS);
	}
		
	public void play() {
		if(selectedFieldIndex != -1 && selectedCard != null && selectedFieldSection != null) {
			try {
				determinePlay();
				gameLog(MessageFormat.format("{0} played a {1} at space {2}", new Object[]{playerMap.get(currentPlayer).getName(), selectedCard.getName(), String.valueOf(selectedFieldIndex)}));
				if(currentPlayer== 1) {
					currentPlayer = 2;
				} else {
					currentPlayer = 1;
				}
				
				if(fieldIsFull) {
					shutDownPlayExecutor();
					gameLog("The armies are set. Now, they must be enhanced.");
					timeDelay(PHASE_DELAY);
					enhancementPhase();
				} else if(checkForFullField()) {
					fieldIsFull = true;
					gameLog(MessageFormat.format("A field is full! The drums of war approach. {0}, make your final move!", playerMap.get(currentPlayer).getName()));
				} else {
					gameLog(MessageFormat.format("{0} goes next.", playerMap.get(currentPlayer).getName()));
				}
				
			} catch (InvalidPlayException e) {
				gameLog(MessageFormat.format("Invalid play: {0} at slot {1}", new Object[]{playerMap.get(currentPlayer).getName(), selectedCard.getName(), String.valueOf(selectedFieldIndex)}));
				LOGGER.log(Level.FINE, e.toString(), e);
			} catch (Exception e) {
				gameLog("There was a major error. Please try again.");
				LOGGER.log(Level.FINE, e.toString(), e);
			} finally {
				selectedFieldIndex = -1;
				selectCard(selectedCard);
				selectedFieldSection = null;
			}
		}
		

	}
	
	public void selectCard(Card selectedCard) {
		if(this.selectedCard != null && this.selectedCard == selectedCard) {
			selectedCard.setSelected(false);
			this.selectedCard = null;
		} else {
			if(getCurrHand.apply(currentPlayer).contains(selectedCard)) {
				if(this.selectedCard != null) {
					this.selectedCard.setSelected(false);
				}
				selectedCard.setSelected(true);
				this.selectedCard = selectedCard;
			} else {
				gameLog(MessageFormat.format("This card is not in your hand, {0}", playerMap.get(currentPlayer).getName()));
			}
		}
	}
	
	public void selectFieldLocation(Card[] fieldSection, int index) {
		if(selectedCard != null) {
			this.selectedFieldSection = fieldSection;
			this.selectedFieldIndex = index;
		} else {
			gameLog("Please select a card first.");
		}
	}

	public void determinePlay() throws InvalidPlayException {
		//No need to check for nulls, determinePlay is only called by playPhase after checking for nulls.
		if(selectedCard.getType().equals("Creature") && selectedFieldSection.getClass().getSimpleName().equals("CreatureCard[]")) {
			if(getCurrHand.apply(currentPlayer).contains(selectedCard)) {
				currentField.addCreature(currentPlayer, (CreatureCard)selectedCard, selectedFieldIndex);
				moveCard();
			} else {
				gameLog(MessageFormat.format("This card is not in your hand, {0}", playerMap.get(currentPlayer).getName()));
			}
		}
		else if(selectedCard.getType().equals("Enhancement") && selectedFieldSection.getClass().getSimpleName().equals("EnhancementCard[]")) {
			if(getCurrHand.apply(currentPlayer).contains(selectedCard)) {
				currentField.addEnhancement((EnhancementCard)selectedCard, selectedFieldIndex);
				moveCard();
			} else {
				gameLog(MessageFormat.format("This card is not in your hand, {0}", playerMap.get(currentPlayer).getName()));
			}
		} 
		else {
			throw new InvalidPlayException();
		}
	}
	
	public void moveCard() {
		CellLocation loc;
		if(selectedCard.getType().equals("Enhancement")) {
			loc = CellLocation.ENHANCEMENT;
		} else if (currentPlayer == 1) {
			loc = CellLocation.P1;
		} else {
			loc = CellLocation.P2;
		}
		gui.addCard(selectedCard, loc, selectedFieldIndex);
		getCurrHand.apply(currentPlayer).remove(selectedCard);
		forceDraw(currentPlayer);
	}
	
	public void forceDraw(int player) {
		playerMap.get(player).drawToFullHand();
		gui.updateHand(player, playerMap.get(player).getMyHand());
	}
	
	public boolean checkForFullField() {
		boolean fullField1 = true;
		boolean fullField2 = true;
		for(int i = 0; i < currentField.getP1Creatures().length; i++) {
			if(currentField.getP1Creatures()[i] == null) {
				fullField1 = false;
			}
		}
		for(int i = 0; i < currentField.getP2Creatures().length; i++) {
			if(currentField.getP2Creatures()[i] == null) {
				fullField2 = false;
			}
		}
		return(fullField1 || fullField2);
	}
	
	public void enhancementPhase() {
		gameLog("Enhancement phase, START!");
		CreatureCard[] player1Field = currentField.getP1Creatures();
		EnhancementCard[] enhancements = currentField.getEnhancements();
		CreatureCard[] player2Field = currentField.getP2Creatures();
		for(int i = 0; i <= 3; i++){
			enhance(player1Field[i], player2Field[i], enhancements[i]);
			timeDelay(EFFECT_DELAY);
		}
		for(int i = 0; i <= 3; i++){
			enhance(player1Field[i+1], player2Field[i+1], enhancements[i]);
			timeDelay(EFFECT_DELAY);
		}
		currentField = new FieldModel(player1Field, player2Field, enhancements);
		gameLog("The battle is upon us!");
		timeDelay(PHASE_DELAY);
		battlePhase();
	}
	
	public void enhance(CreatureCard creatureCard1, CreatureCard creatureCard2, EnhancementCard enhancementCard){
		if(enhancementCard != null) {
			if(creatureCard1 != null && creatureCard1.getIsRare()) {
				gameLog(MessageFormat.format("Enhancing {0} with {1}", new Object[]{creatureCard1.toString(), enhancementCard.toString()}));
				creatureCard1.adjustPower(enhancementCard);
				gameLog(MessageFormat.format("{0}'s new power: " + String.valueOf(creatureCard1.getAttackPower()), new Object[]{creatureCard1.getName()}));
			}
			if(creatureCard2 != null && creatureCard2.getIsRare()) {
				gameLog(MessageFormat.format("Enhancing {0} with {1}", new Object[]{creatureCard2.toString(), enhancementCard.toString()}));
				creatureCard2.adjustPower(enhancementCard);
				gameLog(MessageFormat.format("{0}'s new power: " + String.valueOf(creatureCard2.getAttackPower()), new Object[]{creatureCard2.getName()}));
			}
		}
	}

	public void battlePhase(){
		gameLog("Battle phase, START!");
		CreatureCard[] player1Field = currentField.getP1Creatures();
		CreatureCard[] player2Field = currentField.getP2Creatures();
		EnhancementCard[] enhancements = currentField.getEnhancements();
		for(int i = 0; i < 5; i++){
			battle(player1Field[i], player2Field[i]);
			timeDelay(EFFECT_DELAY);
		}
		currentField = new FieldModel(player1Field, player2Field, enhancements);
		checkWinner();
	}
	
	public void battle(CreatureCard creatureCard1, CreatureCard creatureCard2){
		if(creatureCard1 != null && creatureCard2 != null) {
			if(creatureCard1.getAttackPower(creatureCard2.getCreatureType()) == creatureCard2.getAttackPower(creatureCard1.getCreatureType())){
				gameLog(MessageFormat.format("{0} and {1} destroy one another!", new Object[]{creatureCard1.getName(), creatureCard2.getName()}));
				creatureCard1.destroy();
				creatureCard1.setDead(true);
				creatureCard2.destroy();
				creatureCard2.setDead(true);
			}
			else if(creatureCard1.getAttackPower(creatureCard2.getCreatureType()) > creatureCard2.getAttackPower(creatureCard1.getCreatureType())){
				gameLog(MessageFormat.format("{0} crushes {1}!", new Object[]{creatureCard1.getName(), creatureCard2.getName()}));
				creatureCard2.destroy();
				creatureCard2.setDead(true);
			}
			else {
				gameLog(MessageFormat.format("{1} crushes {0}!", new Object[]{creatureCard1.getName(), creatureCard2.getName()}));
				creatureCard1.destroy();
				creatureCard1.setDead(true);
			}
		}
	}
	
	public void checkWinner() {
		int p1Creatures = 0;
		int p2Creatures = 0;
		for(int i = 0; i < currentField.getP2Creatures().length; i++) {
			if(!currentField.getP2Creatures()[i].isDead()) {
				p2Creatures++;
			}
			if(!currentField.getP1Creatures()[i].isDead()) {
				p1Creatures++;
			}
		}
		if(p1Creatures > p2Creatures) {
			playerMap.get(1).addGame(true);
			playerMap.get(2).addGame(false);
			gameLog(MessageFormat.format("{0} wins!\n{0} has played {1} games, with {2} wins.\n{3} has played {4} games, with {5} wins.", 
					new Object[]{playerMap.get(1).getName(), playerMap.get(1).getGames(), playerMap.get(1).getWins(), 
								 playerMap.get(2).getName(), playerMap.get(2).getGames(), playerMap.get(2).getWins()
								 }));
		} else if (p2Creatures > p1Creatures) {
			playerMap.get(1).addGame(false);
			playerMap.get(2).addGame(true);
			gameLog(MessageFormat.format("{0} wins!\n{0} has played {1} games, with {2} wins.\n{3} has played {4} games, with {5} wins.", 
					new Object[]{playerMap.get(2).getName(), playerMap.get(2).getGames(), playerMap.get(2).getWins(), 
								 playerMap.get(1).getName(), playerMap.get(1).getGames(), playerMap.get(1).getWins()
								 }));
		} else {
			playerMap.get(1).addGame(false);
			playerMap.get(2).addGame(false);
			gameLog(MessageFormat.format("Congratulations, {0} and {1}! You''re both losers!\n{0} has played {2} games, with {3} wins.\n{1} has played {4} games, with {5} wins.", 
					new Object[]{playerMap.get(1).getName(), playerMap.get(2).getName(), playerMap.get(1).getGames(), 
								 playerMap.get(1).getWins(), playerMap.get(2).getGames(), playerMap.get(2).getWins()
								 }));
		}
	}
	
	public void gameLog(String log) {
		LOGGER.log(Level.INFO, log);
		JTextArea logBox = gui.getLogTextBox();
		JTextArea newLogBox = new JTextArea(logBox.getText() + System.lineSeparator() + log);
		gui.setLogTextBox(newLogBox);
	}
	
	public void shutDownPlayExecutor() {
		playExecutor.shutdownNow();
		LOGGER.log(Level.CONFIG, "Shutting down the play executor");
	}
	
	public void timeDelay(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} 
		catch (InterruptedException e) {
			
		}
	}
	
	private void clearField() {
		for(JPanel panel : gui.getNorthCellList()) {
			panel.removeAll();
			panel.repaint();
		}
		for(JPanel panel : gui.getSouthCellList()) {
			panel.removeAll();
			panel.repaint();
		}
		for(JPanel panel : gui.getEnhancementCellList()) {
			panel.removeAll();
			panel.repaint();
		}
	}
	
	public FieldModel getCurrentField() {
		return currentField;
	}
	
	public void setCurrentField(FieldModel newField) {
		this.currentField = newField;
	}
	
	public Card getSelectedCard() {
		return selectedCard;
	}
	
	public Card[] getSelectedFieldSection() {
		return selectedFieldSection;
	}
	
	public int getSelectedFieldIndex() {
		return selectedFieldIndex;
	}
	
	public Player getPlayer(int player) {
		return playerMap.get(player);
	}
	
	public int getCurrentPlayerInt() {
		return currentPlayer;
	}

	public void savePlayers() 
	{
		resetGame(false);
		player1.serialize(player1.getName() + ".ser");
		player2.serialize(player2.getName() + ".ser");
	}
}
