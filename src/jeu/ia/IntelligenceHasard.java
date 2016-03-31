/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu.ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import jeu.Case;
import jeu.Plateau;

/**
 *
 * @author Podoko
 */
public class IntelligenceHasard extends IntelligenceBase{

    public IntelligenceHasard(Plateau plate) {
        super(plate);
    }
    
    
    @Override
    public int mouvement() throws NoFreeCaseException {
        
        Random rnd  = new Random();
        ArrayList<Integer> jouables = casesJouables();
        
        return jouables.get(rnd.nextInt(jouables.size()));
    }
    
}
