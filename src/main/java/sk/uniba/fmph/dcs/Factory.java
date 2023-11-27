package sk.uniba.fmph.dcs;
import java.util.ArrayList;
import java.util.Iterator;


public class Factory implements TyleSource{
    private static final int MAX_TILES = 4;
    private final BagInterface bag;
    private final TableCenter tableCenter;
    private final ArrayList<Tile> tiles;

    public Factory(BagInterface bag, TableCenter tableCenter) {
        this.bag = bag;
        this.tableCenter = tableCenter;
        tiles = new ArrayList<>();
        startNewRound();
    }

    @Override
    public ArrayList<Tile> take(int index) {
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
    public String State() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tile tile : tiles) {
            stringBuilder.append(tile.toString());
        }
        return stringBuilder.toString();
    }
}
