package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public class TableArea {
    ArrayList<TyleSource> tyleSources;

    public TableArea(ArrayList<TyleSource> tyleSources) {
        this.tyleSources = tyleSources;
    }

    ArrayList<Tile> take(int sourceIdx, int idx) {
        return tyleSources.get(sourceIdx).take(idx);
    }

    boolean isRoundEnd() {
        for (TyleSource tyleSource : tyleSources) {
            if (!tyleSource.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    void startNewRound() {
        for (TyleSource tyleSource : tyleSources) {
            tyleSource.startNewRound();
        }
    }

    public String State() {
        String toReturn = "";
        for (final TyleSource tyleSource : tyleSources) {
            toReturn += tyleSource.toString();
        }
        return toReturn;
    }
}
