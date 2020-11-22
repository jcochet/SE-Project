package App;

import java.util.HashSet;
import java.util.Set;

public class Termite {
	
	//****************************************************************************************************
	/**
	 *	ATTRIBUTS
	 */
	//****************************************************************************************************
	
    private int posX, posY;

    
	//****************************************************************************************************
	/**
	 *	CONSTRUCTEURS
	 */
	//****************************************************************************************************
	
    public Termite(int posX, int posY) {
    	this.setPosition(posX, posY);
    }

    
	//****************************************************************************************************
	/**
	 *	METHODES
	 */
	//****************************************************************************************************
	
    public Set<Termite> findNeighbor(Set <Termite> termites, int radius) {
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
