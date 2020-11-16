package App;

import java.util.Random;

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
		/* Number of Termite */
		int nbTermite = 10;
		/* Termite movement radius */
		int terMovRad = 100;
		/* Maximum iteration */
		long iterMax = maxTime;
		
		for (int i = 0; i < nbTermite; i++) {
			
		}
	}

	// main
	public static void main(String[] args) {
		int ITMAX = 10000; // number of iterations

		// BitCounter
		int n = 50;
		Objective obj = new BitCounter(n);
		Data D = obj.solutionSample();
		RandomWalk rw = new RandomWalk(D, obj, ITMAX);
		System.out.println(rw);
		System.out.println("starting point : " + rw.getSolution());
		System.out.println("optimizing ...");
		rw.optimize();
		System.out.println(rw);
		System.out.println("solution : " + rw.getSolution());
		System.out.println();

		// Fermat
		int exp = 2;
		int ndigits = 10;
		obj = new Fermat(exp, ndigits);
		D = obj.solutionSample();
		rw = new RandomWalk(D, obj, ITMAX);
		System.out.println(rw);
		System.out.println("starting point : " + rw.getSolution());
		System.out.println("optimizing ...");
		rw.optimize();
		System.out.println(rw);
		System.out.println("solution : " + rw.getSolution());
		Data x = new Data(rw.solution, 0, ndigits - 1);
		Data y = new Data(rw.solution, ndigits, 2 * ndigits - 1);
		Data z = new Data(rw.solution, 2 * ndigits, 3 * ndigits - 1);
		System.out.print(
				"equivalent to the equation : " + x.posLongValue() + "^" + exp + " + " + y.posLongValue() + "^" + exp);
		if (rw.objValue == 0.0)
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
		rw = new RandomWalk(D, cp, ITMAX);
		System.out.println(rw);
		System.out.println("starting point : " + rw.getSolution());
		System.out.println("optimizing ...");
		rw.optimize();
		System.out.println(rw);
		System.out.println("solution : " + rw.getSolution());
		cp.value(rw.solution);
		System.out.println("corresponding to the matrix :\n" + cp.show());
	}

}
