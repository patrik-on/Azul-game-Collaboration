package sk.uniba.fmph.dcs;

import sk.uniba.fmph.dcs.Points;
import sk.uniba.fmph.dcs.Tile;

public interface WallLinePutTileInterface {
    boolean canPutTile(Tile tile);
    Points putTile(Tile tile);

    void setLineUp(WallLine wallLine);
    void setLineDown(WallLine wallLine);
}
