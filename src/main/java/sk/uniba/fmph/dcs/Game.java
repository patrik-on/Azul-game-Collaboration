package sk.uniba.fmph.dcs;

import java.util.ArrayList;

import static sk.uniba.fmph.dcs.FinishRoundResult.GAME_FINISHED;

public class Game implements GameInterface {
    public static final String GAME_OVER = "GAME_OVER";
    private TableArea tableArea;
    private Bag bag;
    private ArrayList<Board> boards;

    private GameObserver gameObserver;

    private int startingPlayerId;

    private int curentPlayerId;

    public Game(int numberOfPlayers, ArrayList<Board> boards, TableArea tableArea, Bag bag, GameObserver gameObserver) {
        this.tableArea = tableArea;
        this.bag = bag;
        this.boards = boards;
        this.gameObserver = gameObserver;
        curentPlayerId = 0;
    }


    @Override
    public boolean take(int playerId, int sourceId, int idx, int destinationIdx) {
        if (playerId != curentPlayerId) {
            return false;
        }
        ArrayList<Tile> tiles = tableArea.take(sourceId, idx);
        if (tiles.contains(Tile.STARTING_PLAYER)) {
            startingPlayerId = playerId;
        }
        if (tiles.size() == 0) {
            return false;
        }

        boards.get(playerId).put(destinationIdx, tiles);
        if (tableArea.isRoundEnd()) {
            curentPlayerId = startingPlayerId;
            for (Board board : boards) {
                FinishRoundResult result = board.finishRound();
                if (result == GAME_FINISHED) {
                    for (Board board1 : boards) {
                        if (board1.finishRound() == GAME_FINISHED) {
                            gameObserver.notifyEverybody(GAME_OVER);

                            return finishGame();
                        }
                    }

                } else {
                    tableArea.startNewRound();
                }
            }
        }
        curentPlayerId = (curentPlayerId + 1) % boards.size();
        return true;
    }
    boolean finishGame(){
        int maxPoints = 0;
        int winnerId = 0;
        for(int i = 0; i < boards.size(); i++){
            int points = boards.get(i).getPoints().getValue();
            gameObserver.notifyEverybody("Player " + i + " has " + points + " points");
            if(points > maxPoints){
                maxPoints = points;
                winnerId = i;
            }
        }
        gameObserver.notifyEverybody("Player " + winnerId + " won with " + maxPoints + " points");
        return true;
    }

}
