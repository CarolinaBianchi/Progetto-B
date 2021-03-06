package controllers;

import gui.startGameGUI.PlayerInfoRow;
import gui.startGameGUI.PlayersPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;

/**
 * Listens to the click of the remove button. When the button is clicked, it
 * removes a player row from the players panel.
 */
public class RemovePlayerListener implements ActionListener {

    private List<PlayerInfoRow> players;
    private PlayersPanel playersPanel;

    /**
     * Creates a new RemovePlayerListener.
     *
     * @param playersPanel
     * @param players
     */
    public RemovePlayerListener(PlayersPanel playersPanel, List<PlayerInfoRow> players) {
        this.players = players;
        this.playersPanel = playersPanel;
    }

    /**
     * Removes a row from the panel of the players.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        PlayerInfoRow player = getPlayerByButton(source);
        players.remove(player);
        playersPanel.removePlayer(player);
    }

    /**
     * Returns the row to which the button belongs.
     *
     * @param removeButton
     * @return
     */
    private PlayerInfoRow getPlayerByButton(JButton removeButton) {
        for (PlayerInfoRow player : players) {
            if (removeButton.equals(player.getRemoveButton())) {
                return player;
            }
        }
        return null;
    }

}
