package lesson5;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static lesson5.TestClass.factorial1;
import static lesson5.TestClass.factorial2;
import static org.junit.Assert.assertNull;

public class TestFile {

    @Test
    public void testFactorial1(){

        assertNull(factorial1(-1));

        int result = factorial1(0);
        assertEquals("Wrong value!", 1, result, 0);

        result = factorial1(1);
        assertEquals("Wrong value!", 1, result, 0);

        result = factorial1(5);
        assertEquals("Wrong value!", 120, result, 0);
    }

    @Test
    public void testFactorial2(){
        assertNull(factorial2(-1));

        int result = factorial2(0);
        assertEquals("Wrong value!", 1, result, 0);

        result = factorial2(1);
        assertEquals("Wrong value!", 1, result, 0);

        result = factorial2(5);
        assertEquals("Wrong value!", 120, result, 0);
    }

}