package v1;

/**
 * 
 * @author Craig Wert, Brendan Armstrong, Alex Lam, Sean McMillan
 * @version 5/1/17
 */
public class FieldModel {
	
	private CreatureCard[] p1Creatures = new CreatureCard[5];
	private CreatureCard[] p2Creatures = new CreatureCard[5];
	private EnhancementCard[] enhancements = new EnhancementCard[4];
	
	/**
	 * No argument constructor for FieldModel
	 */
	public FieldModel() {
		
	}
	
	/**
	 * Constructor for FieldModel
	 * 
	 * @param p1 player 1's creatures on the field
	 * @param p2 player 2's creatures on the field
	 * @param en all enhancements on the field
	 */
	public FieldModel(CreatureCard[] p1, CreatureCard[] p2, EnhancementCard[] en){
		this.p1Creatures = p1;
		this.p2Creatures = p2;
		this.enhancements = en;
	}

	/**
	 * Gets player 1's creatures
	 * 
	 * @return array of player 1's creatures
	 */
	public CreatureCard[] getP1Creatures() {
		return p1Creatures;
	}
	
	/**
	 * Gets player 2's creatures
	 * 
	 * @return array of player 2's creatures
	 */
	public CreatureCard[] getP2Creatures() {
		return p2Creatures;
	}
	
	/**
	 * Gets the enhancements
	 * 
	 * @return array of the enhancements
	 */
	public EnhancementCard[] getEnhancements() {
		return enhancements;
	}
	
	/**
	 * Adds a creature on the field 
	 * 
	 * @param player player playing the creature
	 * @param newCard creature being played
	 * @param index spot on the field where the creature is being played
	 * @throws InvalidPlayException if the play is not valid
	 */
	public void addCreature(int player, CreatureCard newCard, int index) throws InvalidPlayException {
		if((p1Creatures[index] != null && player == 1) || (p2Creatures[index] != null && player == 2)) {
			throw new InvalidPlayException();
		}
		else if (player == 1) {
			p1Creatures[index] = newCard;
		}
		else if (player == 2) {
			p2Creatures[index] = newCard;
		}
	}

	/**
	 * Adds an enhancement on the field
	 * 
	 * @param newCard enhancement being played
	 * @param index spot on the field where the enhancement is being played
	 * @throws InvalidPlayException if the play is not valid
	 */
	public void addEnhancement(EnhancementCard newCard, int index) throws InvalidPlayException {
		if(enhancements[index] != null || !newCard.getClass().getSimpleName().equals("EnhancementCard")) {
			throw new InvalidPlayException();
		}
		else {
			enhancements[index] = newCard;
		}
	}
}