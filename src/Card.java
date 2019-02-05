package v1;

import java.io.Serializable;

import v1.CardListener;

public abstract class Card implements Serializable{
	private boolean isRare;
	private boolean selected;
	private String name = "";
	private String type;
	private CardListener listener;
	
	public Card(boolean isRare, String name){
		this.isRare = isRare;
		this.name = name;
	}
	
	public Card()
	{
		
	}
	
	public String getType() {
		return type;
	}
	
	public boolean getIsRare(){
		return isRare;
	}

	public boolean getSelected() {
		return selected;
	}
	
	public String getName() {
		return name;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		update();
	}
	
	public void setListener(CardListener listener) {
		this.listener = listener;
	}
	
	public void update() {
		if(listener != null) {
			listener.update();
		}
	}
	
	public void destroy() {
		if(listener != null) {
			listener.destroy();
		}
	}
	
	public abstract void serialize(String fileName);

	public abstract Card deserialize(String fileName);
	
}
