//Douglas MacGregor
//V01008370
//August 05 2025

package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class SquareTest {

    Square square;
    Square squareNorth;
    Square squareEast;
    Square squareSouth;
    Square squareWest;

    Direction northDirection;
    Direction southDirection;
    Direction eastDirection;
    Direction westDirection;

    Unit unit;
    Unit unit2;
    Unit unit3;

    @BeforeEach
    void setUp() {
        square = new Square() {
            @Override
            public boolean isAccessibleTo(Unit unit) {
                return false;
            }

            @Override
            public Sprite getSprite() {
                return null;
            }
        };

        squareNorth = new Square() {
            @Override
            public boolean isAccessibleTo(Unit unit) {
                return false;
            }

            @Override
            public Sprite getSprite() {
                return null;
            }
        };

        squareSouth = new Square() {
            @Override
            public boolean isAccessibleTo(Unit unit) {
                return false;
            }

            @Override
            public Sprite getSprite() {
                return null;
            }
        };

        squareEast = new Square() {
            @Override
            public boolean isAccessibleTo(Unit unit) {
                return false;
            }

            @Override
            public Sprite getSprite() {
                return null;
            }
        };

        squareWest = new Square() {
            @Override
            public boolean isAccessibleTo(Unit unit) {
                return false;
            }

            @Override
            public Sprite getSprite() {
                return null;
            }
        };

        westDirection = Direction.WEST;
        eastDirection = Direction.EAST;
        northDirection = Direction.NORTH;
        southDirection = Direction.SOUTH;
        unit = Mockito.mock(Unit.class);
        unit2 = Mockito.mock(Unit.class);
        unit3 = Mockito.mock(Unit.class);
    }

    @Test
    public void squareTest() {
        List<Unit> list =  square.getOccupants();
        assertThat(list).hasSize(0);
        Square sq = square.getSquareAt(northDirection);
        assertThat(sq).isNull();
        sq = square.getSquareAt(southDirection);
        assertThat(sq).isNull();
        sq = square.getSquareAt(westDirection);
        assertThat(sq).isNull();
        sq = square.getSquareAt(eastDirection);
        assertThat(sq).isNull();
    }

    @Test
    public void linkAndGetTest(){
        square.link(squareNorth, northDirection);
        square.link(squareEast, eastDirection);
        square.link(squareSouth, southDirection);
        square.link(squareWest, westDirection);
        assertThat(square.getSquareAt(northDirection)).as("North link and get failed").isEqualTo(squareNorth);
        assertThat(square.getSquareAt(southDirection)).as("South link and get failed").isEqualTo(squareSouth);
        assertThat(square.getSquareAt(westDirection)).as("West link and get failed").isEqualTo(squareWest);
        assertThat(square.getSquareAt(eastDirection)).as("East link and get failed").isEqualTo(squareEast);
    }

    @Test
    void directionLinkConsistencyTest() {
        square.link(squareEast, Direction.EAST);
        squareEast.link(square, Direction.WEST);
        assertThat(square.getSquareAt(Direction.EAST)).isEqualTo(squareEast);
        assertThat(squareEast.getSquareAt(Direction.WEST)).isEqualTo(square);
    }

    @Test
    void unlinkedDirectionReturnsNullTest() {
        assertThat(square.getSquareAt(Direction.SOUTH)).isNull();
    }

    @Test
    public void linkWithNullDirectionTest() {
        assertThatThrownBy(() -> square.link(squareNorth, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void getAndPutOccupantsTest(){
        List<Unit> list =  square.getOccupants();
        assertThat(list).as("Square should initially have no occupants").hasSize(0);
        when(unit.hasSquare()).thenReturn(true);
        when(unit.getSquare()).thenReturn(square);
        when(unit2.hasSquare()).thenReturn(true);
        when(unit2.getSquare()).thenReturn(square);
        when(unit3.hasSquare()).thenReturn(true);
        when(unit3.getSquare()).thenReturn(square);
        square.put(unit);
        list =  square.getOccupants();
        assertThat(list).as("Square should have one occupants").hasSize(1);
        assertThat(list.get(0)).as("The one occupant should be the only added unit").isEqualTo(unit);
        square.put(unit2);
        square.put(unit3);
        list =  square.getOccupants();
        assertThat(list).as("Square should have one occupants").hasSize(3);
        assertThat(list.get(0)).isEqualTo(unit);
        assertThat(list.get(1)).isEqualTo(unit2);
        assertThat(list.get(2)).isEqualTo(unit3);
    }

    @Test
    public void putNullTest() {
        // should fail
        assertThatThrownBy(() -> square.put(null))
                .isInstanceOf(AssertionError.class);
        square.put(unit);
        assertThatThrownBy(() -> square.put(unit)).isInstanceOf(AssertionError.class);;
    }

    @Test
    public void putDuplicateUnitTest() {
        // should fail
        when(unit.hasSquare()).thenReturn(true);
        when(unit.getSquare()).thenReturn(square);
        square.put(unit);
        assertThatThrownBy(() -> square.put(unit))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    public void getSquareAtNullTest() {
        // should return null
        assertThat(square.getSquareAt(null)).isNull();
    }

    @Test
    public void removeTest(){
        List<Unit> list =  square.getOccupants();
        assertThat(list).as("Square should initially have no occupants").hasSize(0);
        when(unit.hasSquare()).thenReturn(true);
        when(unit.getSquare()).thenReturn(square);
        when(unit2.hasSquare()).thenReturn(true);
        when(unit2.getSquare()).thenReturn(square);
        when(unit3.hasSquare()).thenReturn(true);
        when(unit3.getSquare()).thenReturn(square);
        square.put(unit);
        list =  square.getOccupants();
        assertThat(list).as("Square should have one occupant").hasSize(1);
        assertThat(list.get(0)).isEqualTo(unit);
        square.remove(unit);
        list =  square.getOccupants();
        assertThat(list).as("Square should have zero occupants").hasSize(0);
        square.put(unit2);
        square.put(unit3);
        square.put(unit);
        list =  square.getOccupants();
        assertThat(list).as("Square should have three occupants").hasSize(3);
        square.remove(unit3);
        square.remove(unit2);
        square.remove(unit);
        list =  square.getOccupants();
        assertThat(list).as("Square should have zero occupants").hasSize(0);
    }

    @Test
    public void removeNullTest() {
        // should fail
        assertThatThrownBy(() -> square.remove(null))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    public void invariantTest(){
        List<Unit> list =  square.getOccupants();
        assertThat(list).as("Square should initially have no occupants").hasSize(0);
        when(unit.hasSquare()).thenReturn(true);
        when(unit.getSquare()).thenReturn(square);
        when(unit2.hasSquare()).thenReturn(true);
        when(unit2.getSquare()).thenReturn(square);
        when(unit3.hasSquare()).thenReturn(true);
        when(unit3.getSquare()).thenReturn(squareNorth);
        square.put(unit);
        assertThat(square.invariant()).isTrue();
        square.put(unit2);
        assertThat(square.invariant()).isTrue();
        square.put(unit3);
        assertThat(square.invariant()).isFalse();
    }

    @Test
    public void invariantFailingTest() {
        when(unit.hasSquare()).thenReturn(true);
        when(unit.getSquare()).thenReturn(squareNorth);
        square.put(unit);
        assertThat(square.invariant()).isFalse();
    }

}
