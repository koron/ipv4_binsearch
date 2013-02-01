package net.kaoriya.qb.ipv4_binsearch;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CIDRTest
{

    @Test
    public void startAndEnd0()
    {
        CIDR m0 = new CIDR(new IPv4(0), 0);
        Assert.assertEquals(0, m0.getStart().intValue());
        Assert.assertEquals(0xffffffff, m0.getEnd().intValue());

        CIDR m1 = new CIDR(new IPv4(0), 1);
        Assert.assertEquals(0, m1.getStart().intValue());
        Assert.assertEquals(0x7fffffff, m1.getEnd().intValue());

        CIDR m2 = new CIDR(new IPv4(0), 2);
        Assert.assertEquals(0, m2.getStart().intValue());
        Assert.assertEquals(0x3fffffff, m2.getEnd().intValue());

        CIDR m3 = new CIDR(new IPv4(0), 3);
        Assert.assertEquals(0, m3.getStart().intValue());
        Assert.assertEquals(0x1fffffff, m3.getEnd().intValue());

        CIDR m4 = new CIDR(new IPv4(0), 4);
        Assert.assertEquals(0, m4.getStart().intValue());
        Assert.assertEquals(0x0fffffff, m4.getEnd().intValue());
    }

    @Test
    public void fromStringNG()
    {
        Assert.assertNull(CIDR.fromString(null));
        Assert.assertNull(CIDR.fromString("/"));
        Assert.assertNull(CIDR.fromString("foo/"));
        Assert.assertNull(CIDR.fromString("foo/bar"));
        Assert.assertNull(CIDR.fromString("0.0.0.0/"));
        Assert.assertNull(CIDR.fromString("0.0.0.0/foo"));
        Assert.assertNull(CIDR.fromString("0.0.0.0/-1"));
        Assert.assertNull(CIDR.fromString("0.0.0.0/33"));
    }

    @Test
    public void fromStringOK()
    {
        Assert.assertEquals(new CIDR(new IPv4(0, 0, 0, 0), 0),
                CIDR.fromString("0.0.0.0/0"));
        Assert.assertEquals(new CIDR(new IPv4(0, 0, 0, 0), 1),
                CIDR.fromString("0.0.0.0/1"));
        Assert.assertEquals(new CIDR(new IPv4(0, 0, 0, 0), 2),
                CIDR.fromString("0.0.0.0/2"));
        Assert.assertEquals(new CIDR(new IPv4(0, 0, 0, 0), 32),
                CIDR.fromString("0.0.0.0/32"));
    }

    @Test
    public void fromStringOK_SubnetMask()
    {
        Assert.assertEquals(CIDR.fromString("0.0.0.0/0"),
                CIDR.fromString("0.0.0.0/0.0.0.0"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/1"),
                CIDR.fromString("0.0.0.0/128.0.0.0"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/2"),
                CIDR.fromString("0.0.0.0/192.0.0.0"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/3"),
                CIDR.fromString("0.0.0.0/224.0.0.0"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/4"),
                CIDR.fromString("0.0.0.0/240.0.0.0"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/5"),
                CIDR.fromString("0.0.0.0/248.0.0.0"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/6"),
                CIDR.fromString("0.0.0.0/252.0.0.0"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/7"),
                CIDR.fromString("0.0.0.0/254.0.0.0"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/8"),
                CIDR.fromString("0.0.0.0/255.0.0.0"));

        Assert.assertEquals(CIDR.fromString("0.0.0.0/31"),
                CIDR.fromString("0.0.0.0/255.255.255.254"));
        Assert.assertEquals(CIDR.fromString("0.0.0.0/32"),
                CIDR.fromString("0.0.0.0/255.255.255.255"));
    }

    @Test
    public void notMatch()
    {
        assertThat(CIDR.fromString("1.2.3.4/0"),
                is(not(CIDR.fromString("1.2.3.4/1"))));
        assertThat(CIDR.fromString("1.2.3.4/10"),
                is(not(CIDR.fromString("1.2.3.4/11"))));
        assertThat(CIDR.fromString("1.1.1.1/31"),
                is(not(CIDR.fromString("1.1.1.0/31"))));
    }
}
