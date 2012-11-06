package net.kaoriya.qb.ipv4_binsearch;

public final class IPv4Integer
{

    public static int valueOf(IPv4 v)
    {
        return valueOf(v.intValue());
    }

    public static int valueOf(int v) {
        return v - 0x80000000;
    }

}
