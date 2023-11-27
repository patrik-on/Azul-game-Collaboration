package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public class TableArea {
    ArrayList<TyleSource> tyleSources;

    public TableArea(TableCenter tb, ArrayList<Factory> factories) {
        this.tyleSources = new ArrayList<>();
        this.tyleSources.add(tb);
        this.tyleSources.addAll(factories);

    }

    ArrayList<Tile> take(int sourceId, int idx) {
        ArrayList<Tile> fin = new ArrayList<>();

        //System.out.println(_tyleSources.size() + " _tylesource" );
        if(sourceId < 0 || sourceId >= tyleSources.size()) {
            return fin;
        }
        TyleSource tyleSource = tyleSources.get(sourceId);
        if (tyleSource.isEmpty()) {
            return fin;
        }
        if(idx < 0 || idx > 4) {
            return fin;
        }
        for (Tile t : tyleSource.take(idx)) {
            fin.add(t);
        }
        return fin;
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
        StringBuilder ans = new StringBuilder();
        ans.append("TyleSources:\n");
        for (TyleSource ts: this.tyleSources) {
            ans.append(ts.State()).append("\n");
        }
        return ans.toString();
    }
}
