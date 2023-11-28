package sk.uniba.fmph.dcs;

import java.util.*;

public class PatternLine implements PatternLineInterface {
    private final int capacity;
    private final WallLineInterface wallLine;
    private final FloorInterface floor;
    private final UsedTilesGiveInterface usedTiles;
    private List<Tile> tiles;

    public PatternLine(int capacity, WallLineInterface wallLine, FloorInterface floor, UsedTilesGiveInterface usedTiles) {
        this.capacity = capacity;
        this.wallLine = wallLine;
        this.floor = floor;
        this.usedTiles = usedTiles;
        this.tiles = new ArrayList<>(capacity);
    }

    @Override
    public void put(List<Tile> incomingTiles) {
        if (incomingTiles.isEmpty()) return;

        Tile firstTile = incomingTiles.get(0);
        if (!wallLine.canPutTile(firstTile) || (!this.tiles.isEmpty() && this.tiles.get(0) != firstTile)) {
            floor.put(incomingTiles);
            return;
        }

        int spaceLeft = capacity - this.tiles.size();
        int tilesToAdd = Math.min(spaceLeft, incomingTiles.size());

        this.tiles.addAll(incomingTiles.subList(0, tilesToAdd));

        if (tilesToAdd < incomingTiles.size()) {
            floor.put(incomingTiles.subList(tilesToAdd, incomingTiles.size()));
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
