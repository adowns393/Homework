
public class CardHand 
{
	private Card[] hand;
	private static final int SIZE_OF_HAND = 5;
	
	//This constructor builds a poker hand of five cards
	public CardHand(DeckOfCards deck)
	{
		hand = new Card[SIZE_OF_HAND];
		
		for(int i = 0; i < hand.length; i++)
			hand[i] = deck.dealCard();
	}
	
	//Checks if the hand has a flush
	private int checkFlush()
	{
		//This array stores the total count of each card face
		int facesCount[] = {0, 0, 0, 0};
		
		//This loop increments the face count by 1 in the appropriate index for each card
		for(int i = 0; i < hand.length; i++)
		{
			switch (hand[i].getSuit())
			{
			case "Hearts":
				facesCount[0]++;
				break;
			case "Diamonds":
				facesCount[1]++;
				break;
			case "Spades":
				facesCount[2]++;
				break;
			case "Clubs":
				facesCount[3]++;
				break;
			}
		}
		
		//Goes through each index and returns 1 if the hand has five cards of the same value
		for(int i = 0; i < 4; i++)
			if (facesCount[i] == 5)
				return 1;
		
		//If none of the indices had a count of 5, there is no flush in the hand
		return 0;
	}
	
	//Checks the hand for pairs and full houses
	private String checkPairs()
	{
		//This array will act as a bucket for the value of each card. For readability,
		//the array is size 14 instead of 13 so that, for example, an ace of value 1
		//will be stored in index 1 instead of 0.
		int cardCount[] = new int[14];
		
		//These two ints store the number of cards in a pair. At most, only two
		//pairs can exist in a five-card hand.
		int pair1 = 0;
		int pair2 = 0;
		
		//Zeroes out the array
		for(int i = 0; i < 14; i++)
			cardCount[i] = 0;
		
		//Evaluates the value of each card and increments the bucket value at the appropriate index
		for(int i = 0; i < SIZE_OF_HAND; i++)
			cardCount[ hand[i].getValue() ]++;
		
		//Goes through each bucket value and, if it's >= 2, stores that value in a pair int whose
		//value is 0.
		for(int i = 0; i < 14; i++)
		{
			if (cardCount[i] > 1)
			{
				if (pair1 == 0)
					pair1 = cardCount[i];
				else
				{
					pair2 = cardCount[i];
				}
			}
		}
		
		//Evaluates the result and returns it
		if(pair1 == 4)
			return "Four of a Kind";
		else if((pair1 == 3 && pair2 == 2) || (pair1 == 2 && pair2 == 3))
			return "Full House";
		else if(pair1 == 3)
			return "Three of a Kind";
		else if(pair1 == 2 && pair2 == 2)
			return "Two Pair";
		else if(pair1 == 2)
			return "Pair";
		else
			return "No Pair";
	}
	
	//Checks if the hand contains a straight
	private int checkStraight()
	{
		//This array will act as a bucket for the value of each card. For readability,
		//the array is size 14 instead of 13 so that, for example, an ace of value 1
		//will be stored in index 1 instead of 0.
		int cardCount[] = new int[14];
		
		//This int will store the value where the first non-empty bucket value is.
		int index = 0;
		
		//Zeroes out the array
		for(int i = 0; i < 14; i++)
			cardCount[i] = 0;
		
		//Evaluates the value of each card and increments the bucket value at the appropriate index
		for(int i = 0; i < SIZE_OF_HAND; i++)
			cardCount[ hand[i].getValue() ]++;
		
		//Goes through the bucket until a non-empty bucket index is found and stores that index
		while(cardCount[index] == 0)
		{
			index++;
		}
		
		//Checks the value of the next 4 bucket indices. If all of them have a value of 1,
		//the hand contains a straight. If not, there is not straight in the hand.
		for(int limit = index + 5; index < limit; index++)
		{
			if(limit > cardCount.length)
				return 0;
			if(cardCount[index] != 1)
				return 0;
		}
		
		//If the function did not return in the for-loop, the hand contains a straight.
		return 1;
	}
	
	//Goes through the possible hand combinations and returns a string representation of the highest result.
	public String getHand()
	{
		if (checkStraight() == 1 && checkFlush() == 1)
			return "Striahgt Flush";
		else if(checkPairs() == "Four of a Kind")
			return "Four of a Kind";
		else if(checkPairs() == "Full House")
			return "Full House";
		else if(checkFlush() == 1)
			return "Flush";
		else if(checkStraight() == 1)
			return "Striahgt";
		else if(checkPairs() == "Three of a Kind")
			return "Three of a Kind";
		else if(checkPairs() == "Two Pair")
			return "Two Pair";
		else if(checkPairs() == "Pair")
			return "Pair";
		else
			return "No Hand";
	}
	
	//Goes through the possible hand combinations and returns an int representation of the highest result.
	public int getHandRank()
	{
		if (checkStraight() == 1 && checkFlush() == 1)
			return 8;
		else if(checkPairs() == "Four of a Kind")
			return 7;
		else if(checkPairs() == "Full House")
			return 6;
		else if(checkFlush() == 1)
			return 5;
		else if(checkStraight() == 1)
			return 4;
		else if(checkPairs() == "Three of a Kind")
			return 3;
		else if(checkPairs() == "Two Pair")
			return 2;
		else if(checkPairs() == "Pair")
			return 1;
		else
			return 0;
	}
	
	//Returns the card at the specified index of the hand
	public Card getCard(int index)
	{
		return hand[index];
	}
	
}
