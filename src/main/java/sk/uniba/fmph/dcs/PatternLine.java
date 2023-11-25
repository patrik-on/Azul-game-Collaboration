package sk.uniba.fmph.dcs;

import java.util.*;

public class PatternLine implements PatternLineInterface {
    private final int capacity;
    private final WallLineInterface wallLine;
    private final FloorPutInterface floor;
    private final UsedTilesGiveInterface usedTiles;
    private List<Tile> tiles;

    public PatternLine(int capacity, WallLineInterface wallLine, FloorPutInterface floor, UsedTilesGiveInterface usedTiles) {
        this.capacity = capacity;
        this.wallLine = wallLine;
        this.floor = floor;
        this.usedTiles = usedTiles;
        this.tiles = new ArrayList<>(capacity);
    }

    @Override
    public void put(List<Tile> tiles) {
        Tile firstTile = tiles.iterator().next();
        if (!wallLine.canPutTile(firstTile) || (!this.tiles.isEmpty() && !this.tiles.get(0).equals(firstTile))) {
            floor.put(tiles);
            return;
        }

        List<Tile> fallingTiles = new ArrayList<>();
        for (Tile tile : tiles) {
            if (this.tiles.size() < capacity) {
                this.tiles.add(tile);
            } else {
                fallingTiles.add(tile);
            }
        }

        if (!fallingTiles.isEmpty()) {
            floor.put(fallingTiles);
        }
    }

    @Override
    public Points finishRound() {
        if (tiles.size() != capacity) {
            return new Points(0);
        }

        Tile tile = tiles.get(0);
        tiles.clear();
        tiles.add(tile); // Keep one tile for the wall line
        usedTiles.give(new ArrayList<>(tiles)); // Give away extra tiles
        tiles.clear(); // Clear the pattern line
        return wallLine.putTile(tile);
    }

    @Override
    public String state() {
        StringBuilder stateBuilder = new StringBuilder();
        for (Tile tile : tiles) {
            stateBuilder.append(tile.toString());
        }
        return stateBuilder.toString();
    }
}
