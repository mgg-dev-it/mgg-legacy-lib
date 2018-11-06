package hu.mag.lang;

import java.util.HashMap;
import java.util.Vector;

public class LookupInteger {

    private Integer integer;

    private Vector<Integer> vValue = new Vector<Integer>();
    private Vector<String> vDisplay = new Vector<String>();
    private HashMap<Integer, String> hashmap = new HashMap<Integer, String>();

    public LookupInteger(HashMap<Integer, String> hashmap) {
        this.hashmap = hashmap;
        this.integer = null;
        init();
    }

    public LookupInteger(HashMap<Integer, String> hashmap, int value) {
        this.hashmap = hashmap;
        this.integer = new Integer(value);
        init();
    }

    public LookupInteger(HashMap<Integer, String> hashmap, Integer value) {
        this.hashmap = hashmap;
        this.integer = value;
        init();
    }

    private void init() {
        Integer[] ia = hashmap.keySet().toArray(new Integer[0]);
        for (int i = 0; i < hashmap.size(); i++) {
            vValue.add(ia[i]);
            vDisplay.add(hashmap.get(ia[i]));
        }
    }

    public void setValue(int i) {
        integer = new Integer(i);
    }

    public Integer getValue() {
        return (integer);
    }

    public String getDisplay() {
        if (integer == null) {
            return (null);
        }
        return (hashmap.get(integer));
    }

    public int getIntValue() {
        return (integer.intValue());
    }

    public void setIndex(int index) {
        if (index < 0) {
            integer = null;
            return;
        }
        if (index >= vValue.size()) {
            integer = null;
            return;
        }
        integer = vValue.elementAt(index);
    }

    public HashMap<Integer, String> getHashMap() {
        return (hashmap);
    }

    public Vector<String> getDisplayVector() {
        return (vDisplay);
    }

    public String toString() {
        if (integer == null) {
            return ("");
        } else {
            return (integer.toString());
        }
    }

    public void addToLookup(int i, String s) {
        vValue.add(new Integer(i));
        vDisplay.add(s);
        hashmap.put(new Integer(i), s);
    }

    public int getCount() {
        return (vValue.size());
    }

    public int getLookupValueAt(int iIndex) {
        return (vValue.elementAt(iIndex).intValue());
    }

    public String getLookupDisplayAt(int iIndex) {
        return (vDisplay.elementAt(iIndex));
    }

    public String getDisplayFromValue(Integer value) {
        if (value == null) {
            return (null);
        }
        return (hashmap.get(value));
    }

    public int getIndex(Integer value) {
        if (value == null) {
            return (-1);
        }
        for (int i = 0; i < vValue.size(); i++) {
            if (vValue.elementAt(i).intValue() == value.intValue()) {
                return (i);
            }
        }
        return (-1);
    }
}
