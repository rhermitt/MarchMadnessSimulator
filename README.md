# March Madness Simulator
Welcome to the NCAA March Madness Bracket Simulator.
This simulator uses historical probability based on past performances of each seed, along with an element of randomness,
to predict who will be victorious in each game of the NCAA Men's basketball tournament.
After predicting each game and finally coming up with the winner, the simulator also randomly generates the final score
of the championship game since the ESPN tournament challenge requires it as a tiebreaker.

<hr style="border:2px solid gray"> </hr>

## Running a Simulation
In order to simulate a March Madness Bracket, you will need to create a text file containing the list of the teams in the tournament.
See below for how this file should be formatted.
You can also optionally create files that indicate which seeds are participating in the first four (If you are simulating
a tournament in which the first four is being played) and any predetermined winners you would like to see the simulation guarantee.
You will then need to place these file names into another text file which is passed to the program.
You can run your simulation by running Simulator.java.

You also must ensure that the files "numAdvanceProb.txt", "roundProb.txt", and "SeedMatchupProb.txt" are all in the project folder.
These files drive the simulation and contain the historical probablity that it used to determine the winners.

---

### The Teams File
The Teams File contains the names of the teams in the order that they appear in the bracket.
When translating a full March Madness Bracket to a text file, read the matchups from the top left down then from the top right down.
The Teams File should be formatted with no more than one team per line.

If you are using the first four, then the first 8 teams in the teams file should be the teams competing in the first four.
The first four games should appear in the order that they are to be filled into the bracket.
If your simulation includes the first four, insert a blank line wherever there needs to be a team that will be filled by the first four.

#### Teams File Example:
Say you want to simulate the 2021 NCAA Men's Basketball Tournament.
The 2021 bracket saw the following first four games:
* Norfolk State vs. Appalachian State
* Wichita State vs. Drake
* Mount Saint Mary's vs. Texas Southern
* Michigan State vs. UCLA

Reading the bracket from the top left down, the winner of Norfolk State vs. Appalachian State is the first first-four game to fill a spot, so it appears first in the Teams File.
The rest of the games fill spots in the above order, and thus are listed in that order within the Teams File.
In 2021, the winner of Norfolk State vs. Appalachian State filled the second spot in the bracket, facing Gonzaga in the first round.
The game below that one was Oklahoma vs. Missouri. Thus, the first 12 lines of the teams file was:
1. Norfolk State
2. Appalachian State
3. Wichita State
4. Drake
5. Mount Saint Mary's
6. Texas Southern
7. Michigan State
8. UCLA
9. Gonzaga
10. 
11. Oklahoma
12. Missouri

The pattern continues for the rest of the file.

If your simulation does not include the first four, simply transcribe the teams in order, one per line, to make up the teams file.

---
### First Four Seeds File
If your simulation includes the first four, you must create a text file containing the seeds that the first four winners will occupy.
The file should be one line with the seed numbers separated by spaces.

#### First Four Seeds File Example:
In the 2021 NCAA Tournament, the Norfolk State vs. Appalachian State winner was a 16 seed, the Wichita State vs Drake winner was an 11 seed,
the Mount Saint Mary's vs. Texas Southern winner was a 16 seed, and the Michigan State vs. UCLA winner was an 11 seed.
This is the order that these games would fill into the main bracket.
Thus the First Four Seeds File for 2021 is one line containing:
1. 16 11 16 11

---
### Winners File
If you want to make sure that your simulation includes specific teams winning up to specific rounds, you can do that by creating a Winners File.
The Winners File contains a sequence of two-line statements with a team name on top, and a round number on the bottom.
The team name is the team that will win, and the number is the round number that they are guaranteed to win.
Note that a team is not limited to the round that you set, it is possible that they continue to win games beyond that round.
The round numbers correspond to the following rounds:

0. The team will win in the First Four
1. The team will win in the First Round
2. The team will win in the Second Round
3. The team will win in the Sweet Sixteen
4. The team will win in the Elite Eight
5. The team will win in the Final Four
6. The team will win in the National Championship

If you enter a number greater than 6, this will result in that team winning the National Championship.

NOTE: If you choose two teams that play each other to both be winners in a certain round, the program will terminate throwing
a DoubleWinnerException with a message containing the two teams and the round in conflict.

WARNING: If you select the Number of Teams Advacing Simulation Method, you will be unable to use predetermined winners
(More on this later).

#### Winners File Example:
Say you have been watching Rutgers and Gonzaga all season, and you're sure that Rutgers will win the National Championship
Gonzaga will at least make it through the Sweet Sixteen. In 2021, this is not a problem since Rutgers and Gonzaga were on
opposite sides of the bracket. Your Winners File would look like this:

1. Rutgers
2. 6
3. Gonzaga
4. 3

Remember that this does not eliminate the possibility that Gonzaga will advance further than the Elite Eight.
However, it does ensure that the other National Championship participant (besides Rutgers), whether Gonzaga or not, will lose in that game.

---

### The Input File to the Simulator
When you run Simulator.java, you will be prompted for "the files file".
This is a file containing the teams file and either or both of the optional First Four Seeds Files and Winners Files.

IMPORTANT: If you choose not to use any or both of the optional files, you must create blank lines on both lines 2 and 3
in the files file. This means that your files file should always be 4 lines, even if only the first one has content.

The file should be formatted as follows:
1. The teams File [Required]
2. The First Four Seeds File [Optional - But must have a blank line if not]
3. The Winners File [Optional - But must have a blank line if not]
4. 

#### Files File Example - No First Four and No Winners
A basic teams file which contains region names and seeds ("SeedNames1.txt") is stored in this project.
This team list does not utilize the First Four. To Simulate this tournament without predetermined winners, the Files File
would be:

1. SeedNames1.txt
2. 
3. 
4. 

This Files File is included in this project, called "files1.txt"

#### Files File Example - With All Optional Files
Say you want to simulate the 2021 NCAA Men's Basketball Tournament, which includes the first four, and you want Rutgers
to Win the National Championship. With you properly created files ready, your Files File would look like:

1. teams2021.txt
2. firstfourseeds2021.txt
3. RutgersWins.txt
4. 

All three of the above files and the files file ("filesRutgersWins2021.txt") are included in this project.

---
### Choosing a Simulation Method
When you run Simulator.java, you will be prompted with a message asking if you want to simulate the first round using
Number Advance Probability as opposed to Seed-Matchup Probability. These are two different ways of determining the first round winners.

**Seed-Matchup Probability** uses historical results of all matchups between any two given seeds to pick the winner.
Choose this option by typing "no" when prompted.

**Number Advance Probability** uses historical results of the number of each seed to advance through the first round over the years.
For each seed 1-8, it uses historical data to choose the number of that seed that will advance, then chooses which regions these
advancing teams will come from. It then advances the remaining higher seeded teams who's opponents were not chosen to advance.
For example: Using historical data, the simulation may determine that three 4-Seeds will advance to the second round.
It will then randomly choose which three 4-seed teams will advance and move them to the second round. The remaining 4-13 game will have the
13 seed advance. Note that this method only applies to the first round. Even if you choose Number Advance Probability,
all subsequent rounds will be simulated using Seed-Matchup. Choose this option by typing "yes" when prompted.

**NOTE** - You cannot use predetermined winners if you choose Number Advance Probability.

For the 2021 Tournament:
* Seed-Matchup Probability scored an average of 765.6 on ESPN Tournament Challenge in nine simulations.
* Number Advance Probability scored an average of 717.5 on ESPN Tournament Challenge in four simulations.

---
### Getting Your Results
After choosing your simulation method and entering your Files File, the tournament will automatically
simulate and display all results to the console. Each game will have its result printed. Each line will contain and full round,
and games will be in the order that they are contained in the tournament. Your results can then be transferred manually to the
bracket pool or Tournament Challenge of your choice. Since ESPN Tournament Challenge requires it as a tiebreaker, the simulator
also displays a final score for the National Championship Game. This is a random score in reasonable range for a College Basketball Game.
