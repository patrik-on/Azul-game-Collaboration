package sk.uniba.fmph.dcs;
import java.util.*;
import java.io.*;

public final class UsedTyles implements Serializable{
    private static volatile UsedTyles fINSTANCE;
    private ArrayList<Tile> tiles;

    public static UsedTyles getInstance(){
        if (fINSTANCE == null) {
            synchronized (UsedTyles.class) {
                if (fINSTANCE == null) fINSTANCE = new UsedTyles();
            }
        }
        return fINSTANCE;
    }

    public void give(ArrayList<Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    public ArrayList<Tile> takeAll() {
        ArrayList<Tile> toReturn = new ArrayList(tiles);
        tiles.removeAll(tiles);
        return toReturn;
    }

    public String state() {
        String toReturn = "";
        for (final Tile tile : tiles) {
            toReturn += tile.toString();
        }
        return toReturn;
    }


    private UsedTyles(){
        tiles = new ArrayList<>();
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance();
    }
}
