package sk.uniba.fmph.dcs;

import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;

public class GameIntegrationTest {
    private Game game;
    private Bag bag;
    private TableArea tableArea;
    private List tileSources;
    private GameObserver gameObserver;
    private TableCenter tableCenter;
    private int startingPlayerId;
    ArrayList<BoardInterface> boards = new ArrayList<>();

    @Before
    public void setUp() {
        int playerCount = 2;
        List<PatternLineInterface> patternLines;

        ArrayList<Points> pointPattern = new ArrayList<>(Arrays.asList(new Points(-1), new Points(-1), new Points(-2), new Points(-2), new Points(-2), new Points(-3), new Points(-3)));
        UsedTiles usedTiles = new UsedTiles();



        for (int i = 0; i < playerCount; i++) {
            Floor floor = new Floor(usedTiles, pointPattern);
            ArrayList<Points> points = new ArrayList<>();
            List<WallLineInterface> wallLines = new ArrayList<>();
            bag = new Bag(usedTiles);


            ArrayList<LinkedList<Tile>> tileTypesList = new ArrayList<>();
            tileTypesList.add(new LinkedList<>(Arrays.asList(Tile.BLUE, Tile.YELLOW, Tile.RED, Tile.GREEN, Tile.BLACK)));
            tileTypesList.add(new LinkedList<>(Arrays.asList(Tile.BLACK, Tile.BLUE, Tile.YELLOW, Tile.RED, Tile.GREEN)));
            tileTypesList.add(new LinkedList<>(Arrays.asList(Tile.GREEN, Tile.BLACK, Tile.BLUE, Tile.YELLOW, Tile.RED)));
            tileTypesList.add(new LinkedList<>(Arrays.asList(Tile.RED, Tile.GREEN, Tile.BLACK, Tile.BLUE, Tile.YELLOW)));
            tileTypesList.add(new LinkedList<>(Arrays.asList(Tile.YELLOW, Tile.RED, Tile.GREEN, Tile.BLACK, Tile.BLUE)));


            for (int j = 0; j < 5; j++) {
                wallLines.add(new WallLine(tileTypesList.get(j), null, null));
            }

            for (int j = 0; j < 4; j++) {
                wallLines.get(j).setLineUp((WallLine) wallLines.get(j + 1));
            }

            for (int j = 1; j < 5; j++) {
                wallLines.get(j).setLineDown((WallLine) wallLines.get(j - 1));
            }


            patternLines = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                patternLines.add(new PatternLine(j + 1, wallLines.get(j), floor, usedTiles));
            }


            FinalPointsCalculation finalPointsCalculation = new FinalPointsCalculation();
            GameFinished gameFinished = new GameFinished();

            boards.add(new Board(floor, points, patternLines, wallLines, finalPointsCalculation, gameFinished));
        }


        tableCenter = new TableCenter();
        tileSources = new ArrayList<>();
        tileSources.add(tableCenter);
        tileSources.add(new Factory(bag,tableCenter));
        for(int i = 0; i < playerCount; i++){
            tileSources.add(new Factory(bag,tableCenter));
            tileSources.add(new Factory(bag,tableCenter));
        }
        tableArea = new TableArea(tileSources);

        this.gameObserver = new GameObserver();
        ConsoleGameObserver consoleGameObserver = new ConsoleGameObserver();
        gameObserver.registerObserver(consoleGameObserver);


        game = new Game(playerCount, boards, tableArea,bag,gameObserver);

    }

    @Test
    public void testGameEndToEnd() {
        tableArea.startNewRound();
        System.out.println("Start of first round: \n" + tableArea.state());


        game.take(game.getCurrentPlayerId(), 2, 0, 0);
        game.take(game.getCurrentPlayerId(), 4, 0, 0);

        game.take(game.getCurrentPlayerId(), 1, 0, 1);
        game.take(game.getCurrentPlayerId(), 3, 0, 1);

        game.take(game.getCurrentPlayerId(), 5, 2, 2);
        game.take(game.getCurrentPlayerId(), 0, 4, 2);

        game.take(game.getCurrentPlayerId(), 0, 0, 3);
        game.take(game.getCurrentPlayerId(), 0, 3, 3);

        game.take(game.getCurrentPlayerId(), 0, 0, 4);
        game.take(game.getCurrentPlayerId(), 0, 0, 4);

        assertEquals(2, boards.get(0).getPoints().getValue());
        assertEquals(4, boards.get(1).getPoints().getValue());

        System.out.println("After taking from Factory 1 and 2: \n" + boards.get(0).state());
        System.out.println("After taking from Factory 1 and 2: \n" + boards.get(1).state());
        System.out.println("After taking from Factory 1 and 2: \n" + tableArea.state());



        while(!game.isGameOver) {
            for(int i = 5; i > 0 ; i--) {
                game.take(game.getCurrentPlayerId(), i, 0, i-1);
            }
            while (!tableCenter.isEmpty()) {
                game.take(game.getCurrentPlayerId(), 0, 0, 0);
            }
        }

        assertEquals(0, boards.get(0).getPoints().getValue());
        assertEquals(4, boards.get(1).getPoints().getValue());
    }
}