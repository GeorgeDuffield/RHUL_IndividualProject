package kernel;

import java.util.ArrayList;

/**
 * Object which is used to represent a cluster of data. Contains the basic methods for processing clusters.
 */
public class Cluster {
    private ArrayList<DataPoint> dataPoints;

    public Cluster (){
        dataPoints = new ArrayList<DataPoint>();
    }

    public Cluster(DataPoint data){
        dataPoints = new ArrayList<DataPoint>();
        dataPoints.add(data);
    }

    public Cluster(DataPoint[] data){
        dataPoints = new ArrayList<DataPoint>();
        
        for(DataPoint d : data){
            dataPoints.add(d);
        }
    }

    public int size(){
        return dataPoints.size();
    }

    public DataPoint get(int index){
        return dataPoints.get(index);
    }

    public void add(DataPoint data){
        dataPoints.add(data);
    }

    public boolean contains (DataPoint dp){
        return dataPoints.contains(dp);
    }

    public String toString(){
        String returnString = "";
        for(int i = 0; i < dataPoints.size(); i++){
            if(i != dataPoints.size() - 1){
                returnString += dataPoints.get(i).toString() + ", ";
            } else {
                returnString += dataPoints.get(i).toString();
            }

        }

        return returnString;
    }
}
