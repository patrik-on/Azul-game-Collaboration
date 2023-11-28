package sk.uniba.fmph.dcs;

import java.util.List;

public interface TableAreaInterface {
    List<Tile> take(int sourceIdx, int idx);
    boolean isRoundEnd();
    void startNewRound();
    String state();
}
