package org.craft.demo.board;

import org.craft.demo.constant.Symbol;

import java.util.Optional;

public class Cell {
    private int cellNumber;
    private Optional<Symbol> symbol;

    public Cell(int cellNumber) {
       this.cellNumber = cellNumber;
       this.symbol = Optional.empty();
    }

    public void place(Symbol symbol) {
        this.symbol = Optional.of(symbol);
    }

    public Optional<Symbol> getSymbol() {
        return this.symbol;
    }

    public int getCellNumber() {
        return this.cellNumber;
    }

    public boolean isOccupied() {
        return !this.symbol.isEmpty();
    }

}
