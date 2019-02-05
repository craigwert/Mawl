package v1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class EnhancementCard extends Card implements Serializable {

	private Integer enhancePower;
	private String type = "Enhancement";
	
	public EnhancementCard(boolean isRare, String name, Integer enhancePower){
		super(isRare, name);
		this.enhancePower = enhancePower;
	}
	
	public EnhancementCard() 
	{
		
	}

	public String getType() {
		return type;
	}
	
	public Integer getEnhancement() {
		return enhancePower;
	}
	
	public String toString() {
		return type + ": " + getName() + ", power: " + String.valueOf(enhancePower);
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
		Card c = new EnhancementCard();

		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
		{
			c = (EnhancementCard) in.readObject();

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
}
