package pw.edu.pl.clustering;

import java.util.Vector;

public class VectorMath {

	private VectorMath() {
		
	}
	
	/**
     * Adds the second {@code Vector} to the first {@code Vector} and returns 
     * the result.
     *
     * @param vector1 The destination vector to be summed onto.
     * @param vector2 The source vector to sum from.
     */
    public static void add(Vector<Double> vector1, Vector<Double> vector2) {
        if (vector2.size() != vector1.size())
            throw new IllegalArgumentException(
                    "Vectors of different sizes cannot be added");
        
            int length = vector2.size();
            for (int i = 0; i < length; ++i) {
                double value = vector2.get(i) + vector1.get(i);
                vector1.set(i, value);
            }
    }
    
    public static void scale(Vector<Double> vector, double scale) {
    	for(int i = 0; i<vector.size(); i++) {
    		double value = vector.get(i);
    		value = value/scale;
    		vector.set(i, value);
    	}
    }
    
    public static Vector<Double> create(String line, String separator, int[] without) {
    	String[] values = line.split(separator);
    	int columns;
    	if(without != null) {
    		columns = values.length - without.length;
    	} else {
    		columns = values.length;
    	}
    	Vector<Double> vector = new Vector<Double>(columns);
    	int withoutIndex = 0;
    	int vectorIndex = 0;
    	for(int i = 0; i<values.length; i++) {
    		for(int w = 0; i<with)
    	}
    	
    	return vector;
    }

}
