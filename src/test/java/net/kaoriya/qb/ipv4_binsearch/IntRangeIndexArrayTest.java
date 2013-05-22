package net.kaoriya.qb.ipv4_binsearch;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntRangeIndexArrayTest
{

    @Test
    public void findEmpty() {
        IntRangeTable<Integer> table = new IntRangeTable<Integer>();
        IntRangeIndexArray array = new IntRangeIndexArray(table);
        assertEquals(-1, array.findIndex(50));
        assertEquals(-1, array.findIndex(150));
        assertEquals(-1, array.findIndex(250));
        assertEquals(-1, array.findIndex(350));
        assertEquals(-1, array.findIndex(450));
    }

    @Test
    public void addOrder() {
        IntRangeTable<Integer> table1 = new IntRangeTable<Integer>();
        table1.add(100, 199, 1111);
        table1.add(300, 399, 3333);
        table1.add(500, 599, 5555);
        IntRangeIndexArray array1 = new IntRangeIndexArray(table1);
        assertEquals(0, array1.findIndex(150));
        assertEquals(1, array1.findIndex(350));
        assertEquals(2, array1.findIndex(550));

        IntRangeTable<Integer> table2 = new IntRangeTable<Integer>();
        table2.add(500, 599, 5555);
        table2.add(300, 399, 3333);
        table2.add(100, 199, 1111);
        IntRangeIndexArray array2 = new IntRangeIndexArray(table2);
        assertEquals(0, array2.findIndex(150));
        assertEquals(1, array2.findIndex(350));
        assertEquals(2, array2.findIndex(550));

        IntRangeTable<Integer> table3 = new IntRangeTable<Integer>();
        table3.add(500, 599, 5555);
        table3.add(100, 199, 1111);
        table3.add(300, 399, 3333);
        IntRangeIndexArray array3 = new IntRangeIndexArray(table3);
        assertEquals(0, array3.findIndex(150));
        assertEquals(1, array3.findIndex(350));
        assertEquals(2, array3.findIndex(550));
    }

}
