package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BagTest {
    private MockUsedTiles mockUsedTiles;
    private Bag Bag;

    @Before
    public void setupTestEnvironment() {
        mockUsedTiles = new MockUsedTiles();
        Bag = new Bag(mockUsedTiles);
    }

    static class MockUsedTiles implements UsedTilesTakeInterface {
        public ArrayList<Tile> usedTiles;

        public MockUsedTiles() {
            usedTiles = new ArrayList<>();
        }

        @Override
        public Collection<Tile> takeAll() {
            return usedTiles;
        }
    }

    @Test
    public void testBagInitialization() {
        assertEquals("Bag should have 100 tiles initially.", 100, Bag.state().length());
        Map<String, Integer> initialTileDistribution = countTilesInString(Bag.state());
        initialTileDistribution.forEach((color, count) -> assertEquals("Each color should have 20 tiles initially.", 20, (int) count));
    }

    @Test
    public void testTileRetrievalAndRefilling() {
        List<Tile> tilesTaken = new ArrayList<>();
        tilesTaken.addAll(Bag.take(5));
        tilesTaken.addAll(Bag.take(5));
        tilesTaken.addAll(Bag.take(5));
        tilesTaken.addAll(Bag.take(10));
        tilesTaken.addAll(Bag.take(10));
        mockUsedTiles.usedTiles.addAll(tilesTaken);

        assertEquals("Bag should have 65 tiles after taking some.", 65, Bag.state().length());

        Map<Tile, Integer> takenTileCounts = countTakenTiles(tilesTaken);
        Map<String, Integer> remainingTileCounts = countTilesInString(Bag.state());

        assertTrue("Total number of tiles should remain constant.", validateTileTotals(takenTileCounts, remainingTileCounts));

        Bag.take(90); // Attempt to take more than available
        assertEquals("Bag should refill from used tiles when emptied.", 10, Bag.state().length());
    }

    private Map<String, Integer> countTilesInString(String tileString) {
        Map<String, Integer> tileCount = new HashMap<>();
        for (String color : tileString.split("")) {
            tileCount.put(color, tileCount.getOrDefault(color, 0) + 1);
        }
        return tileCount;
    }

    private Map<Tile, Integer> countTakenTiles(List<Tile> takenTiles) {
        Map<Tile, Integer> tileCount = new HashMap<>();
        for (Tile tile : takenTiles) {
            tileCount.put(tile, tileCount.getOrDefault(tile, 0) + 1);
        }
        return tileCount;
    }

    private boolean validateTileTotals(Map<Tile, Integer> takenTiles, Map<String, Integer> remainingTiles) {
        for (Tile tile : List.of(Tile.RED, Tile.GREEN, Tile.BLUE, Tile.YELLOW, Tile.BLACK)) {
            int takenCount = takenTiles.getOrDefault(tile, 0);
            int remainingCount = remainingTiles.getOrDefault(tile.toString(), 0);
            if (takenCount + remainingCount != 20) {
                return false;
            }
        }
        return true;
    }
}
