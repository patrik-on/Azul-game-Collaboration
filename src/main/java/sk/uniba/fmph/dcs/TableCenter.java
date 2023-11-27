package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.Collection;

public class TableCenter implements TyleSource, TableCenterInterface {
    private ArrayList<Tile> tiles;
    private boolean isFirst;

    public TableCenter() {
        tiles = new ArrayList<Tile>();
        isFirst = true;
    }
    @Override
    public void add(Collection<Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    @Override
    public ArrayList<Tile> take(int idx) {
        if(idx > tiles.size()) throw new IndexOutOfBoundsException("Index out of bounds");
        Tile pickedTile = tiles.get(idx);
        ArrayList<Tile> pickedTiles = new ArrayList<Tile>();
        if(isFirst) pickedTiles.add(tiles.remove(0));
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
        return tiles.isEmpty();
    }

    @Override
    public void startNewRound() {
        tiles = new ArrayList<Tile>();
        isFirst = true;
        tiles.add(Tile.STARTING_PLAYER);
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
