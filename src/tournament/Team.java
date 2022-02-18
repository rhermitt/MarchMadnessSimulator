import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

class Team {
	
	String teamName;
	int seed;
	
	public Team(String teamName, int seed) throws FileNotFoundException {
		this.teamName = teamName;
		this.seed = seed;
	}

	@Override
	public String toString() {
		return this.teamName;
	}
}