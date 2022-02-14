# March Madness Simulator
Welcome to the NCAA March Madness Bracket Simulator. This simulator uses historical probability based on past performances of each seed, along with an element of randomness, to predict who will be victorious in each game of the NCAA Men's basketball tournament. After predicting each game and finally coming up with the winner, the simulator also randomly generates the final score of the championship game since the ESPN tournament challenge requires it as a tiebreaker.

The input is a file containing other file names.

<hr style="border:2px solid gray"> </hr>

## Running a Simulation
In order to simulate a March Madness Bracket, you will need to create a text file containing the list of the teams in the tournament.
See below for how this file should be formatted.
You can also optionally create files that indicate which seeds are participating in the first four (If you are simulating
a tournament in which the first four is being played) and any predetermined winners you would like to see the simulation guarantee.

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