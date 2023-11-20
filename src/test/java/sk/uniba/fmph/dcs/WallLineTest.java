package sk.uniba.fmph.dcs;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WallLineTest {
  private WallLine wallLine;

  @Before
  public void setUp() {
    LinkedList<Tile> tileTypes1 = new LinkedList<Tile>();
    tileTypes1.add(Tile.RED);
    tileTypes1.add(Tile.BLUE);
    tileTypes1.add(Tile.BLACK);
    tileTypes1.add(Tile.GREEN);
    tileTypes1.add(Tile.YELLOW);

    LinkedList<Tile> tileTypes2 = new LinkedList<Tile>(tileTypes1.subList(1,4));
    tileTypes2.add(Tile.RED);

    WallLine anotherWallLine = new WallLine(tileTypes2,null,null);
    wallLine =  new WallLine(tileTypes1,null,null);
  }

  @Test
  public void testCanPutTiles(){
    assertTrue("WallLine.canPutTile should return true if tile is in WallLine tile types " +
                        "and this tileType isn't already occupied by tile.",wallLine.canPutTile(Tile.BLACK));
    assertFalse("WallLine.canPutTile should return false if tile isn't in WallLine tile types.",
            wallLine.canPutTile(Tile.STARTING_PLAYER));
    wallLine.putTile(Tile.RED);
    assertFalse("WallLine.canPutTile should return false if tile is already occupied by tile",
            wallLine.canPutTile(Tile.RED));
  }

  @Test
  public void testGetTiles(){
    LinkedList<Optional<Tile>> empty = new LinkedList<>();
    for (int i = 0; i < 5; i++) {
      empty.add(Optional.empty());
    }
    assertEquals("WallLine.getTiles should return list of optional.empty if no tiles are occupied."
            ,empty,wallLine.getTiles());

    wallLine.putTile(Tile.BLUE);
    ArrayList<Optional<Tile>> blueTile = new ArrayList<>(empty);
    blueTile.set(1,Optional.ofNullable(Tile.BLUE));
    assertEquals("WallLine.getTiles should return list of optional.empty and Blue tile on correct place " +
                    "according to tileTypes if only Blue tile is occupied."
            ,blueTile,wallLine.getTiles());

    wallLine.putTile(Tile.RED);
    wallLine.putTile(Tile.GREEN);
    wallLine.putTile(Tile.BLACK);
    wallLine.putTile(Tile.YELLOW);
    ArrayList<Optional<Tile>> full = new ArrayList<>();
    full.add(Optional.of(Tile.RED));
    full.add(Optional.of(Tile.BLUE));
    full.add(Optional.of(Tile.BLACK));
    full.add(Optional.of(Tile.GREEN));
    full.add(Optional.of(Tile.YELLOW));
    assertEquals("WallLine.getTiles should return full list of tiles on correct places " +
                    "according to tileTypes if every tile is occupied."
            ,full,wallLine.getTiles());
  }
}
