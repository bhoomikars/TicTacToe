package org.craft.demo.view;

import org.craft.demo.board.Cell;
import org.craft.demo.model.Player;

import java.util.Optional;

public class PresentationLayer {
    public PresentationLayer() {
    }

    public void printTossResults(Player tossWinner, Player loser) {
        System.out.println("Player " + tossWinner.getName() + " won the toss");
        System.out.println("First player is " + tossWinner.getName());
        System.out.println("Second player is " + loser.getName());
    }

    public void printResults(Optional<Player> winner) {
        if (!winner.isEmpty()) {
            System.out.println("Player " + winner.get().getName() + " won the game!");
            return;
        }
        System.out.println("Match draw!");
    }

    public void displayMatrix(Cell[][] matrix) {
        System.out.println("\n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j].isOccupied()) {
                    System.out.print(matrix[i][j].getSymbol().get() + " ");
                } else {
                    System.out.print(matrix[i][j].getCellNumber() + " ");
                }
            }
            System.out.println();
        }
    }
}
