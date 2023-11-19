package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WallLine {
    private ArrayList<Tile> tileTypes;
    private WallLine lineUp;
    private WallLine lineDown;

    public WallLine(ArrayList<Tile> tileTypes, WallLine lineUp, WallLine lineDown) {
        this.tileTypes = tileTypes;
        this.lineUp = lineUp;
        this.lineDown = lineDown;
    }
    
    Boolean canPutTile(Tile tile){
        return false;
    }

    List<Optional<Tile>> getTiles(){
        return null;
    }

    Points putTile(Tile tile){
        return null;
    }

    String state(){
        return null;
    }
}
