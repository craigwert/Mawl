package v1;

import java.time.LocalDate;

public class DeckFullException extends Exception
{
	protected LocalDate timeStamp;

	public DeckFullException() 
	{
		this.timeStamp = LocalDate.now();
	}
	
	public LocalDate getTimeStamp() 
	{
		return timeStamp;
	}

}
