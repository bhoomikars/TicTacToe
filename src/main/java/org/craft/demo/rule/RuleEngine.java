package org.craft.demo.rule;

import org.craft.demo.board.Cell;
import org.craft.demo.constant.Symbol;

import java.util.List;

public class RuleEngine {
    private List<Rule> winningRules;
    private List<PlacementStrategy> winningStrategies;
    private List<PlacementStrategy> movementStrategies;

    public RuleEngine(List<Rule> winningRules, List<PlacementStrategy> winningStrategies, List<PlacementStrategy> movementStrategy) {
        this.winningRules = winningRules;
        this.winningStrategies = winningStrategies;
        this.movementStrategies = movementStrategy;
    }

    public boolean evaluateWin(Cell[][] matrix, Symbol symbol) {
        return this.winningRules
                .stream()
                .anyMatch(rule -> rule.evaluate(matrix, symbol));
    }

    public int getWinningMove(Cell[][] matrix, Symbol symbol) {
        return this.winningStrategies
                .stream()
                .map(strategy -> strategy.getNextMove(matrix, symbol))
                .filter(result -> result != -1)
                .findFirst()
                .orElse(-1);
    }

    public int getNextSmartMove(Cell[][] matrix, Symbol symbol) {
        return this.movementStrategies
                .stream()
                .map(strategy -> strategy.getNextMove(matrix, symbol))
                .filter(cell -> cell != -1)
                .findFirst()
                .orElse(-1);
    }

}
