/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ballcupgame;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author glaba
 */

public class Main{
    Random ran;
    ArrayList <Cup> clist;
    
    public Main(Random random) { /// Defaults to 3
        clist=new ArrayList<>();
        this.ran = new Random();
        Cup c = new Cup(1);
        Cup d = new Cup(2);
        Cup e = new Cup(3);
        clist.add(c);
        clist.add(d);
        clist.add(e);
    }
    
    
    
    public Main(Random random, int num){
        this.ran = new Random();
        for (int i = 1; i <= num; i++) {
            Cup f = new Cup(i);
            clist.add(f);
        }
    }
    
    
    public Cup getCup(int id){
        for(Cup cup:clist){
            if(cup.getId()==id){
                return cup;
            }
            
        }
        return null;
    }

    public void shuffle() {
        int x = ran.nextInt(1, clist.size()+1);
        for (Cup cup : clist) {
            if (cup.getId() == x){
                cup.giveBall();
            }else{
                cup.removeBall();
            }
        }
            
    }
    
   public void savescore(String s, int i){
       try{
           PrintWriter out = new PrintWriter(new FileWriter("Scores.txt"));
           out.println(s+" : "+i);
           
       }catch (Exception e){
           System.out.println("Oh no! We encountered an exception in the code!");
           System.out.println("X X");
           System.out.println(" O ");
       }
   } 

    
}