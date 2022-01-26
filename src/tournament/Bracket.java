/*********************************************************************************************
 *	This class contains the Bracket object and its methods, which can be used in conjunction
 *	with the Game object to simulate a March Madness Bracket by analyzing various probabilities.
 *
 *  @author:Bobby Hermitt
 *
 ********************************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Bracket {
	/*Integer array containing the standard ordering of seeds in a region.*/
	private static int[] seedOrder = {1, 16, 8, 9, 5, 12, 4, 13, 6, 11, 3, 14, 7, 10, 2, 15};
	
	/*The bracket consists of arrays of games representing each round*/
	private Game[] firstFour;
	private Game[] firstRound;
	private Game[] secondRound;
	private Game[] sweetSixteen;
	private Game[] eliteEight;
	private Game[] finalFour;
	private Game championship;
	/*The following Hash Table will contain any pre-determined winners by the user and
	* the final round they will be forced to win.*/
	private HashMap<String,Integer> winners;
	/*The following Array List of games will contain references to the first round games
	 * that are awaiting the result of a first four game.*/
	private ArrayList<Game> needFirstFour;
	/*The following two-dimensional array will contain the probability that any seed will win a game
	 * in each round. The first round is based completely on probability, while the later round
	 * probabilities do not add up to 1 which opens the door for later round upsets.*/
	private int[][] seedProbability;
	/*The following two-dimensional array will contain the true probability in matchups of any seeds
	 * during the rounds prior to the final four.*/
	private int[][] matchupProbability;
	/*The following 2D Array will contain the probability that each number of seeds
	 * advances to the next round.*/
	 private int[][] numAdvance;
	
	 
	/**Constructor - fills the first four games with their teams, if applicable, fills the first round
	 * games, keeping the spots for the first four null. Fills the winners Hash table.
	 * Does all this by calling helper methods fillTeams and fillWinners.
	 * 
	 * @param files - File containing the teams file, a file containing the seeds of the first four teams in order,
	 * the winners file, the first round number advance probability file (if in use),
	 * the true seed probability file, and the seed-round probabilites file in that order. One file name per line.
	 * Number advance file should be a blank line if not used.
	 * First four seeds file should be a blank line if not simulating the first four.
	 * PRE DETERMINED WINNERS ARE NOT COMPATIBLE WITH THE NUMBER ADVANCE PROBABILTIY
	 * IF THEY TRY TO BE USED SIMULTANEUOUSLY, THE WINNERS WILL BE IGNORED.
	 * */
	public Bracket(String files) throws FileNotFoundException {
		firstFour = new Game[4];
		firstRound = new Game[32];
		secondRound = new Game[16];
		sweetSixteen = new Game[8];
		eliteEight = new Game[4];
		finalFour = new Game[2];
		Scanner bobby = new Scanner(new File(files));
		/*Gets the team file from the fileNames file and calls the appropriate fill
		 * teams method to populate the round arrays with games.
		 */
		String teamsFile = bobby.nextLine();
		//teamsFile = "files/".concat(teamsFile);
		String firstFourSeeds = bobby.nextLine();
		if(!firstFourSeeds.equals("")) {
			//firstFourSeeds = "files/".concat(firstFourSeeds);
			needFirstFour = new ArrayList<Game>(4);
			fillTeamsFF(teamsFile, firstFourSeeds);
		}
		else {
			fillTeamsNoFF(teamsFile);
		}
		/*Gets the winners file from the fileNames file and calls the fillWinners method
		 * to populate the hashMap with the predetermined winners.
		 */
		winners = new HashMap<String,Integer>();
		String winnersFile = bobby.nextLine();
		//winnersFile = "files/".concat(winnersFile);
		fillWinners(winnersFile);
		
		/*Gets the probability that each number of seeds advances in the first
		 * round, if in use.*/
		numAdvance = new int[8][5];
		String numAdvanceFile = bobby.nextLine();
		if(!numAdvanceFile.equals("")) {
			//numAdvanceFile = "files/".concat(numAdvanceFile);
			fillNumAdvance(numAdvanceFile);
		}
		
		/*Gets the true Matchup probability file from the filenames file and calls the fillMatchupProb
		 * method to populate the 2D array with the probabilities*/
		matchupProbability = new int[16][16];
		String matchups = bobby.nextLine();
		//matchups = "files/".concat(matchups);
		fillMatchupProb(matchups);
		
		/*Gets the seed-round probability file from the filenames file and calls the fillSeedProbability
		 * method to populate the 2D array with the probabilities.
		 */
		seedProbability = new int[16][7];
		String roundProb = bobby.nextLine();
		//roundProb = "files/".concat(roundProb);
		fillSeedProbability(roundProb);
		bobby.close();
	}
	
	/**Fills teams in the first four and first round from a text file containing
	 * all teams in the tournament. File must be formatted with one team per line.
	 * Blank Line should be used if first round matchup involves a first four team. First four
	 * games must be listed in the order that their teams should be inserted to the
	 * bracket. The seeds file should have four numbers, all on one line separated by spaces.*/
	private void fillTeamsFF(String teams, String seeds) throws FileNotFoundException {
		Scanner bobby = new Scanner(new File(teams));
		Scanner justin = new Scanner(new File(seeds));
		/*Filling in the first four*/
		for(int i=0; i<8; i+=2) {
			String teamName1 = bobby.nextLine();
			String teamName2 = bobby.nextLine();
			int seed = justin.nextInt();
			
			Team team1 = new Team(teamName1, seed);
			Team team2 = new Team(teamName2, seed);
			firstFour[i/2] = new Game(team1,team2);
		}
		
		justin.close();
		
		/*nextSeed represents the index in the static seedOrder array that
		 * contains the next seed to be assigned to a team.*/
		int nextSeed = 0;
		
		/*Filling in the first round, leaving null spots for the winners of the first
		 * four games.*/
		for(int i=0; i<64; i+=2) {
			
			String teamName1 = bobby.nextLine();
			String teamName2 = bobby.nextLine();
			
			Team team1,team2;
						
			team1 = teamName1.equals("") ? null : new Team(teamName1, seedOrder[nextSeed]);
			team2 = teamName2.equals("") ? null : new Team(teamName2, seedOrder[nextSeed+1]);
			
			firstRound[i/2] = new Game(team1,team2);
			if(firstRound[i/2].team1 == null || firstRound[i/2].team2 == null) {
				needFirstFour.add(firstRound[i/2]);
			}
			
			/*Increment seed consistent with i. If it reaches 16, then a whole
			 * region has been input and the counter must reset to 0.*/
			nextSeed += 2;
			if(nextSeed >= 16) {
				nextSeed = 0;
			}
			
		}
		
		bobby.close();		
	}
	
	/**Fills teams in the first round from a text file. This method will only be
	 * executed from the constructor if the simulation does not involve the first
	 * four. File formatted with one team per line.*/
	private void fillTeamsNoFF(String teamsFile) throws FileNotFoundException{
		Scanner bobby = new Scanner(new File(teamsFile));
		/*nextSeed represents the index in the static seedOrder array that
		 * contains the next seed to be assigned to a team.*/
		int nextSeed = 0;
		for(int i=0; i<64; i+=2) {
			
			String teamName1 = bobby.nextLine();
			String teamName2 = bobby.nextLine();
			Team team1 = new Team(teamName1, seedOrder[nextSeed]);
			Team team2 = new Team(teamName2, seedOrder[nextSeed+1]);
			firstRound[i/2] = new Game(team1, team2);
			
			/*Increment seed consistent with i. If it reaches 16, then a whole
			 * region has been input and the counter must reset to 0.*/
			nextSeed += 2;
			if(nextSeed >= 16) {
				nextSeed = 0;
			}
		}
		bobby.close();
	}
	
	/**Takes the winners from a text file formatted as follows:
	 * [Team]
	 * [Round]
	 * [Team]
	 * [Round]
	 * Where a team winning in the first four would have round 0, winning the first round
	 * would have 1, the second round 2, etc.*/
	private void fillWinners(String winnersFile) throws FileNotFoundException{
		Scanner bobby = new Scanner(new File(winnersFile));
		while(bobby.hasNextLine()) {	
			String team = bobby.nextLine();
			int roundWon = Integer.parseInt(bobby.nextLine());
			winners.put(team, roundWon);
		}
		bobby.close();
	}
	
	/*Fills the matrix containing the probability that each possible number
	 * of each seed advances to the second round. Formatted as follows:
	 * [probability of 0 1 seeds advance] [probability of 1 1 seed advance]...
	 * [probability of 0 2 seeds advance]...
	 * ...
	 * [probability of 0 16 seeds advance]...
	 */
	private void fillNumAdvance(String numAdvanceFile) throws FileNotFoundException {
		Scanner bobby = new Scanner(new File(numAdvanceFile));
		for(int i=0; i<8; i++) {
			for(int j=0; j<5; j++) {
				int prob = bobby.nextInt();
				numAdvance[i][j] = prob;
			}
		}
		bobby.close();
	}
	
	/*Fills the probability that a seed will win each round. File formatted as follows:
	 * Right now the data in this method does not include the first four.
	 * [1 Seed chance to win first round] [1 Seed chance to win second round]...
	 * [2 Seed chance to win first Round] [2 Seed chance to win second round]...
	 * ...
	 */
	private void fillSeedProbability(String probFile) throws FileNotFoundException{
		Scanner bobby = new Scanner(new File(probFile));
		for(int i=0; i<16; i++) {
			for(int j=1; j<7; j++) {
				int chance = bobby.nextInt();
				seedProbability[i][j] = chance;
			}
		}
		bobby.close();
	}
	
	/*Fills the probability that a seed will win each round. File formatted as follows:
	 * Right now this method does not support simulating the first four.
	 * [1 Seed chance to beat 1 Seed] [1 Seed chance to beat 2 Seed]...[1 Seed chance to beat 16 Seed]
	 * [2 Seed chance to beat 1 Seed] [2 Seed chance to beat 2 Seed]...[1 Seed chance to beat 16 Seed]
	 * ...
	 * [16 Seed chance to beat 1 Seed]
	 * 
	 * Matchups between the same seeds or teams that have never played should be entered
	 * as -1.
	 * The data used in this method cannot be applied to the first four or to the final four
	 * and championship game.*/
	private void fillMatchupProb(String matchupFile) throws FileNotFoundException{
		Scanner bobby = new Scanner(new File(matchupFile));
		for(int i=0; i<16; i++) {
			for(int j=0; j<16; j++) {
				int prob = bobby.nextInt();
				matchupProbability[i][j] = prob;
			}
		}
		bobby.close();
	}
	
	/*Simulates the tournament by calling all the individual simulate round
	 * methods. Accounts for tournaments that do not use the first four and
	 * tournaments that use the various styles of simulating the first round.*/
	public void simulateTournament() throws DoubleWinnerException{	
		if(firstFour[0] != null) {	
			simulateFirstFour();
		}
		
		/*Accounting for the multiple ways to simulate the first round.*/
		boolean useNumAdvance = false;
		for(int i=0; i<numAdvance.length; i++) {
			if(numAdvance[i][0] != 0) {
				useNumAdvance = true;
			}
		}
		if(useNumAdvance) {
			simulateFirstRoundNumAdvance();
		}
		else {
			simulateFirstRound();
		}
		simulateSecondRound();
		simulateSweetSixteen();
		simulateEliteEight();
		simulateFinalFour();
		simulateChampionshipGame();
	}
	
	private void simulateFirstFour() throws DoubleWinnerException {
		for(int i=0; i<firstFour.length; i++) {
			/*Check if both are winners, then if only one is a winner. If not, simulate the game.*/
			if(winners.containsKey(firstFour[i].team1.teamName) && winners.containsKey(firstFour[i].team2.teamName)) {
				throw new DoubleWinnerException(firstFour[i].team1.teamName + " and " + firstFour[i].team2.teamName + " are both winners in the first four.");
			}
			else if(winners.containsKey(firstFour[i].team1.teamName)) {
				firstFour[i].simulateGame(firstFour[i].team1);
			}
			else if(winners.containsKey(firstFour[i].team2.teamName)) {
				firstFour[i].simulateGame(firstFour[i].team2);
			}
			else {
				firstFour[i].simulateGame(0, matchupProbability, seedProbability);
			}
		}
		
		/*Update the teams that were waiting on first four results by using the
		 * needFirstFour ArrayList.*/
		for(int i=0; i<4; i++) {
			Game currGame = needFirstFour.get(i);
			if(currGame.team1 == null) {
				currGame.team1 = firstFour[i].winner;
			}
			else {
				currGame.team2 = firstFour[i].winner;
			}
		}
	}
	
	private void simulateFirstRound() throws DoubleWinnerException {
		for(int i=0; i<firstRound.length; i++) {
			/*Check if both teams are slated to be winners in this round. Throw and exception
			 * if so.*/
			if((winners.containsKey(firstRound[i].team1.teamName) && winners.get(firstRound[i].team1.teamName) >= 1) && (winners.containsKey(firstRound[i].team2.teamName) && winners.get(firstRound[i].team2.teamName) >= 1)){
				throw new DoubleWinnerException(firstRound[i].team1.teamName + " and " + firstRound[i].team2.teamName + " are both winners in the first round.");
			}
			/*Check if either is the sole winner.*/
			else if(winners.containsKey(firstRound[i].team1.teamName)&& winners.get(firstRound[i].team1.teamName) >= 1 ) {
				firstRound[i].simulateGame(firstRound[i].team1);
			}
			else if(winners.containsKey(firstRound[i].team2.teamName) && winners.get(firstRound[i].team2.teamName) >= 1 ) {
				firstRound[i].simulateGame(firstRound[i].team2);
			}
			/*Otherwise simulate it.*/
			else {
				firstRound[i].simulateGame(1, matchupProbability, seedProbability);
			}
			
			/*Populate the next round with the winners of this round. If its an odd numbered
			 * game from this round, then it should be team 2 in the next round.*/
			if(i % 2 == 0) {
				secondRound[i/2] = new Game(firstRound[i].winner, null);
			}
			else {
				secondRound[i/2].team2 = firstRound[i].winner;
			}
			
		}
	}
	
	/*Simulates the first round using the probability that each number of a seed
	 * advances to the second round. Since no statistical analysis is in use,
	 * random regions are picked for the winners.*/
	private void simulateFirstRoundNumAdvance() {
		/*Modifier to add to the game number in the appropriate region
		 * in the first round array.*/
		int[] addForRegion = {0,0,7,5,3,2,4,6,1,1,6,4,2,3,5,7,0};
		
		/*Simulate the results for the 1-8 seeds because the 9-16 is just the
		 * inverse of the appropriate matchup.*/
		for(int seed=1; seed<9; seed++) {
			/*Chances for the number of each seed that advances.*/
			ArrayList<Integer> chances = new ArrayList<Integer>(10000000);
			/*Get the probability for each number of each seed and add them
			 * to the chances array.*/
			for(int i=0; i<5; i++) {
				int numAdvanceChance = numAdvance[seed-1][i];
				for(int j=0; j<numAdvanceChance; j++) {
					chances.add(i);
				}
			}
			/*Pick a random number of the current seed to advance.*/
			int numAdvance = chances.get((int)(Math.random()*chances.size()));
			/*Create a new array list containing the 4 regions labelled as 
			 * 0,1,2,3 so that they can be removed so as to ensure a duplicate
			 * region is not picked.*/
			ArrayList<Integer> regionPick = new ArrayList<Integer>(4);
			for(int i=0; i<4; i++) {
				regionPick.add(i);
			}
			for(int i=0; i<numAdvance; i++) {
				/*The game number that needs its winner updated, given by 8r+mod where
				 * r is the region number and 'mod' is the modifier given by the
				 * array at the start of the method.*/
				int region = (int)(Math.random()*regionPick.size());
				int game = 8*regionPick.remove(region) + addForRegion[seed];
				/*Set the winner to the appropriate team. The if condition should
				 * always be satisfied, but it serves as a possible error check
				 * and correction.*/
				if(Math.round(firstRound[game].team1.seed) == seed) {
					firstRound[game].winner = firstRound[game].team1;
				}
				else {
					firstRound[game].winner = firstRound[game].team2;
				}
			}
			
			/*Set the winners in the remaining regions to the inverse of the current
			 * seed.*/
			for(int i=0; i<regionPick.size(); i++) {
				int game = 8*regionPick.get(i) + addForRegion[seed];
				/*Again, this condition should always be satisfied, but it serves
				 * as a possible error check and correction.*/
				if(Math.round(firstRound[game].team2.seed) == 17-seed) {
					firstRound[game].winner = firstRound[game].team2;
				}
				else {
					firstRound[game].winner = firstRound[game].team1;
				}
			}
		}
		
		/*Now all the second round games must be set.*/
		for(int i=0; i<32; i++) {
			if(i%2 == 0) {
				secondRound[i/2] = new Game(firstRound[i].winner, null);
			}
			else {
				secondRound[i/2].team2 = firstRound[i].winner;
			}
		}
	}
	
	private void simulateSecondRound() throws DoubleWinnerException{
		for(int i=0; i<secondRound.length; i++) {
			if((winners.containsKey(secondRound[i].team1.teamName) && winners.get(secondRound[i].team1.teamName) >= 2) && (winners.containsKey(secondRound[i].team2.teamName) && winners.get(secondRound[i].team2.teamName) >= 2)) {
				throw new DoubleWinnerException(secondRound[i].team1.teamName + " and " + secondRound[i].team2.teamName + " are both winners in the second round.");
			}
			else if(winners.containsKey(secondRound[i].team1.teamName) && winners.get(secondRound[i].team1.teamName) >= 2) {
				secondRound[i].simulateGame(secondRound[i].team1);
			}
			else if(winners.containsKey(secondRound[i].team2.teamName) && winners.get(secondRound[i].team2.teamName) >= 2) {
				secondRound[i].simulateGame(secondRound[i].team2);
			}
			else {
				secondRound[i].simulateGame(2, matchupProbability, seedProbability);
			}
			
			if(i%2 == 0) {
				sweetSixteen[i/2] = new Game(secondRound[i].winner, null);
			}
			else {
				sweetSixteen[i/2].team2 = secondRound[i].winner;
			}
		}
	}
	
	private void simulateSweetSixteen() throws DoubleWinnerException {
		for(int i=0; i<sweetSixteen.length; i++) {
			if((winners.containsKey(sweetSixteen[i].team1.teamName) && winners.get(sweetSixteen[i].team1.teamName) >= 3) && (winners.containsKey(sweetSixteen[i].team2.teamName) && winners.get(sweetSixteen[i].team2.teamName) >= 3)){
				throw new DoubleWinnerException(sweetSixteen[i].team1.teamName + " and " + sweetSixteen[i].team2.teamName + " are both winners in the Sweet Sixteen.");
			}
			else if(winners.containsKey(sweetSixteen[i].team1.teamName) && winners.get(sweetSixteen[i].team1.teamName) >= 3) {
				sweetSixteen[i].simulateGame(sweetSixteen[i].team1);
			}
			else if(winners.containsKey(sweetSixteen[i].team2.teamName) && winners.get(sweetSixteen[i].team2.teamName) >= 3) {
				sweetSixteen[i].simulateGame(sweetSixteen[i].team2);
			}
			else {
				sweetSixteen[i].simulateGame(3, matchupProbability, seedProbability);
			}
			
			if(i%2 == 0) {
				eliteEight[i/2] = new Game(sweetSixteen[i].winner, null);
			}
			else {
				eliteEight[i/2].team2 = sweetSixteen[i].winner;
			}
		}
	}
	
	private void simulateEliteEight() throws DoubleWinnerException {
		for(int i=0; i<eliteEight.length; i++) {
			if((winners.containsKey(eliteEight[i].team1.teamName) && winners.get(eliteEight[i].team1.teamName) >= 4) && (winners.containsKey(eliteEight[i].team2.teamName) && winners.get(eliteEight[i].team2.teamName) >= 4)){
				throw new DoubleWinnerException(eliteEight[i].team1.teamName + " and " + eliteEight[i].team2.teamName + " are both winners in the Elite Eight.");
			}
			else if(winners.containsKey(eliteEight[i].team1.teamName) && winners.get(eliteEight[i].team1.teamName) >= 4) {
				eliteEight[i].simulateGame(eliteEight[i].team1);
			}
			else if(winners.containsKey(eliteEight[i].team2.teamName) && winners.get(eliteEight[i].team2.teamName) >= 4) {
				eliteEight[i].simulateGame(eliteEight[i].team2);
			}
			else {
				eliteEight[i].simulateGame(4, matchupProbability, seedProbability);
			}
			
			if(i%2 == 0) {
				finalFour[i/2] = new Game(eliteEight[i].winner, null);
			}
			else {
				finalFour[i/2].team2 = eliteEight[i].winner;
			}
		}
	}
	
	private void simulateFinalFour() throws DoubleWinnerException {
		for(int i=0; i<finalFour.length; i++) {
			if((winners.containsKey(finalFour[i].team1.teamName) && winners.get(finalFour[i].team1.teamName) >= 5) && (winners.containsKey(finalFour[i].team2.teamName) && winners.get(finalFour[i].team2.teamName) >= 5)){
				throw new DoubleWinnerException(finalFour[i].team1.teamName + " and " + finalFour[i].team2.teamName + " are both winners in the Final Four.");
			}
			else if(winners.containsKey(finalFour[i].team1.teamName) && winners.get(finalFour[i].team1.teamName) >= 5) {
				finalFour[i].simulateGame(finalFour[i].team1);
			}
			else if(winners.containsKey(finalFour[i].team2.teamName) && winners.get(finalFour[i].team2.teamName) >= 5) {
				finalFour[i].simulateGame(finalFour[i].team2);
			}
			else {
				finalFour[i].simulateGame(5, matchupProbability, seedProbability);
			}
		}
		
		championship = new Game(finalFour[0].winner, finalFour[1].winner);
	}
	
	private void simulateChampionshipGame() throws DoubleWinnerException {
		if((winners.containsKey(championship.team1.teamName) && winners.get(championship.team1.teamName) >= 6) && (winners.containsKey(championship.team2.teamName) && winners.get(championship.team2.teamName) >= 6)){
			throw new DoubleWinnerException(championship.team1.teamName + " and " + championship.team2.teamName + " are both winners in the Championship Game.");
		}
		else if(winners.containsKey(championship.team1.teamName) && winners.get(championship.team1.teamName) >= 6) {
			championship.simulateGame(championship.team1);
		}
		else if(winners.containsKey(championship.team2.teamName) && winners.get(championship.team2.teamName) >= 6) {
			championship.simulateGame(championship.team2);
		}
		else {
			championship.simulateGame(6, matchupProbability, seedProbability);
		}
	}
	
	public String toString() { 
		String tournament = "Round 1: " + roundToString(firstRound) + "\n" +
			"Round 2: " + roundToString(secondRound) + "\n" + 
			"Sweet Sixteen: " + roundToString(sweetSixteen) + "\n" +
			"Elite Eight: " + roundToString(eliteEight) + "\n" +
			"Final Four: " + roundToString(finalFour) + "\n" + 
			"Championship: " + championship + "\n" +
			"National Champion: " + championship.winner.teamName + "\n" +
			RandomScore.printRandomScore(50, 85);
		
		if(firstFour[0] != null) {
			tournament = "FirstFour: " + roundToString(firstFour) + "\n" + tournament;
		}
		
		return tournament;
	}
	
	/*
	 * private String statsToString(Team team) { String stats = new String("");
	 * Set<String> keys = team.stats.keySet(); Iterator<String> iterator =
	 * keys.iterator(); while(iterator.hasNext()) { String stat = iterator.next();
	 * stats += "(" + stat + ": " + team.stats.get(stat) + ") "; } return stats; }
	 */
	
	private String winnersToString() {
		String winners = new String("");
		Set<String> keys = this.winners.keySet();
		Iterator<String> iterator = keys.iterator();
		while(iterator.hasNext()) {
			String winner = iterator.next();
			winners += "(" + winner + ", " + this.winners.get(winner) + ") ";
		}
		return winners;
	}
	
	private String roundToString(Game[] round) {
		String games = new String("");
		for(int i=0; i<round.length-1; i++) {
			games += round[i] + " ";
		}
		games += round[round.length-1];
		return games;
	}
	
//	private String arrayListToString(ArrayList<Game> a) {
//		String games = new String("");
//		for(int i=0; i<a.size()-1; i++) {
//			games += a.get(i) + " ";
//		}
//		games += a.get(a.size()-1);
//		return games;
//	}
}