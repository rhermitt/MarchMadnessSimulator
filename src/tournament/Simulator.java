import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
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
		System.out.println("Do you want to sim the first round using Number Advance Probability (Enter yes/no)");
		System.out.println("WARNING: You cannot use predetermined winners if you choose yes: ");
		String choice = bobby.next();
		while(!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")){
			System.out.print("Please enter yes or no: ");
			choice = bobby.next();
		}
		boolean usingNumAdvance = choice.toLowerCase().equals("yes");
		if(input == 2){
			System.out.print("Enter the files file: ");
			String filesFile = bobby.next();
			Bracket tournament = new Bracket(filesFile, usingNumAdvance);
			tournament.simulateTournament();
			System.out.println(tournament);
		}

		bobby.close();
		
	}

}
