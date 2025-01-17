package sk.uniba.fmph.dcs;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import static sk.uniba.fmph.dcs.FinishRoundResult.GAME_FINISHED;

public class Game implements GameInterface {
    public static final String GAME_OVER = "GAME_OVER";
    public boolean isGameOver = false;
    private TableAreaInterface tableArea;
    private BagInterface bag;
    private ArrayList<BoardInterface> boards;

    private ObserverInterface gameObserver;
    private int seed;

    private int startingPlayerId;

    private int curentPlayerId;

    public Game(int numberOfPlayers, ArrayList<BoardInterface> boards, TableAreaInterface tableArea, BagInterface bag, ObserverInterface gameObserver) {
        this.tableArea = tableArea;
        this.bag = bag;
        this.boards = boards;
        this.gameObserver = gameObserver;
        seed = 0;
        Random random = new Random(seed);
        startingPlayerId = random.nextInt(numberOfPlayers);
        curentPlayerId = startingPlayerId;
    }


    @Override
    public boolean take(int playerId, int sourceId, int idx, int destinationIdx) {
        if (playerId != curentPlayerId) {
            return false;
        }
        List<Tile> tiles = tableArea.take(sourceId, idx);
        if (tiles == null) {
            return false;
        }

        if (tiles.contains(Tile.STARTING_PLAYER)) {
            startingPlayerId = playerId;
        }
        if (tiles.isEmpty()) {
            return false;
        }

        boards.get(playerId).put(destinationIdx, tiles);
        if (tableArea.isRoundEnd()) {
            curentPlayerId = startingPlayerId;
            FinishRoundResult result = FinishRoundResult.NORMAL;
            for (BoardInterface board : boards) {
                result = board.finishRound();
            }
            if ( result == GAME_FINISHED) {
                for (BoardInterface board1 : boards) {
                    board1.endGame();
                }
                gameObserver.notify(GAME_OVER);
                isGameOver = true;
                return finishGame();
            } else {
                tableArea.startNewRound();
            }

        }
        curentPlayerId = (curentPlayerId + 1) % boards.size();
        return true;
    }

    @Override
    public int getCurrentPlayerId() {
        return curentPlayerId;
    }

    boolean finishGame(){
        int maxPoints = 0;
        int winnerId = 0;
        for(int i = 0; i < boards.size(); i++){
            int points = boards.get(i).getPoints().getValue();
            gameObserver.notify("Player " + i + " has " + points + " points");
            if(points > maxPoints){
                maxPoints = points;
                winnerId = i;
            }
        }
        gameObserver.notify("Player " + winnerId + " won with " + maxPoints + " points");
        return true;
    }


}
