package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

class MockFloor implements FloorInterface {
    public ArrayList<Tile> floorTiles;

    public MockFloor() {
        floorTiles = new ArrayList<>();
    }

    @Override
    public void put(Collection<Tile> incomingTiles) {
        this.floorTiles.addAll(incomingTiles);
    }

    @Override
    public String state() {
        String toReturn = "";
        for (final Tile tile : floorTiles) {
            toReturn += tile.toString();
        }
        return toReturn;
    }

    @Override
    public Points finishRound() {
        return null;
    }
}

class MockWallLine implements WallLineInterface {
    public ArrayList<Optional<Tile>> wallLineTiles;

    public MockWallLine() {
        wallLineTiles = new ArrayList<>();
    }

    @Override
    public boolean canPutTile(Tile tile) {
        return !wallLineTiles.contains(tile);
    }

    @Override
    public ArrayList<Optional<Tile>> getTiles() {
        return wallLineTiles;
    }

    @Override
    public Points putTile(Tile tile) {
        wallLineTiles.add(Optional.ofNullable(tile));
        return new Points(10);
    }

    @Override
    public void setLineUp(WallLine wallLine) {

    }

    @Override
    public void setLineDown(WallLine wallLine) {

    }

    @Override
    public String state() {
        return null;
    }
}

public class PatternLineUnitTest {
    private FakeUsedTiles usedTiles;
    private MockFloor mockFloor;
    private MockWallLine mockWallLine;
    private PatternLine testPatternLine;

    @Before
    public void initialize() {
        usedTiles = new FakeUsedTiles();
        mockWallLine = new MockWallLine();
        mockFloor = new MockFloor();
        testPatternLine = new PatternLine(4, mockWallLine, mockFloor, usedTiles);
    }

    @Test
    public void verifyPatternLineOperations() {
        assertEquals("New PatternLine should be empty.", "", testPatternLine.state());
        testPatternLine.put(List.of(Tile.RED, Tile.RED));
        String currentState = testPatternLine.state();
        assertEquals("PatternLine should now contain 'RR'.", "RR", testPatternLine.state());
        testPatternLine.put(List.of(Tile.GREEN, Tile.GREEN));
        assertEquals("PatternLine should not add 'GG' when 'RR' is present.", currentState, testPatternLine.state());

        String floorState = "";
        for (Tile tile : mockFloor.floorTiles) floorState += tile.toString();
        assertEquals("Tiles not added to PatternLine should go to Floor.", "GG", floorState);

        testPatternLine.finishRound();
        assertEquals("PatternLine should remain unchanged if not fully filled.", currentState, testPatternLine.state());

        testPatternLine.put(List.of(Tile.RED, Tile.RED, Tile.RED));
        assertEquals("PatternLine should now be fully filled with 'RRRR'.", "RRRR", testPatternLine.state());

        floorState = "";
        for (Tile tile : mockFloor.floorTiles) floorState += tile.toString();
        assertEquals("Overflow tiles should be added to Floor.", "GGR", floorState);

        testPatternLine.finishRound();
        assertEquals("PatternLine should be empty after finishRound().", "", testPatternLine.state());
    }
}
