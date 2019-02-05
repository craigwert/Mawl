package v1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CardListener implements Serializable{
	CardPanel panel;
	
	public CardListener(CardPanel panel) {
		this.panel = panel;
	}
	
	public void update() {
		panel.update();
	}
	
	public void destroy() {
		panel.setVisible(false);
	}
	
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
	
	public CardListener deserialize(String fileName)
	{
		CardListener c = new CardListener(new CardPanel());
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
		{
			c = (CardListener) in.readObject();
			
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
