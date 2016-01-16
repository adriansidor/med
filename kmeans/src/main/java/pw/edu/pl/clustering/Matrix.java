package pw.edu.pl.clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Matrix {

    /**
     * The list of {@code DoubleVector}s providing the values for the {@code
     * Matrix}.
     */
    protected List<Vector<Double>> vectors;

    /**
     * The number of columns in the {@code Matrix}
     */
    protected int columns;

    /**
     * Constructs a matrix from the list of vectors where the first list element
     * is treated as the first row in the matrix and so on.
     *
     * @throws IllegalArgumentException if the list is empty or if the vectors
     * have different lengths
     */
    public Matrix(List<Vector<Double>> vectors) {
        if (vectors.size() == 0)
            throw new IllegalArgumentException(
                "Must provide at least one vector");
        // Copy the contents to an ArrayList to guarantee O(1) row access
        this.vectors = new ArrayList<Vector<Double>>(vectors.size());
        columns = vectors.get(0).size();
        for (Vector<Double> t : vectors) {
            if (t.size() != columns)
                throw new IllegalArgumentException(
                    "Cannot create ragged matrix from list of vectors");
            this.vectors.add(t);
        }        
    }
    
/*    public Matrix(File file) {
    	try {
    		FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null) {
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }*/

    /**
     * {@inheritDoc}
     */
    public double get(int row, int column) {
        return vectors.get(row).get(column);
    }

    /**
     * {@inheritDoc}
     */
    public double[] getColumn(int column) {
        int i = 0;
        double[] columnValues = new double[vectors.size()];

        for (Vector<Double> vector : vectors)
            columnValues[i++] = vector.get(column);
        return columnValues;
    }

    /**
     * {@inheritDoc}
     */
    public Vector<Double> getColumnVector(int column) {
        int i = 0;
        Vector<Double> columnValues = new Vector<Double>(vectors.size());

        for (Vector<Double> vector : vectors)
            columnValues.set(i++, vector.get(column));
        return columnValues;
    }

    /**
     * {@inheritDoc}
     */
    //oryginalnie zwracalo double[]
    public Double[] getRow(int row) {
    	Double[] rows = new Double[this.columns];
        vectors.get(row).toArray(rows);
        
        return rows;
    }

    /**
     * {@inheritDoc}
     */
    public Vector<Double> getRowVector(int row) {
        return vectors.get(row);
    }

    /**
     * {@inheritDoc}
     */
    public int columns() {
        return columns;
    }

    /**
     * {@inheritDoc}
     */
    public int rows() {
        return vectors.size();
    }

    /**
     * {@inheritDoc}
     */
    public double[][] toDenseArray() {
        double[][] result = new double[vectors.size()][columns];
        int row = 0;
        for (Vector<Double> vector : vectors) {
            for (int i = 0; i < vector.size(); ++i)
                result[row][i] = vector.get(i);
            row++;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void set(int row, int column, double value) {
    	Vector<Double> vector = vectors.get(row);
        vector.set(column, value);
    }

    /**
     * {@inheritDoc}
     */
    public void setColumn(int column, double[] values) {
        int i = 0;
        for (Vector<Double> vector : vectors)
            vector.set(column, values[i++]);
    }

    /**
     * {@inheritDoc}
     */
    public void setColumn(int column, Vector<Double> values) {
        int i = 0;
        for (Vector<Double> vector : vectors)
            vector.set(column, values.get(i++));
    }

    /**
     * {@inheritDoc}
     */
    public void setRow(int row, double[] values) {
    	Vector<Double> v = vectors.get(row);
        for (int i = 0; i < values.length; ++i)
            v.set(i, values[i]);
    }

    /**
     * {@inheritDoc}
     */
    public void setRow(int row, Vector<Double> values) {
    	Vector<Double> v = vectors.get(row);
        for (int i = 0; i < values.size(); ++i)
            v.set(i, values.get(i));
    }
}

