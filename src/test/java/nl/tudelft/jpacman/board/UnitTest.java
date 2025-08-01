package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class UnitTest {

    Unit unit;

    Square square;

    @BeforeEach
    void setUp() {
         unit = new Unit() {
            @Override
            public Sprite getSprite() {
                return null;
            }
        };
        square = mock(Square.class);
    }
    
    @Test
    void setDirectionTest(){}

    @Test
    void getDirectionTest(){}

    @Test
    void UnitDeclarationTest(){}

    @Test
    void getSquareTest(){}

    @Test
    void hasSquareTest(){}

    @Test
    void occupyTest(){}

    @Test
    void leaveSquareTest(){}

    @Test
    void invariantTest(){}

    @Test
    void squaresAheadOf(){}

}
