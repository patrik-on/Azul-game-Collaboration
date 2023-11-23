package sk.uniba.fmph.dcs;
import java.util.ArrayList;
import java.util.Iterator;


public class Factory implements TyleSource {
    private static final int MAX_TILES = 4;
    private final BagInterface bag;
    private final TableCenterInterface tableCenter;
    private final ArrayList<Tile> tiles;

    public Factory(BagInterface bag, TableCenterInterface tableCenter) {
        this.bag = bag;
        this.tableCenter = tableCenter;
        tiles = new ArrayList<>();
        startNewRound();
    }

    @Override
    public ArrayList<Tile> take(int index) throws IllegalArgumentException {
        if (index < 0 || index >= tiles.size()) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        Tile selectedColor = tiles.get(index);
        ArrayList<Tile> pickedTiles = new ArrayList<>();
        Iterator<Tile> iterator = tiles.iterator();

        while (iterator.hasNext()) {
            Tile tile = iterator.next();
            if (tile == selectedColor) {
                pickedTiles.add(tile);
                iterator.remove();
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
        tiles.addAll(bag.take(MAX_TILES));

    }

    @Override
    public String State() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tile tile : tiles) {
            stringBuilder.append(tile.toString());
        }
        return stringBuilder.toString();
    }
}
