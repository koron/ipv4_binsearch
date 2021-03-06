package net.kaoriya.qb.ipv4_binsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

import net.kaoriya.qb.serialized_array.MessagePackableConverter;

public class Benchmark1
{

    public static class Watch {
        private final String message;
        private long startTime;
        private long accumulateTime = 0;
        public Watch(String message) {
            this.message = message;
            start();
        }
        public void stop() {
            if (this.startTime >= 0) {
                split();
            }
            System.out.println(String.format(
                        "[%2$12.6fs] %1$s",
                        this.message,
                        this.accumulateTime * 0.000000001));
        }
        public void start() {
            this.startTime = System.nanoTime();
        }
        public void split() {
            this.accumulateTime += System.nanoTime() - this.startTime;
            this.startTime = -1;
        }
        public long getAccumulateTime() {
            return this.accumulateTime;
        }
    }

    public static class Value implements MessagePackable {
        private String stringProperty;
        public Value() { }
        public Value(String stringValue) {
            this.stringProperty = stringValue;
        }
        public String getStringProperty() {
            return this.stringProperty;
        }
        public void setStringProperty(String value) {
            this.stringProperty = value;
        }
        public void readFrom(Unpacker unpacker) throws IOException {
            this.stringProperty = unpacker.readString();
        }
        public void writeTo(Packer packer) throws IOException {
            packer.write(this.stringProperty);
        }
    }

    public static final String CHARS =
        "0123456789_-ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String randomString(Random r, int len) {
        int unit = len / 10;
        int finalLen = (int)(len + unit * r.nextGaussian());
        StringBuilder s = new StringBuilder(finalLen);
        for (int i = 0; i < finalLen; ++i) {
            s.append(CHARS.charAt(r.nextInt(CHARS.length())));
        }
        return s.toString();
    }

    public static IPv4Table<Value> newTable1(long seed, int count, int size) {
        Random r = new Random(seed);
        IPv4Table<Value> t = new IPv4Table<Value>();
        long unit = 0xFFFFFFFFL / count;
        long start = 0;
        for (int i = 0; i < count; ++i) {
            long end = start + unit - 1;
            Value value = new Value(randomString(r, size));
            t.add(start, end, value);
            start += unit;
        }
        return t;
    }

    /**
     * Create a IPv4Table2 (off-heap version).
     */
    public static IPv4Table2<Value> newTable2(long seed, int count, int size)
        throws Exception
    {
        return new IPv4Table2<Value>(newTable1(seed, count, size),
                new MessagePackableConverter<Value>(Value.class), true);
    }

    /**
     * Create a IPv4Table2 (on-heap version).
     */
    public static IPv4Table2<Value> newTable3(long seed, int count, int size)
        throws Exception
    {
        return new IPv4Table2<Value>(newTable1(seed, count, size),
                new MessagePackableConverter<Value>(Value.class), false);
    }

    public static List<IPv4Table<Value>> newTableList(int count) {
        ArrayList<IPv4Table<Value>> list =
            new ArrayList<IPv4Table<Value>>(count);
        for (int i = 0; i < count; ++i) {
            list.add(newTable1(i, 1000000, 50));
        }
        return list;
    }

    static abstract class Bench1 {
        String label;
        long count;

        Bench1(String label) {
            this.label = label;
        }

        Bench1 run() throws Exception {
            System.out.format("benchmark query %1$s in 10 sec:", this.label);
            IPv4TableBase<Value> t = newTable();
            System.gc();
            long count = query(t, 10000, 0);
            System.out.println(String.format(" %1$.2f/sec (total %2$d)",
                        count / 10.0, count));
            this.count = count;
            return this;
        }

        static long query(
                IPv4TableBase<Value> t,
                long msec,
                long seed)
        {
            Random r = new Random(seed);
            long count = 0;
            long end = System.currentTimeMillis() + msec;
            while (System.currentTimeMillis() < end) {
                Value v = t.find(r.nextInt());
                ++count;
            }
            return count;
        }

        long getCount() {
            return this.count;
        }

        abstract IPv4TableBase<Value> newTable() throws Exception;
    }

    public static void benchmark1() throws Exception
    {
        System.out.println("Benchmark1 executing:");
        System.out.println();

        long count1, count2, count3;

        Bench1 b1 = new Bench1("IPv4Table") {
            IPv4TableBase<Value> newTable() throws Exception {
                return newTable1(0, 1000000, 50);
            }
        }.run();

        Bench1 b2 = new Bench1("IPv4Table2 (off-heap)") {
            IPv4TableBase<Value> newTable() throws Exception {
                return newTable2(0, 1000000, 50);
            }
        }.run();

        Bench1 b3 = new Bench1("IPv4Table2 (heap)") {
            IPv4TableBase<Value> newTable() throws Exception {
                return newTable3(0, 1000000, 50);
            }
        }.run();

        System.out.println();
        System.out.println("Benchmark#1 results:");
        System.out.println(String.format(
                    "    off-heap/objects ratio: %1$7.2f%%",
                    (double)b2.getCount() * 100.0 / b1.getCount()));
        System.out.println(String.format(
                    "    heap/objects ratio:     %1$7.2f%%",
                    (double)b3.getCount() * 100.0 / b1.getCount()));
        System.out.println(String.format(
                    "    heap/off-heap ratio:    %1$7.2f%%",
                    (double)b3.getCount() * 100.0 / b2.getCount()));
    }

    public static void benchmark2(long seed, int count) throws Exception
    {
        System.out.println("Benchmark2 executing:");
        System.out.println();

        Random r1 = new Random(seed);
        Random r2 = new Random(seed);

        IPv4Table<Value> t1 = null;
        IPv4Table2<Value> t2 = newTable2(0, 1000000, 50);
        Watch w0 = new Watch("GC: init");
        System.gc();
        w0.stop();
        Watch w1 = new Watch("GC: under holding many object");
        Watch w2 = new Watch("GC: under holding few object");

        for (int i = 0; i < count; ++i) {
            t1 = newTable1(r1.nextLong(), 1000000, 50);
            t2 = null;
            w1.start();
            System.gc();
            w1.split();

            t1 = null;
            t2 = newTable2(r1.nextLong(), 1000000, 50);
            w2.start();
            System.gc();
            w2.split();
        }
        w1.stop();
        w2.stop();

        long a1 = w1.getAccumulateTime();
        long a2 = w2.getAccumulateTime();
        System.out.println();
        System.out.println(String.format(
                    "Benchmark#2 ratio: %1$.2f%%",
                    (double)a2 * 100.0 / a1));
    }

    static abstract class Bench3 {
        long generateTime;
        long gcTime;

        abstract IPv4TableBase<Value> newTable(long seed) throws Exception;

        Bench3 run(long seed, int count) throws Exception {
            Random r = new Random(seed);

            IPv4TableBase<Value> t = newTable(0);
            Watch w0 = new Watch("GC: init");
            System.gc();
            w0.stop();
            Watch w1 = new Watch("Generate table");
            Watch w2 = new Watch("GC: after generating");
            for (int i = 0; i < count; ++i) {
                w1.start();
                t = newTable(r.nextLong());
                w1.split();
                w2.start();
                System.gc();
                w2.split();
            }
            w1.stop();
            w2.stop();

            this.generateTime = w1.getAccumulateTime();
            this.gcTime = w2.getAccumulateTime();
            System.out.println(String.format("Total: %1$.2f",
                        getTotal() * 0.000000001));
            return this;
        }

        long getTotal() {
            return this.generateTime + this.gcTime;
        }
    }

    public static void benchmark3(long seed, int count) throws Exception
    {
        System.out.println("Benchmark3 executing:");
        System.out.println();

        System.out.println("Many objects");
        Bench3 b1 = new Bench3() {
            IPv4TableBase<Value> newTable(long seed) throws Exception {
                return newTable1(seed, 1000000, 50);
            }
        }.run(seed, count);

        System.out.println("Few objects (off-heap)");
        Bench3 b2 = new Bench3() {
            IPv4TableBase<Value> newTable(long seed) throws Exception {
                return newTable2(seed, 1000000, 50);
            }
        }.run(seed, count);

        System.out.println("Few objects (heap)");
        Bench3 b3 = new Bench3() {
            IPv4TableBase<Value> newTable(long seed) throws Exception {
                return newTable3(seed, 1000000, 50);
            }
        }.run(seed, count);

        System.out.println();
        System.out.println("Benchmark#3 results:");
        System.out.println(String.format(
                    "    off-heap/objects ratio: %1$7.2f%%",
                    (double)b2.getTotal() * 100.0 / b1.getTotal()));
        System.out.println(String.format(
                    "    heap/objects ratio:     %1$7.2f%%",
                    (double)b3.getTotal() * 100.0 / b1.getTotal()));
        System.out.println(String.format(
                    "    heap/off-heap ratio:    %1$7.2f%%",
                    (double)b3.getTotal() * 100.0 / b2.getTotal()));
    }

    public static void main(String[] args) throws Exception
    {
        String name = args[0];
        if ("1".equals(name)) {
            benchmark1();
        } else if ("2".equals(name)) {
            benchmark2(0, 10);
        } else if ("3".equals(name)) {
            benchmark3(0, 10);
        } else {
            System.out.println(String.format("Unknown task: %1$s", name));
        }
    }
}
