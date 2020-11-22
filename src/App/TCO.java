package App;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	@Override
	public void optimize() // by Termite Colony Optimization
	{
		/* Random */
	    Random R = new Random();
	      
		/* Number of Termite */
		int N = ?;
		/* Termite movement radius */
		int Tr = ?;
		/* Maximum iteration */
		long itermax = maxTime;

		/* Termites */
		int width = ?;
		int height = ?;
		Set<Termite> X = new HashSet<>();
		
		/* Initialize all termite randomly */
		for (int i = 0; i < N; i++) {
			X.add(new Termite(R.nextInt(width), R.nextInt(height)));
		}
		
		/* Pheromone table */
		double T[][] = new double[width][height];
		
		for (long iter = 0; iter < itermax; iter++) {
			
			/* Compute fitness */
			int coveredPoints = ?;
			int totalPoints = ?;
			float coverage = 100 * (coveredPoints / totalPoints);
			int alpha = 2;
			double fitness = Math.pow(coverage, alpha);
			
			/* For all available location site */
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					/* Evaporation Rate  */
					float e = 0.7f;
					/* Update pheromone table */
					T[i][j] = (1 - e) * T[i][j] + 1 / (fitness + 1);
				}
			}
			
			/* For all termite t */
			for (Termite t: X) {
				/* Find the neighbor positions for termite t */
				Set<Termite> neighbors = t.findNeighbor(X, Tr);
				
				/* If termite t has neighbor */
				if (!neighbors.isEmpty()) {
					/* Select best neighbor with higher probability */
					double totalPheromone = 0;
					for (int i = 0; i < T.length; i++) {
						for (int j = 0; j < T.length; j++) {
							totalPheromone += T[i][j];
						}
					}
					int newPosX;
					int newPosY;
					double newPosP = 0;
					for (Termite termite : neighbors) {
						int s = ?;
						int b = ?;
						int n = s * (1 - b);
						double p = (T[termite.getPosX()][termite.getPosY()] * n) / (totalPheromone / n);
						if (p > newPosP) {
							newPosX = termite.getPosX();
							newPosY = termite.getPosY();
							newPosP = p;
						}
					}
					t.setPosition(newPosX, newPosY);
				} else {
					/* Select position randomly */
					t.setPosition(R.nextInt(width), R.nextInt(height));
				}
				T[t.getPosX()][t.getPosY()]++;
			}
			
			/* Adjust the radius Tr */
			Tr--;
			if(Tr < 1) {
				Tr = 1;
			}
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
