package net.kaoriya.qb.ipv4_binsearch;

import org.junit.Test;
import static org.junit.Assert.*;

import net.kaoriya.qb.serialized_array.SerializableConverter;

public class IPv4Table2Test
{

    @Test
    public void docSample1() throws Exception
    {
        IPv4Table<String> t1 = new IPv4Table<String>();
        t1.add(IPv4.fromString("192.168.0.8"),
                IPv4.fromString("192.168.0.15"), "foo");
        t1.add(IPv4.fromString("192.168.1.100"),
                IPv4.fromString("192.168.3.200"), "bar");
        t1.add(IPv4.fromString("10.0.0.0"),
                IPv4.fromString("10.1.0.0"), "baz");

        IPv4Table2 t = new IPv4Table2(t1, new SerializableConverter<String>());

        assertEquals("foo", t.find(IPv4.fromString("192.168.0.12")));
        assertEquals("bar", t.find(IPv4.fromString("192.168.2.0")));
        assertEquals("baz", t.find(IPv4.fromString("10.0.23.254")));

        assertNull(t.find(IPv4.fromString("0.0.0.0")));
        assertNull(t.find(IPv4.fromString("9.255.255.255")));
        assertNull(t.find(IPv4.fromString("10.1.0.1")));
        assertNull(t.find(IPv4.fromString("127.0.0.1")));
    }

    @Test
    public void findByInt1() throws Exception
    {
        IPv4Table<String> t1 = new IPv4Table<String>();
        t1.add(  0,  99, "foo");
        t1.add(100, 199, "bar");

        IPv4Table2 t = new IPv4Table2(t1, new SerializableConverter<String>());

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
    public void findByInt2() throws Exception
    {
        IPv4Table<String> t1 = new IPv4Table<String>();
        t1.add(  0,  99, "foo");
        t1.add(101, 199, "bar");

        IPv4Table2 t = new IPv4Table2(t1, new SerializableConverter<String>());

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
    public void findByIP1() throws Exception
    {
        IPv4Table<String> t1 = new IPv4Table<String>();
        t1.add(IPv4.fromString("192.168.0.8"),
                IPv4.fromString("192.168.0.15"), "foo");

        IPv4Table2 t = new IPv4Table2(t1, new SerializableConverter<String>());

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
    public void findByCIDR1() throws Exception
    {
        IPv4Table<String> t1 = new IPv4Table<String>();
        t1.add(CIDR.fromString("192.168.0.0/16"), "foo");

        IPv4Table2 t = new IPv4Table2(t1, new SerializableConverter<String>());

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

    @Test
    public void findByCIDR1withHeap() throws Exception
    {
        IPv4Table<String> t1 = new IPv4Table<String>();
        t1.add(CIDR.fromString("192.168.0.0/16"), "foo");

        IPv4Table2 t = new IPv4Table2(t1, new SerializableConverter<String>(),
                false);

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
