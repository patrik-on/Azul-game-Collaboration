package sk.uniba.fmph.dcs;
import java.util.ArrayList;

public class TableArea {
    ArrayList<TileSource> factories;
    TableCenter tableCenter;
    Bag bag;

    public TableArea(int playerCount, Bag bag) {
        this.bag = bag;
        factories.add(new Factory(this));
        for (int i = 0; i < playerCount; i++) {
            factories.add(new Factory(this));
            factories.add(new Factory(this));
        }
        tableCenter = new TableCenter();
    }

    public TableCenter getTableCenter() {
        return tableCenter;
    }

    ArrayList<Tile> take(int sourceIdx, int idx) {
        if (sourceIdx == -1) {
            return tableCenter.take(idx);
        }
        return factories.get(sourceIdx).take(idx);
    }

    boolean isRoundEnd() {
        for (TileSource tileSource : factories) {
            if (!tileSource.isEmpty()) {
                return false;
            }
        }
        return tableCenter.isEmpty();
    }

    void startNewRound() {
        for (TileSource tileSource : factories) {
            tileSource.startNewRound();
        }
        tableCenter.startNewRound();
    }

    public String State() {
        return "Don't ask me about my state";
    }
}
