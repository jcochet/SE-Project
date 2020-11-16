package App;

import java.util.ArrayList;
import java.util.List;

public class Termite {
	
    private int posX, posY;

    public Termite(int posX, int posY) {
    	setPos(posX, posY);
    }

    public List<Termite> findNeighbors(List<Termite> termites, int radius) {
    	List<Termite> neighbors = new ArrayList<>();
        for (Termite t: termites) {
            if (isNeighbor(t.getPosX(), t.getPosY(), radius)) {
                neighbors.add(t);
            }
        }
        return neighbors;
    }

    private boolean isNeighbor(int x, int y, int r) {
        if ((x <= posX - r) && (x >= posX + r) && (y <= posY - r) && (y >= posY + r)) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getPosX() {
        return posX;
    }
    
    public int getPosY(){
        return posY;
    }
    
    public void setPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

}
