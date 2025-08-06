package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UnitSquareIntergrationTest {

    Unit unit1;
    Unit unit2;
    Unit unit3;

    Square square;
    Square squareEast;
    Square squareSouth;
    Square squareWest;
    Square squareNorth;

    @BeforeEach
    void setUp() {
        unit1 = new Unit() {
            @Override
            public Sprite getSprite() {
                return null;
            }
        };

        unit2 = new Unit() {
            @Override
            public Sprite getSprite() {
                return null;
            }
        };

        unit3 = new Unit() {
            @Override
            public Sprite getSprite() {
                return null;
            }
        };

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
    }

    @Test
    public void squareGetAndPutOccupantsTest(){
        List<Unit> list =  square.getOccupants();
        assertThat(list).as("Square should initially have no occupants").hasSize(0);
        square.put(unit1);
        list =  square.getOccupants();
        assertThat(list).as("Square should have one occupants").hasSize(1);
        assertThat(list.get(0)).as("The one occupant should be the only added unit").isEqualTo(unit1);
        square.put(unit2);
        square.put(unit3);
        list =  square.getOccupants();
        assertThat(list).as("Square should have one occupants").hasSize(3);
        assertThat(list.get(0)).isEqualTo(unit1);
        assertThat(list.get(1)).isEqualTo(unit2);
        assertThat(list.get(2)).isEqualTo(unit3);
    }

    @Test
    public void squareRemoveTest(){
        List<Unit> list =  square.getOccupants();
        assertThat(list).as("Square should initially have no occupants").hasSize(0);
        square.put(unit1);
        list =  square.getOccupants();
        assertThat(list).as("Square should have one occupant").hasSize(1);
        assertThat(list.get(0)).isEqualTo(unit1);
        square.remove(unit1);
        list =  square.getOccupants();
        assertThat(list).as("Square should have zero occupants").hasSize(0);
        square.put(unit2);
        square.put(unit3);
        square.put(unit1);
        list =  square.getOccupants();
        assertThat(list).as("Square should have three occupants").hasSize(3);
        square.remove(unit3);
        square.remove(unit2);
        square.remove(unit1);
        list =  square.getOccupants();
        assertThat(list).as("Square should have zero occupants").hasSize(0);
    }

    @Test
    public void squareInvariantTest(){
        List<Unit> list =  square.getOccupants();
        assertThat(list).as("Square should initially have no occupants").hasSize(0);
        square.put(unit1);
        assertThat(square.invariant()).isTrue();
        square.put(unit2);
        assertThat(square.invariant()).isTrue();
        square.put(unit3);
        assertThat(square.invariant()).isFalse();
    }

    @Test
    void getOccupyHasLeaveTest(){
        assertThat(unit1.hasSquare()).as("unit should not have a square to start").isFalse();
        unit1.occupy(square);
        assertThat(unit1.hasSquare()).as("unit should have a square now").isTrue();
        assertThat(unit1.getSquare()).as("the square the unit has should be the same what it was set too").isEqualTo(square);
        unit1.leaveSquare();
        assertThat(unit1.hasSquare()).as("unit should have left the square").isFalse();
        unit1.occupy(squareEast);
        assertThat(unit1.hasSquare()).as("unit should have a square now").isTrue();
        assertThat(unit1.getSquare()).as("the square the unit has should be the same what it was set too").isEqualTo(squareEast);
        unit1.leaveSquare();
        assertThat(unit1.hasSquare()).as("unit should have left the square").isFalse();
        unit1.occupy(square);
        assertThat(unit1.hasSquare()).as("unit should have a square now").isTrue();
        unit1.occupy(squareEast);
        assertThat(unit1.getSquare()).as("unit should square2 as square now").isEqualTo(squareEast);
    }

    @Test
    void reoccupySameSquareTest() {
        unit1.occupy(square);
        unit1.occupy(square);
        List<Unit> occupants = square.getOccupants();
        assertThat(occupants).containsExactly(unit1);
    }

    @Test
    void occupyReplacesOldSquareTest() {
        unit1.occupy(square);
        unit1.occupy(squareEast);
        assertThat(square.getOccupants()).doesNotContain(unit1);
        assertThat(squareEast.getOccupants()).contains(unit1);
    }

    @Test
    void multipleUnitsMoveTest() {
        unit1.occupy(square);
        unit2.occupy(squareEast);
        unit1.occupy(squareEast);
        assertThat(square.getOccupants()).doesNotContain(unit1);
        assertThat(squareEast.getOccupants()).contains(unit1, unit2);
    }

    @Test
    void leaveWithoutOccupyTest() {
        unit1.leaveSquare(); // shouldn't throw or crash
        assertThat(unit1.hasSquare()).isFalse();
    }

    @Test
    void squaresAheadOf(){
        Square squareEastEast = new Square() {
            @Override
            public boolean isAccessibleTo(Unit unit) {
                return false;
            }

            @Override
            public Sprite getSprite() {
                return null;
            }
        };
        square.link(squareEast, Direction.EAST);
        squareEast.link(squareEastEast, Direction.EAST);
        unit1.occupy(square);
        Square result = unit1.squaresAheadOf(0);
        assertThat(result).isEqualTo(square);
        result = unit1.squaresAheadOf(1);
        assertThat(result).isEqualTo(squareEast);
        result = unit1.squaresAheadOf(2);
        assertThat(result).isEqualTo(squareEastEast);
    }


}
