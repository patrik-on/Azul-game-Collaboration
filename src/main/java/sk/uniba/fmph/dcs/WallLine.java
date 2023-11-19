package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WallLine {
    private ArrayList<Tile> tileTypes;
    private WallLine lineUp;
    private WallLine lineDown;

    private boolean[] occupiedTiles;


    public WallLine(ArrayList<Tile> tileTypes, WallLine lineUp, WallLine lineDown) {
        this.tileTypes = tileTypes;
        this.lineUp = lineUp;
        this.lineDown = lineDown;

        this.occupiedTiles = new boolean[tileTypes.size()]; 
    }
    
    Boolean canPutTile(Tile tile){
        if (tileTypes.contains(tile) && !occupiedTiles[tileTypes.indexOf(tile)]) {
            return true;
        }
        return false;
    }

    List<Optional<Tile>> getTiles(){
        ArrayList<Optional<Tile>> tiles = new ArrayList<>();
        for (int i = 0; i < tileTypes.size(); i++) {
            if(occupiedTiles[i]){
                tiles.add(Optional.ofNullable(this.tileTypes.get(i)));
            }else{
                Tile t = null;
                tiles.add(Optional.ofNullable(t));
            }
        }
        return tiles;
    }

    Points putTile(Tile tile) throws Exception{
        if (canPutTile(tile)){
            this.occupiedTiles[tileTypes.indexOf(tile)] = true;
            return null;
        }else{
            throw new Exception("The tile given is wrong!");
        }
    }

    String state(){
        String toReturn = "";
        for (int i = 0; i < tileTypes.size(); i++) {
            if(occupiedTiles[i]){
                toReturn += tileTypes.get(i).toString();
            }
        }
        return toReturn;
    }
}
