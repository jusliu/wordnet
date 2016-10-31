package ngordnet;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {

    public TimeSeries() {
        super();
    }
    
    public TimeSeries(TimeSeries<T> ts) {
        super(ts);
    }
    
    @SuppressWarnings("unchecked")
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        super(ts);
        ts = (TimeSeries<T>) (ts.clone());
        while (!ts.isEmpty()) {
            int key = ts.firstKey();
            if (key < startYear || key > endYear) {
                this.remove(key);
            }
            ts.remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> sum = new TimeSeries<Double>();
        TimeSeries<T> copy = new TimeSeries<T>();
        copy.putAll(this);
        while (!copy.isEmpty()) {
            int key = copy.firstKey();
            sum.put(key, copy.get(key).doubleValue());
            copy.remove(key);
        }
        ts = (TimeSeries<? extends Number>) (ts.clone());
        while (!ts.isEmpty()) {
            int key = ts.firstKey();
            if (sum.containsKey(key)) {
                sum.put(key, (ts.get(key).doubleValue() + sum.get(key).doubleValue()));
            } else {
                sum.put(key, ts.get(key).doubleValue());
            }
            ts.remove(key);
        }
        return sum;
    }
    
    @SuppressWarnings("unchecked")
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> sum = new TimeSeries<Double>();
        TimeSeries<T> copy = new TimeSeries<T>();
        copy.putAll(this);
        while (!copy.isEmpty()) {
            int key = copy.firstKey();
            sum.put(key, copy.get(key).doubleValue());
            copy.remove(key);
        }
        ts = (TimeSeries<? extends Number>) (ts.clone());
        while (!ts.isEmpty()) {
            int key = ts.firstKey();
            if (sum.containsKey(key)) {
                sum.put(key, (sum.get(key).doubleValue() / ts.get(key).doubleValue()));
            } else {
                throw new IllegalArgumentException();
            }
            ts.remove(key);
        }
        return sum;
    }
    
    public Collection<Number> years() {
        ArrayList<Number> years = new ArrayList<Number>();
        TimeSeries<T> copy = new TimeSeries<T>();
        copy.putAll(this);
        while (!copy.isEmpty()) {
            int key = copy.firstKey();
            years.add(key);
            copy.remove(key);
        }
        return years;
    }
    
    public Collection<Number> data() {
        ArrayList<Number> data = new ArrayList<Number>();
        TimeSeries<T> copy = new TimeSeries<T>();
        copy.putAll(this);
        while (!copy.isEmpty()) {
            int key = copy.firstKey();
            data.add(copy.get(key));
            copy.remove(key);
        }
        return data;
    }
}
