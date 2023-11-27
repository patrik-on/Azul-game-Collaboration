package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/*class FakeBagArea implements BagInterface {
    ArrayList<Tile> tiles;

    public FakeBagArea() {
        tiles = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tiles.add(Tile.RED);
        }
    }

    @Override
    public List<Tile> take(int count) {
        ArrayList<Tile> takenTiles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            takenTiles.add(tiles.remove(0));
        }
        return takenTiles;
    }

    @Override
    public String state() {
        return null;
    }
}

class FakeFactory implements TyleSource {
    FakeBagArea bag;
    TableCenter tableCenter;
    ArrayList<Tile> tiles;
    int factorySize;

    FakeFactory(TableCenter tableCenter, FakeBagArea bag) {
        this.bag = bag;
        this.tableCenter = tableCenter;
        tiles = new ArrayList<>();
        factorySize = 4;
    }

    @Override
    public ArrayList<Tile> take(int idx) {
        Tile pickedTile = tiles.get(idx);
        ArrayList<Tile> pickedTiles = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) == pickedTile) {
                pickedTiles.add(tiles.remove(i));
                i--;
            }
        }
        tableCenter.add(tiles);
        tiles.clear();
        return pickedTiles;
    }

    @Override
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    @Override
    public void startNewRound() {
        tiles.clear();
        tiles.addAll(bag.take(factorySize));
    }

    @Override
    public String State() {
        String toReturn = "";
        for (final Tile tile : tiles) {
            toReturn += tile.toString();
        }
        return toReturn;
    }
}

public class TableAreaTest {
    private FakeBagArea bag;
    private ArrayList<TyleSource> tyleSources;
    private int playerCount;
    private TableCenter tableCenter;

    @Before
    public void setUp() {
        playerCount = 4;
        bag = new FakeBagArea();
        tyleSources = new ArrayList<>();
        tableCenter = new TableCenter();
        tyleSources.add(tableCenter);
        tyleSources.add(new FakeFactory(tableCenter, bag));
        for (int i = 0; i < playerCount; i++) {
            tyleSources.add(new FakeFactory(tableCenter, bag));
            tyleSources.add(new FakeFactory(tableCenter, bag));
        }
    }

    @Test
    public void test_tableArea() {
        TableArea tableArea = new TableArea(tyleSources);
        tableArea.startNewRound();
        assertEquals("TablaArea Should take 4 RED Tiles.", new ArrayList<Tile>(List.of(Tile.RED, Tile.RED, Tile.RED, Tile.RED)), tableArea.take(1, 1));

    }
}*/
