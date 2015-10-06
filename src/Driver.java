
public class Driver {
	
	// Please note that these numbers are probably bullshit, as I have
	// no idea what half of these things mean
	public static final int MAX_ITERATIONS = 10;
	public static final int NUMBER_OF_ANTS = 1000;
	public static final double PHEROMONE = 832;
	public static final double EVAPORATION = 0.5;
	public static final double CONVERGENCE_CRITERIA = 1;
	
	// Starting and ending point variables
	public static final int STARTING_X = 0;
	public static final int STARTING_Y = 0;
	public static final int ENDING_X = 50;
	public static final int ENDING_Y = 50;

	public static void main(String[] args) {
		System.out.println("Hello World");
		System.out.println("Yoooooooooooo");
		
		Stack<Integer> a = new Stack<Integer>();
		a.push(1);
		a.push(2);
		System.out.println(a.top());
		a.pop();
		System.out.println(a.top());
		a.pop();
		a.pop();
	}

}
