package net.kaoriya.qb.ipv4_binsearch;

public interface IPv4Map<T>
{
    void add(IPv4 start, IPv4 end, T data);

    void add(int start, int end, T data);

    void add(IPv4Mask mask, T data);

    void clear();

    T find(IPv4 value);

    T find(int value);
}
