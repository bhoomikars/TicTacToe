package org.craft.demo.board;

import org.craft.demo.model.Player;
import org.craft.demo.constant.Symbol;

import java.util.Optional;

class BoardState {
    private static final int MAX_NUMBER_OF_MOVES = 9;

    private boolean isGameOver;
    private Cell[][] matrix;
    private Player human;
    private Player bot;
    private Optional<Player> winner;
    private Player currentTurnPlayer;
    private int numberOfMoves;

    BoardState(Cell[][] matrix, Player bot) {
        this.matrix = matrix;
        this.winner = Optional.empty();
        this.bot = new Player("COMPUTER");
        this.isGameOver = false;
        this.numberOfMoves = 0;
    }

    public void setHuman(Player human) {
        this.human = human;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Player getInvertedPlayer(Player player) {
        if (player == human) {
            return bot;
        }
        return human;
    }

    public void changeTurn() {
        this.currentTurnPlayer = getInvertedPlayer(this.currentTurnPlayer);
    }

    public void incrementMoves() {
        this.numberOfMoves++;
    }

    boolean canContinue() {
        return this.numberOfMoves != MAX_NUMBER_OF_MOVES && this.winner.isEmpty();
    }

    public Cell[][] getMatrix() {
        return matrix;
    }

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public boolean isBotTurn() {
        return this.bot == this.currentTurnPlayer;
    }

    public Player getBot() {
        return bot;
    }

    public Player getHuman() {
        return human;
    }

    public void setHumanTurn() {
        this.currentTurnPlayer = this.human;
    }

    public void setBotTurn() {
        this.currentTurnPlayer = this.bot;
    }

    public void setBotSymbol(Symbol symbol) {
        this.bot.setSymbol(symbol);
    }

    public void setHumanSymbol(Symbol symbol) {
        this.human.setSymbol(symbol);
    }

    public void setWinner(Optional<Player> winner) {
        this.winner = winner;
    }

    public void setGameOver() {
        this.isGameOver = true;
    }

    public Optional<Player> getWinner() {
        return winner;
    }
}
