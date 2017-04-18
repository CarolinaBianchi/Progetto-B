package risiko;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

class Game {

    private RisikoMap map;
    private List<Player> players;
    private Player activePlayer;
    private Player winner;
    private Phase phase;
    private AttackResult attackResult;

    public Game(int nrPlayers) throws Exception {
        this.players = new ArrayList<>();
        this.activePlayer = null;
        this.winner = null;
        this.map = new RisikoMap();
        init(nrPlayers);
    }

    /**
     * Inizializza il gioco. Ovvero chiama il metodo della mappa per
     * l'assegnazione iniziale dei territori ai giocatori -
     * assignCountriesToPlayers() - , setta un giocatore a caso come
     * activePlayer
     *
     * @author Federico
     * @throws rilancia l'eccezione che potrebbe lanciare la mappa nel caso in
     * cui l'url del file dei territori fosse sbagliato.
     */
    private void init(int nrPlayers) throws Exception {

        buildPlayers(nrPlayers);
        map.assignCountriesToPlayers(players);

        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(players.size());
        activePlayer = players.get(randomIndex);

    }

    //aggiungere nomi diversi per i giocatori
    private void buildPlayers(int nrPlayers) {

        for (int i = 0; i < nrPlayers; i++) {
            this.players.add(new Player("nome"));
        }
    }

    /**
     * controlla ed esegue l'attacco se questo è velido e fatto dal giocatore
     * corrente controlla (se attacco riuscito e conquista) che sia il vincitore
     * sposta nrA armate nel territorio conquistato salva attacker e defender e
     * le armate perse in un nuovo oggetto attackResult e i player se
     * isConquered chiama hasLost
     *
     * @param countryAttack territorio dal quale parte l'attacco
     * @param countrydefense territorio in difesa
     * @param player giocatore che vuole eseguire l'attacco
     * @return se l'azione è stata eseguita
     * @author Andrea
     */
    public void attack(Country countryAttack, Country countrydefense, int nrA, int nrD) {
        //TODO
    }

    /*
        ridà il risultato
        @author Andrea
     */
    public AttackResult getAttackResult() {
        //TODO
        return null;
    }

    /*
        CONTROLLA SE IL DIFENSORE DEVE ESSERE ELIMINATO DAL GIOCO
     */
    private void hasLost(Player defenderPlayer) {
        if (map.hasLost(defenderPlayer)) {
            players.remove(defenderPlayer);
        }
    }

    /**
     * controlla e aggiunge le armate al territorio, queste vengono prese dal
     * campo bonusArmies del giocatore fino ad esaurimento
     *
     * @param player giocatore che vuole eseguire azione
     * @param nArmies numero di armate da aggiungere
     * @param country territorio sul quale aggiungerle
     * @return
     */
    public boolean reinforce(Country country, int nArmies) {
        if (activePlayer.getBonusArmies() - nArmies >= 0) {
            activePlayer.decrementBonusArmies(nArmies);
            country.addArmies(nArmies);
            return true;
        }
        return false;
    }

    /*
        cambia la fase (controlla se sei in rifornimento che abbia messo tutti i bonus) se è l'ultima chiama passTurn
        @author Carolina
     */
    public void nextPhase() {
        //TODO
    }

    /**
     * chiama la funzione map.computeBonusArmies passa il turno del giocatore se
     * non c'è niente in sospeso (tipo assegnazione armate o altro) aggiorna
     * active player e rimette la fase alla fase 0
     *
     * @param player il giocatore che vuole passare il turno
     * @author Carolina
     */
    public void passTurn() {
        //TODO
    }

    /**
     * controlla se il giocatore ha vinto
     *
     * @author Carolina
     */
    public void hasWon(Player player) {
        //TODO
    }

    /*
        crea funcioni che riprendono le cose fatte nella mappa controlAttacker, controlDefender, getMaxArmies
     */
    public boolean controlAttacker(Country country) {
        return map.controlAttacker(country, activePlayer);
    }

    public boolean controlPlayer(Country country) {
        return map.controlPlayer(country, activePlayer);
    }

    /**
     * Controlla che il territorio non sia dell'active player e che sia un
     * confinante dell'attacker
     */
    public boolean controlDefender(Country attacker, Country defender) {
        return map.controlDefender(attacker, defender, activePlayer);
    }

    /*
        Ridà il massimo numero di armate per lo spinner rispetto al tipo di country
     */
    public int getMaxArmies(Country country, boolean isAttacker) {
        return map.getMaxArmies(country, isAttacker);
    }

    /*
        Metodi per dare info
    
     */
 /*
        Ridà i contry per i combo
     */
    public Country[] getCountryList() {
        return (Country[]) map.getCountriesList().toArray();
    }

    /*
        ridà le info da metter nel text area
     */
    public Map<Country, Player> getCountryPlayer() {
        return map.getCountryPlayer();
    }

    /*
        ridà l'active player e la fase del gioco
     @author Federico
     */
    public String getInfo() {
        //TODO
        return null;
    }
//    public void play() {
//
//        int counter = 0;
//        while (this.winner == null) {
//            playNextTurn();
//        }
//        System.out.println("Winner:"+players.indexOf(winner));
//    }
//
//    public void playNextTurn() {
//        
//        winner = (map.checkIfWinner(activePlayer)) ? activePlayer : null;
//        
//        // Setto come giocatoreAttivo il prossimo giocatore
//        nextTurn();
//
//        // Fase di rinforzo : 
//        reinforce(activePlayer);
//        
//        // Fase di attacco : 
//        while (activePlayer.wants2Attack() && map.playerCanAttack(activePlayer) && winner==null) {
//            performAttackPhase();
//        }
//
//    }
//
//    private void performAttackPhase() {
//        Country[] countries = activePlayer.chooseFightingCountries();
//        //Country[] countries = map.getFightingCountries(activePlayer);   //ANDREA, nemmeno a me convince molto il megaGiro dell'istruzione precedente, con l'istruzione della riga corrente, si dovrebbero ottenere gli stessi risultati saltando un paio di passaggi.
//
//        if (countries != null) {
//            Player defendPlayer = map.getPlayerFromCountry(countries[1]);
//            int nrA = activePlayer.chooseNrArmies('a', countries[0]);
//            int nrD = defendPlayer.chooseNrArmies('d', countries[1]);
//
//            boolean conquered = attack(countries, nrA, nrD);
//            if (conquered) {
//                //System.out.println("Conquistato:"+countries[1].getName()+"da "+players.indexOf(activePlayer));
//                map.updateOnConquer(countries, nrA);
//                winner = (map.checkIfWinner(activePlayer)) ? activePlayer : null;
//            }
//        } else {
//            playNextTurn();
//        }
//    }
//
//    /**
//     * Chiede alla mappa di _perform_ la fase di rinforzo.
//     *
//     */
//    private void reinforce(Player activePlayer) {
//        map.reinforce(activePlayer);
//    }
//
//    /**
//     * Setta come activePlayer il successivo nel giro.
//     *
//     * @author Federico
//     */
//    private void nextTurn() {
//
//        ListIterator<Player> iter = players.listIterator(players.indexOf(activePlayer) + 1);
//
//        if (iter.hasNext()) {
//            activePlayer = iter.next();
//        } else {
//            activePlayer = players.get(0);
//        }
//    }
//
//    /**
//     * Delega alla mappa di gettare le fighting countries del giocatore
//     *
//     * @param player il giocatore di turno
//     * @return array di 2 countries, country[0] atttaccante, [1] attaccato
//     */
//    Country[] getFightingCountries(Player player){
//        return map.getFightingCountries(player);
//    }
//
//    /**
//     * @param countries
//     * @param nrA
//     * @param nrD
//     * @return true se il territorio è stato conquistato
//     * @author Andrea
//     */
//    public boolean attack(Country[] countries, int nrA, int nrD) {
//        int armiesLost[] = computeAttackResult(nrA, nrD);
//        countries[0].removeArmies(armiesLost[0]);
//        countries[1].removeArmies(armiesLost[1]);
//        return map.isConquered(countries[1]);
//    }
//
//    /**
//     * @return array da 2 elementi, il primo valore è il numero di armate perse
//     * dall'attaccante, il secondo il numero di armate perse dal difensore.
//     * @author Andrea
//     */
//    public int[] computeAttackResult(int nrA, int nrD) {
//        int resultsDiceAttack[] = rollDice(nrA);
//        int resultsDiceDefens[] = rollDice(nrD);
//        int armiesLost[] = new int[2];
//        int min = (nrA > nrD) ? nrD : nrA;
//        for (int i = 0; i < min; i++) {
//            if (resultsDiceAttack[i] > resultsDiceDefens[i]) {
//                armiesLost[1]++;
//            } else {
//                armiesLost[0]++;
//            }
//        }
//        return armiesLost;
//    }
//
//    /**
//     * @param nrDice numero di dadi da tirare
//     * @return un array[nrDadi]con i risultati del lancio in ordine decrescente!
//     * @author Andrea
//     */
//    private int[] rollDice(int nrDice) {
//        int dices[] = new int[nrDice];
//        int tmp;
//        for (int i = 0; i < nrDice; i++) {
//            dices[i] = rollDice();
//        }
//        Arrays.sort(dices);             //ANDREA, pultroppo non ho trovato un metodo di libreria per ordinare in modo decrescente dei tipi di dati primitivi
//        if (nrDice > 1) {
//            tmp = dices[0];
//            dices[0] = dices[nrDice - 1];
//            dices[nrDice - 1] = tmp;
//        }
//        return dices;
//    }
//
//    /**
//     * @return un numero random da 1 a 6
//     * @author Andrea
//     */
//    private int rollDice() {
//        return (int) (Math.random() * 6) + 1;
//    }
//
//    public RisikoMap getRisikoMap() {
//        return this.map;
//    }

}
