/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ballcupgame;

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
