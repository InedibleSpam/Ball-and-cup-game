/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Final;

/**
 *
 * @author Schro
 */
public class Cup {
    boolean hasball;
    int id;

    public Cup(int id) {
        this.id = id;
        this.hasball = false;
    }
    public void removeball(){
        this.hasball = false;
    }
    public void giveball(){
        this.hasball = true;
    }

    public boolean isHasball() {
        return hasball;
    }

    public int getId() {
        return id;
    }
    
}
