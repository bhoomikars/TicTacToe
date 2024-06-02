package org.craft.demo;

import org.craft.demo.board.Board;
import org.craft.demo.factory.RuleFactory;
import org.craft.demo.rule.RuleEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        RuleEngine ruleEngine = new RuleEngine(RuleFactory.getWinningRules(), RuleFactory.getWinningStrategies(), RuleFactory.getSmartMovementStrategies());
        Board board = new Board(ruleEngine);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Starting Game");
        System.out.println("Hello Human Player, Please enter your name");

        try {
            board.registerPlayer(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        board.callToss();

        while (!board.isGameOver()) {
            System.out.println("Player's turn ");
            try {
                board.playTurn(Integer.parseInt(reader.readLine()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}