package sk.uniba.fmph.dcs;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Factory implements TileSourceInterface{
    private static final int MAX_TILES = 4;
    private final BagInterface bag;
    private final TableCenterInterface tableCenter;
    private final ArrayList<Tile> tiles;

    public Factory(BagInterface bag, TableCenterInterface tableCenter) {
        this.bag = bag;
        this.tableCenter = tableCenter;
        tiles = new ArrayList<>();
    }

    @Override
    public List<Tile> take(int index) {
        if (index < 0 || index >= tiles.size()) {
            return null;
        }

        Tile chosenTile = tiles.get(index);
        ArrayList<Tile> toReturn = new ArrayList<>();
        ArrayList<Tile> toTableCenter = new ArrayList<>();

        Iterator<Tile> iterator = tiles.iterator();
        while (iterator.hasNext()) {
            Tile tile = iterator.next();
            if (tile.equals(chosenTile)) {
                toReturn.add(tile);
                iterator.remove();
            } else {
                toTableCenter.add(tile);
                iterator.remove();
            }
        }
        System.out.println("toReturn: " + toReturn);
        System.out.println("toTableCenter: " + toTableCenter);
        tableCenter.add(toTableCenter);
        return toReturn;
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
    public String state() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tile tile : tiles) {
            stringBuilder.append(tile.toString());
        }
        return stringBuilder.toString();
    }
}
