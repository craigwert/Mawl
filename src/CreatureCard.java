package v1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CreatureCard extends Card implements Serializable{
	
	private boolean isDead = false;
	private Integer attackPower;
	private CreatureType creatureType;
	private String type = "Creature";
	
	public CreatureCard (boolean isRare, String name, Integer attackPower, CreatureType creatureType){
		super(isRare, name);
		this.attackPower = attackPower;
		this.creatureType = creatureType;
	}
	
	public CreatureCard()
	{
		
	}
	
	public String getType() {
		return type;
	}
	
	public Integer getAttackPower(){
		return attackPower;
	}
	
	public Integer getAttackPower(CreatureType enemyType){
		if((this.creatureType == CreatureType.TOOTH && enemyType == CreatureType.FIST) ||
			(this.creatureType == CreatureType.FIST && enemyType == CreatureType.CLAW) ||
			(this.creatureType == CreatureType.CLAW && enemyType == CreatureType.TOOTH)) {
			return (int)(attackPower * 1.5);
		} else {
			return attackPower;
		}
	}
	
	public void adjustPower(EnhancementCard enhancer){
		this.attackPower += enhancer.getEnhancement();
		update();
	}
	
	public String toString() {
		return type + ": " + getName() + ", power: " + String.valueOf(attackPower) + ", type: " + creatureType;
	}
	
	public CreatureType getCreatureType(){
		return creatureType;
	}

	@Override
	public void serialize(String fileName) 
	{
		try (ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(fileName));)
		{
			out.writeObject(this);
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}	
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Card deserialize(String fileName) 
	{

		Card c = new CreatureCard();
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
		{
			c = (CreatureCard) in.readObject();
			
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
			return c;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
