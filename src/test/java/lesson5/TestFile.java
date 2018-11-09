package lesson5;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static lesson5.TestClass.factorial1;
import static lesson5.TestClass.factorial2;
import static org.junit.Assert.assertNull;

public class TestFile {

    @Test
    public void testFactorial1Null(){

        assertNull(factorial1(-1));
    }

    @Test
    public void testFactorial1Zero(){

        int result = factorial1(0);
        assertEquals("Wrong value!", 1, result, 0);
    }

    @Test
    public void testFactorial1One(){

        int result = factorial1(1);
        assertEquals("Wrong value!", 1, result, 0);
    }

    @Test
    public void testFactorial1Five(){

        int result = factorial1(5);
        assertEquals("Wrong value!", 120, result, 0);
    }

    @Test
    public void testFactorial2Null(){
        assertNull(factorial2(-1));
    }

    @Test
    public void testFactorial2Zero(){

        int result = factorial2(0);
        assertEquals("Wrong value!", 1, result, 0);
    }

    @Test
    public void testFactorial2One(){

        int result = factorial2(1);
        assertEquals("Wrong value!", 1, result, 0);
    }

    @Test
    public void testFactorial2Five(){

        int result = factorial2(5);
        assertEquals("Wrong value!", 120, result, 0);
    }

}