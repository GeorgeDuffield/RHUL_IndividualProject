package kernel;

import java.util.ArrayList;

/**
 * Object which is used to represent a single sample from the database.
 */
public class DataPoint {
    private ArrayList<Double> numericalFeatures;
    private ArrayList<String> categoricalFeatures;
    private String originalString;

    // a copy of the numerical features is kept for displaying clustering results
    private ArrayList<Double> originalNumericalFeatures;

    public DataPoint() {
        numericalFeatures = new ArrayList<Double>();
        categoricalFeatures = new ArrayList<String>();
        originalNumericalFeatures = new ArrayList<>();
    }

    public DataPoint(String DatabaseLine) {
        numericalFeatures = new ArrayList<Double>();
        categoricalFeatures = new ArrayList<String>();
        originalString = DatabaseLine;
        originalNumericalFeatures = new ArrayList<>();
    }

    public DataPoint(String DatabaseLine, String[] categoricalData, Double[] numericalData) {
        numericalFeatures = new ArrayList<Double>();
        categoricalFeatures = new ArrayList<String>();
        originalString = DatabaseLine;
        originalNumericalFeatures = new ArrayList<>();

        for (int i = 0; i < categoricalData.length; i++) {
            categoricalFeatures.add(categoricalData[i]);
        }

        for (int i = 0; i < numericalData.length; i++) {
            numericalFeatures.add(numericalData[i]);
            originalNumericalFeatures.add(numericalData[i]);
        }
    }

    public Double getNumerical(int i) {
        return numericalFeatures.get(i);
    }

    public void setNumerical(int position, double newValue) {
        numericalFeatures.set(position, newValue);
    }

    public Double getOriginalNumerical(int i) {
        return originalNumericalFeatures.get(i);
    }

    /**
     * Called as part of adding numerical values to a new datapoint.
     * @param feature the value which is to be added
     */
    public void addNumerical(Double feature) {
        numericalFeatures.add(feature);
        originalNumericalFeatures.add(feature);
    }

    public int sizeNumerical() {
        return numericalFeatures.size();
    }

    public String getCategorical(int i) {
        return categoricalFeatures.get(i);
    }

    public void setCategorical(int position, String newValue) {
        categoricalFeatures.set(position, newValue);
    }

    public void addCategorical(String feature) {
        categoricalFeatures.add(feature);
    }

    public int sizeCategorical() {
        return categoricalFeatures.size();
    }

    public int sizeTotal() {
        return (categoricalFeatures.size() + numericalFeatures.size());
    }

    public String toString() {
        return originalString;
    }
}
