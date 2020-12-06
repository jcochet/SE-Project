package App;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Semaphore;

/* TCO class
*
* binMeta project
*
* last update: Dec 6, 2020
*
* AM, ABABOU Sarah, COCHET Julien
*/

public class TCO extends binMeta {

	//****************************************************************************************************
	/**
	 *	ATTRIBUTS
	 */
	//****************************************************************************************************
	
    // Data
    private Data D;
	// Termite movement radius
    private int terMouvRad;
    // Set of termites
    private Set<Termite> termites;
	// Solutions table dimension
    private int tabWidth,tabHeight;
	// Pheromone table
    private double pheromones[][];
    

    
	//****************************************************************************************************
	/**
	 *	CONSTRUCTEURS
	 */
	//****************************************************************************************************
	
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
	
	
	//****************************************************************************************************
	/**
	 *	METHODES
	 */
	//****************************************************************************************************

	@Override
	public void optimize() // by TCO
	{
		Random R = new Random();
		D = new Data(this.solution);
		long startime = System.currentTimeMillis();

		// Solutions table
		tabWidth = 8;
		tabHeight = D.numberOfBytes();
		boolean soluTab[][] = new boolean[tabWidth][tabHeight];

		// loading the data structure (the bits) in the 2-dimensional matrix
		for (int i = 0; i < tabWidth; i++) {
			for (int j = 0; j < tabHeight; j++) {
				soluTab[i][j] = (D.getCurrentBit() == 1);
				D.moveToNextBit();
			}
		}

		// Pheromone table
		pheromones = new double[tabWidth][tabHeight];
		for (int i = 0; i < tabWidth; i++) {
			for (int j = 0; j < tabHeight; j++) {
				pheromones[i][j] = 0;
			}
		}

		// Number of Termite
		int nbTermite = 10;
		// Termite movement radius
		terMouvRad = D.numberOfBits();
		// Set of termites
		termites = new HashSet<>();

		// Initialize all termite position randomly
		Semaphore sem = new Semaphore(1);
		for (int i = 0; i < nbTermite; i++) {
			int posX = R.nextInt(tabWidth);
			int posY = R.nextInt(tabHeight);
			termites.add(new Termite(sem, this, 0, 0));
			pheromones[posX][posY]++;
		}

		// Fitness
		double fitness = obj.value(solution);
		// Evaporation rate
		float evapRate = 0.7f;
		// Initialize pheromone table
		for (int i = 0; i < tabWidth; i++) {
			for (int j = 0; j < tabHeight; j++) {
				pheromones[i][j] = (1 - evapRate) * pheromones[i][j] + 1 / (fitness + 1);
			}
		}

		// FIRST ITERATION
		for (Termite termite : termites) {
			termite.start();
		}
		// Adjust the radius
		terMouvRad--;

		// MAIN LOOP
		while (System.currentTimeMillis() - startime < this.maxTime) {
			// Compute fitness
			fitness = obj.value(solution);

			// Update pheromone table
			for (int i = 0; i < tabWidth; i++) {
				for (int j = 0; j < tabHeight; j++) {
					pheromones[i][j] = (1 - evapRate) * pheromones[i][j] + 1 / (fitness + 1);
				}
			}

			// For each termite
			for (Termite termite : termites) {
				termite.run();
			}

			// Adjust the radius
			terMouvRad--;
		}

	}
	
	
	//****************************************************************************************************
	/**
	 *	GETTERS
	 */
	//****************************************************************************************************
	
	public Data getD() {
		return this.D;
	}

	public int getTerMouvRad() {
		return this.terMouvRad;
	}
	
	public Set<Termite> getTermites() {
		return Collections.unmodifiableSet(this.termites);
	}

	public double[][] getPheromones() {
		return this.pheromones;
	}
	
	public int getTabWidth() {
		return this.tabWidth;
	}
	
	public int getTabHeight() {
		return this.tabHeight;
	}
	
	
	//****************************************************************************************************
	/**
	 *	SETTERS
	 */
	//****************************************************************************************************
	
	public void setObjValue(Double objValue) {
		this.objValue = objValue;
	}
	
	public void setSolution(Data solution) {
		this.solution = solution;
	}
	
	public void setD(Data D) {
		this.D = D;
	}

	public void incrPheromones(int i, int j) {
		this.pheromones[i][j]++;
	}
	
	
	//****************************************************************************************************
	/**
	 *	MAIN
	 */
	//****************************************************************************************************
	
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
