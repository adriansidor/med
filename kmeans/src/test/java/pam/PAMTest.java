package pam;

import org.junit.Test;
import pw.edu.pl.clustering.Matrix;
import pw.edu.pl.clustering.PAMClustering;
import pw.edu.pl.clustering.RandIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Artur on 2016-01-24.
 */
public class PAMTest {

    private static final String filename = "resources/iris.data";

    private static final String separator = ",";

    @Test
    public void pam_test() throws IOException {
        File file = new File(filename);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        List<Vector<Double>> vectors = new ArrayList<Vector<Double>>();
        List<Integer> oc = new ArrayList<Integer>();
        while( (line = br.readLine()) != null ) {
            String[] values = line.split(separator);
            Vector<Double> vector = new Vector<Double>(values.length-1);
            for(int i = 0; i<values.length-1; i++) {
                vector.add(i, Double.valueOf(values[i]));
            }
            vectors.add(vector);

            String clazz = values[values.length-1];
            if(clazz.equals("Iris-setosa")) {
                oc.add(0);
            } else if(clazz.equals("Iris-versicolor")) {
                oc.add(1);
            } else {
                oc.add(2);
            }
        }
        br.close();
        fr.close();
        Matrix matrix = new Matrix(vectors);
        PAMClustering pam = new PAMClustering();
        int[] clusters = pam.cluster(matrix, 3);
        int[][] classMatrix = new int[3][3];
        for(int i = 0; i<oc.size(); i++) {
            classMatrix[oc.get(i)][clusters[i]]++;
        }
        System.out.println("	   setosa versicolor virginica");
        System.out.println("setosa       " + classMatrix[0][0] + "       " + classMatrix[0][1] + " 	" + classMatrix[0][2]);
        System.out.println("versicolor    " + classMatrix[1][0] + "       " + classMatrix[1][1] + " 	" + classMatrix[1][2]);
        System.out.println("virginica     " + classMatrix[2][0] + "       " + classMatrix[2][1] + " 	" + classMatrix[2][2]);

        System.out.println("Rand index: " + new RandIndex().calculate(oc.toArray(new Integer[oc.size()]), clusters));
    }


}
