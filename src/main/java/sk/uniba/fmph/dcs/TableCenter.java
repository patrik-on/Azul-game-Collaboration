package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public class TableCenter implements TileSource{
    private ArrayList<Tile> tiles;
    private boolean isFirstPlayer;

    public TableCenter() {
        tiles = new ArrayList<>();
        isFirstPlayer = true;
    }

    public void add(ArrayList<Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    @Override
    public ArrayList<Tile> take(int idx) {
        Tile pickedTile = tiles.get(idx);
        ArrayList<Tile> pickedTiles = new ArrayList<Tile>();
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) == pickedTile) {
                pickedTiles.add(tiles.remove(i));
                i--;
            }
        }
        if (isFirstPlayer) {
            isFirstPlayer = false;
            pickedTiles.add(tiles.remove(tiles.indexOf(Tile.STARTING_PLAYER)));
        }
        return pickedTiles;
    }

    @Override
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    @Override
    public void startNewRound() {
        isFirstPlayer = true;
        tiles = new ArrayList<Tile>();
        tiles.add(Tile.STARTING_PLAYER);
    }

    @Override
    public String State() {
        return "Don't ask me about my state";
    }
}
