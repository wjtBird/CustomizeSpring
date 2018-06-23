package primesdemo;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wjt on 2018/6/9.
 */
public class PrimesDemoTest {

    @Test
    public void NotPrimesTest() {
        int[] emptyArray = new int[0];
        Assert.assertArrayEquals(emptyArray, PrimesDemo.getPrimes(new int[]{0,6,4}));
        Assert.assertArrayEquals(emptyArray, PrimesDemo.getPrimes(new int[]{0,4,6,8,10,14}));
    }


    @Test
    public void primesTest() {
        int[] expectArray = new int[]{2,3,5,7,11,23,43};
        Assert.assertArrayEquals(expectArray,PrimesDemo.getPrimes(new int[]{1,2,3,4,5,7,11,34,23,55,43}));
    }
}
