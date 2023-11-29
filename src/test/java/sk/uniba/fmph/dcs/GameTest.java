package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FakeUsedTilesTake implements UsedTilesGiveInterface, UsedTilesTakeInterface {
    private ArrayList<Tile> tiles;

    public FakeUsedTilesTake() {
        tiles = new ArrayList<>();
    }

    public String state() {
        StringBuilder toReturn = new StringBuilder();
        for (final Tile tile : tiles) {
            toReturn.append(tile.toString());
        }
        return toReturn.toString();
    }

    @Override
    public Collection<Tile> takeAll() {
        return null;
    }

    @Override
    public void give(Collection<Tile> ts) {
        tiles.addAll(ts);
    }
}

class FakeTableCenterGame implements TableCenterInterface {
    private ArrayList<Tile> tiles;
    private boolean isFirst;

    public FakeTableCenterGame() {
        tiles = new ArrayList<>();
        isFirst = true;
    }

    @Override
    public void add(List<Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    @Override
    public List<Tile> take(int idx) {
        if (idx > tiles.size()) throw new IndexOutOfBoundsException("Index out of bounds");
        Tile pickedTile = tiles.get(idx);
        ArrayList<Tile> pickedTiles = new ArrayList<Tile>();
        if (isFirst) pickedTiles.add(tiles.remove(0));
        isFirst = false;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) == pickedTile) {
                pickedTiles.add(tiles.remove(i));
                i--;
            }
        }
        return pickedTiles;
    }

    @Override
    public boolean isEmpty() {
        return this.tiles.isEmpty();
    }

    @Override
    public void startNewRound() {
        tiles = new ArrayList<>();
        isFirst = true;
        tiles.add(Tile.STARTING_PLAYER);
    }

    @Override
    public String state() {
        StringBuilder toReturn = new StringBuilder();
        for (final Tile tile : tiles) {
            toReturn.append(tile.toString());
        }
        return toReturn.toString();
    }
}

class FakeTableArea implements TableAreaInterface {
    List<TileSourceInterface> tileSources;

    public FakeTableArea(List<TileSourceInterface> tileSources) {
        this.tileSources = tileSources;
    }

    @Override
    public List<Tile> take(int sourceIdx, int idx) {
        return tileSources.get(sourceIdx).take(idx);
    }

    @Override
    public boolean isRoundEnd() {
        for (TileSourceInterface tileSource : tileSources) {
            if (!tileSource.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void startNewRound() {
        for (TileSourceInterface tileSource : tileSources) {
            tileSource.startNewRound();
        }
    }

    @Override
    public String state() {
        StringBuilder toReturn = new StringBuilder();
        for (final TileSourceInterface tileSource : tileSources) {
            toReturn.append(tileSource.toString());
        }
        return toReturn.toString();
    }
}

class FakeBoard implements BoardInterface {

    @Override
    public void put(int destinationIdx, List<Tile> tiles) {

    }

    @Override
    public FinishRoundResult finishRound() {
        return null;
    }

    @Override
    public void endGame() {

    }

    @Override
    public String state() {
        return null;
    }

    @Override
    public Points getPoints() {
        return null;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }
}

class FakeObserver implements ObserverInterface {

    @Override
    public void notify(String newState) {

    }
}

public class GameTest {

    Game game;
    TableAreaInterface tableArea;
    ArrayList<BoardInterface> allBoards;
    List<TileSourceInterface> tileSources;
    ObserverInterface observer;
    int playerCount;
    int startingPlayerId;
    int currentPlayerId;
    boolean isGameOver;

    @Before
    public void setUp() {
        FakeTableCenterGame tableCenter = new FakeTableCenterGame();
        UsedTilesTakeInterface usedTiles = new FakeUsedTilesTake();
        Bag bag = new Bag(usedTiles);
        Factory factory = new Factory(bag, tableCenter);
        tileSources = new ArrayList<>();
        tileSources.add(factory);
        tileSources.add(tableCenter);

        tableArea = new FakeTableArea(tileSources);
        allBoards = new ArrayList<>();

        BoardInterface board1 = new FakeBoard();
        BoardInterface board2 = new FakeBoard();
        allBoards.add(board1);
        allBoards.add(board2);

        observer = new FakeObserver();

        playerCount = 2;
        startingPlayerId = 1;
        currentPlayerId = 1;

        isGameOver = false;

        game = new Game(playerCount, allBoards, tableArea, bag, observer);

    }

    @Test
    public void testGame() {

        String factoryState = tileSources.get(0).state();
        assertFalse("Verify the factory is not empty after initialization.", factoryState.isEmpty());

        System.out.println("Factory state before any player action: " + factoryState);
        System.out.println("Table Center state before any player action: " + tileSources.get(1).state());

        int startingPlayer = game.getCurrentPlayerId();
        assertTrue("Check if the starting player successfully took tiles from the factory.", game.take(startingPlayer, 0, 0, 0));


        assertNotEquals("Ensure the turn has passed to the next player after the starting player's action.", startingPlayer, game.getCurrentPlayerId());
        assertFalse("Confirm that the starting player, now out of turn, cannot take again.", game.take(startingPlayer, 0, 0, 0));

        System.out.println("Factory state after the starting player's turn: " + factoryState);
        System.out.println("Table Center state after the starting player's turn: " + tileSources.get(1).state());

        assertEquals("Expect the factory to be empty after the tiles are taken.", "", tileSources.get(0).state());
        assertThrows(IllegalArgumentException.class, () -> game.take(game.getCurrentPlayerId(), 0, 0, 0), "An IllegalArgumentException should be thrown when a player attempts to take from an empty factory.");

        String tableCenterState = tileSources.get(1).state();

        assertTrue("Check that the Table Center initially contains the Starting Player Tile.", tableCenterState.contains("S"));

        System.out.println("Table Center state after taking from the factory: " + tableCenterState);

        int nextPlayer = game.getCurrentPlayerId();

        assertTrue("Verify that the next player can successfully take tiles from the Table Center.", game.take(nextPlayer, 1, 1, 0));

        assertNotEquals("Ensure the turn has passed to the next player after the next player's action.", nextPlayer, game.getCurrentPlayerId());
        assertFalse("Confirm that the next player, now out of turn, cannot take again.", game.take(nextPlayer, 1, 1, 0));

        tableCenterState = tileSources.get(1).state();

        assertFalse("Confirm that the Starting Player Tile is no longer in the Table Center after the take.", tableCenterState.contains("S"));

        System.out.println("Table Center state after the next player's turn: " + tableCenterState);

    }

}