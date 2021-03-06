import java.awt.*;
import java.util.ArrayList;

/**
 * this class is for the controlling of the informations of the Army and the Reinforcements
 */
public class Gamer {
    public Color color;
    public ArrayList<Territory> myTerritory = new ArrayList<Territory>();
    public int reinforcements = 0;
    public int reinforcementsPlacedThisTurn = 0; //// FIXME: 31/12/15
    String playerName;
      private boolean isHuman = false;

    /**
     * base information for a non player charakter
     * if the player is human so it will be set in the constructor to true and blue;
     */

    public Gamer(Color color, boolean isHuman, String playerName) {
        this.isHuman = isHuman;
        this.color = color;
        this.playerName = playerName;

    }

    //getOwner returns which gamer controls a given territory. Iterative check.
    public static Gamer getOwner(Territory currentlySelected, Gamer computerPlayer1, Gamer humanPlayer1) {

        for (Territory t : computerPlayer1.myTerritory) {
            if (t.equals(currentlySelected)) {
                return computerPlayer1;
            }
        }

        for (Territory t : humanPlayer1.myTerritory) {
            if (t.equals(currentlySelected)) {
                return humanPlayer1;
            }
        }

        return new Gamer(Color.BLACK, false, "dummy");
    }


 /*   public void captureForComputerPlayerAfterStartTerritoryIsSet() {
        if (this.myTerritory.size() > 0) {
            while (true) {
                int randTerritory = 0 + (int) (Math.random() * this.myTerritory.size());

                int randNeighbours = 0 + (int) (Math.random() * this.myTerritory.get(randTerritory).getNeighbours().size());

                if (!this.myTerritory.contains(this.myTerritory.get(randTerritory).getNeighbours().get(randNeighbours)) &&
                        this.myTerritory.get(randTerritory).getNeighbours().get(randNeighbours).getArmyCount() == 0) {
                    this.createNewArmy(this.myTerritory.get(randTerritory).getNeighbours().get(randNeighbours));
                    return;
                }
            }
        }
    }*/

    public void captureTerritory(Territory toCapture) {
        myTerritory.add(toCapture);
        toCapture.addArmy(1);
        System.out.println("succesfully captured: " + toCapture.getName());
    }

    public void captureTerritory(Territory toCapture, boolean isATakeOver) {

        if (isATakeOver) {
            myTerritory.add(toCapture);
            toCapture.army = 0;
            System.out.println("succesfully captured: " + toCapture.getName());
        } else {
            captureTerritory(toCapture);
        }
    }

    public boolean init(Territory area) {
        if (this.myTerritory.size() == 0) {
            this.createNewArmy(area);
            return true;
        } else    // check if the selected area is a neightbour of the baseTerretory
        {
            if (!this.myTerritory.contains(area))    // the selected is not allowed to be in the Territory list
            {
                for (int i = 0; i < this.myTerritory.size(); i++) {
                    if (this.myTerritory.get(i).checkNeighbour(area)) {
                        this.createNewArmy(area);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * by a fight there a three states with the randoms if army 1 == army 2 there happens nothing
     * army 1 >  army 2 there army 1 won the game
     * army 1 <  army 2 there army 2 won the game
     * the looser by not equal will lose one army
     *
     * @param otherArmy the random number of the attacker
     * @return 0 if its equal
     * 1 if the otherArmy got a better value
     * 2 if this army got a better value
     */
    private int fight(int otherArmy) {
        int thisArmy = 1 + (int) (Math.random() * 6);

        if (thisArmy > otherArmy)
            return 2;
        else if (thisArmy == otherArmy)
            return 0;
        else
            return 1;

    }

    public void removeTerritory(Territory t) {
        this.myTerritory.remove(t);
    }

    public void createNewArmy(Territory t) {
        if (this.myTerritory.contains(t)) {
            t.addArmy(1);
        }
    }

    public void calculateReinforcements() {

        if (this.playerName == "Human Player 1") {
            this.reinforcements = 11;
        }
        if (this.playerName == "CPU Player 1") {
            this.reinforcements = 2000;
        }
    }

    public void captureTerritory(Territory toCapture, boolean isATakeOver, int numberOfTransfer) {

        if (isATakeOver) {
            myTerritory.add(toCapture);
            toCapture.army = numberOfTransfer;
            System.out.println("succesfully captured: " + toCapture.getName());
        } else {
            captureTerritory(toCapture);
        }
    }

/*    public void createNewReinforcements() {
        this.reinforcements++;
    }

    public void moveArmy(Territory from, Territory to, ArrayList<Gamer> otherPlayer) {
        int army = from.getArmyCount();
        army--; // because one army must be at the Territory

        if (army > 3)    // max 3 armys can move per move
            army = 3;

        if (this.myTerritory.contains(to))    // move on the own ground   -- no fight
        {
            from.removeArmy(army);
            to.addArmy(army);
        } else    // this is an enemy ground -- fight
        {
            for (int i = 0; i < otherPlayer.size(); i++) {
                if (!otherPlayer.get(i).equals(this)) {
                    if (otherPlayer.get(i).myTerritory.contains(from)) {
                        int erg = -1;
                        for (int j = 0; j < army; j++) {
                            erg = otherPlayer.get(i).fight((1 + (int) (Math.random() * 6)));
                            if (erg == 0)    // no one wone
                            {
                                army--;
                                j--;
                            } else if (erg == 1) // this player won
                            {
                                to.removeArmy(1);

                                if (to.getArmyCount() == 0) // no enemy Armys left on the Territory  - capture
                                {
                                    otherPlayer.get(i).removeTerritory(to); // remove Territory from the otherPlayer
                                    this.myTerritory.add(to);                // add Territory to this Player

                                    from.removeArmy(army);
                                    to.addArmy(army);                        // set Correct count of Army

                                    return;
                                }

                            } else if (erg == 2) // otherPlayer won
                            {
                                army--;
                                j--;
                            }
                        }


                        return;
                    }
                }
            }
        }

    }

    public boolean setReinforcementToArmy(Territory t) {
        if (this.reinforcements > 0) {
            this.reinforcements--;
            createNewArmy(t);
            return true;
        }
        return false;
    }

    public int getAnzPossibleReinforcementsAvialable() {
        return this.reinforcements;
    }*/

}
