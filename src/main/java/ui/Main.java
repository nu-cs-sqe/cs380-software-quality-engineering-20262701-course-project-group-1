package ui;

import domain.GameManager;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        TerminalPrinting printer = new TerminalPrinting(System.out);
        Scanner in = new Scanner(System.in);
        new GameLoop(gameManager, printer, in).run();
    }
}
