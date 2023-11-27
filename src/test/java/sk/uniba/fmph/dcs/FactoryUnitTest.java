package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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

class MockTableCenter extends TableCenter {
    public ArrayList<Tile> tileCollection = new ArrayList<>();

    @Override
    public void add(Collection<Tile> incomingTiles) {
        this.tileCollection.addAll(incomingTiles);
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
        assertEquals("Factory should initially contain MAX_NUMBER_OF_TILES (4).", 4, tileFactory.State().length());
        assertEquals("Check initial state of factory.", "RGBR", tileFactory.State());
        assertEquals("Invalid index should result in no action (null).", true, tileFactory.take(-1) == null && tileFactory.take(5) == null);

        String currentState = tileFactory.State();
        tileFactory.take(-1);
        assertEquals("State should remain unchanged after an invalid index.", currentState, tileFactory.State());
        assertEquals("Factory should not be empty if state is not empty.", false, tileFactory.isEmpty());

        ArrayList<Tile> extractedTiles = tileFactory.take(0);
        assertEquals("Factory should be empty after take operation.", false, tileFactory.isEmpty());

        boolean uniformTileType = true;
        for (Tile tile : extractedTiles)
            if (tile != extractedTiles.get(0)) {
                uniformTileType = false;
                break;
            }

        assertEquals("All tiles from take() should be of the same type.", true, uniformTileType);

        String tableCenterState = "";
        for (Tile tile : centerArea.tileCollection) {
            tableCenterState += tile.toString();
        }
        assertEquals("Remaining tiles should be in TableCenter.", "GB", tableCenterState);

        tileFactory.startNewRound();
        assertEquals("Factory should be refilled with new tiles after startNewRound().", false, tileFactory.isEmpty());
    }
}
