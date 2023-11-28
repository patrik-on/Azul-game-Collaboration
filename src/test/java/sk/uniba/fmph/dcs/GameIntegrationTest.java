package sk.uniba.fmph.dcs;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static sk.uniba.fmph.dcs.Game.GAME_OVER;


public class GameIntegrationTest {

    public static class MockBag implements BagInterface {
        private final List<Tile> tiles;
        private final UsedTilesTakeInterface usedTiles;

        public MockBag(final UsedTilesTakeInterface usedTiles) {
            this.tiles = new ArrayList<>();
            this.usedTiles = usedTiles;

            this.tiles.addAll(List.of(Tile.RED,Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE, Tile.BLACK,Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE, Tile.BLACK,Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE, Tile.BLACK));
        }

        @Override
        public List<Tile> take(int count) {
            List<Tile> toReturn = new ArrayList<>();
            while (toReturn.size() < count) {
                if (tiles.isEmpty()) {
                    tiles.addAll(usedTiles.takeAll());
                    Collections.shuffle(tiles);
                }
                if (!tiles.isEmpty()) {
                    toReturn.add(tiles.remove(tiles.size() - 1));
                } else {
                    break; // No more tiles available
                }
            }
            return toReturn;
        }

        @Override
        public String state() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Tile tile : tiles) {
                stringBuilder.append(tile.toString());
            }
            return stringBuilder.toString();
        }
    }


    static Board addPlayer(UsedTiles usedTiles_instance, ArrayList<Points> pointPattern){
        ArrayList<Tile> tileTypes = new ArrayList<>();
        tileTypes.add(Tile.RED);
        tileTypes.add(Tile.BLUE);
        tileTypes.add(Tile.GREEN);
        tileTypes.add(Tile.YELLOW);
        tileTypes.add(Tile.BLACK);
        ArrayList<WallLine> wallLines = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            wallLines.add(new WallLine(tileTypes, null, null));
        }

        Floor floor_instance = new Floor(usedTiles_instance, pointPattern);

        ArrayList<PatternLine> patternLines = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            patternLines.add(new PatternLine(i+1, wallLines.get(i), floor_instance, usedTiles_instance));

        }
        Board board = new Board(floor_instance, new Points(0), (List) patternLines, (List) wallLines);

        return board;
    }

    @Test
    public void test(){

        GameObserver go = new GameObserver();

        UsedTiles usedTiles_instance = new UsedTiles();
        MockBag bag_instance = new MockBag(usedTiles_instance);
        ArrayList<Points> pointPattern = new ArrayList<>();
        pointPattern.add(new Points(1));
        pointPattern.add(new Points(1));
        pointPattern.add(new Points(2));
        pointPattern.add(new Points(2));
        pointPattern.add(new Points(2));
        pointPattern.add(new Points(3));
        pointPattern.add(new Points(3));
        ArrayList<ObserverInterface> poz = new ArrayList<>();

        int pocetHracov = 2;
        ArrayList<Board> boards = new ArrayList<>();
        for (int i = 0; i < pocetHracov; i++){
            Board b = addPlayer(usedTiles_instance, pointPattern);
            boards.add(b);
        }

        TableCenter tableCenter = new TableCenter();

        ArrayList<Factory> factories = new ArrayList<>();
        for (int i = 0; i < 2; i++){
            Factory f = new Factory(bag_instance, tableCenter);
            factories.add(f);
        }


        TableArea tableArea_instance = new TableArea(tableCenter, factories);
        tableArea_instance.startNewRound();
        System.out.println(tableArea_instance.State());


        Game game = new Game(pocetHracov, boards, tableArea_instance, bag_instance, go);


        // Invalid requests
        // Invalid playerId, sourceId, idx, destinationIdx
        assertFalse(game.take(-5, -8, -1, -1));

        // Invalid playerId
        assertFalse(game.take(28, 1, 1, 1));

        // Invalid idx
        assertFalse(game.take(0, 1, -1, 1));

        // Invalid sourceId
        assertFalse(game.take(0, 28, 1, 1));

        //play
        for (int i = 0; i < 2; i++){
            assertEquals(new Points(0), boards.get(i).getPoints());
        }

        assertTrue(game.take(0, 1, 2, 0));

        assertTrue(game.take(1, 2, 1, 0));

        System.out.println(tableArea_instance.State());
        System.out.println(boards.get(0).state());
        System.out.println(boards.get(1).state());

        assertTrue(game.take(0, 0, 4, 3));

        assertTrue(game.take(1, 0, 1, 1));
        System.out.println(tableArea_instance.State());
        System.out.println(boards.get(0).state());
        System.out.println(boards.get(1).state());


        assertTrue(game.take(0, 0, 0, 3));

        assertTrue(game.take(1, 0, 0, 0));
        System.out.println(boards.get(0).state());
        System.out.println(boards.get(1).state());

    }
}
