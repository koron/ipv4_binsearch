package net.kaoriya.qb.ipv4_binsearch;

import java.util.Iterator;

public final class IPv4 implements Iterable<Integer>
{
    private static String[] bitsPattern = new String[256];

    static {
        for (int i = 0, I = bitsPattern.length; i < I; ++i) {
            StringBuilder s = new StringBuilder();
            for (int mask = 0x80; mask != 0; mask >>= 1) {
                s.append(((i & mask) == 0) ? '0' : '1');
            }
            bitsPattern[i] = s.toString();
        }
    }

    private final int[] values = new int[4];

    public IPv4(int a, int b, int c, int d)
    {
        this.values[0] = a;
        this.values[1] = b;
        this.values[2] = c;
        this.values[3] = d;
    }

    public IPv4(int num) {
        this.values[0] = (num >> 24) & 0xff;
        this.values[1] = (num >> 16) & 0xff;
        this.values[2] = (num >>  8) & 0xff;
        this.values[3] = (num >>  0) & 0xff;
    }

    public IPv4()
    {
        this(0, 0, 0, 0);
    }

    public int getValue(int index) {
        return this.values[index];
    }

    public void setValue(int index, int value) {
        this.values[index] = value;
    }

    public int intValue() {
        return
            ((this.values[0] & 0xff) << 24) |
            ((this.values[1] & 0xff) << 16) |
            ((this.values[2] & 0xff) <<  8) |
            ((this.values[3] & 0xff) <<  0);
    }

    @Override
    public Iterator<Integer>iterator() {
        return new Iterator<Integer>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return this.index < IPv4.this.values.length;
            }
            @Override
            public Integer next() {
                return IPv4.this.values[this.index++];
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof IPv4)) {
            return false;
        } else if (o == this) {
            return true;
        } else {
            return intValue() == ((IPv4)o).intValue();
        }
    }

    @Override
    public int hashCode() {
        return intValue();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("IPv4{\"")
            .append(this.values[0]).append('.')
            .append(this.values[1]).append('.')
            .append(this.values[2]).append('.')
            .append(this.values[3])
            .append("\"}");
        return s.toString();
    }

    public static IPv4 fromString(String str)
    {
        if (str == null) {
            return null;
        }
        String[] values = str.split("\\.", 4);
        if (values.length != 4) {
            return null;
        }
        int[] nums = new int[4];
        try {
            for (int i = 0; i < 4; ++i) {
                int n = Integer.parseInt(values[i]);
                if (n < 0 || n > 255) {
                    return null;
                }
                nums[i] = n;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return new IPv4(nums[0], nums[1], nums[2], nums[3]);
    }

    public void appendBitsString(StringBuilder s) {
        s.append(bitsPattern[this.values[0]]);
        s.append(bitsPattern[this.values[1]]);
        s.append(bitsPattern[this.values[2]]);
        s.append(bitsPattern[this.values[3]]);
    }

    public String toBitsString() {
        StringBuilder s = new StringBuilder();
        appendBitsString(s);
        return s.toString();
    }
}
