package net.kaoriya.qb.ipv4_binsearch;

public final class CIDR implements IPv4Mask
{
    public final int[] MASKS = new int[] {
        0x00000000, 0x80000000, 0xc0000000, 0xe0000000,
        0xf0000000, 0xf8000000, 0xfc000000, 0xfe000000,
        0xff000000, 0xff800000, 0xffc00000, 0xffe00000,
        0xfff00000, 0xfff80000, 0xfffc0000, 0xfffe0000,
        0xffff0000, 0xffff8000, 0xffffc000, 0xffffe000,
        0xfffff000, 0xfffff800, 0xfffffc00, 0xfffffe00,
        0xffffff00, 0xffffff80, 0xffffffc0, 0xffffffe0,
        0xfffffff0, 0xfffffff8, 0xfffffffc, 0xfffffffe,
        0xffffffff,
    };

    private final IPv4 address;
    private final int addressInt;
    private final int mask;

    private IPv4 addressStart;
    private IPv4 addressEnd;

    public CIDR(IPv4 address, int mask) {
        this.address = address;
        this.addressInt = address.intValue();
        this.mask = mask;
    }

    public IPv4 getAddress() { return this.address; }
    public int getMask() { return this.mask; }

    public boolean match(int value) {
        return (value & MASKS[this.mask]) == this.addressInt ? true : false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(obj instanceof CIDR)) {
            return false;
        }

        CIDR that = (CIDR)obj;
        if ((this.address == null && that.address != null) ||
                (this.address != null && !this.address.equals(that.address))) {
            return false;
        } else if (this.mask != this.mask) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("CIDR{")
            .append("address=").append(this.address).append(",")
            .append("mask=").append(this.mask)
            .append("}");
        return s.toString();
    }

    public static CIDR fromString(String s) {
        if (s == null) {
            return null;
        }

        String[] values = s.split("/", 2);
        if (values.length != 2) {
            return null;
        }

        IPv4 address = IPv4.fromString(values[0]);
        if (address == null) {
            return null;
        }

        IPv4 ipMask = IPv4.fromString(values[1]);
        if (ipMask != null) {
            return fromSubnetMask(address, ipMask);
        }

        int mask = -1;
        try {
            mask = Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        if (mask < 0 || mask > 32) {
            return null;
        }

        return new CIDR(address, mask);
    }

    public static CIDR fromSubnetMask(IPv4 address, IPv4 mask) {
        if (address == null || mask == null) {
            return null;
        }

        int maskValue = 0;
        for (int value = mask.intValue(); (value & 0x80000000) != 0;
                value <<= 1) {
            ++maskValue;
        }

        return new CIDR(address, maskValue);
    }

    public String toBitsString() {
        StringBuilder s = new StringBuilder();
        this.address.appendBitsString(s);
        s.setLength(this.mask);
        return s.toString();
    }

    public IPv4 getStart()
    {
        if (this.addressStart == null) {
            int n = this.addressInt & MASKS[this.mask];
            this.addressStart = new IPv4(n);
        }
        return this.addressStart;
    }

    public IPv4 getEnd()
    {
        if (this.addressEnd == null) {
            int m = MASKS[this.mask];
            int n = (this.addressInt & m) | (0xffffffff & ~m);
            this.addressEnd = new IPv4(n);
        }
        return this.addressEnd;
    }

}
