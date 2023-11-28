package sk.uniba.fmph.dcs;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardIntegrationTest {

    private Board board;
    private FloorInterface floor;
    private ArrayList<Points> points;
    private List<PatternLineInterface> patternLines;
    private List<WallLineInterface> wallLines;

    @Before
    public void setUp() {
        // Floor, points.
        UsedTiles usedTiles = new UsedTiles();
        ArrayList<Points> pointPattern = new ArrayList<>(Arrays.asList(new Points(-1), new Points(-1), new Points(-2), new Points(-2), new Points(-2), new Points(-3), new Points(-3)));

        floor = new Floor(usedTiles, pointPattern);
        points = new ArrayList<>();

        // Wall lines.
        LinkedList<Tile> tileTypes1 = new LinkedList<>(Arrays.asList(Tile.BLUE, Tile.YELLOW, Tile.RED, Tile.GREEN, Tile.BLACK));
        LinkedList<Tile> tileTypes2 = new LinkedList<>(Arrays.asList(Tile.BLACK, Tile.BLUE, Tile.YELLOW, Tile.RED, Tile.GREEN));
        LinkedList<Tile> tileTypes3 = new LinkedList<>(Arrays.asList(Tile.GREEN, Tile.BLACK, Tile.BLUE, Tile.YELLOW, Tile.RED));
        LinkedList<Tile> tileTypes4 = new LinkedList<>(Arrays.asList(Tile.RED, Tile.GREEN, Tile.BLACK, Tile.BLUE, Tile.YELLOW));
        LinkedList<Tile> tileTypes5 = new LinkedList<>(Arrays.asList(Tile.YELLOW, Tile.RED, Tile.GREEN, Tile.BLACK, Tile.BLUE));

        WallLine wallLine1 = new WallLine(tileTypes1, null, null);
        WallLine wallLine2 = new WallLine(tileTypes2, null, null);
        WallLine wallLine3 = new WallLine(tileTypes3, null, null);
        WallLine wallLine4 = new WallLine(tileTypes4, null, null);
        WallLine wallLine5 = new WallLine(tileTypes5, null, null);

        wallLine1.setLineDown(wallLine2);
        wallLine2.setLineDown(wallLine3);
        wallLine3.setLineDown(wallLine4);
        wallLine4.setLineDown(wallLine5);
        wallLine5.setLineUp(wallLine4);
        wallLine4.setLineUp(wallLine3);
        wallLine3.setLineUp(wallLine2);
        wallLine2.setLineUp(wallLine1);

        wallLines = Arrays.asList(wallLine1, wallLine2, wallLine3, wallLine4, wallLine5);
        // Pattern lines.
        patternLines = Arrays.asList(new PatternLine(1,wallLine1 , floor,usedTiles ), new PatternLine(2, wallLine2 , floor,usedTiles), new PatternLine(3, wallLine3 , floor,usedTiles), new PatternLine(4,wallLine4 , floor,usedTiles), new PatternLine(5, wallLine5 , floor,usedTiles));

        // Board.
        FinalPointsCalculation finalPointsCalculation = new FinalPointsCalculation();
        GameFinished gameFinished = new GameFinished();

        board = new Board(floor, points, patternLines, wallLines, finalPointsCalculation, gameFinished);
    }

    @Test
    public void testBoard() {
        // Round 1.
        board.put(0, new ArrayList<>(List.of(Tile.YELLOW)));           // One tile is on the floor.
        board.put(1, new ArrayList<>(Arrays.asList(Tile.BLACK, Tile.BLACK, Tile.BLACK)));
        board.put(2, new ArrayList<>(Arrays.asList(Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE)));
        board.put(3, new ArrayList<>(Arrays.asList(Tile.RED, Tile.RED)));
        board.put(4, new ArrayList<>(Arrays.asList(Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE)));

        String expectedState = """
                Pattern Lines:
                I
                LL
                BBB
                RR
                BBBBB
                Wall Lines:
                  
                  
                  
                  
                  
                Floor:
                LB
                Points[value=0]
                """;
        assertEquals(expectedState, board.state());

        assertEquals(FinishRoundResult.NORMAL, board.finishRound());

        expectedState = """
               Pattern Lines:
               
               
               
               RR
               
               Wall Lines:
               I
               L
               B
               
               B
               Floor:
               
               Points[value=2]
                """;
        assertEquals(expectedState, board.state());

        // Round 2.
        board.put(0, new ArrayList<>(List.of(Tile.BLACK)));
        board.put(1, new ArrayList<>(Arrays.asList(Tile.RED, Tile.RED)));
        board.put(2, new ArrayList<>(Arrays.asList(Tile.BLACK, Tile.BLACK, Tile.BLACK)));
        board.put(3, new ArrayList<>(Arrays.asList(Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE)));
        board.put(4, new ArrayList<>(Arrays.asList(Tile.YELLOW, Tile.YELLOW, Tile.YELLOW, Tile.YELLOW, Tile.YELLOW)));

        expectedState = """
                Pattern Lines:
                L
                RR
                LLL
                RR
                IIIII
                Wall Lines:
                I
                L
                B
                                
                B
                Floor:
                BBBB
                Points[value=2]            
                """;
        assertEquals(expectedState, board.state());

        assertEquals(FinishRoundResult.NORMAL, board.finishRound());

        expectedState = """
                Pattern Lines:
                
                
                
                RR
                
                Wall Lines:
                IL
                LR
                LB
                
                IB
                Floor:
                
                Points[value=1]
                """;
        assertEquals(expectedState, board.state());

        // Round 3... lets skip ahead a little.
        board.put(0, new ArrayList<>(List.of(Tile.GREEN)));

        assertEquals(FinishRoundResult.NORMAL, board.finishRound());
        assertEquals(4, board.getPoints().getValue());

        // Round 4.
        board.put(0, new ArrayList<>(List.of(Tile.RED)));

        assertEquals(FinishRoundResult.NORMAL, board.finishRound());
        assertEquals(8, board.getPoints().getValue());

        // Round 5.
        board.put(0, new ArrayList<>(List.of(Tile.BLUE)));

        // First row is now full, which means end of game.
        assertEquals(FinishRoundResult.GAME_FINISHED, board.finishRound());
        // The final points should be 33.
        // assertEquals(33, board.getPoints().getValue());
    }
}