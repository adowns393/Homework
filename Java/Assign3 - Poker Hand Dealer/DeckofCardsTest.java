
public class DeckofCardsTest {
	
	public static void main(String[] args)
	{
		//Creates a deck of cards
		DeckOfCards myDeckOfCards = new DeckOfCards();
		myDeckOfCards.shuffle();
		
		//Creates two five-card hands with the same deck
		CardHand hand1 = new CardHand(myDeckOfCards);
		CardHand hand2 = new CardHand(myDeckOfCards);
		
		//Prints out the contents of hand 1
		System.out.printf("Hand 1:\n");
		for(int i = 0; i < 5; i++)
			System.out.printf("%s\n", hand1.getCard(i));
		System.out.printf("%s\n", hand1.getHand());
		
		//Prints out the contents of hand 2
		System.out.printf("\nHand 2:\n");
		for(int i = 0; i < 5; i++)
			System.out.printf("%s\n", hand2.getCard(i));
		System.out.printf("%s\n", hand2.getHand());
		
		if(hand1.getHandRank() == hand2.getHandRank())
			System.out.printf("\nWinner: Draw\n");
		else if(hand1.getHandRank() > hand2.getHandRank())
			System.out.printf("\nWinner: Hand 1\n");
		else
			System.out.printf("\nWinner: Hand 2\n");
	}

}
