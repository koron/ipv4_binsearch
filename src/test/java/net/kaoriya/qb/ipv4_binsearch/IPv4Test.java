package net.kaoriya.qb.ipv4_binsearch;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public class IPv4Test
{

    @Test
    public void getValue()
    {
        IPv4 v = new IPv4(10, 20, 30, 40);
        Assert.assertEquals(10, v.getValue(0));
        Assert.assertEquals(20, v.getValue(1));
        Assert.assertEquals(30, v.getValue(2));
        Assert.assertEquals(40, v.getValue(3));
    }

    @Test
    public void setValue()
    {
        IPv4 v = new IPv4();
        v.setValue(1, 20);
        v.setValue(3, 40);
        Assert.assertEquals( 0, v.getValue(0));
        Assert.assertEquals(20, v.getValue(1));
        Assert.assertEquals( 0, v.getValue(2));
        Assert.assertEquals(40, v.getValue(3));
    }

    @Test
    public void iterator()
    {
        IPv4 v = new IPv4(10, 20, 30, 40);
        Iterator<Integer> iter = v.iterator();

        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(10, iter.next().intValue());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(20, iter.next().intValue());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(30, iter.next().intValue());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(40, iter.next().intValue());
        Assert.assertFalse(iter.hasNext());
    }

    @Test
    public void intValue()
    {
        Assert.assertEquals(0, new IPv4(0, 0, 0, 0).intValue());
        Assert.assertEquals(1, new IPv4(0, 0, 0, 1).intValue());
        Assert.assertEquals(256, new IPv4(0, 0, 1, 0).intValue());
        Assert.assertEquals(65536, new IPv4(0, 1, 0, 0).intValue());
        Assert.assertEquals(16777216, new IPv4(1, 0, 0, 0).intValue());
        Assert.assertEquals(2147483647,
                new IPv4(127, 255, 255, 255).intValue());
        Assert.assertEquals(-2147483648, new IPv4(128, 0, 0, 0).intValue());
        Assert.assertEquals(-2, new IPv4(255, 255, 255, 254).intValue());
        Assert.assertEquals(-1, new IPv4(255, 255, 255, 255).intValue());
    }

    @Test
    public void fromInt()
    {
        Assert.assertEquals(new IPv4(0, 0, 0, 0), new IPv4(0));
        Assert.assertEquals(new IPv4(0, 0, 0, 1), new IPv4(1));
        Assert.assertEquals(new IPv4(0, 0, 1, 0), new IPv4(256));
        Assert.assertEquals(new IPv4(0, 1, 0, 0), new IPv4(65536));
        Assert.assertEquals(new IPv4(1, 0, 0, 0), new IPv4(16777216));
        Assert.assertEquals(new IPv4(127, 255, 255, 255),
                new IPv4(2147483647));
        Assert.assertEquals(new IPv4(128, 0, 0, 0), new IPv4(-2147483648));
        Assert.assertEquals(new IPv4(255, 255, 255, 254), new IPv4(-2));
        Assert.assertEquals(new IPv4(255, 255, 255, 255), new IPv4(-1));
    }

    @Test
    public void equals()
    {
        IPv4 v1a = new IPv4(0, 0, 0, 0);
        IPv4 v2a = new IPv4(127, 0, 0, 1);
        IPv4 v3a = new IPv4(192, 168, 0, 1);
        IPv4 v4a = new IPv4(192, 168, 0, 254);
        IPv4 v1b = new IPv4(0, 0, 0, 0);
        IPv4 v2b = new IPv4(127, 0, 0, 1);
        IPv4 v3b = new IPv4(192, 168, 0, 1);
        IPv4 v4b = new IPv4(192, 168, 0, 254);

        Assert.assertEquals(v1a, v1b);
        Assert.assertEquals(v2a, v2b);
        Assert.assertEquals(v3a, v3b);
        Assert.assertEquals(v4a, v4b);

        Assert.assertFalse(v1a.equals(v2a));
        Assert.assertFalse(v1a.equals(v3a));
        Assert.assertFalse(v1a.equals(v4a));
    }

    @Test
    public void fromString()
    {
        Assert.assertNull(IPv4.fromString(null));
        Assert.assertNull(IPv4.fromString(""));
        Assert.assertNull(IPv4.fromString("1"));
        Assert.assertNull(IPv4.fromString("1.2"));
        Assert.assertNull(IPv4.fromString("1.2.3"));
        Assert.assertEquals(new IPv4(1, 2, 3, 4), IPv4.fromString("1.2.3.4"));
        Assert.assertNull(IPv4.fromString("1.2.3.4.5"));

        Assert.assertEquals(new IPv4(0, 1, 2, 3), IPv4.fromString("0.1.2.3"));
        Assert.assertNull(IPv4.fromString("-1.1.2.3"));
        Assert.assertNull(IPv4.fromString("256.1.2.3"));
        Assert.assertNull(IPv4.fromString("0.-2.2.3"));
        Assert.assertNull(IPv4.fromString("0.257.2.3"));

        Assert.assertNull(IPv4.fromString("0.1.2.foo"));
    }
}
