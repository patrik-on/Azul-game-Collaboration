package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BagTest {
    private FakeUsedTilesBag usedTiles;
    private Bag bag;

    @Before
    public void setUp() {
        usedTiles = new FakeUsedTilesBag();
        bag = new Bag(usedTiles);
    }
    static class FakeUsedTilesBag implements UsedTilesTakeInterface {
        public ArrayList<Tile> tiles;
        public FakeUsedTilesBag() {tiles = new ArrayList();}
        @Override
        public Collection<Tile> takeAll() {
            return tiles;
        }
    }

    @Test
    public void testInitialBagState() {
        assertEquals("Bag when created should contain 100 tiles.", 100, bag.state().length());
        Map<String, Integer> tileCount = countTiles(bag.state());
        tileCount.forEach((color, count) -> assertEquals("There should be 20 tiles of each color at the beginning.", 20, (int) count));
    }

    @Test
    public void testTileTakingAndRefilling() {
        List<Tile> takenTiles = new ArrayList<>();
        takenTiles.addAll(bag.take(4));
        takenTiles.addAll(bag.take(5));
        takenTiles.addAll(bag.take(5));
        takenTiles.addAll(bag.take(6));
        usedTiles.tiles.addAll(takenTiles);

        assertEquals("There should be 80 tiles left in bag.", 80, bag.state().length());

        Map<Tile, Integer> takenTileCounts = countTakenTiles(takenTiles);
        Map<String, Integer> remainingTileCounts = countTiles(bag.state());

        assertTrue("Take should not mess with the number of all tiles.", validateTotalTiles(takenTileCounts, remainingTileCounts));

        bag.take(82); // Attempt to take more tiles than are available
        assertEquals("If there are fewer tiles in the bag than requested, the bag will refill from UsedTiles.", 18, bag.state().length());
    }

    private Map<String, Integer> countTiles(String state) {
        Map<String, Integer> tileCount = new HashMap<>();
        for (String c : state.split("")) {
            tileCount.put(c, tileCount.getOrDefault(c, 0) + 1);
        }
        return tileCount;
    }

    private Map<Tile, Integer> countTakenTiles(List<Tile> takenTiles) {
        Map<Tile, Integer> count = new HashMap<>();
        for (Tile tile : takenTiles) {
            count.put(tile, count.getOrDefault(tile, 0) + 1);
        }
        return count;
    }

    private boolean validateTotalTiles(Map<Tile, Integer> taken, Map<String, Integer> remaining) {
        for (Tile tile : List.of(Tile.RED, Tile.GREEN, Tile.BLUE, Tile.YELLOW, Tile.BLACK)) {
            int takenCount = taken.getOrDefault(tile, 0);
            int remainingCount = remaining.getOrDefault(tile.toString(), 0);
            if (takenCount + remainingCount != 20) {
                return false;
            }
        }
        return true;
    }
}
