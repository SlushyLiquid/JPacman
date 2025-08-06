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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnitTest {

    Unit unit;

    Square square1;

    @BeforeEach
    void setUp() {
         unit = new Unit() {
            @Override
            public Sprite getSprite() {
                return null;
            }
        };
        square1 = mock(Square.class);
        doNothing().when(square1).put(any());
        doNothing().when(square1).remove(any());
    }

    @Test
    void setAndGetDirectionTest(){
        unit.setDirection(Direction.EAST);
        assertThat(unit.getDirection()).isEqualTo(Direction.EAST);
        unit.setDirection(Direction.NORTH);
        assertThat(unit.getDirection()).isEqualTo(Direction.NORTH);
        unit.setDirection(Direction.SOUTH);
        assertThat(unit.getDirection()).isEqualTo(Direction.SOUTH);
        unit.setDirection(Direction.WEST);
        assertThat(unit.getDirection()).isEqualTo(Direction.WEST);
    }

    @Test
    void nullDirectionTest() {
        unit.setDirection(null);
        assertThat(unit.getDirection()).isNull();
    }

    @Test
    void UnitDeclarationTest(){
        Unit u = new Unit() {
            @Override
            public Sprite getSprite() {
                return null;
            }
        };
        assertThat(u.getDirection()).isEqualTo(Direction.EAST);
    }

    @Test
    void getOccupyHasLeaveSquareTest(){
        List<Unit> occupants = new ArrayList<>();
        occupants.add(unit);
        when(square1.getOccupants()).thenReturn(occupants);
        Square square2 = mock(Square.class);
        doNothing().when(square2).put(any());
        doNothing().when(square2).remove(any());
        when(square2.getOccupants()).thenReturn(occupants);
        assertThat(unit.hasSquare()).as("unit should not have a square to start").isFalse();
        unit.occupy(square1);
        assertThat(unit.hasSquare()).as("unit should have a square now").isTrue();
        assertThat(unit.getSquare()).as("the square the unit has should be the same what it was set too").isEqualTo(square1);
        unit.leaveSquare();
        assertThat(unit.hasSquare()).as("unit should have left the square").isFalse();
        unit.occupy(square2);
        assertThat(unit.hasSquare()).as("unit should have a square now").isTrue();
        assertThat(unit.getSquare()).as("the square the unit has should be the same what it was set too").isEqualTo(square2);
        unit.leaveSquare();
        assertThat(unit.hasSquare()).as("unit should have left the square").isFalse();
        unit.occupy(square1);
        assertThat(unit.hasSquare()).as("unit should have a square now").isTrue();
        unit.occupy(square2);
        assertThat(unit.getSquare()).as("unit should square2 as square now").isEqualTo(square2);
    }

    @Test
    void occupyNullSquareThrowsAssertionErrorTest() {
        assertThatThrownBy(() -> unit.occupy(null))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    void leaveSquareTwiceTest() {
        List<Unit> occupants = new ArrayList<>();
        occupants.add(unit);
        when(square1.getOccupants()).thenReturn(occupants);
        unit.occupy(square1);
        unit.leaveSquare();
        unit.leaveSquare(); // Should not throw anything
        assertThat(unit.hasSquare()).isFalse();
    }

    @Test
    void unitRemovedFromPreviousSquareBeforeOccupyNewTest() {
        Square newSquare = mock(Square.class);
        List<Unit> occupants1 = new ArrayList<>();

        when(square1.getOccupants()).thenReturn(occupants1);
        doAnswer(inv -> {
            occupants1.add(inv.getArgument(0));
            return null;
        }).when(square1).put(any());

        doAnswer(inv -> {
            occupants1.remove(inv.getArgument(0));
            return null;
        }).when(square1).remove(any());
        List<Unit> occupants2 = new ArrayList<>();

        when(newSquare.getOccupants()).thenReturn(occupants2);

        doAnswer(inv -> {
            occupants2.add(inv.getArgument(0));
            return null;
        }).when(newSquare).put(any());

        doAnswer(inv -> {
            occupants2.remove(inv.getArgument(0));
            return null;
        }).when(newSquare).remove(any());
        when(square1.isAccessibleTo(any())).thenReturn(true);
        when(newSquare.isAccessibleTo(any())).thenReturn(true);
        unit.occupy(square1);
        unit.occupy(newSquare);
        verify(square1).remove(unit);
    }

    @Test
    void unitRemovedFromSquareOnLeaveTest() {
        List<Unit> occupants = new ArrayList<>();
        occupants.add(unit);
        when(square1.getOccupants()).thenReturn(occupants);
        when(square1.isAccessibleTo(any())).thenReturn(true);
        unit.occupy(square1);
        unit.leaveSquare();
        verify(square1).remove(unit);
    }

    @Test
    void invariantAfterOccupyTest() {
        List<Unit> occupants = new ArrayList<>();
        when(square1.getOccupants()).thenReturn(occupants);
        doAnswer(invocation -> {
            Unit u = invocation.getArgument(0);
            occupants.add(u);
            return null;
        }).when(square1).put(any(Unit.class));
        unit.occupy(square1);
        assertThat(unit.hasSquare()).isTrue();
        assertThat(unit.getSquare()).isEqualTo(square1);
        assertThat(square1.getOccupants()).contains(unit);
    }


    @Test
    void invariantWithoutSquareTest() {
        assertThat(unit.invariant()).isTrue();
    }

    @Test
    void invariantWithCorrectSquareOccupantTest() {
        List<Unit> occupants = new ArrayList<>();
        occupants.add(unit);
        when(square1.getOccupants()).thenReturn(occupants);

        unit.occupy(square1);
        assertThat(unit.invariant()).isTrue();
    }

    @Test
    void invariantPassesIfUnitInSquareOccupantsTest() {
        Square mockSquare = mock(Square.class);
        List<Unit> occupants = new ArrayList<>();
        when(mockSquare.getOccupants()).thenAnswer(inv -> occupants);
        doAnswer(invocation -> {
            Unit u = invocation.getArgument(0);
            occupants.add(u);
            return null;
        }).when(mockSquare).put(any(Unit.class));
        when(mockSquare.isAccessibleTo(any())).thenReturn(true);
        unit.occupy(mockSquare);
        assertThat(unit.invariant()).isTrue();
    }

    @Test
    void invariantFailsWhenUnitNotInOccupantsListTest() {
        List<Unit> occupants = new ArrayList<>();
        occupants.add(unit);
        when(square1.getOccupants()).thenReturn(occupants);
        unit.occupy(square1);
        when(square1.getOccupants()).thenReturn(Collections.emptyList());
        assertThat(unit.invariant()).isFalse();
    }


    @Test
    void squaresAheadOfTest(){
        List<Unit> occupants = new ArrayList<>();
        occupants.add(unit);
        when(square1.getOccupants()).thenReturn(occupants);
        Square square2 = mock(Square.class);
        Square square3 = mock(Square.class);
        when(square1.getSquareAt(Direction.EAST)).thenReturn(square2);
        when(square2.getSquareAt(Direction.EAST)).thenReturn(square3);
        unit.occupy(square1);
        Square result = unit.squaresAheadOf(0);
        assertThat(result).isEqualTo(square1);
        result = unit.squaresAheadOf(1);
        assertThat(result).isEqualTo(square2);
        result = unit.squaresAheadOf(2);
        assertThat(result).isEqualTo(square3);
    }

    @Test
    void squaresAheadOfWithoutOccupyingThrowsTest() {
        unit.setDirection(Direction.EAST);
        assertThatThrownBy(() -> unit.squaresAheadOf(1))
                .isInstanceOf(AssertionError.class);
    }

}
