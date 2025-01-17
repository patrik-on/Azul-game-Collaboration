package sk.uniba.fmph.dcs;

import java.util.*;

public final class Bag implements BagInterface {
    private final List<Tile> tiles;
    private final UsedTilesTakeInterface usedTiles;
    private final int randomSeed ;

    public Bag(final UsedTilesTakeInterface usedTiles) {
        this.tiles = new ArrayList<>();
        this.usedTiles = usedTiles;
        initializeTiles();
        this.randomSeed = 0;
    }

    private void initializeTiles() {
        for (Tile tile : List.of(Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE, Tile.BLACK)) {
            for (int i = 0; i < 20; i++) {
                tiles.add(tile);
            }
        }
        Collections.shuffle(tiles, new Random(randomSeed));
    }

    @Override
    public List<Tile> take(int count) {
        List<Tile> toReturn = new ArrayList<>();
        while (toReturn.size() < count) {
            if (tiles.isEmpty()) {
                tiles.addAll(usedTiles.takeAll());
                Collections.shuffle(tiles, new Random(randomSeed));
            }
            if (!tiles.isEmpty()) {
                toReturn.add(tiles.remove(tiles.size() - 1));
            } else {
                break; // No more tiles available
            }
        }
        return toReturn;
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
