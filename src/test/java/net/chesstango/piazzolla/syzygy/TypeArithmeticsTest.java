package net.chesstango.piazzolla.syzygy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class TypeArithmeticsTest {


    @Test
    public void test_sum_bytes() {
        byte[] w = new byte[]{(byte) 0xBF, (byte) 0xF0};

        // sentencia original
        // int v = w[0] + ((w[1] & 0x0f) << 8);

        int v = (w[0] & 0xFF)  + ((w[1] & 0x0F) << 8);

        assertEquals(0xBF, v);
    }
}
