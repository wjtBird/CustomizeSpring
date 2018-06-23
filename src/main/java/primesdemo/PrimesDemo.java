package primesdemo;

import java.util.Arrays;

/**
 * Created by wjt on 2018/6/9.
 */
public class PrimesDemo {

    public static int[] getPrimes(int[] integers) {
        if (integers.length == 0) {
            return new int[0];
        }


        int[] primeArray = new int[0];
        for (int i = 0; i < integers.length; i++) {

            if (integers[i] < 2) {
                continue;
            }
            if (isPrime(integers[i])) {
                primeArray = Arrays.copyOf(primeArray, primeArray.length + 1);
                primeArray[primeArray.length - 1] = integers[i];
            }
        }

        return primeArray;
    }

    private static boolean isPrime(int integer) {
        boolean isPrime = true;
        for (int j = 2; j < integer / 2 + 1; j++) {
            if (integer % j == 0) {
                isPrime = false;
                break;
            }
        }

        return isPrime;
    }
}
