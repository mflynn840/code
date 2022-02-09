//Michael Flynn
//10/29/2021
//this program plays through a game of war

//Limitations:
//this program cannot handle if all players in war get out
//more than two winners situation

//it also may have a logic error in it if there is a case that I did not test!

//this was a really hard project, I spent many hours on it and I just coulndt keep going after this, sorry

//I did enjoy it though because I learned a lot from this project (more than any other that I have done)
//1)how to debug code using print statements that print the data along the way
//2) naming variable names that make sense so the code is self commenting (a must for this project)
//3) tons of work with array manipulation and using classes/oop

//not sure how this project can count for less points than the card class it was a lot more work!



import java.util.Scanner;


public class NewWar{
   
   public final static int NUMBER_OF_SUITS = 4;
   public final static int NUMBER_OF_CARDS_IN_DECK = 52;
   public static void main(String[] args){
      
      //make a scanner
      Scanner sc = new Scanner(System.in);
      
      int numPlayers = 0;
      
      
      //prompt the user for the number of players
      System.out.println("How many players for this game (52 max)");
         
      numPlayers = sc.nextInt();
         
      if(numPlayers > 52) System.out.println("there are only 52 cards in a deck, so there cannot be more than 52 players, try again.");
         
      
      //prompt user for number of rounds
      System.out.println("How many rounds would you like to play? (more than 10,000 can cause instability)");
      int rounds = sc.nextInt();
      
      //start the game
      Hand[] playersHands = dealCards(numPlayers);
      game(playersHands, rounds);
   
   
   
   }
   public static Hand[] dealCards(int numPlayers){
   
      //Make a deck (of type war) to use for this game
      Deck mainDeck = new Deck("war");
   
      //immediatly shuffle the deck
      mainDeck.shuffle();
      Hand[] playersHands = new Hand[numPlayers];
      
      //deal cards (one to each player until the deck is empty)
      while(!mainDeck.isEmpty()){
      
         for(int playerIndex = 0; playerIndex < numPlayers; playerIndex++){
         
            //first run make the hands
            if((mainDeck.numCards() > (NUMBER_OF_CARDS_IN_DECK-numPlayers)) && (!mainDeck.isEmpty())) {
         
               playersHands[playerIndex] = new Hand(mainDeck.deal(1), (playerIndex + 1));
        

            
            //each subsiqent run fill in the rest of the hands 
            }else if(!mainDeck.isEmpty()) {
            
               playersHands[playerIndex].addCards(mainDeck.deal(1));
       
            
            }
         }
         
         
      }
      
      return playersHands;
   }
   
   //this method will run the game and return the winning players index
   public static void game(Hand[] playersHands, int numRounds){
   
      
           
      //this loop represents each round in the game
      
      Card[] cardPot = new Card[playersHands.length]; //make the pot that all of the players throw their cards into
      
      
      for(int round = 1; round <= numRounds; round++){ 
         System.out.println("\n==========Round" + round + "==============");
         
         //loop through each player
         for(int playerIndex = 0; playerIndex<playersHands.length; playerIndex++){

            
            // DEBUGGING PURPOSES System.out.println("Checking if " + (playersHands[playerIndex].getPlayerNumber()) + " has enough cards");
            
            //if they dont have any cards
            if(playersHands[playerIndex].getHandLength() == 0){
               
               
               playersHands = removePlayer(playerIndex , playersHands); //use a method to remove the player from game
               
               //copy over values into new cardPot that is one less in size:
               Card[] newPot = new Card[cardPot.length-1];
               for(int index = 0; index<newPot.length; index++){
                  newPot[index] = cardPot[index];
               
               
               
               }
               cardPot = newPot;
               if(playerIndex == playersHands.length-1){
               
                  break;
               
               }
               else playerIndex--;//copmensate for the the removed player
               
               
                
            }
            //if they do have enough cards
            else{
               cardPot[playerIndex] = playersHands[playerIndex].removeTopCard(); //remove a card from each hand and add it to the card pot
               
               System.out.println("Player "+ playersHands[playerIndex].getPlayerNumber() + " deals a " + playersHands[playerIndex].getLastDeltRankString());  
             
         
            }
         }
            
            //use a method to find out who delt the highest card after all players have gone
            int[] matchResults = findWinningCard(cardPot);
            
            if(matchResults[0] == -1){ //-1 return means war situation
            
               System.out.println("Time for War!");
               haveWar(playersHands, matchResults[1], cardPot);
               
               //multiple wars and awarding cards are handled in the haveWar method so after this runs, the round is over
               //haveWar(playersHands, matchResults[1], cardPot);
             
             //if there was a winner  
            }else if(matchResults[0] == 1){
               
               //find their index in the Hand array:
               int winningPlayerIndex = findWinningPlayer(matchResults[1], playersHands);
               //DEBUGGING System.out.println("winningPLayerIndex is  " + winningPlayerIndex + "they had " + playersHands[winningPlayerIndex].getHandLength() + "cards");
               
               //award the winning player their cards
               playersHands[winningPlayerIndex].addCards(cardPot);
               System.out.println("\nPlayer " + playersHands[winningPlayerIndex].getPlayerNumber() + " wins " + cardPot.length + " cards");
               
               //empty the pot
               Card[] newPot = new Card[playersHands.length];
               cardPot = newPot;
            
            
            }
         
      
        
    }
      //end of round
      
      displayResults(playersHands);
   }
   
   //dual purpose method for finding the winning cards value, aswell as determining if there is a war
   public static int[] findWinningCard(Card[] cardPot){
   
      //variable to assist the serach
      int duplicateRank = 0;//holds the rank of the duplicate card in question
      int largestCardRank = 0;//holds the rank value of the highest card
       
      int[] results = new int[2];//an array to hold the results(one winner index (or -1) plus potential winning index's if war
      
      for(int potIndex = 0; potIndex < cardPot.length; potIndex++){
        
        //if a duplicate to the largest card is found
        if((cardPot[potIndex] != null) && (cardPot[potIndex].getRankInt() == largestCardRank)){
        
            duplicateRank = cardPot[potIndex].getRankInt(); //save the rank
        
        }
        //if a larger card is found
        else if((cardPot[potIndex] != null) && (cardPot[potIndex].getRankInt() > largestCardRank)){
     
            largestCardRank = cardPot[potIndex].getRankInt(); //save the rank of the card as an int
            
        }
        
      }
      
      //if the duplicate rank is not overpowered by the largest rank, trigger war
      if(duplicateRank == largestCardRank){
      
         results[0] = -1; 
         
      }
      //if a winner was found
      else{
      
         results[0] = 1; //1 means no war
         
      }
      
      results[1] = largestCardRank; //save the card rank that won no matter what
      
      //DEBUGGING ONLY System.out.println(" " + results[0] + " " + results[1]); //debugging purposes only
   
      return results;
      
      
      
   }
   //this method returns the index in playersHands that has the winning card
   public static int findWinningPlayer(int winningCardRank, Hand[] playersHands){
      
         int winningPlayerIndex = 0;
         
         for(int playerIndex = 0; playerIndex<playersHands.length; playerIndex++){
         
            if(winningCardRank == playersHands[playerIndex].getLastDeltRank()){
            
               winningPlayerIndex = playerIndex;
               
            }
         
         }
         return winningPlayerIndex;
         
   }   
   
   
   
   //this method removes players from the game when they run out of cards:
   public static Hand[] removePlayer(int playerIndex, Hand[] playersHands){
      
      Hand[] newPlayersHands = new Hand[playersHands.length-1];
      
      System.out.println("Player " + (playersHands[playerIndex].getPlayerNumber()) + " has run out of cards, they will be removed from the game");
      
      int newIndex = 0;
      
       //populates new array     
      for(int oldIndex = 0; oldIndex <= newPlayersHands.length; oldIndex++){
      
         //only copy over players still in the game
         if(oldIndex != playerIndex){
            
            //System.out.println("adding player " + playersHands[oldIndex].getPlayerNumber());
            newPlayersHands[newIndex] = playersHands[oldIndex];
            newIndex++;
         
         
         }
      
      
      }
      System.out.println("There are now " + newPlayersHands.length + " players left.");
      
      return newPlayersHands;
   }
   
   public static void haveWar(Hand[] playersHands, int winningRank, Card[] cardPot){
      //we need to first test if players have enough cards for the war
      //find the players who have matching cards
      //each player deals 4 cards, and the last cards only are compared
      
      int[] playersInWar = new int[4]; //only 4 cards in each suit, so we cannot have more than 4 players in war
      
      Card[] warWinnings = new Card[12]; //make a cardPot for the face down(we dont know how many players are going to be out, assume that all players are out (12 card 4 max people * 3 cards to lose)
      
      //first find out who is in the war then assign those players to to the array
      int currentPlayersInWarIndex = 0;
      int currentWinningsPotIndex = 0;
      int forefitCards = 0; 
      
      for(int playerIndex = 0; playerIndex < playersHands.length; playerIndex++){//loop through the players to find who has winning card and if they are eligable for war;
          
          //if the player delt the winning card rank and they have enough cards to compete in the war:
          
          System.out.println();//make a blank line
          
          if(playersHands[playerIndex].getLastDeltRank() == winningRank && playersHands[playerIndex].getHandLength() >= 4){
          
            System.out.println("player " + playersHands[playerIndex].getPlayerNumber() + " has been invited to the war");
            
            playersInWar[currentPlayersInWarIndex] = playerIndex;
            
            currentPlayersInWarIndex++;
          
          }
      
         //if they won and dont have enough cards
         else if(playersHands[playerIndex].getLastDeltRank() == winningRank && playersHands[playerIndex].getHandLength() < 4){
              
              //if they have 0 cards remove them from the game
              if(playersHands[playerIndex].isEmpty()){
              
                 
                 removePlayer(playerIndex, playersHands);
                          
              }
              
              //if they do have cards add their cards to the card pot if they have any then remove them:
              else{
                 System.out.println(" player " + playersHands[playerIndex].getPlayerNumber() + " does not have enough cards to compete in the war, so they forefit their remaining cards");
                 
                 //add cards to winnings pot:
                 for(int index = 0; index < playersHands[playerIndex].getHandLength(); index++){
                   
                   warWinnings[currentWinningsPotIndex] = playersHands[playerIndex].removeTopCard();
                   currentWinningsPotIndex++;
                   forefitCards++;
              
                 }
              
              }
          
          
          }
       }
    
       
          
                  
       
       //clean up the players in war array to remove potential nulls
       int[] cleanedPlayersInWar = new int[currentPlayersInWarIndex];
       
       //cleanup the players in war array to remove null or 0 values
       for(int playersInWarIndex = 0; playersInWarIndex < cleanedPlayersInWar.length; playersInWarIndex++){
         cleanedPlayersInWar[playersInWarIndex] = playersInWar[playersInWarIndex];
       
       
       }
       
       playersInWar = cleanedPlayersInWar;
        
       //clean up the winnings pot so it is of the correct size:
       Card[] newWarWinnings = new Card[forefitCards + playersInWar.length * 3]; //the war pot will contain the cards forefitted plus 3 cards from each player remaining
       
       for(int newWarWinningsIndex = 0; newWarWinningsIndex< forefitCards; newWarWinningsIndex++){
          
          newWarWinnings[newWarWinningsIndex] = warWinnings[newWarWinningsIndex]; 
       
       
       
       }
       
       warWinnings = newWarWinnings;
      
       
       Card[]faceUpCards = new Card[playersInWar.length]; //make one for the face up cards
       
       //if there are still at least two players left:
       if(playersInWar.length > 1){
       
          //each player left places 3 cards in the winnings pot:
          
          int playerIndex = 0;
          
          while(playerIndex < playersInWar.length){
          
             for(int cardNumber = 0; cardNumber < 3; cardNumber++){
              
               warWinnings[currentWinningsPotIndex] = playersHands[playersInWar[playerIndex]].removeTopCard();
               currentWinningsPotIndex++;
               
          
              
             
             }
             playerIndex++;
          }
          
          //each payer places 1 card in the face up cards:
          int faceUpCardsIndex = 0;
          for(int playerIndex2 = 0; playerIndex2 < playersInWar.length; playerIndex2++){
          
            faceUpCards[faceUpCardsIndex] = playersHands[playersInWar[playerIndex2]].removeTopCard();
            
            System.out.println("player " + playersHands[playersInWar[playerIndex2]].getPlayerNumber() + " deals a " + playersHands[playersInWar[playerIndex2]].getLastDeltRankString());
            faceUpCardsIndex++;
          }
         
          
          //find out who wins:
   
          int[] winnerOfWar = findWinningCard(faceUpCards);
          
          //add all of the pots together:
          cardPot = addArrays(warWinnings, cardPot);
          cardPot = addArrays(cardPot, faceUpCards);
          
          //if another war:
          if(winnerOfWar[0] == -1){
            
            //the card pot gets all of the cards from war winnings and face up cards
            System.out.println("Time for another war!");
            
            haveWar(playersHands, winnerOfWar[1], cardPot);
          
          }
          else if(winnerOfWar[0] == 1){
            int winnerOfWarIndex = findWinningPlayer(winnerOfWar[1], playersHands);
            System.out.println("player " + playersHands[winnerOfWarIndex].getPlayerNumber() + " wins the war, they win " + cardPot.length + " cards");
            playersHands[winnerOfWarIndex].addCards(cardPot);
            
          }
        //more than one p]ayer in war situation:
       }
       
       //if there is only one player left:
       else if(playersInWar.length<1){
       
          cardPot = addArrays(warWinnings, cardPot);
          cardPot = addArrays(cardPot, faceUpCards);
          
          playersHands[playersInWar.length].addCards(cardPot);   
      
       }
    }
      
    public static Card[] addArrays(Card[] cardSetA, Card[] cardSetB){
   
      Card[] newArray = new Card[cardSetA.length + cardSetB.length];
      
      for(int index = 0; index<newArray.length; index++){
      
         if(index< cardSetA.length) newArray[index] = cardSetA[index];
         else newArray[index] = cardSetB[index-cardSetA.length];
      
      
      }
   return newArray;
   
   } 
   public static void displayResults(Hand[] playersHands){
      
      System.out.println("\n\n===================== Results ==================");
      //print the number of cards each player left in the game has;
      for(int playerIndex = 0; playerIndex < playersHands.length; playerIndex++){
         System.out.println("Player " + playersHands[playerIndex].getPlayerNumber() + " ends the game with " +  playersHands[playerIndex].getHandLength());   
      
      }
      
      //display the winner:
      int mostCardsPlayerIndex = 0;
      int mostCards = 0;
      int duplicateIndex = 0;
      for(int playerIndex = 0; playerIndex < playersHands.length; playerIndex++){
         System.out.println("Player " + playersHands[playerIndex].getPlayerNumber() + " ends the game with : " + playersHands[playerIndex].getHandLength() + " cards");
         if(playersHands[playerIndex].getHandLength() > mostCards){
         
            mostCardsPlayerIndex = playerIndex;
            mostCards = playersHands[playerIndex].getHandLength();
            
            
         }
         else if(playersHands[playerIndex].getHandLength() == mostCards){
            duplicateIndex = playerIndex;
         
         }
         
      
      
      }
      //if no duplicates were found
      if(duplicateIndex == 0)
      System.out.println("\n WINNER: Player " + playersHands[mostCardsPlayerIndex].getPlayerNumber() + " wins the game with:" + playersHands[mostCardsPlayerIndex].getHandLength() + " cards");
      
      //if two winners
      else
      System.out.println("\nWINNERS: Tie, players " + playersHands[mostCardsPlayerIndex].getPlayerNumber() + " and " + playersHands[duplicateIndex].getPlayerNumber() + " Tie the game");
   
   
   
   
   
   }
     

   
//end of program

}


/*
==========Round1==============
 Player 1 deals a Ace of Diamonds
 Player 2 deals a 8 of Diamonds
 Player 3 deals a Jack of Hearts
 Player 4 deals a 8 of Spades
 Player 5 deals a 4 of Spades
 Player 6 deals a Queen of Diamonds
 Player 7 deals a 10 of Spades
 Player 8 deals a 6 of Diamonds
 Player 9 deals a 9 of Hearts
 Player 10 deals a 8 of Clubs
 
 Player 1 wins 10 cards
 
 ==========Round2==============
 Player 1 deals a 2 of Hearts
 Player 2 deals a Jack of Spades
 Player 3 deals a 7 of Clubs
 Player 4 deals a 3 of Diamonds
 Player 5 deals a 4 of Hearts
 Player 6 deals a 4 of Diamonds
 Player 7 deals a 6 of Clubs
 Player 8 deals a 3 of Clubs
 Player 9 deals a 2 of Clubs
 Player 10 deals a Ace of Spades
 
 Player 10 wins 10 cards
 
 ==========Round3==============
 Player 1 deals a 3 of Hearts
 Player 2 deals a 3 of Spades
 Player 3 deals a Jack of Diamonds
 Player 4 deals a 7 of Diamonds
 Player 5 deals a 2 of Diamonds
 Player 6 deals a King of Spades
 Player 7 deals a 10 of Hearts
 Player 8 deals a Ace of Clubs
 Player 9 deals a 7 of Spades
 Player 10 deals a 9 of Spades
 
 Player 8 wins 10 cards
 
 ==========Round4==============
 Player 1 deals a Queen of Hearts
 Player 2 deals a 5 of Diamonds
 Player 3 deals a 6 of Spades
 Player 4 deals a 8 of Hearts
 Player 5 deals a Jack of Clubs
 Player 6 deals a 10 of Clubs
 Player 7 deals a 5 of Hearts
 Player 8 deals a 7 of Hearts
 Player 9 deals a King of Hearts
 Player 10 deals a 9 of Diamonds
 
 Player 9 wins 10 cards
 
 ==========Round5==============
 Player 1 deals a Queen of Spades
 Player 2 deals a 10 of Diamonds
 Player 3 deals a 5 of Spades
 Player 4 deals a Queen of Clubs
 Player 5 deals a 6 of Hearts
 Player 6 deals a Ace of Hearts
 Player 7 deals a King of Clubs
 Player 8 deals a King of Diamonds
 Player 9 deals a 9 of Clubs
 Player 10 deals a 5 of Clubs
 
 Player 6 wins 10 cards
 
 ==========Round6==============
 Player 1 deals a 2 of Spades
 Player 2 deals a 4 of Clubs
 Player 3 has run out of cards, they will be removed from the game
 There are now 9 players left.
 Player 4 has run out of cards, they will be removed from the game
 There are now 8 players left.
 Player 5 has run out of cards, they will be removed from the game
 There are now 7 players left.
 Player 6 deals a Queen of Spades
 Player 7 has run out of cards, they will be removed from the game
 There are now 6 players left.
 Player 8 deals a 3 of Hearts
 Player 9 deals a Queen of Hearts
 Player 10 deals a 2 of Hearts
 Time for War!
 
 
 
 player 6 has been invited to the war
 
 
 player 9 has been invited to the war
 
 player 6 deals a 6 of Hearts
 player 9 deals a Jack of Clubs
 player 9 wins the war, they win 14 cards
 
 ==========Round7==============
 Player 1 deals a Ace of Diamonds
 Player 2 has run out of cards, they will be removed from the game
 There are now 5 players left.
 Player 6 deals a Ace of Hearts
 Player 8 deals a 3 of Spades
 Player 9 deals a 10 of Clubs
 Player 10 deals a Jack of Spades
 Time for War!
 
 player 1 has been invited to the war
 
 player 6 has been invited to the war
 
 
 
 player 1 deals a 4 of Spades
 player 6 deals a 5 of Clubs
 player 6 wins the war, they win 13 cards
 
 ==========Round8==============
 Player 1 deals a Queen of Diamonds
 Player 6 deals a 8 of Diamonds
 Player 8 deals a Jack of Diamonds
 Player 9 deals a 5 of Hearts
 Player 10 deals a 7 of Clubs
 
 Player 1 wins 5 cards
 
 ==========Round9==============
 Player 1 deals a 10 of Spades
 Player 6 deals a Jack of Hearts
 Player 8 deals a 7 of Diamonds
 Player 9 deals a 7 of Hearts
 Player 10 deals a 3 of Diamonds
 
 Player 6 wins 5 cards
 
 ==========Round10==============
 Player 1 deals a 6 of Diamonds
 Player 6 deals a 8 of Spades
 Player 8 deals a 2 of Diamonds
 Player 9 deals a King of Hearts
 Player 10 deals a 4 of Hearts
 
 Player 9 wins 5 cards
 
 
 ===================== Results ==================
 Player 1 ends the game with 7
 Player 6 ends the game with 15
 Player 8 ends the game with 5
 Player 9 ends the game with 20
 Player 10 ends the game with 5
 Player 1 ends the game with : 7 cards
 Player 6 ends the game with : 15 cards
 Player 8 ends the game with : 5 cards
 Player 9 ends the game with : 20 cards
 Player 10 ends the game with : 5 cards
 
  WINNER: Player 9 wins the game with:20 cards
  
  ==========================================================================================================================
  SAMPLE 2:
  
   ==========Round1==============
 Player 1 deals a 8 of Clubs
 Player 2 deals a 5 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round2==============
 Player 1 deals a 4 of Spades
 Player 2 deals a 5 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round3==============
 Player 1 deals a 9 of Spades
 Player 2 deals a 6 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round4==============
 Player 1 deals a 3 of Clubs
 Player 2 deals a 6 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round5==============
 Player 1 deals a Queen of Diamonds
 Player 2 deals a 10 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round6==============
 Player 1 deals a 7 of Spades
 Player 2 deals a 4 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round7==============
 Player 1 deals a King of Hearts
 Player 2 deals a 6 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round8==============
 Player 1 deals a 7 of Hearts
 Player 2 deals a King of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round9==============
 Player 1 deals a 9 of Clubs
 Player 2 deals a 2 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round10==============
 Player 1 deals a 3 of Diamonds
 Player 2 deals a 7 of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round11==============
 Player 1 deals a 10 of Hearts
 Player 2 deals a 8 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round12==============
 Player 1 deals a King of Spades
 Player 2 deals a 2 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round13==============
 Player 1 deals a Queen of Spades
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round14==============
 Player 1 deals a 5 of Spades
 Player 2 deals a 9 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round15==============
 Player 1 deals a 4 of Diamonds
 Player 2 deals a Queen of Clubs
 
 Player 2 wins 2 cards
 
 ==========Round16==============
 Player 1 deals a King of Clubs
 Player 2 deals a 10 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round17==============
 Player 1 deals a Jack of Spades
 Player 2 deals a 10 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round18==============
 Player 1 deals a 8 of Hearts
 Player 2 deals a 6 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round19==============
 Player 1 deals a 5 of Clubs
 Player 2 deals a 4 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round20==============
 Player 1 deals a Jack of Clubs
 Player 2 deals a 7 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round21==============
 Player 1 deals a 3 of Spades
 Player 2 deals a Jack of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round22==============
 Player 1 deals a Ace of Clubs
 Player 2 deals a Ace of Diamonds
 Time for War!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 player 1 deals a Queen of Hearts
 player 2 deals a 9 of Diamonds
 player 1 wins the war, they win 10 cards
 
 ==========Round23==============
 Player 1 deals a 8 of Clubs
 Player 2 deals a 4 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round24==============
 Player 1 deals a 5 of Diamonds
 Player 2 deals a 5 of Hearts
 Time for War!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 player 1 deals a 10 of Clubs
 player 2 deals a King of Diamonds
 player 2 wins the war, they win 10 cards
 
 ==========Round25==============
 Player 1 deals a 7 of Spades
 Player 2 deals a 3 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round26==============
 Player 1 deals a 4 of Clubs
 Player 2 deals a 7 of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round27==============
 Player 1 deals a King of Hearts
 Player 2 deals a Queen of Spades
 
 Player 1 wins 2 cards
 
 ==========Round28==============
 Player 1 deals a 6 of Clubs
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round29==============
 Player 1 deals a 9 of Clubs
 Player 2 deals a 5 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round30==============
 Player 1 deals a 2 of Diamonds
 Player 2 deals a 9 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round31==============
 Player 1 deals a 10 of Hearts
 Player 2 deals a 4 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round32==============
 Player 1 deals a 8 of Spades
 Player 2 deals a Queen of Clubs
 
 Player 2 wins 2 cards
 
 ==========Round33==============
 Player 1 deals a King of Spades
 Player 2 deals a 3 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round34==============
 Player 1 deals a 2 of Clubs
 Player 2 deals a Jack of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round35==============
 Player 1 deals a King of Clubs
 Player 2 deals a 9 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round36==============
 Player 1 deals a 10 of Diamonds
 Player 2 deals a 6 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round37==============
 Player 1 deals a Jack of Spades
 Player 2 deals a Queen of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round38==============
 Player 1 deals a 10 of Spades
 Player 2 deals a 3 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round39==============
 Player 1 deals a 8 of Hearts
 Player 2 deals a 6 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round40==============
 Player 1 deals a 6 of Diamonds
 Player 2 deals a 7 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round41==============
 Player 1 deals a 5 of Clubs
 Player 2 deals a 5 of Diamonds
 Time for War!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 player 1 deals a 2 of Hearts
 player 2 deals a 4 of Clubs
 player 2 wins the war, they win 10 cards
 
 ==========Round42==============
 Player 1 deals a Jack of Diamonds
 Player 2 deals a 7 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round43==============
 Player 1 deals a 8 of Diamonds
 Player 2 deals a 6 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round44==============
 Player 1 deals a 2 of Spades
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round45==============
 Player 1 deals a 3 of Hearts
 Player 2 deals a 2 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round46==============
 Player 1 deals a Ace of Spades
 Player 2 deals a 9 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round47==============
 Player 1 deals a Ace of Clubs
 Player 2 deals a 8 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round48==============
 Player 1 deals a Ace of Diamonds
 Player 2 deals a Queen of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round49==============
 Player 1 deals a Queen of Hearts
 Player 2 deals a 2 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round50==============
 Player 1 deals a 9 of Diamonds
 Player 2 deals a Jack of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round51==============
 Player 1 deals a 8 of Clubs
 Player 2 deals a Jack of Spades
 
 Player 2 wins 2 cards
 
 ==========Round52==============
 Player 1 deals a 4 of Spades
 Player 2 deals a Queen of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round53==============
 Player 1 deals a 7 of Spades
 Player 2 deals a 6 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round54==============
 Player 1 deals a 3 of Diamonds
 Player 2 deals a 7 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round55==============
 Player 1 deals a King of Hearts
 Player 2 deals a 4 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round56==============
 Player 1 deals a Queen of Spades
 Player 2 deals a Jack of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round57==============
 Player 1 deals a 9 of Clubs
 Player 2 deals a 7 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round58==============
 Player 1 deals a 5 of Spades
 Player 2 deals a 5 of Hearts
 Time for War!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 player 1 deals a 3 of Spades
 player 2 deals a 5 of Diamonds
 player 2 wins the war, they win 10 cards
 
 ==========Round59==============
 Player 1 deals a King of Clubs
 Player 2 deals a 2 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round60==============
 Player 1 deals a 9 of Spades
 Player 2 deals a 4 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round61==============
 Player 1 deals a 10 of Diamonds
 Player 2 deals a 2 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round62==============
 Player 1 deals a 6 of Spades
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round63==============
 Player 1 deals a 10 of Spades
 Player 2 deals a 9 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round64==============
 Player 1 deals a 3 of Clubs
 Player 2 deals a Jack of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round65==============
 Player 1 deals a 8 of Hearts
 Player 2 deals a 8 of Clubs
 Time for War!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 player 1 deals a 8 of Diamonds
 player 2 deals a 3 of Diamonds
 player 1 wins the war, they win 10 cards
 
 ==========Round66==============
 Player 1 deals a 6 of Clubs
 Player 2 deals a 7 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round67==============
 Player 1 deals a 3 of Hearts
 Player 2 deals a 10 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round68==============
 Player 1 deals a 2 of Diamonds
 Player 2 deals a 4 of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round69==============
 Player 1 deals a Ace of Spades
 Player 2 deals a King of Spades
 
 Player 1 wins 2 cards
 
 ==========Round70==============
 Player 1 deals a 9 of Hearts
 Player 2 deals a 10 of Clubs
 
 Player 2 wins 2 cards
 
 ==========Round71==============
 Player 1 deals a Ace of Clubs
 Player 2 deals a King of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round72==============
 Player 1 deals a 8 of Spades
 Player 2 deals a 5 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round73==============
 Player 1 deals a Ace of Diamonds
 Player 2 deals a 5 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round74==============
 Player 1 deals a Queen of Clubs
 Player 2 deals a 5 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round75==============
 Player 1 deals a Queen of Hearts
 Player 2 deals a 3 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round76==============
 Player 1 deals a 2 of Clubs
 Player 2 deals a 5 of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round77==============
 Player 1 deals a 7 of Spades
 Player 2 deals a 6 of Spades
 
 Player 1 wins 2 cards
 
 ==========Round78==============
 Player 1 deals a 6 of Diamonds
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round79==============
 Player 1 deals a King of Hearts
 Player 2 deals a 3 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round80==============
 Player 1 deals a 4 of Hearts
 Player 2 deals a Jack of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round81==============
 Player 1 deals a Queen of Spades
 Player 2 deals a 6 of Clubs
 
 Player 1 wins 2 cards
 
 ==========Round82==============
 Player 1 deals a Jack of Clubs
 Player 2 deals a 7 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round83==============
 Player 1 deals a 9 of Clubs
 Player 2 deals a 3 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round84==============
 Player 1 deals a 7 of Clubs
 Player 2 deals a 10 of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round85==============
 Player 1 deals a King of Clubs
 Player 2 deals a 2 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round86==============
 Player 1 deals a 2 of Hearts
 Player 2 deals a 4 of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round87==============
 Player 1 deals a 9 of Spades
 Player 2 deals a 9 of Hearts
 Time for War!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 player 1 deals a 10 of Spades
 player 2 deals a 6 of Diamonds
 player 1 wins the war, they win 10 cards
 
 ==========Round88==============
 Player 1 deals a 9 of Diamonds
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round89==============
 Player 1 deals a 6 of Hearts
 Player 2 deals a 4 of Hearts
 
 Player 1 wins 2 cards
 
 ==========Round90==============
 Player 1 deals a Jack of Diamonds
 Player 2 deals a Jack of Hearts
 Time for War!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 player 1 deals a Queen of Diamonds
 player 2 deals a 4 of Diamonds
 player 1 wins the war, they win 10 cards
 
 ==========Round91==============
 Player 1 deals a 8 of Hearts
 Player 2 deals a 9 of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round92==============
 Player 1 deals a 8 of Clubs
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round93==============
 Player 1 deals a 8 of Diamonds
 Player 2 deals a 8 of Hearts
 Time for War!
 
 player 1 has been invited to the war
 
  player 2 does not have enough cards to compete in the war, so they forefit their remaining cards
 
 ==========Round94==============
 Player 1 deals a 3 of Diamonds
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round95==============
 Player 1 deals a Ace of Spades
 Player 2 deals a 3 of Diamonds
 
 Player 1 wins 2 cards
 
 ==========Round96==============
 Player 1 deals a King of Spades
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round97==============
 Player 1 deals a Ace of Clubs
 Player 2 deals a King of Spades
 
 Player 1 wins 2 cards
 
 ==========Round98==============
 Player 1 deals a King of Diamonds
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 ==========Round99==============
 Player 1 deals a 8 of Spades
 Player 2 deals a King of Diamonds
 
 Player 2 wins 2 cards
 
 ==========Round100==============
 Player 1 deals a 5 of Clubs
 Player 2 deals a Ace of Hearts
 
 Player 2 wins 2 cards
 
 
 ===================== Results ==================
 Player 1 ends the game with 44
 Player 2 ends the game with 4
 Player 1 ends the game with : 44 cards
 Player 2 ends the game with : 4 cards
 
  WINNER: Player 1 wins the game with:44 cards
  
  ======================================================================================================================================
  
  
  RUN 3:
  
  
   How many players for this game (52 max)
 5
 How many rounds would you like to play? (more than 10,000 can cause instability)
 12
 
 ==========Round1==============
 Player 1 deals a 7 of Diamonds
 Player 2 deals a Queen of Spades
 Player 3 deals a 6 of Diamonds
 Player 4 deals a 9 of Spades
 Player 5 deals a 4 of Hearts
 
 Player 2 wins 5 cards
 
 ==========Round2==============
 Player 1 deals a Ace of Diamonds
 Player 2 deals a 6 of Spades
 Player 3 deals a 7 of Spades
 Player 4 deals a King of Spades
 Player 5 deals a Queen of Diamonds
 
 Player 1 wins 5 cards
 
 ==========Round3==============
 Player 1 deals a 8 of Diamonds
 Player 2 deals a Ace of Spades
 Player 3 deals a King of Diamonds
 Player 4 deals a Ace of Hearts
 Player 5 deals a 3 of Hearts
 Time for War!
 
 
 player 2 has been invited to the war
 
 
 player 4 has been invited to the war
 
 player 2 deals a 10 of Diamonds
 player 4 deals a 8 of Hearts
 player 2 wins the war, they win 13 cards
 
 ==========Round4==============
 Player 1 deals a 2 of Spades
 Player 2 deals a 7 of Hearts
 Player 3 deals a 9 of Diamonds
 Player 4 deals a 3 of Diamonds
 Player 5 deals a Jack of Hearts
 
 Player 5 wins 5 cards
 
 ==========Round5==============
 Player 1 deals a Jack of Diamonds
 Player 2 deals a 2 of Hearts
 Player 3 deals a King of Hearts
 Player 4 deals a 7 of Clubs
 Player 5 deals a 5 of Clubs
 
 Player 3 wins 5 cards
 
 ==========Round6==============
 Player 1 deals a 4 of Diamonds
 Player 2 deals a 10 of Hearts
 Player 3 deals a Ace of Clubs
 Player 4 deals a 9 of Clubs
 Player 5 deals a 5 of Diamonds
 
 Player 3 wins 5 cards
 
 ==========Round7==============
 Player 1 deals a Jack of Clubs
 Player 2 deals a 5 of Hearts
 Player 3 deals a 10 of Spades
 Player 4 has run out of cards, they will be removed from the game
 There are now 4 players left.
 
 Player 1 wins 4 cards
 
 ==========Round8==============
 Player 1 deals a King of Clubs
 Player 2 deals a 7 of Diamonds
 Player 3 deals a 4 of Spades
 Player 5 deals a 4 of Clubs
 
 Player 1 wins 4 cards
 
 ==========Round9==============
 Player 1 deals a Queen of Hearts
 Player 2 deals a Queen of Spades
 Player 3 deals a Jack of Spades
 Player 5 deals a 6 of Hearts
 Time for War!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 
 
 player 1 deals a 6 of Spades
 player 2 deals a 6 of Clubs
 Time for another war!
 
 player 1 has been invited to the war
 
 player 2 has been invited to the war
 
 
 player 5 has been invited to the war
 player 1 deals a Jack of Clubs
 player 2 deals a 3 of Clubs
 player 5 deals a 7 of Hearts
 player 3 wins the war, they win 24 cards
 
 ==========Round10==============
 Player 1 deals a 5 of Hearts
 Player 2 deals a 2 of Diamonds
 Player 3 deals a 2 of Clubs
 Player 5 deals a 9 of Diamonds
 
 Player 5 wins 4 cards
 
 ==========Round11==============
 Player 1 deals a 10 of Spades
 Player 2 deals a 8 of Diamonds
 Player 3 deals a Jack of Diamonds
 Player 5 deals a 3 of Diamonds
 
 Player 3 wins 4 cards
 
 ==========Round12==============
 Player 1 deals a 
 Player 2 deals a Ace of Spades
 Player 3 deals a 2 of Hearts
 Player 5 deals a Jack of Hearts
 
 Player 2 wins 4 cards
 
 
 ===================== Results ==================
 Player 1 ends the game with 4
 Player 2 ends the game with 9
 Player 3 ends the game with 36
 Player 5 ends the game with 4
 Player 1 ends the game with : 4 cards
 Player 2 ends the game with : 9 cards
 Player 3 ends the game with : 36 cards
 Player 5 ends the game with : 4 cards
 
  WINNER: Player 3 wins the game with:36 cards
  
  
  */
  
  

   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   

