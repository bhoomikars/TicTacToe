package org.craft.demo.factory;

import org.craft.demo.board.Cell;
import org.craft.demo.constant.Symbol;
import org.craft.demo.rule.PlacementStrategy;
import org.craft.demo.rule.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class RuleFactory {
    private static final int MAX_SIZE = 3; 

    private static List<Rule> winningRules;
    private static List<PlacementStrategy> winningStrategies;
    private static List<PlacementStrategy> smartMovementStrategies;

    static {
        winningRules = buildWinningRules();
        winningStrategies = buildWinningStrategies();
        smartMovementStrategies = buildSmartMovementStrategies();
    }

    public static List<Rule> getWinningRules() {
        return winningRules;
    }

    public static List<PlacementStrategy> getWinningStrategies() {
        return winningStrategies;
    }

    public static List<PlacementStrategy> getSmartMovementStrategies() {
        return smartMovementStrategies;
    }

    private static boolean checkForMatch(Cell[][] matrix, Symbol winningSymbol, boolean isRowWise) {
        for (int i = 0; i < MAX_SIZE; i++) {
            int matched = 0;
            for (int j = 0; j < MAX_SIZE; j++) {
                Cell cell = isRowWise ? matrix[i][j] : matrix[j][i];
                if (!cell.isOccupied() || !cell.getSymbol().get().equals(winningSymbol)) {
                    break;
                }
                matched++;
            }

            if (matched == MAX_SIZE) {
                return true;
            }
        }

        return false;
    }

    private static boolean diagonalMatch(Cell[][] matrix, Symbol winningSymbol, boolean leftToRightDiagonal) {
        // left to right diagonal
        int matched = 0;
        for (int i = 0; i < MAX_SIZE; i++) {
            Cell cell;
            if (leftToRightDiagonal) {
                cell = matrix[i][i];
            } else {
                cell = matrix[i][MAX_SIZE - 1 - i];
            }
            if (!cell.isOccupied() || !cell.getSymbol().get().equals(winningSymbol)) {
                break;
            }
            matched++;
        }

        return matched == MAX_SIZE;
    }
    
    private static int getWinningMoveStraight(Cell[][] matrix, Symbol symbol, boolean rowWise) {
        for (int i = 0; i < MAX_SIZE; i++) {
            int matched = 0, nextCell = -1;
            for (int j = 0; j < MAX_SIZE; j++) {
                Cell cell = rowWise ? matrix[i][j] : matrix[j][i];
                if (!cell.isOccupied()) {
                    nextCell = cell.getCellNumber();
                    continue;
                }
                if (cell.getSymbol().get().equals(symbol)) {
                    matched++;
                }
            }

            if (matched == MAX_SIZE - 1 && nextCell != -1) {
                return nextCell;
            }
        }
        return -1; 
    }
    
    private static int getWinningMoveDiagonally(Cell[][] matrix, Symbol symbol, boolean leftToRight) {
        int matched = 0, nextCell = -1;
        for (int i = 0; i < 3; i++) {
            Cell cell = leftToRight ? matrix[i][i] : matrix[i][MAX_SIZE - 1 - i]; 
            if (!cell.isOccupied()) {
                nextCell = cell.getCellNumber();
                continue;
            }
            if (cell.getSymbol().get().equals(symbol)) {
                matched++;
            }
        }

        if (matched == MAX_SIZE - 1 && nextCell != -1) {
            return nextCell;
        }
        
        return -1;
    }


    private static List<Rule> buildWinningRules() {
        Rule columnMatch = (matrix, symbol) -> checkForMatch(matrix, symbol, false);
        Rule rowMatch = (matrix, symbol) -> checkForMatch(matrix, symbol, true);
        Rule leftToRightDiagonalMatch = (matrix, symbol) -> diagonalMatch(matrix, symbol, true);
        Rule rightToDiagonalMatch = (matrix, symbol) -> diagonalMatch(matrix, symbol, false);
        return Arrays.asList(rowMatch, columnMatch, leftToRightDiagonalMatch, rightToDiagonalMatch);
    }
    
    private static List<PlacementStrategy> buildWinningStrategies() {
        PlacementStrategy rowWinningPlacement = (matrix, symbol) -> getWinningMoveStraight(matrix, symbol, true);
        PlacementStrategy columnWinningPlacement = (matrix, symbol) -> getWinningMoveStraight(matrix, symbol, false);
        PlacementStrategy leftToRightDiagonalWinningPlacement = (matrix, symbol) -> getWinningMoveDiagonally(matrix, symbol, true);
        PlacementStrategy rightToLeftDiagonalWinningPlacement = (matrix, symbol) -> getWinningMoveDiagonally(matrix, symbol, false);
        return Arrays.asList(rowWinningPlacement, columnWinningPlacement, leftToRightDiagonalWinningPlacement, rightToLeftDiagonalWinningPlacement);
    }

    private static List<PlacementStrategy> buildSmartMovementStrategies() {
        return Arrays.asList(RuleFactory::getFreeCenter, RuleFactory::getAnyFreeCorner, RuleFactory::getAnyFreeCell);
    }

    private static List<Cell> filterMatrix(Cell[][] matrix, Predicate<Cell> predicate) {
        List<Cell> filteredCells = new ArrayList<>();
        for (int i = 0; i < MAX_SIZE; i++) {
            for (int j = 0; j < MAX_SIZE; j++) {
                if (predicate.test(matrix[i][j])) {
                    filteredCells.add(matrix[i][j]);
                }
            }
        }
        return filteredCells;
    }

    private static int getFreeCenter(Cell[][] matrix, Symbol symbol) {
        int center = MAX_SIZE / 2;
        Cell centerCell = matrix[center][center];
        return centerCell.isOccupied() ? -1 : centerCell.getCellNumber();
    }

    private static int getAnyFreeCorner(Cell[][] matrix, Symbol symbol) {
        List<Integer> corners = Arrays.asList(1, 3, 7, 9);
        return filterMatrix(matrix, (cell) -> !cell.isOccupied() && corners.contains(cell.getCellNumber()))
                .stream()
                .findFirst()
                .map(Cell::getCellNumber)
                .orElse(-1);
    }

    private static int getAnyFreeCell(Cell[][] matrix, Symbol symbol) {
        return filterMatrix(matrix, (cell) -> !cell.isOccupied())
                .stream()
                .findFirst()
                .map(Cell::getCellNumber)
                .orElse(-1);
    }
}
