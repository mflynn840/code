//MF
//so im thinking hand should be a data field for player
//player interfaces hand
import java.util.ArrayList;

public class HandComparison{

   public static final int NUMBER_OF_CARDS_IN_HAND = 5;

   public static void main(String[] args){
   
      ArrayList<Card> hand = new ArrayList<Card>();
      
      Deck d1 = new Deck("poker");
      d1.shuffle();
      
      
      Hand h1 = new Hand(d1.deal(5));
      h1.sortHand();
      System.out.println(h1.toString()); 
      
     
      System.out.println(getHandName(h1.getHand()));
      //System.out.println(isFourOfAKind(hand));
      //System.out.println(hand.get(0).getSuit());
   }
   
   public static boolean isRoyalFlush(ArrayList<Card> hand){
   
      //the lowest rank index in a royal flush will always be 10
      //loop the array and if the lowest index is less than 10 return false
      //good here!
      
      for(int index = 0; index<hand.size(); index++){
      
         if(hand.get(index).getRankInt() < 10){
            return false;
         
         }
      
         
      }
      
      //now we have to test that all of the cards are the same suit
      //first store the suit of the first card then compare all others to it.
      String suit = "";
      for(int index = 0; index< hand.size(); index++){
      
         if(index == 0){
         
            suit = hand.get(index).getSuitString();
            
         }else{
         
            if(!hand.get(index).getSuitString().equals(suit)){
            
               return false;
            
            }
         
         }
      }
         int currentRankValue = 0;
         
         //finally all cards must be in acsending order
         //teach me sorting algorithm?
         //this code will work as long as I sort the hand by rank
         for(int index = 0; index< hand.size(); index++){
         
            if(index == 0){
            
               currentRankValue = hand.get(index).getRankInt();
               
            }else{
            
               if(hand.get(index).getRankInt() != (currentRankValue+1)){
               
                  return false;
               }
               
               currentRankValue = hand.get(index).getRankInt();
            
            }
         
         }
         
         //if all tests pass the hand is a royal flush
       return true;
      
      }
      
   
   public static boolean isStraightFlush(ArrayList<Card> hand){
   
      //same algorithm as for royal flush but dont need to check that the minimum card is a 10, actually the minimum card cannot be a 1o because then its a royal flush
      //if (!isRoyalFlush() && insert algorithm for flush...return true)
      if(isRoyalFlush(hand)){
      
         return false;
         
      }
      
      String suit = "";
      //make sure that all cards are same suit
      for(int index = 0; index< hand.size(); index++){
         if(index == 0){
         
            suit = hand.get(index).getSuitString();
            
         }else{
         
            if(!hand.get(index).getSuitString().equals(suit)){
            
               return false;
            
            }
         }
      }
         
      //make sure the cards are in ascending order:
      int currentRankValue = 0;
         
      for(int index = 0; index< hand.size(); index++){
         
         if(index == 0){
            
            currentRankValue = hand.get(index).getRankInt();
               
         }else{
            
            if(hand.get(index).getRankInt() != (currentRankValue+1)){
               
               return false;
            }
               
            currentRankValue = hand.get(index).getRankInt();
            
         }
         
      }
         
         return true;  
      
      
   }
     
   
   public static boolean isFourOfAKind(ArrayList<Card> hand){
   
      //search first four values, if they are all the same return true
      //if first search returns false then search last four cards, if they are same return true
      //else return false
      
      //search first four cards
      boolean firstFour = true;
      int rank = 0;
      for(int index = 0; index < (NUMBER_OF_CARDS_IN_HAND -1); index++){
      
           if(index == 0){
           
              rank = hand.get(index).getRankInt();
            
           }else{
              
              if(hand.get(index).getRankInt() != rank){
              
                firstFour = false;
              
              }
               
           
           }    
         
      }
      
      boolean lastFour = true;
      
      for(int index = (NUMBER_OF_CARDS_IN_HAND -1); index > 0 ; index--){
      
         if(index == 4){
         
            rank = hand.get(index).getRankInt();
            
         
         }else{
            if(hand.get(index).getRankInt() != rank){
            
               lastFour = false;
            
            } 
         }
         
      }
      
      if(firstFour || lastFour){
      
         return true;
         
      }else{
      
         return false;
      }
      
   }

   public static boolean isFullHouse(ArrayList<Card> hand){
   
      ///search first three cards for three of kind then second two cards for pair (use other methods)
      //search first two cards for pair then last three for 3 of kind
      
      if(isPair(hand) && isThreeOfAKind(hand)) return true;
      else return false;
         
      
      
      
   }
 
   public static boolean isFlush(ArrayList<Card> hand){
   
      //check if all cards are same rank and is not flush or straight flush
      
      String suit = "";
      //make sure all cards are the same suit
      for(int index = 0; index< hand.size(); index++){
      
         if(index == 0){
         
            suit = hand.get(index).getSuitString();
            
         }else{
         
            if(!hand.get(index).getSuitString().equals(suit)){
            
               return false;
            
            }
         
         }
      }
      if(isRoyalFlush(hand) || isStraightFlush(hand)){
         return false;
      }
      else return true;
   
   }
   
   
   public static boolean isStraight(ArrayList<Card> hand){
   
      //search for ascending order of rank and is not straight or royal flush
      int currentRankValue = 0;
      
      for(int index = 0; index< hand.size(); index++){
         
         if(index == 0){
            
            currentRankValue = hand.get(index).getRankInt();
               
         }else{
            
            if(hand.get(index).getRankInt() != (currentRankValue+1)){
               
               return false;
            }
               
            currentRankValue = hand.get(index).getRankInt();
            
         }
         
      }
      
      
      return true;
   }
   
   
   public static boolean isThreeOfAKind(ArrayList<Card> hand){
   
      //first three then last three to find three of kind, make sure its not a full house!
      
      
      boolean firstThree = true;
      int rank = 0;
      for(int index = 0; index < (NUMBER_OF_CARDS_IN_HAND -2); index++){
      
           if(index == 0){
           
              rank = hand.get(index).getRankInt();
            
           }else{
              
              if(hand.get(index).getRankInt() != rank){
              
                firstThree = false;
              
              }
               
           
           }    
         
      }
      
      boolean lastThree = true;
      
      for(int index = (NUMBER_OF_CARDS_IN_HAND -1); index > 1 ; index--){
      
         if(index == 4){
         
            rank = hand.get(index).getRankInt();
            
         
         }else{
         
            if(hand.get(index).getRankInt() != rank){
            
               lastThree = false;
            
            } 
         }
         
      }
      
      if(firstThree || lastThree){
      
         return true;
         
      }else{
      
         return false;
      }
      
      
   }
   
   
   public static boolean isTwoPair(ArrayList<Card> hand){
      //search each set of adjacent cards for a pair usin isPair()
      int pairCounter = 0;
      
      //check each set of adjacent cards, if they are the same add 1 to pair counter
      for(int index = 0; index< hand.size()-1; index++){
         
         if(hand.get(index).getRankInt() == hand.get(index+1).getRankInt()){
         
            pairCounter++;
         
         }
      
      }
      
      if(pairCounter == 2){
         
         return true;
         
      }else return false;

   }
   
   
   public static boolean isPair(ArrayList<Card> hand){
      
      int pairCounter = 0;
      
      //check each set of adjacent cards, if they are the same add 1 to pair counter
      for(int index = 0; index< hand.size()-1; index++){
         
         if(hand.get(index).getRankInt() == hand.get(index+1).getRankInt()){
         
            pairCounter++;
         
         }
      
      }
      
      if(pairCounter >= 1){
         
         return true;
         
      }else return false;
      
   }
   
   
   public static String highCard(ArrayList<Card> hand){
      //find the highest rank card
      int highRank = 0;
      int highIndex = 0;
      
      for(int index = 0; index< hand.size(); index++){
      
         if(index == 0){
         
            highRank = hand.get(index).getRankInt();
            
         }else{
         
            if(hand.get(index).getRankInt() > highRank){
            
               highRank = hand.get(index).getRankInt();
               highIndex = index;
               
            }
         
         }
      
      }
      
      return hand.get(highIndex).toString();
   }
   
   public static String getHandName(ArrayList<Card> hand){
      //call all above methods
      if(isRoyalFlush(hand)){
      
      }else if(isStraightFlush(hand)){
      
         return "Stright Flush";
      
      }else if(isFourOfAKind(hand)){
      
         return "Four of a Kind";
      
      }else if(isFullHouse(hand)){
      
         return "Full House";
      
      }else if(isFlush(hand)){
      
         return "Flush";
      
      }else if(isStraight(hand)){
      
         return "Straight";
      
      }else if(isThreeOfAKind(hand)){
      
         return "Three of a Kind";
      
      }else if(isTwoPair(hand)){
      
         return "Two Pair";
      
      }else if(isPair(hand)){
      
         return "Pair";
      
      }else{
      
         return highCard(hand);
         
      }
      return "";
   }
   
   //we may want to store the name of the hand and highest rank card in the player object (which is currently named hand object)
   















}