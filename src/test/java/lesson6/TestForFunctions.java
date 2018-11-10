package lesson6;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import annotation.Smoke;
import org.junit.platform.engine.TestExecutionResult;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static lesson6.TestFunctions.circleArea;
import static lesson6.TestFunctions.rectangleArea;
import static lesson6.TestFunctions.triangleArea;

public class TestForFunctions{

    @ParameterizedTest(name = "Check value ''{0}'' = ''{1}''")
    @DisplayName("Check circle area function with csv source")
    @CsvSource({ "1, 3.141592653589793", "2, 12.566370614359172", "100, 31415.926535897932" })
    @Tag("positive")
    public void circleValueTest(double value, double expectedResult){
        double result = circleArea(value);
        assertThat("Incorrect!", result, equalTo(expectedResult));
    }

    @ParameterizedTest(name = "Check invalid value ''{0}'' for circle area")
    @DisplayName("Check circle area function with value source")
    @ValueSource(doubles = {-1.1, 0.0, Double.MAX_VALUE })
    @Tag("negative")
    public void circleValueExceptionsTest(double value){
        assertThrows(IllegalArgumentException.class, () -> {
            circleArea(value);
        });
    }

    @ParameterizedTest(name = "Check rectangle ''{0}'' x ''{1}'' square = ''{2}''")
    @DisplayName("Check rectangle area function with method source")
    @MethodSource("valuesProvider")
    @Tag("positive")
    public void rectangleTest(double x, double y, double expectedResult){
        double result = rectangleArea(x, y);
        assertThat("Incorrect!", result, equalTo(expectedResult));
    }

    private static Stream<Arguments> valuesProvider() {
        return Stream.of(
                Arguments.of(1.0, 1.0, 1.0),
                Arguments.of(0.5, 0.3, 0.15),
                Arguments.of(2.0, 5.5, 11.0)
        );
    }

    @ParameterizedTest(name = "Check rectangle ''{0}'' x ''{1}''")
    @DisplayName("Check invalid values for rectangle area function")
    @MethodSource("invalidRectangleValuesProvider")
    @Tag("negative")
    public void rectangleTest(double x, double y){
        assertThrows(IllegalArgumentException.class, () -> {
            rectangleArea(x, y);;
        });
    }

    private static Stream<Arguments> invalidRectangleValuesProvider() {
        return Stream.of(
                Arguments.of(0.0, new Random().nextDouble()),
                Arguments.of(-0.1, new Random().nextDouble()),
                Arguments.of( new Random().nextDouble(), 0.0),
                Arguments.of( new Random().nextDouble(), - new Random().nextDouble()),
                Arguments.of( 3.5, Double.MAX_VALUE),
                Arguments.of(Double.MAX_VALUE, 2.0)
        );
    }

    @ParameterizedTest(name = "Check triangle with a = ''{0}'' and h = ''{1}'' square = ''{2}''")
    @DisplayName("Check triangle area function with method source")
    @MethodSource("triangleValuesProvider")
    @Tag("positive")
    public void triangleTest(double a, double h, double expectedResult){
        double result = triangleArea(a, h);
        assertThat("Incorrect!", result, equalTo(expectedResult));
    }

    private static Stream<Arguments> triangleValuesProvider() {
        return Stream.of(
                Arguments.of(1.0, 1.0, 0.58),
                Arguments.of(0.5, 0.3, 0.075),
                Arguments.of(2.0, 5.5, 5.5)
        );
    }

    @ParameterizedTest(name = "Check triangle with a = ''{0}'' and h = ''{1}''")
    @DisplayName("Check invalid values for triangle area function")
    @MethodSource("invalidTriangleValuesProvider")
    @Tag("negative")
    @Smoke
    public void triangleTest(double a, double h){
        assertThrows(IllegalArgumentException.class, () -> {
            triangleArea(a, h);
        });
    }

    private static Stream<Arguments> invalidTriangleValuesProvider() {
        return Stream.of(
                Arguments.of(0.0, new Random().nextDouble()),
                Arguments.of(-0.1, new Random().nextDouble()),
                Arguments.of( new Random().nextDouble(), 0.0),
                Arguments.of( new Random().nextDouble(), - new Random().nextDouble()),
                Arguments.of( 3.5, Double.MAX_VALUE),
                Arguments.of(Double.MAX_VALUE, 2.0)
        );
    }
}