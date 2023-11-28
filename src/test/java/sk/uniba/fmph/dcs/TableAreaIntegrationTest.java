package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableAreaIntegrationTest {
    private TableArea tableArea;
    private BagInterface bag;
    private TableCenterInterface tableCenter;
    private ArrayList<TileSourceInterface> tileSources;
    private Factory factory1;
    private Factory factory2;

    @Before
    public void setUp() {
        UsedTiles usedTiles = new UsedTiles();
        bag = new Bag(usedTiles);
        tableCenter = new TableCenter();
        tileSources = new ArrayList<>();
        tileSources.add(tableCenter);
        factory1 = new Factory(bag, tableCenter);
        factory2 = new Factory(bag, tableCenter);
        tileSources.add(factory1);
        tileSources.add(factory2);

        tableArea = new TableArea(tileSources);
    }

    @Test
    public void testTableArea() {
        tableArea.startNewRound();

        // Verify initial state of factories
        assertFalse("Factory 1 should have tiles after starting a new round.", factory1.isEmpty());
        assertFalse("Factory 2 should have tiles after starting a new round.", factory2.isEmpty());

        System.out.println("Start of first round: \n" + tableArea.state());

        // Take tiles from Factory 1 and check its state
        assertFalse("Tiles should be successfully taken from Factory 1.", tableArea.take(1,0).isEmpty());
        System.out.println("After taking from Factory 1: \n" + tableArea.state());
        assertTrue("Factory 1 should be empty after tiles are taken.", factory1.isEmpty());

        // Take tiles from Factory 2 and check its state
        assertFalse("Tiles should be successfully taken from Factory 2.", tableArea.take(2,0).isEmpty());
        System.out.println("After taking from Factory 2: \n" + tableArea.state());
        assertTrue("Factory 2 should be empty after tiles are taken.", factory2.isEmpty());

        // Check for the starting player tile in the Table Center
        assertTrue("The Table Center should have the starting player tile.", this.tableCenter.state().contains("S"));

        // Attempt to take a tile from a non-existing index and expect an exception
        assertThrows(IndexOutOfBoundsException.class, () -> tableArea.take(0, 100000));

        // Check if the round has not ended yet
        assertFalse("The round should not have ended yet.", tableArea.isRoundEnd());

        // Continue taking tiles from the Table Center
        while (!tableCenter.isEmpty()) {
            assertFalse("Tiles should be successfully taken from the Table Center until it's empty.", tableArea.take(0,0).isEmpty());
        }

        // Check if the Table Center is empty and if the round has ended
        assertTrue("The Table Center should be empty after all tiles are taken.", tableCenter.isEmpty());
        assertTrue("The round should end after emptying both factories and the Table Center.", tableArea.isRoundEnd());

        System.out.println("End of the round: \n" + tableArea.state());

        // Start a new round
        tableArea.startNewRound();
        System.out.println("Start of a new round: \n" + tableArea.state());
    }

}