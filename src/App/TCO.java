package App;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/* binMeta class
*
* binMeta project
*
* last update: Nov 9, 2020
*
* ABABOU Sarah, COCHET Julien
*/

public class TCO extends binMeta {

	// Termite Colony Optimization constructor
	public TCO(Data startPoint, Objective obj, long maxTime) {
		try {
			String msg = "Impossible to create TCO object: ";
			if (maxTime <= 0)
				throw new Exception(msg + "the maximum execution time is 0 or even negative");
			this.maxTime = maxTime;
			if (startPoint == null)
				throw new Exception(msg + "the reference to the starting point is null");
			this.solution = startPoint;
			if (obj == null)
				throw new Exception(msg + "the reference to the objective is null");
			this.obj = obj;
			this.objValue = this.obj.value(this.solution);
			this.metaName = "Termite Colony Optimization";
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * @Override public void optimize() // by Termite Colony Optimization { //
	 * Random Random R = new Random(); // Data Data D = new Data(this.solution);
	 * 
	 * // Number of Termite int N = 100; // Termite movement radius int Tr = 100; //
	 * Maximum iteration long itermax = maxTime;
	 * 
	 * // Termites int width = 8; int height = D.numberOfBytes(); Set<Termite> X =
	 * new HashSet<>();
	 * 
	 * // Initialize all termite randomly for (int i = 0; i < N; i++) { X.add(new
	 * Termite(R.nextInt(width), R.nextInt(height))); }
	 * 
	 * // Pheromone table double T[][] = new double[width][height];
	 * 
	 * // Solutions table Data Solutions[][] = new Data[width][height]; for (int i =
	 * 0; i < Solutions.length; i++) { for (int j = 0; j < Solutions[i].length; j++)
	 * { Solutions[i][j] = new Data(D.numberOfBits(), D.getCurrentBit() == 1);
	 * D.moveToNextBit(); } }
	 * 
	 * // Fitness double fitness = obj.value(solution);
	 * 
	 * for (long iter = 0; iter < itermax; iter++) {
	 * 
	 * // Compute fitness /* int coveredPoints = ?; int totalPoints = ?; float
	 * coverage = 100 * (coveredPoints / totalPoints); int alpha = 2; double fitness
	 * = Math.pow(coverage, alpha);
	 */
	/*
	 * fitness = obj.value(solution);
	 * 
	 * // For all available location site for (int i = 0; i < width; i++) { for (int
	 * j = 0; j < height; j++) { // Evaporation Rate float e = 0.7f; // Update
	 * pheromone table T[i][j] = (1 - e) * T[i][j] + 1 / (fitness + 1); } }
	 * 
	 * // For all termite t for (Termite t : X) { // Find the neighbor positions for
	 * termite t Set<Termite> neighbors = t.findNeighbor(X, Tr);
	 * 
	 * // If termite t has neighbor if (!neighbors.isEmpty()) { // Select best
	 * neighbor with higher probability double totalPheromone = 0; for (int i = 0; i
	 * < T.length; i++) { for (int j = 0; j < T.length; j++) { totalPheromone +=
	 * T[i][j]; } } int newPosX = R.nextInt(width); int newPosY = R.nextInt(height);
	 * double newPosP = 0; for (Termite termite : neighbors) { int s = 1; int b = 0;
	 * int n = s * (1 - b); double p = (T[termite.getPosX()][termite.getPosY()] * n)
	 * / (totalPheromone / n); if (p > newPosP) { newPosX = termite.getPosX();
	 * newPosY = termite.getPosY(); newPosP = p; } } t.setPosition(newPosX,
	 * newPosY); } else { // Select position randomly
	 * t.setPosition(R.nextInt(width), R.nextInt(height)); }
	 * T[t.getPosX()][t.getPosY()]++; }
	 * 
	 * for (Termite termite : X) { Data newD =
	 * Solutions[termite.getPosX()][termite.getPosY()]; double value =
	 * obj.value(newD); if (this.objValue > value) { this.objValue = value;
	 * this.solution = new Data(newD); } }
	 * 
	 * // Adjust the radius Tr Tr--; if (Tr < 1) { Tr = 1; } }
	 */

	@Override
	public void optimize() // by TCO
	{
		Random R = new Random();
		Data D = new Data(this.solution);
		long startime = System.currentTimeMillis();
		
		// Solutions table
		int width = 8;
		int height = D.numberOfBytes();
		boolean soluTab[][] = new boolean[width][height];

	    // loading the data structure (the bits) in the 2-dimensional matrix
	    for (int i = 0; i < width; i++)
	    {
	    	for (int j = 0; j < height; j++)
	        {
	    		soluTab[i][j] = (D.getCurrentBit() == 1);
	            D.moveToNextBit();
	        }
	    }
	    
		// Pheromone table
	    double pheromones[][] = new double[width][height];
	    for (int i = 0; i < width; i++)
	    {
	    	for (int j = 0; j < height; j++)
	        {
	    		pheromones[i][j] = 0;
	        }
	    }

		// Number of Termite
		int nbTermite = 10;
		// Termite movement radius
		int terMouvRad = D.numberOfBits();
	    // Set of termites
	    Set<Termite> termites = new HashSet<>();

		// Initialize all termite position randomly
	    for (int i = 0; i < nbTermite; i++) {
	    	int posX = R.nextInt(width);
	    	int posY = R.nextInt(height);
	    	termites.add(new Termite(0, 0));
	    	pheromones[posX][posY]++;
	    }
	    
	    // Fitness
	    double fitness = obj.value(solution);
		// Evaporation rate
		float evapRate = 0.7f;

		// main loop
		while (System.currentTimeMillis() - startime < this.maxTime) {
			// Compute fitness
			fitness = obj.value(solution);

			// Update pheromone table
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					pheromones[i][j] = (1 - evapRate) * pheromones[i][j] + 1 / (fitness + 1);
				}
			}
			
			// Find the neighbor positions for each termite
			for (Termite termite : termites) {
				Set<Termite> neighbors = termite.findNeighbors(termites, terMouvRad);
				
				if(!neighbors.isEmpty()) {
					// Select best neighbor
					for (Termite neighbor : neighbors) {
						int posX = neighbor.getPosX();
						int posY = neighbor.getPosY();
						Data newD = D.selectInNeighbour(posX, posY);
						
						double totalPhero = 0;
						for (int i = 0; i < pheromones.length; i++) {
							for (int j = 0; j < pheromones[i].length; j++) {
								totalPhero += pheromones[i][j];
							}
						}
						
						// evaluating the quality of the generated solution
						double value = obj.value(newD);
						//if (this.objValue > value) {
						if (((pheromones[posX][posY] * this.objValue) / (totalPhero * this.objValue)) > ((pheromones[posX][posY] * value) / (totalPhero * value))) {
							this.objValue = value;
							this.solution = new Data(newD);
							termite.setPosition(posX, posY);
							pheromones[posX][posY]++;
						}
						
						// the walk continues from the new generated solution
						D = newD;
					}
					
				} else {
					int posX = R.nextInt(width);
					int posY = R.nextInt(height);
					Data newD = D.selectInNeighbour(posX, posY);
					termite.setPosition(posX, posY);
					pheromones[posX][posY]++;
					
					// evaluating the quality of the generated solution
					double value = obj.value(newD);
					if (this.objValue > value) {
						this.objValue = value;
						this.solution = new Data(newD);
					}
					
					// the walk continues from the new generated solution
					D = newD;
				}
			}
			
			// Adjust the radius
			terMouvRad--;
		}

	}

	// main
	public static void main(String[] args) {
		int ITMAX = 10000; // number of iterations

		// BitCounter
		int n = 50;
		Objective obj = new BitCounter(n);
		Data D = obj.solutionSample();
		TCO tco = new TCO(D, obj, ITMAX);
		System.out.println(tco);
		System.out.println("starting point : " + tco.getSolution());
		System.out.println("optimizing ...");
		tco.optimize();
		System.out.println(tco);
		System.out.println("solution : " + tco.getSolution());
		System.out.println();

		// Fermat
		int exp = 2;
		int ndigits = 10;
		obj = new Fermat(exp, ndigits);
		D = obj.solutionSample();
		tco = new TCO(D, obj, ITMAX);
		System.out.println(tco);
		System.out.println("starting point : " + tco.getSolution());
		System.out.println("optimizing ...");
		tco.optimize();
		System.out.println(tco);
		System.out.println("solution : " + tco.getSolution());
		Data x = new Data(tco.solution, 0, ndigits - 1);
		Data y = new Data(tco.solution, ndigits, 2 * ndigits - 1);
		Data z = new Data(tco.solution, 2 * ndigits, 3 * ndigits - 1);
		System.out.print(
				"equivalent to the equation : " + x.posLongValue() + "^" + exp + " + " + y.posLongValue() + "^" + exp);
		if (tco.objValue == 0.0)
			System.out.print(" == ");
		else
			System.out.print(" ?= ");
		System.out.println(z.posLongValue() + "^" + exp);
		System.out.println();

		// ColorPartition
		n = 4;
		int m = 14;
		ColorPartition cp = new ColorPartition(n, m);
		D = cp.solutionSample();
		tco = new TCO(D, cp, ITMAX);
		System.out.println(tco);
		System.out.println("starting point : " + tco.getSolution());
		System.out.println("optimizing ...");
		tco.optimize();
		System.out.println(tco);
		System.out.println("solution : " + tco.getSolution());
		cp.value(tco.solution);
		System.out.println("corresponding to the matrix :\n" + cp.show());
	}

}
