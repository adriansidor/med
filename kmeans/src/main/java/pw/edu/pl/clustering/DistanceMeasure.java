package pw.edu.pl.clustering;

import java.util.Vector;

public class DistanceMeasure {
	
	private DistanceMeasure() {
		
	}

	public static double euclideanDistance(Vector<Double> a, Vector<Double> b) {
        check(a, b);

        double sum = 0;
        for (int i = 0; i < a.size(); ++i)
            sum += Math.pow((a.get(i) - b.get(i)), 2);
        return Math.sqrt(sum);
    }
	
	private static void check(Vector<Double> a, Vector<Double> b) {
        if (a.size() != b.size())
            throw new IllegalArgumentException(
                    "input vector lengths do not match");
    }


}
