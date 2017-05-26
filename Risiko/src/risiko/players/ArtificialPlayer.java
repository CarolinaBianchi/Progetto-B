/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risiko.players;

import exceptions.PendingOperationsException;
import utils.GameObserver;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import risiko.Action;
import risiko.Game;

/**
 *
 * @author emanuela
 */
public class ArtificialPlayer extends Player implements Runnable, GameObserver {

    private Game game;

    int maxArmiesAttack;
    int maxArmiesDefense;
    boolean maxArmiesSet;
    boolean canAttack;

    private int attackRate = 100;
    private Action currentAction;
    private int reinforceSpeed = 100;
    private ArtificialPlayerSettings setting;
    private int declareSpeed = 500;
    private int attackSpeed = 500;

    /**
     * crea un delay tra successivi rinforzi di un giocatore artificiale
     *
     * @param reinforceSpeed tempo in ms tra un rinforzo e l'altro
     */
    public void setReinforceSpeed(int reinforceSpeed) {
        this.reinforceSpeed = reinforceSpeed;
    }

    /**
     *
     * @param name
     * @param color
     */
    public ArtificialPlayer(String name, String color, Game game) {
        super(name, color);
        this.game = game;
        currentAction = Action.NOACTION;
    }

    /**
     * aggiunge armate ai territori posseduti fino a che non sono esaurite
     */
    private synchronized void randomReinforce() {

        String[] myCountries = game.getMyCountries(this);

        while (game.canReinforce(1, this)) {
            int index = new Random().nextInt(myCountries.length);
            game.reinforce(myCountries[index], 1, this);
            try {
                this.wait(reinforceSpeed);
            } catch (InterruptedException ex) {

            }
        }
    }

    /**
     * esegue un attacco
     */
    private synchronized void randomAttack() {
        int i = 2;

        while (i > 0) {
            if (canAttack) {
                randomSingleAttack();
                i--;
            }
            //se non può attaccare da nessun territorio viene fermato l'attacco
            if (game.getAllAttackers(this).length == 0) {
                i = 0;
            }
        }

    }

    /**
     * esegue un attacco per turno
     */
    private synchronized void randomSingleAttack() {
        maxArmiesSet = false;

        String[] myCountries = game.getAllAttackers(this);
        String[] opponentCountries;
        int index, defendIndex;
        int tries = 10;

        do {
            //set country attacco
            index = new Random().nextInt(myCountries.length);
            game.setAttackerCountry(myCountries[index], this);

            //set country difesa
            opponentCountries = game.getAllDefenders(myCountries[index]);
            tries--;
        } while (opponentCountries.length == 0 && tries > 0);
        if (tries > 0) {
            defendIndex = new Random().nextInt(opponentCountries.length);
            game.setDefenderCountry(opponentCountries[defendIndex], this);
            //aspetta la risposta da game per il settaggio di max armies
            try {
                this.wait(10);
            } catch (InterruptedException ex) {

            }

            //set armate attacco
            if (maxArmiesSet) {
                int nrA = new Random().nextInt(maxArmiesAttack);
                if (nrA == 0) {
                    nrA = 1;
                }
                game.setAttackerArmies(nrA, this);
            } else {
                //se maxarmiesset non è true attacco con il massimo numero di armate
                game.setAttackerArmies(-1, this);
            }

            game.declareAttack(this);

            try {
                this.wait(declareSpeed);
            } catch (InterruptedException ex) {

            }
        }
    }

    private synchronized void defend() {
        if (maxArmiesSet) {
            int nrD = new Random().nextInt(this.maxArmiesDefense);
            if (nrD == 0) {
                nrD = 1;
            }
            game.setDefenderArmies(nrD, this);
        } else {
            //se maxarmiesset non è true difendo con il massimo numero di armate
            game.setDefenderArmies(-1, this);
        }

        game.confirmAttack(this);
        try {
            this.wait(attackSpeed);
        } catch (InterruptedException ex) {

        }
    }

    /**
     * esegue il turno del giocatore artificiale
     */
    @Override
    public void run() {
        while (currentAction != Action.ENDGAME) {
            try {
                if (game.getActivePlayer().equals(this)) {
                    switch (game.getPhase()) {
                        case REINFORCE:
                            randomReinforce();
                            break;
                        case FIGHT:
                            canAttack = true;
                            //this.randomSingleAttack();
                            this.randomAttack();
                            break;
                    }
                }
                switch (this.currentAction) {
                    case DEFEND:
                        this.defend();
                        break;
                }

                if (!game.canReinforce(1, this)) {
                    game.nextPhase(this);
                }
            } catch (PendingOperationsException ex) {
                //l'eccezione delle armate ancora da assegnare viene data quando si sovrappongono le operazioni di 2 giocatori
                Logger.getLogger(ArtificialPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void updateOnReinforce(String countryName, int bonusArmies) {
    }

    public void updateOnPhaseChange(String player, String phase) {
    }

    @Override
    public void updateOnSetAttacker(String countryName) {
    }

    @Override
    public void updateOnSetDefender(String countryAttackerName, String countryDefenderName, String defenderPlayer, int maxArmiesAttacker, int maxArmiesDefender) {
        this.maxArmiesAttack = maxArmiesAttacker;
        this.maxArmiesDefense = maxArmiesDefender;
        this.maxArmiesSet = true;
    }

    @Override
    public void updateOnAttackResult(String attackResultInfo, boolean isConquered, boolean canAttackFromCountry, int maxArmiesAttacker, int maxArmiesDefender, int[] attackerDice, int[] defenderDice) {
        canAttack = true;
    }

    @Override
    public void updateOnVictory(String winner) {
        this.currentAction = Action.ENDGAME;
    }

    @Override
    public void updateOnCountryAssignment(String[] countries, int[] armies, String[] colors) {
    }

    @Override
    public void updateOnArmiesChange(String country, int armies, String color) {
    }

    @Override
    public void updateOnSetFromCountry(String countryName) {
    }

    @Override
    public void updateOnNextTurn() {
    }

    @Override
    public void updateOnDrawnCard(String cardName) {
    }

    @Override
    public void updateOnPhaseChange(String player, String phase, String color) {
    }
    
    public void updateOnDefend(String defender, String countryDefender, String attacker, String countryAttacker, int nrA, boolean isArtificialPlayer) {
        if (this.getName().equals(defender)) {
            this.currentAction = Action.DEFEND;  
        }   
        //canAttack = false;
    }

}