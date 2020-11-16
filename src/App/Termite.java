package App;

import java.util.HashSet;
import java.util.Set;

public class Termite {
    private int posX, posY;

    public int getPosX() {
        return posX;
    }
    public int getPosY(){
        return posY;
    }
    public Termite(int posX, int posY) {
        this.posX=posX;
        this.posY=posY;
    }


    Set <Termite> findNeighbor (Set <Termite> termites , int radius ){
        Set<Termite> neighbors = new HashSet <>();
        for (Termite t: termites ) {
            if ( isNeighbor(t.getPosX(),t.getPosY(),radius)){
                neighbors.add(t);
            }
        }
        return neighbors;
    }

    private boolean isNeighbor ( int x ,int y ,int r ){
        if ((x <= posX - r) && (x >=posX +r) &&(y <= posY - r) && (y >= posY +r)){
            return true;
        }
        return false;
    }

}
