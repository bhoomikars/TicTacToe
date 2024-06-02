package org.craft.demo.board;

import org.craft.demo.exception.CellOccupiedException;
import org.craft.demo.exception.GameOverException;
import org.craft.demo.model.Player;
import org.craft.demo.view.PresentationLayer;
import org.craft.demo.rule.RuleEngine;
import org.craft.demo.constant.Symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;


public class Board {
    private static final int MAX_SIZE = 3;

    private Map<Integer, Cell> cellByNumber;
    private BoardState state;
    private RuleEngine ruleEngine;
    private PresentationLayer presentationLayer;

    public Board(RuleEngine ruleEngine) {
        this.cellByNumber = new HashMap<>();
        this.state = new BoardState(initMatrix(), new Player("COMPUTER"));
        this.ruleEngine = ruleEngine;
        this.presentationLayer = new PresentationLayer();
    }

    public void registerPlayer(String name) {
        this.state.setHuman(new Player(name));
    }

    public boolean isGameOver() {
        return this.state.isGameOver();
    }

    public void callToss() {
        if (this.state.isGameOver()) {
            throw new RuntimeException("Game is already over");
        }
        final Random random = new Random();
        boolean playerWins = random.nextInt(2) == 1;
        System.out.println("Conducting toss between 2 players");
        if (playerWins) {
            this.state.setHumanSymbol(Symbol.X);
            this.state.setBotSymbol(Symbol.O);
            this.state.setHumanTurn();
            this.presentationLayer.printTossResults(this.state.getHuman(), this.state.getBot());
        } else {
            this.state.setBotSymbol(Symbol.X);
            this.state.setHumanSymbol(Symbol.O);
            this.state.setBotTurn();
            this.presentationLayer.printTossResults(this.state.getBot(), this.state.getHuman());
        }

        this.presentationLayer.displayMatrix(this.state.getMatrix());

        if (this.state.isBotTurn()) {
            this.playBot();
        }
    }

    public void playTurn(int cellNumber) {
        if (this.state.isGameOver()) {
            throw new GameOverException("Game is already over");
        }

        System.out.println("Placing " + this.state.getCurrentTurnPlayer().getSymbol() + " in " + cellNumber + " for " + this.state.getCurrentTurnPlayer().getName());

        place(cellNumber);

        this.presentationLayer.displayMatrix(this.state.getMatrix());

        if (this.ruleEngine.evaluateWin(this.state.getMatrix(), this.state.getCurrentTurnPlayer().getSymbol())) {
            this.state.setWinner(Optional.of(this.state.getCurrentTurnPlayer()));
        }

        if (!this.state.canContinue()) {
            this.state.setGameOver();
            this.presentationLayer.printResults(this.state.getWinner());
            return;
        }

        this.state.changeTurn();

        if (this.state.isBotTurn()) {
            this.playBot();
        }
    }

    public void place(int cellNumber) {
        Cell cell = this.cellByNumber.get(cellNumber);
        if (cell.isOccupied()) {
            throw new CellOccupiedException("cell is already occupied");
        }
        cell.place(this.state.getCurrentTurnPlayer().getSymbol());
        this.state.incrementMoves();
    }

    private void playBot() {
        System.out.println("Player " + this.state.getBot().getName() + "s turn ");

        BiFunction<Cell[][], Symbol, Integer> botWinStrategy = this.ruleEngine::getWinningMove;
        BiFunction<Cell[][], Symbol, Integer> botDefenseAgainstOppositionWin = this.ruleEngine::getWinningMove;
        BiFunction<Cell[][], Symbol, Integer> botNextSmartMove = this.ruleEngine::getNextSmartMove;

        int nextCell = botWinStrategy.
                andThen(botWinningCell -> {
                    if (botWinningCell != -1) {
                        return botWinningCell;
                    }
                    return botDefenseAgainstOppositionWin.apply(this.state.getMatrix(), this.state.getInvertedPlayer(this.state.getCurrentTurnPlayer()).getSymbol());
                })
                .andThen(defendCell -> {
                    if (defendCell != -1) {
                        return defendCell;
                    }
                    return botNextSmartMove.apply(this.state.getMatrix(), this.state.getCurrentTurnPlayer().getSymbol());
                })
                .apply(this.state.getMatrix(), this.state.getCurrentTurnPlayer().getSymbol());


        if (nextCell != -1) {
            this.playTurn(nextCell);
        }
    }


    private Cell[][] initMatrix() {
        int counter = 1;
        Cell[][] board = new Cell[MAX_SIZE][MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++) {
            for (int j = 0; j < MAX_SIZE; j++) {
                board[i][j] = new Cell(counter);
                this.cellByNumber.put(counter, board[i][j]);
                counter++;
            }
        }
        return board;
    }
}
