//Michael Flynn
//10-12-2021
//This class represents a single playing card

public class Card{

   //Reference variables
   private int suit;
   private int rank;
   private int cardId;
   
   public static final int KING = 13;
   public static final int QUEEN = 12;
   public static final int JACK = 11;
   public static final int ACE = 1;
   public static final int WAR_ACE = 14; //The high ace as ranked in war
   
   public static final int SPADES = 0;
   public static final int HEARTS = 1;
   public static final int DIAMONDS = 2;
   public static final int CLUBS = 3;
   
   //arrays to convert card values and suit codes into their actual values
   static String[] suits ={"Spades", "Hearts", "Diamonds", "Clubs"};
   static String[] ranks ={"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
   
   
   //Constructor that creates cards based on rank and suit
   public Card(int rank, int suit){
   
      this.rank = rank;
      
      this.suit = suit;
      
      //if the card is not a special case (war ace)
      if(rank < this.WAR_ACE) this.cardId = ((13 * this.suit) + (rank-1));
      //leave the card ID be the same for war ace as regular ace
      else this.cardId = (13 * this.suit); 
      
      
           
   }
   

   
   
   //getter methods
   
   public String getSuitString(){
      return suits[suit];
   }
   public String getRankString(){
      return ranks[rank];
   }
   public int getRankInt(){
      return this.rank;
   }
   public int getID(){
     return cardId;
   }
   
   public String toString(){
      
      
      if(rank == 14) return "Ace of " + suits[suit];
         
      else return ranks[rank-1] + " of " + suits[suit];
      
      //return s1;
   
   
   
   
   
   }
}