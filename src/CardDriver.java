package v1;

import java.util.LinkedList;

public class CardDriver {

	public static void main(String[] args) 
	{
		Card bat = new CreatureCard(false, "Bat", 2, CreatureType.TOOTH);
		Card meerkat = new CreatureCard(false, "Meerkat", 2, CreatureType.CLAW);
		Card giraffe = new CreatureCard(true, "Giraffe", 7, CreatureType.FIST);
		Card orca = new CreatureCard(false, "Orca", 9, CreatureType.TOOTH);
		Card hippo = new CreatureCard(true, "Hippo", 9, CreatureType.FIST);
		Card anaconda = new CreatureCard(true, "Anaconda", 7, CreatureType.FIST);
		Card rhino = new CreatureCard(true, "Rhino", 8, CreatureType.FIST);
		Card warthog = new CreatureCard(false, "Warthog", 2, CreatureType.FIST);
		Card bear = new CreatureCard(false, "Bear", 8, CreatureType.FIST);
		Card ostrich = new CreatureCard(false, "Ostrich", 6, CreatureType.CLAW);
		Card elephant = new CreatureCard(true, "Elephant", 9, CreatureType.FIST);
		Card hawk = new CreatureCard(false, "Hawk", 4, CreatureType.CLAW);
		Card doge = new CreatureCard(true, "Doge", 1, CreatureType.CLAW);
		Card turtle = new CreatureCard(false, "Turtle", 4, CreatureType.TOOTH);
		Card harambe = new CreatureCard(true, "Harambe", 9, CreatureType.FIST);
		Card rabbit = new CreatureCard(false, "Rabbit", 2, CreatureType.TOOTH);
		Card wolf = new CreatureCard(false, "Wolf", 6, CreatureType.TOOTH);
		Card cheetah = new CreatureCard(true, "Cheetah", 5, CreatureType.TOOTH);
		Card crocodile = new CreatureCard(false, "Crocodile", 8, CreatureType.TOOTH);
		Card komodo = new CreatureCard(true, "Komodo", 4, CreatureType.TOOTH);
		Card panther = new CreatureCard(false, "Panther", 6, CreatureType.CLAW);
		Card cobra = new CreatureCard(false, "Cobra", 4, CreatureType.TOOTH);
		Card tiger = new CreatureCard(true, "Tiger", 8, CreatureType.CLAW);
		Card panda = new CreatureCard(true, "Panda", 6, CreatureType.FIST);
		Card lightning = new EnhancementCard(false, "Lightning", 3);
		Card rage = new EnhancementCard(false, "Rage", 2);
		Card wave = new EnhancementCard(false, "Wave", -3);
		Card bound = new EnhancementCard(false, "Bound", -2);
		Card dontcare = new EnhancementCard(false, "Don't Care", 10);
		Card freedom = new EnhancementCard(false, "Freedom", 4);
		Card doom = new EnhancementCard(false, "Doom", -5);
		Card freshmeat = new EnhancementCard(false, "Fresh Meat", 4);
		Card drought = new EnhancementCard(false, "Drought", -3);
		Card mire = new EnhancementCard(false, "Mire", -1);
		Card serenity = new EnhancementCard(false, "Serenity", 5);
		Card wildfire = new EnhancementCard(false, "Wildfire", -4);
		
		
		LinkedList<Card> l = new LinkedList<Card>();
		LinkedList<Card> m = new LinkedList<Card>();
		
		
		m.add(bat);
		m.add(giraffe);
		m.add(hippo);
		m.add(anaconda);
		m.add(rhino);
		m.add(warthog);
		m.add(bear);
		m.add(ostrich);
		m.add(elephant);
		m.add(hawk);
		m.add(doge);
		m.add(turtle);
		m.add(harambe);
		m.add(rabbit);
		m.add(wolf);
		m.add(cheetah);
		m.add(crocodile);
		m.add(komodo);
		m.add(panther);
		
		
		l.add(bat);
		l.add(giraffe);
		l.add(hippo);
		l.add(anaconda);
		l.add(rhino);
		l.add(warthog);
		l.add(bear);
		l.add(ostrich);
		l.add(elephant);
		l.add(hawk);
		l.add(doge);
		l.add(turtle);
		l.add(harambe);
		l.add(rabbit);
		l.add(wolf);
		l.add(cheetah);
		l.add(crocodile);
		l.add(komodo);
		l.add(panther);
		l.add(cobra);
		l.add(tiger);
		l.add(panda);
		l.add(lightning);
		l.add(rage);
		l.add(wave);
		l.add(bound);
		l.add(dontcare);
		l.add(freedom);
		l.add(doom);
		l.add(freshmeat);
		l.add(meerkat);
		l.add(orca);
		l.add(serenity);
		l.add(mire);
		l.add(drought);
		l.add(wildfire);
		
		Deck d = new Deck(l);
		Deck c = new Deck(m);
		
		d.serialize("allcards.ser");
		c.serialize("default.ser");
		
		
	}

}
