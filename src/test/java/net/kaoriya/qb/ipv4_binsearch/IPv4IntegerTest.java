package net.kaoriya.qb.ipv4_binsearch;

import org.junit.Test;
import static org.junit.Assert.*;

public class IPv4IntegerTest
{
    @Test
    public void valueOf() {
        assertEquals(-2147483648,
                IPv4Integer.valueOf(new IPv4(0, 0, 0, 0)));
        assertEquals(-1,
                IPv4Integer.valueOf(new IPv4(127, 255, 255, 255)));
        assertEquals(0,
                IPv4Integer.valueOf(new IPv4(128, 0, 0, 0)));
        assertEquals(1,
                IPv4Integer.valueOf(new IPv4(128, 0, 0, 1)));
        assertEquals(2147483647,
                IPv4Integer.valueOf(new IPv4(255, 255, 255, 255)));
    }
}

