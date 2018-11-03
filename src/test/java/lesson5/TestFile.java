package lesson5;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static lesson5.TestClass.square;
import static lesson5.TestClass.squareRoot;
import static lesson5.TestClass.cosine;

public class TestFile {

    @Test
    public void testRoot(){
        double result = squareRoot(25);
        assertEquals("Wrong value!", 5, result, 0);
    }

    @Test
    public void testSquare(){
        double result = square(5);
        assertEquals("Wrong value!", 25, result, 0);
    }

    @Test
    public void testCosine(){
        double result = cosine(0);
        assertEquals("Wrong value!", 1, result, 0);

        result = cosine(Math.PI);
        assertEquals("Wrong value!", result, -1, 0);
    }
}