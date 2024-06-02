package org.craft.demo.rule;

import org.craft.demo.board.Cell;
import org.craft.demo.constant.Symbol;

@FunctionalInterface
public interface Rule {
    boolean evaluate(Cell[][] matrix, Symbol symbol);
}
