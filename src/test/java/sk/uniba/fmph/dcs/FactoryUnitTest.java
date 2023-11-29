package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

class MockBag implements BagInterface {
    private ArrayList<Tile> tilePool;

    public MockBag() {
        tilePool = new ArrayList<>();
        tilePool.addAll(List.of(Tile.RED, Tile.GREEN, Tile.BLUE, Tile.RED, Tile.GREEN, Tile.BLUE, Tile.GREEN, Tile.BLUE));
    }

    @Override
    public ArrayList<Tile> take(int numberOfTiles) {
        ArrayList<Tile> selectedTiles = new ArrayList<>();
        for (int i = 0; i < numberOfTiles; i++) {
            selectedTiles.add(tilePool.get(i));
        }
        for (int i = 0; i < numberOfTiles; i++) {
            tilePool.remove(0);
        }
        return selectedTiles;
    }

    @Override
    public String state() {
        return null;
    }
}

class MockTableCenter implements TableCenterInterface {
    public ArrayList<Tile> tileCollection = new ArrayList<>();


    @Override
    public void add(List<Tile> tiles) {
        tileCollection.addAll(tiles);
    }

    @Override
    public List<Tile> take(int idx) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void startNewRound() {

    }

    @Override
    public String state() {
        return null;
    }
}

public class FactoryUnitTest {

    private MockTableCenter centerArea;
    private MockBag mockBag;
    private Factory tileFactory;

    @Before
    public void initializeTestEnvironment() {
        centerArea = new MockTableCenter();
        mockBag = new MockBag();
        tileFactory = new Factory(mockBag, centerArea);
    }

    @Test
    public void validateFactoryOperations() {
        tileFactory.startNewRound();
        assertEquals("Factory should initially contain MAX_NUMBER_OF_TILES (4).", 4, tileFactory.state().length());
        assertEquals("Check initial state of factory.", "RGBR", tileFactory.state());
        assertTrue("Invalid index should result in no action (null).", tileFactory.take(-1) == null && tileFactory.take(5) == null);

        String currentState = tileFactory.state();
        tileFactory.take(-1);
        assertEquals("State should remain unchanged after an invalid index.", currentState, tileFactory.state());
        assertFalse("Factory should not be empty if state is not empty.", tileFactory.isEmpty());

        List extractedTiles = tileFactory.take(0);
        assertTrue("Factory should be empty after take operation.", tileFactory.isEmpty());

        boolean uniformTileType = true;
        for (Object tile : extractedTiles)
            if (tile != extractedTiles.get(0)) {
                uniformTileType = false;
                break;
            }

        assertTrue("All tiles from take() should be of the same type.", uniformTileType);

        String tableCenterState = "";
        for (Tile tile : centerArea.tileCollection) {
            System.out.println(tile.toString());
            tableCenterState += tile.toString();
        }
        assertEquals("Remaining tiles should be in TableCenter.", "GB", tableCenterState);

        tileFactory.startNewRound();
        assertFalse("Factory should be refilled with new tiles after startNewRound().", tileFactory.isEmpty());
    }
}
