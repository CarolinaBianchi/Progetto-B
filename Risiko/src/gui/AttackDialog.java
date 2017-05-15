package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import risiko.Game;
import utils.PlayAudio;

public class AttackDialog extends JDialog {

    private Game game;
    private JPanel dialogPanel;
    private JLabel attackText;
    private JLabel defenseText;
    private SpinnerNumberModel attackerModel;
    private SpinnerNumberModel defenserModel;
    private JSpinner attackerArmies;
    private JSpinner defenderArmies;
    private JButton execute;
    private JLabel dice1R;
    private JLabel dice2R;
    private JLabel dice3R;
    private JLabel dice1B;
    private JLabel dice2B;
    private JLabel dice3B;
    private JLabel emptyLabel;
    private boolean isConquered;
    private boolean canAttackFromCountry;
    private int maxArmiesAttacker;
    private int[] attackerDice;
    private int[] defenderDice;
    private String defenderCountryName;
    private int maxArmiesDefender;

    public void setMaxArmiesAttacker(int maxArmiesAttacker) {
        this.maxArmiesAttacker = maxArmiesAttacker;
    }

    public void setMaxArmiesDefender(int maxArmiesDefender) {
        this.maxArmiesDefender = maxArmiesDefender;
    }

    public AttackDialog(Game game) {
        this.game = game;
        init();
    }

    private void init() {

        JDialog inputArmies = this;
        dialogPanel = new JPanel(new GridLayout(0, 2));
        attackText = new JLabel(" n armate attaccco");
        defenseText = new JLabel(" n armate difesa");
        attackerModel = new SpinnerNumberModel(1, 1, 1, 1);
        defenserModel = new SpinnerNumberModel(1, 1, 1, 1);
        attackerArmies = new JSpinner(attackerModel);
        defenderArmies = new JSpinner(defenserModel);
        execute = new JButton("Esegui");
        dice1R = new JLabel();
        dice2R = new JLabel();
        dice3R = new JLabel();
        dice1B = new JLabel();
        dice2B = new JLabel();
        dice3B = new JLabel();
        emptyLabel = new JLabel();
        JLabel[] diceR = new JLabel[]{dice1R, dice2R, dice3R};
        JLabel[] diceB = new JLabel[]{dice1B, dice2B, dice3B};
        attackerDice = new int[]{};
        defenderDice = new int[]{};

        execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                String imagePath = "files/images/dice/";
                PlayAudio.play("sounds/tank.wav");
                
                //game.attack() potrebbe impostare a true il valore ritornante da game.haveJustDrowCard()
                //quindi occorre salvare il valore ritornante da game.haveJustDrowCard() prima di invocare game.attack() 
                //useremo la variabile haveJustDrowCard nel caso isConquered fosse true.
                boolean haveJustDrowCard = game.haveJustDrowCard();
                
                game.attack((int) attackerArmies.getValue(), (int) defenderArmies.getValue());
                for (int i = 0; i < diceR.length; i++) {
                    if (i < attackerDice.length) {
                        ImageIcon icon = new ImageIcon(imagePath + attackerDice[i] + "R.png");
                        diceR[i].setIcon(icon);
                    } else {
                        ImageIcon icon = null;
                        diceR[i].setIcon(icon);
                    }
                }
                for (int i = 0; i < diceB.length; i++) {
                    if (i < defenderDice.length) {
                        ImageIcon icon = new ImageIcon(imagePath + defenderDice[i] + "B.png");
                        diceB[i].setIcon(icon);
                    } else {
                        ImageIcon icon = null;
                        diceB[i].setIcon(icon);
                    }
                }

                if (isConquered) {
                    PlayAudio.play("sounds/conquest.wav");
                    
                    //Informa il player della conquista del nuovo territorio, 
                    //nel caso fosse la prima conquista del turno informa anche il player della cardBonus pescata
                    if (haveJustDrowCard) {
                        JOptionPane.showMessageDialog(null, "Complimenti, hai conquistato " + getDefenderCountryName());
                    } else {
                        Image imageDrowedCard = game.getLastCardBonusDrowed();
                        JOptionPane.showMessageDialog(  null, "Complimenti,\nhai conquistato " + getDefenderCountryName()
                                + ",\ne pescato questa carta.", "Conquered",
                                JOptionPane.INFORMATION_MESSAGE, new ImageIcon(imageDrowedCard));
                    }
                    inputArmies.setVisible(false);
                    for (int i = 0; i < diceR.length; i++) {
                        diceR[i].setIcon(null);
                        diceB[i].setIcon(null);
                    }
                    return;
                }
                if (!canAttackFromCountry) {
                    JOptionPane.showMessageDialog(null, "Non è più possibile effettuare attacchi da questo territorio.");
                    inputArmies.setVisible(false);
                    for (int i = 0; i < diceR.length; i++) {
                        diceR[i].setIcon(null);
                        diceB[i].setIcon(null);
                    }
                    return;
                }
                // Seconda volta in cui attacco 
                attackerArmies.setModel(new SpinnerNumberModel(maxArmiesAttacker, 1, maxArmiesAttacker, 1));
                defenderArmies.setModel(new SpinnerNumberModel(maxArmiesDefender, 1, maxArmiesDefender, 1));
            }

        });

        dialogPanel.add(attackText);
        dialogPanel.add(defenseText);
        dialogPanel.add(attackerArmies);
        dialogPanel.add(defenderArmies);
        dialogPanel.add(execute);
        dialogPanel.add(emptyLabel);
        dialogPanel.add(dice1R);
        dialogPanel.add(dice1B);
        dialogPanel.add(dice2R);
        dialogPanel.add(dice2B);
        dialogPanel.add(dice3R);
        dialogPanel.add(dice3B);
        inputArmies.add(dialogPanel);
        inputArmies.setModal(true);
        inputArmies.setSize(600, 300);
    }

    public void setMaxArmies(int maxArmiesAttacker, int maxArmiesDefender) {

        attackerArmies.setModel(new SpinnerNumberModel(maxArmiesAttacker, 1, maxArmiesAttacker, 1));
        defenderArmies.setModel(new SpinnerNumberModel(maxArmiesDefender, 1, maxArmiesDefender, 1));
    }

    public void setIsConquered(boolean isConquered) {
        this.isConquered = isConquered;
    }

    public void setCanAttackFromCountry(boolean canAttackFromCountry) {
        this.canAttackFromCountry = canAttackFromCountry;
    }

    public void updateDice(int[] attackerDice, int[] defenderDice) {
        this.attackerDice = attackerDice;
        this.defenderDice = defenderDice;
    }

    public void setDefenderCountryName(String defenderCountryName) {
        this.defenderCountryName = defenderCountryName;
    }

    private String getDefenderCountryName() {
        return defenderCountryName;
    }
}
