package org.craft.demo.rule;


import org.craft.demo.board.Cell;
import org.craft.demo.constant.Symbol;

@FunctionalInterface
public interface PlacementStrategy {
    int getNextMove(Cell[][] matrix, Symbol symbol);
}
