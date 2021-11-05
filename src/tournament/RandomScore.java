/*********************************************************************************************
 *	This function generates a random score for the championship game. Where both teams'
 *	scores are between lo and hi
 *
 *  @author:Bobby Hermitt
 *
 ********************************************************************************************/

public class RandomScore {

	public static String printRandomScore(int lo, int hi) throws IllegalArgumentException{
		if(lo > hi) {
			throw new IllegalArgumentException("lo cannot be greater than hi");
		}
		
		int winScore = (int)((hi-lo)*Math.random()) + lo;
		int lossScore = (int)((hi-lo)*Math.random()) + lo;
		while(lossScore == winScore) {
			lossScore = (int)((hi-lo)*Math.random()) + lo;
		}
		if(lossScore > winScore) {
			int temp = lossScore;
			lossScore = winScore;
			winScore = temp;
		}
		
		return "The Final Score is: " + winScore + " - " + lossScore;
	}

	public static void main(String[] args) {
		System.out.println(printRandomScore(50, 85));
	}
}
