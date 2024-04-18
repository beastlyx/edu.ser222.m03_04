package edu.ser222.m03_04;

/**
 * A symbol table implemented using a hashtable with chaining.
 * Does not support load balancing or resizing.
 * 
 * @author Borys Banaszkiewicz, Sedgewick and Wayne, Acuna
 */
import java.util.LinkedList;
import java.util.Queue;

public class CompletedTwoProbeChainHT<Key, Value> implements TwoProbeChainHT<Key, Value> {
    
    private static class Entry<Key, Value> {
        private Key key;
        private Value value;

        private Entry() {
            this.key = null;
            this.value = null;
        }
        
        private Entry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
        
        private void setKey(Key key) {
            this.key = key;
        }
        
        private void setValue(Value value) {
            this.value = value;
        }
        
        private Key getKey() {
            return this.key;
        }
        
        private Value getValue() {
            return this.value;
        }
    }
    
    private int M;
    private int N;
    private LinkedList<Entry<Key, Value>>[] st;
    
    //any constructors must be made public
    public CompletedTwoProbeChainHT() {
        this(997);
    }
    
    public CompletedTwoProbeChainHT(int M) {
        this.M = M;
        this.N = 0;
        st = (LinkedList<Entry<Key, Value>>[]) new LinkedList[M];
        for (int i = 0; i < M; i++) {
            st[i] = new LinkedList<>();
        }
    }
    /**
     * Computes the hash (will be used as an index) for a key.
     *
     * @param key Object to hash.
     * @return Hash value.
     */
    @Override
    public int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    /**
     * Computes the alternative hash (will be used as an index) for a key.
     *
     * @param key Object to hash.
     * @return Hash value.
     */
    @Override
    public int hash2(Key key) {
        return (((key.hashCode() & 0x7fffffff) % M) * 31) % M;
    }

    // put key-value pair into the table
    @Override
    public void put(Key key, Value val) {
        int size1 = st[hash(key)].size();
        int size2 = st[hash2(key)].size();
        
        if (contains(key)) {
            LinkedList<Entry<Key, Value>> list1 = st[hash(key)];
            
            for (Entry<Key, Value> entry : list1) {
                if (entry.getKey().equals(key)) {
                    entry.setValue(val);
                    return;
                }
            }
            
            LinkedList<Entry<Key, Value>> list2 = st[hash2(key)];

            for (Entry<Key, Value> entry : list2) {
                if (entry.getKey().equals(key)) {
                    entry.setValue(val);
                    return;
                }
            }
        }
        
        Entry<Key, Value> new_entry = new Entry<>(key, val);
        
        if (size1 <= size2) {
            st[hash(key)].add(new_entry);
            N++;
        } else {
            st[hash2(key)].add(new_entry);
            N++;
        }
    }

    //get value paired with key. returns null if key does not exist.
    @Override
    public Value get(Key key) {
        LinkedList<Entry<Key, Value>> list1 = st[hash(key)];
        LinkedList<Entry<Key, Value>> list2 = st[hash2(key)];
        
        for (Entry<Key, Value> entry : list1) {
            if (entry.getKey().equals(key)) return entry.getValue();
        }
        
        for (Entry<Key, Value> entry : list2) {
            if (entry.getKey().equals(key)) return entry.getValue();
        }
        
        return null;
    }

    //remove key (and its value) from table
    @Override
    public void delete(Key key) {
        LinkedList<Entry<Key, Value>> list1 = st[hash(key)];
        LinkedList<Entry<Key, Value>> list2 = st[hash2(key)];
        
        int index = 0;
        while (index < list1.size()) {
            if (list1.get(index).getKey().equals(key)) {
                list1.remove(index);
                N--;
                return;
            }
            index++;
        }
        
        index = 0;

        while (index < list2.size()) {
            if (list2.get(index).getKey().equals(key)) {
                list2.remove(index);
                N--;
                return;
            }
            index++;
        }
    }
    
    //is there a value paired with key?
    @Override
    public boolean contains(Key key) {
        LinkedList<Entry<Key, Value>> list1 = st[hash(key)];
        LinkedList<Entry<Key, Value>> list2 = st[hash2(key)];
        
        for (Entry<Key, Value> entry : list1) {
            if (entry.getKey().equals(key)) return true;
        }
        for (Entry<Key, Value> entry : list2) {
            if (entry.getKey().equals(key)) return true;
        }
        return false;
    }


    //is the table empty?
    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    //number of key-value pairs
    @Override
    public int size() {
        return N;
    }

    //all keys in the table. if st is empty, returns an empty iterable object (not a null).
    @Override
    public Iterable<Key> keys() {
        LinkedList<Key> keys = new LinkedList<>();
        for (LinkedList<Entry<Key, Value>> list : st) {
            for (Entry<Key, Value> entry : list) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // THESE METHODS ARE ONLY FOR GRADING AND COME FROM THE TWOPROBECHAINHT INTERFACE.
    
    /**
     * Returns the length of the internal array.
     *
     * @return The length of the internal array.
     */
    @Override
    public int getM() {
        return M;
    }
    
    /**
     * Returns the size of the chain at an index in the internal array. For example: if an index is
     * unused, it will return 0. If two key/value pairs have been added at the index, it will return
     * 2.
     *
     * @param i Array index.
     * @return Number of entries saved in list at index.
     */
    @Override
    public int getChainSize(int i) {
        return st[i].size();
    }
}