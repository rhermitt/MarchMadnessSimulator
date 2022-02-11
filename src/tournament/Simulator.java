import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Simulator {

	public static void main(String[] args) throws Exception{
		
		Scanner bobby = new Scanner(System.in);
//		System.out.print("Enter files file: ");
//		String file = bobby.next();
//		Bracket tournament = new Bracket(file);
//		tournament.simulateTournament();
//		System.out.println(tournament);

		System.out.print("Select Input method (Enter '1' or '2'):\n1.) Step-By-Step input\n2.) One file input: ");
		String inputMethod = bobby.next();
		while(!inputMethod.equals("1") && !inputMethod.equals("2")){
			System.out.print("Please enter a valid input method ('1' or '2'): ");
			inputMethod = bobby.next();
		}
		System.out.println(inputMethod);

		int input = Integer.parseInt(inputMethod);
		if(input == 2){
			System.out.print("Enter the files file: ");
			String filesFile = bobby.next();
			Bracket tournament = new Bracket(filesFile);
			tournament.simulateTournament();
			System.out.println(tournament);
		}
		//System.out.println("Enter the file containing the teams: ");

		bobby.close();
		
	}

}
