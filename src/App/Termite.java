package App;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Semaphore;

/* Termite class
 *
 * binMeta project
 *
 * last update: Dec 6, 2020
 *
 * ABABOU Sarah, COCHET Julien
 */

public class Termite extends Thread {
	
	//****************************************************************************************************
	/**
	 *	ATTRIBUTS
	 */
	//****************************************************************************************************

    // Semaphore
	private Semaphore sem;
    // TCO
    private TCO tco;
	// Position
    private int posX, posY;
    
    // Random
    private Random R;
    
    
	//****************************************************************************************************
	/**
	 *	CONSTRUCTEURS
	 */
	//****************************************************************************************************
	
    public Termite(Semaphore sem, TCO tco, int posX, int posY) {
    	this.sem = sem;
    	this.tco = tco;
    	this.setPosition(posX, posY);
    	this.R = new Random();
    }

    
	//****************************************************************************************************
	/**
	 *	METHODES
	 */
	//****************************************************************************************************
	
	@Override
	public void run() {
		// Find the neighbor positions for each termite
		Set<Termite> neighbors = this.findNeighbors(tco.getTermites(), tco.getTerMouvRad());

		if (!neighbors.isEmpty()) {
			// Select best neighbor
			for (Termite neighbor : neighbors) {
				int posX = neighbor.getPosX();
				int posY = neighbor.getPosY();
				Data newD = tco.getD().selectInNeighbour(posX, posY);

				double totalPhero = 0;
				for (int i = 0; i < tco.getPheromones().length; i++) {
					for (int j = 0; j < tco.getPheromones()[i].length; j++) {
						totalPhero += tco.getPheromones()[i][j];
					}
				}

				try {
					sem.acquire();

					// evaluating the quality of the generated solution
					double value = tco.getObj().value(newD);
					if (((tco.getPheromones()[posX][posY] * tco.getObjVal())
							/ (totalPhero * tco.getObjVal())) > ((tco.getPheromones()[posX][posY] * value)
									/ (totalPhero * value))) {

						tco.setObjValue(value);
						tco.setSolution(new Data(newD));
						this.setPosition(posX, posY);
						tco.incrPheromones(posX, posY);

					}

					// the walk continues from the new generated solution
					tco.setD(newD);

					sem.release();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			// Selectrandom position
			int posX = R.nextInt(tco.getTabWidth());
			int posY = R.nextInt(tco.getTabHeight());
			Data newD = tco.getD().selectInNeighbour(posX, posY);
			this.setPosition(posX, posY);
			tco.incrPheromones(posX, posY);

			try {
				sem.acquire();

				// evaluating the quality of the generated solution
				double value = tco.getObj().value(newD);
				if (tco.getObjVal() > value) {
					tco.setObjValue(value);
					tco.setSolution(new Data(newD));
				}

				// the walk continues from the new generated solution
				tco.setD(newD);
				
				sem.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
    public Set<Termite> findNeighbors(Set <Termite> termites, int radius) {
        Set<Termite> neighbors = new HashSet<>();
        for (Termite t: termites ) {
            if (isNeighbor(t.getPosX(), t.getPosY(), radius)) {
                neighbors.add(t);
            }
        }
        return neighbors;
    }

    private boolean isNeighbor(int x, int y, int r) {
        if ((x <= posX - r) && (x >=posX +r) && (y <= posY - r) && (y >= posY +r)) {
            return true;
        }
        return false;
    }
    
    
	//****************************************************************************************************
	/**
	 *	GETTERS
	 */
	//****************************************************************************************************
	
    public int getPosX() {
        return posX;
    }
    
    public int getPosY() {
        return posY;
    }
    
    public Pair<Integer> getPosition() {
        return new Pair<Integer>(getPosX(), getPosY());
    }

    
	//****************************************************************************************************
	/**
	 *	SETTERS
	 */
	//****************************************************************************************************
	
    private void setPosX(int posX) {
        this.posX = posX;
    }
    
    private void setPosY(int posY) {
        this.posY = posY;
    }
    
    public void setPosition(int posX, int posY) {
        setPosX(posX);
        setPosY(posY);
    }

    
	//****************************************************************************************************
	/**
	 *	CLASSES INTERNES
	 */
	//****************************************************************************************************
	
    private class Pair<T> {
    	
        private T a, b;
        
        public Pair(T a, T b) {
            this.a = a;
            this.b = b;
        }
        
        @Override
        public String toString() {
            return "(" + a + ", " + b + ")";
        }
        
    }

}
