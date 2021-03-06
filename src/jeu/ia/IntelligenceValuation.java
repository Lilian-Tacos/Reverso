package jeu.ia;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Random;
import jeu.Case;
import jeu.Plateau;


/**
 * Intelligence artificielle par valuation de cases.
 * 
 * <p>Cette intelligence associe une valeur à chaque case du plateau et propose
 * un coup de façon à obtenir le meilleur nombre de points.</p>
 * 
 * @see IntelligenceBase
 */
public class IntelligenceValuation extends IntelligenceBase{
    // Grille de valeur des cases
    /**
     * Tableau des valeurs associées à chacune des cases
    */
    private int[] valeurs;
    
    /**
    * Initialise l'intelligence atificielle et crée le tableau des valeurs associées
    * aux cases
    * 
    * @param plate le Plateau pour lequel l'intelligence artificielle va 
    * chercher les coups possibles.
    */
    public IntelligenceValuation(Plateau plate) {
        super(plate);
        valeurs = creerGrille();
    }
    
    // Constructeur de la grille
    /**
     * Crée et renvoie un tableau contenant la liste des valeurs associées à chaque 
     * case.
     * 
     * @return le tableau des valeurs
     */
    public static int[] creerGrille(){
        int[] grille = new int[64];
        // On crée un patron correspondant au coin supérieur gauche
        int[] patron = new int[16];
        patron[0]=1000;//angle
        patron[1]=patron[4]=-75;//bords contres angle
        patron[2]=patron[8]=20;//bords proches angle
        patron[3]=patron[12]=8;//bords centre
        patron[5]=-200;//diag contre angle
        patron[6]=patron[7]=patron[9]=patron[13]=-10;//cases proches bord
        patron[10]=patron[11]=patron[14]=2;//cases autour centre
        patron[15]=20;//case du centre
        //On place le patron dans la grille, en le retournant si nécessaire
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                if(i<4){
                    if(j<4){
                        grille[8*i+j]=patron[4*i+j];}//coin sup gauche
                    else{
                        grille[8*i+j]=patron[4*i+(3-(j%4))];}//coin sup droit
                }
                else {
                    if(j<4){
                        grille[8*i+j]=patron[4*(3-(i%4))+j];}//coin inf gauche
                    else{
                        grille[8*i+j]=patron[4*(3-(i%4))+(3-(j%4))];}//coin inf droit
                }
        return grille;
    }
    
    /**
     * 
     * @param plate Plateau du jeu sur lequel on veut compter les points
     * @param blanc Booleen indiquant si le joueur dont on veut compter les points.
     * true signifie qu'on veut compter les points blanc, false les noirs
     * @return Nombre total de points du joueur indiqué.
     */
    // Calcul les points qu'à l'IA en utilisant la grille
    // Si il y a 4 cases libres ou moints, on calcule le nombre de pions
    protected int calculerPoints(Plateau plate,boolean blanc){
        int score = 0;
        // Si le joueur possède un angle les cases autour sont avantageuses
        boolean hautgauche,hautdroit,basgauche,basdroit;
        hautgauche=possedeCase(plate.getDamier()[0],blanc);
        hautdroit=possedeCase(plate.getDamier()[7],blanc);
        basgauche=possedeCase(plate.getDamier()[56],blanc);
        basdroit=possedeCase(plate.getDamier()[63],blanc);
        // Si l'angle est pris la valeur de la case à coté en ligne
        // devient abs(case diag) et inversement
        int valdiag =abs(valeurs[9]);
        int valligne=abs(valeurs[1]);
        for(int i= 0;i<64;i++){
            if(possedeCase(plate.getDamier()[i],blanc)){
                 switch(i){
                    case 1:case 8:score+=(hautgauche?valdiag:valeurs[i]);break;
                    case 9:score+=(hautgauche?valligne:valeurs[i]);break;
                    case 6:case 15:score+=(hautdroit?valdiag:valeurs[i]);break;
                    case 14:score+=(hautdroit?valligne:valeurs[i]);break;
                    case 48:case 57:score+=(basgauche?valdiag:valeurs[i]);break;
                    case 49:score+=(basgauche?valligne:valeurs[i]);break;
                    case 55:case 62:score+=(basdroit?valdiag:valeurs[i]);break;
                    case 54:score+=(basdroit?valligne:valeurs[i]);break;
                    default:score+=valeurs[i];break;
                }
            }
        }
        return score;
    }
    
    // Renvoie vrai si le joueur possède la case
    /**
     * 
     * @param c Case à étudier
     * @param joueur joueur supposé posseder la case. true signifie joueur blanc, 
     * false joueur noir
     * @return true si le joueur indiqué possede effectivement la case.
     */
    protected boolean possedeCase(Case c, boolean joueur){
        return c.remplie() && c.blanche()==joueur;
    }
        
    // L'ia renvoie la case sur laquelle elle va jouer
    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public int mouvement() throws NoFreeCaseException {
        Random rnd  = new Random();
        Plateau copie;
        int scoreIA;
        int meilleurCoup=-30000;
        ArrayList<Integer> positionsMeilleurCoup = new ArrayList<>();
        ArrayList<Integer> jouables= casesJouables();
        for(int i=0;i<jouables.size();i++){
            copie =simuler(plateau, jouables.get(i));
            scoreIA= calculerPoints(copie,plateau.tourBlanc());
            if(scoreIA==meilleurCoup)
                positionsMeilleurCoup.add(jouables.get(i));
            else if(scoreIA>meilleurCoup){
                meilleurCoup = scoreIA;
                positionsMeilleurCoup.clear();
                positionsMeilleurCoup.add(jouables.get(i));
            }
        }
        return positionsMeilleurCoup.get(rnd.nextInt(positionsMeilleurCoup.size()));
    }
}
