
/**
 *
 * @author glaba
 */
public class Cup {
    boolean hasBall;
    int id;
    
    public Cup(int id){
        this.id=id;
        hasBall=false;
    }
    
    public void giveBall(){
        hasBall=true;
    }
    
    public void removeBall(){
        hasBall=false;
        
    }
    
    public boolean checkForBall(){
        if(hasBall==true){
            return true;
        }
        else{
            return false;
        }
    }
    
    public int getId(){
        return id;
    }
    
}
