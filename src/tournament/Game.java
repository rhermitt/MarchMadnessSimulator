/*********************************************************************************************
 *	This class contains the Game object and its methods, which can be used as a single
 *	instance or as a part of a tournament or larger project.
 *
 *  @author:Bobby Hermitt
 *
 ********************************************************************************************/

import java.util.ArrayList;

public class Game {
	
	Team team1;
	Team team2;
	Team winner;
	private ArrayList<Team> chances;

	public Game(Team one, Team two){
		team1 = one;
		team2 = two;
		chances = new ArrayList<Team>(10000);
	}

	@Override
	public String toString() {
		return "(" + team1 + " " + " vs. " + team2 +  " " + ", Winner: " + winner + ")";
	}

	/**Simulates the game based first on true seed-matchup probability
	 * and next on the probability for each seed to win in each round.
	 * The method uses a chances system.* 
	 * @param round - round of the game.
	 * 					0: First Four
	 * 					1: First Round
	 * 					2: Second Round
	 * 					3: Sweet 16
	 * 					4: Elite Eight
	 * 					5: Final Four
	 * 					6: Championship
	 * @param matchups - Two-Dimensional Array containing probabilities that
	 * each seed wins against other seeds.
	 * @param roundProb - Two-Dimensional Array containing probabilities for each seed
	 * to win in each round.
	 */
	public void simulateGame(int round, int[][] matchups, int[][] roundProb) {
		/*Since the first four can't have probability, right now it suffices to
		 * make the simulation 50/50.*/
		if(round == 0) {
			int x = (int)(2*(Math.random()));
			this.winner = x == 0 ? this.team1 : this.team2;
			return;
		}
		
		/*Seed-Matchup probabilities only allow for the rounds before the final four.*/
		if(round < 5) {
			int team1Chance = matchups[team1.seed-1][team2.seed-1];
			int team2Chance = matchups[team2.seed-1][team1.seed-1];
			
			/*If the two teams have never played, seed-matchup can't be used so round
			 * probabilities must be used.*/
			if(team1Chance != -1) {
				/*Add each team's chances to the chances array.*/
				for(int i=0, j=0; i<team1Chance || j<team2Chance; i++, j++) {
					if(i<team1Chance) {
						chances.add(team1);
					}
					if(j<team2Chance) {
						chances.add(team2);
					}
				}
		
				/*Choose a random number from 0 to the number of entries in chances minus 1
				 *and pick the winner.*/
				int win = (int)(chances.size()*Math.random());
				this.winner = chances.get(win);
				return;
			}
		}
		
		/*This code will be used if the game is before the final four or if the
		 * teams have never played each other.*/
		/*Get each team's chances to win from the probability matrix.*/
		int team1Chance = roundProb[team1.seed-1][round];
		int team2Chance = roundProb[team2.seed-1][round];
		
		/*Add each team's chances to the chances array*/
		for(int i=0, j=0; i<team1Chance || j<team2Chance; i++, j++) {
			if(i<team1Chance) {
				chances.add(team1);
			}
			if(j<team2Chance) {
				chances.add(team2);
			}
		}
		
		/*Choose a random number from 0 to the number of entries in chances minus 1
		 *and pick the winner.*/
		int win = (int)(chances.size()*Math.random());
		this.winner = chances.get(win);			
	}
	
	void simulateGame(Team winner) {
		this.winner = winner;
	}
}