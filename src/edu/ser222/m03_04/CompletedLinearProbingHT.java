package edu.ser222.m03_04;

/**
 * A symbol table implemented using a hashtable with linear probing.
 * 
 * @author Borys Banaszkiewicz, Sedgewick and Wayne, Acuna
 */
import java.util.LinkedList;
import java.util.Queue;

public class CompletedLinearProbingHT<Key, Value> implements ProbingHT<Key, Value> {
    
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
    private Entry<Key, Value>[] st;
    
    //any constructors must be made public
    public CompletedLinearProbingHT() {
        this(997);
    }
    
    public CompletedLinearProbingHT(int M) {
        this.M = M;
        this.N = 0;
        st = (Entry<Key, Value>[]) new Entry[M];
    }
    /**
     * Computes the hash (will be used as an index) for a key.
     *
     * @param key Object to hash.
     * @param i number of collisions
     * @return Hash value.
     */
    @Override
    public int hash(Key key, int i) {
        return ((key.hashCode() & 0x7fffffff) + i) % M;
    }
    
    // put key-value pair into the table
    @Override
    public void put(Key key, Value val) {
        int i = 0;
        for (i = hash(key, i); st[i] != null; i = (i + 1) % M) {
            if (st[i].getKey().equals(key)) {
                st[i].setValue(val);
                return;
            }
        }
        st[i] = new Entry(key, val);
        N++;
    }
    
    //get value paired with key. returns null if key does not exist.
    @Override
    public Value get(Key key) {
        int i = 0;
        for (i = hash(key, i); st[i] != null; i = (i + 1) % M) {
            if (st[i].getKey().equals(key)) {
                return st[i].getValue();
            }
        }
        return null;
    }
    
    //remove key (and its value) from table
    @Override
    public void delete(Key key) {
        if (!contains(key)) return;
        int i = 0;
        
        while (!st[hash(key, i)].getKey().equals(key)) {
            i = (i + 1) % M;
        }
        
        st[hash(key, i)] = null;
        N--;
        i = (i + 1) % M;
        
        while(st[hash(key, i)] != null) {
            Key temp_key = st[hash(key, i)].getKey();
            Value temp_val = st[hash(key, i)].getValue();
            st[hash(key, i)] = null;
            N--;
            put(temp_key, temp_val);
            i = (i + 1) % M;
        }
    }

    //is there a value paired with key?
    @Override
    public boolean contains(Key key) {
        int i = 0;
        for (i = hash(key, i); st[i] != null; i = (i + 1) % M) {
            if (st[i].getKey().equals(key)) return true;
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
        for (Entry<Key, Value> entry : st) {
            if (entry != null) keys.add(entry.getKey());
        }
        return keys;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // THESE METHODS ARE ONLY FOR GRADING AND COME FROM THE PROBINGHT INTERFACE.

    @Override
    public int getM() {
        return M;
    }

    @Override
    public Object getTableEntry(int i) {
        return st[i];
    }
}