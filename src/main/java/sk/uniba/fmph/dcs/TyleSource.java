package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public interface TyleSource {
    ArrayList<Tile> take (int idx);
    boolean isEmpty();
    void startNewRound();
    String State();
}
