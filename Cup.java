public class Cup {
    private int id;        
    private boolean hasBall; 

    
    public Cup(int id) {
        this.id = id;
        this.hasBall = false; 
    }

    
    public int getId() {
        return id;
    }

   
    public boolean hasBall() {
        return hasBall;
    }

    public void giveBall() {
        hasBall = true;
    }

    
    public void removeBall() {
        hasBall = false;
    }

    
}