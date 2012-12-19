package net.kaoriya.qb.ipv4_binsearch;

import org.junit.Test;
import static org.junit.Assert.*;

public class IPv4TableTest
{

    @Test
    public void docSample1()
    {
        IPv4Table<String> t = new IPv4Table();
        t.add(IPv4.fromString("192.168.0.8"),
                IPv4.fromString("192.168.0.15"), "foo");
        t.add(IPv4.fromString("192.168.1.100"),
                IPv4.fromString("192.168.3.200"), "bar");
        t.add(IPv4.fromString("10.0.0.0"),
                IPv4.fromString("10.1.0.0"), "baz");

        assertEquals("foo", t.find(IPv4.fromString("192.168.0.12")));
        assertEquals("bar", t.find(IPv4.fromString("192.168.2.0")));
        assertEquals("baz", t.find(IPv4.fromString("10.0.23.254")));

        assertNull(t.find(IPv4.fromString("0.0.0.0")));
        assertNull(t.find(IPv4.fromString("9.255.255.255")));
        assertNull(t.find(IPv4.fromString("10.1.0.1")));
        assertNull(t.find(IPv4.fromString("127.0.0.1")));
    }

    @Test
    public void findByInt1()
    {
        IPv4Table<String> t = new IPv4Table();
        t.add(  0,  99, "foo");
        t.add(100, 199, "bar");

        assertEquals("foo", t.find(0));
        assertEquals("foo", t.find(50));
        assertEquals("foo", t.find(99));
        assertEquals("bar", t.find(100));
        assertEquals("bar", t.find(101));
        assertEquals("bar", t.find(150));
        assertEquals("bar", t.find(199));
        assertNull(t.find(200));
        assertNull(t.find(201));
    }

    @Test
    public void findByInt2()
    {
        IPv4Table<String> t = new IPv4Table();
        t.add(  0,  99, "foo");
        t.add(101, 199, "bar");

        assertEquals("foo", t.find(0));
        assertEquals("foo", t.find(50));
        assertEquals("foo", t.find(99));
        assertNull(t.find(100));
        assertEquals("bar", t.find(101));
        assertEquals("bar", t.find(150));
        assertEquals("bar", t.find(199));
        assertNull(t.find(200));
    }

    @Test
    public void findByIP1()
    {
        IPv4Table<String> t = new IPv4Table();
        t.add(IPv4.fromString("192.168.0.8"),
                IPv4.fromString("192.168.0.15"), "foo");

        assertEquals("foo", t.find(IPv4.fromString("192.168.0.8")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.9")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.10")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.11")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.12")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.13")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.14")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.15")));

        assertNull(t.find(IPv4.fromString("192.168.0.0")));
        assertNull(t.find(IPv4.fromString("192.168.0.1")));
        assertNull(t.find(IPv4.fromString("192.168.0.6")));
        assertNull(t.find(IPv4.fromString("192.168.0.7")));
        assertNull(t.find(IPv4.fromString("192.168.0.16")));
        assertNull(t.find(IPv4.fromString("192.168.0.17")));

        assertNull(t.find(IPv4.fromString("0.0.0.0")));
        assertNull(t.find(IPv4.fromString("127.0.0.1")));
    }

    @Test
    public void findByCIDR1()
    {
        IPv4Table<String> t = new IPv4Table();
        t.add(CIDR.fromString("192.168.0.0/16"), "foo");
        //t.add(new CIDR(IPv4.fromString("192.168.0.0"), 16), "foo");

        assertEquals("foo", t.find(IPv4.fromString("192.168.0.0")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.1")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.0.2")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.1.0")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.2.0")));
        assertEquals("foo", t.find(IPv4.fromString("192.168.255.255")));

        assertNull(t.find(IPv4.fromString("0.0.0.0")));
        assertNull(t.find(IPv4.fromString("255.255.255.255")));
        assertNull(t.find(IPv4.fromString("192.167.255.255")));
        assertNull(t.find(IPv4.fromString("192.169.0.0")));
    }

}
