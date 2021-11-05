import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Simulator {

	public static void main(String[] args) throws Exception{
		
		Scanner bobby = new Scanner(System.in);
		System.out.print("Enter files file: ");
		String file = bobby.next();
		Bracket tournament = new Bracket(file);
		tournament.simulateTournament();
		System.out.println(tournament);
		bobby.close();
		
	}

}
