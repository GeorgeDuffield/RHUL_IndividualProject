package kernel;

import java.util.ArrayList;

public class MeanNormalization {
    /**
     * Used to scale a dataset to prevent large features having a higher priority during the clustering process
     * @param data the data that is to be scaled
     * @return the scaled data
     */
    public ArrayList<DataPoint> scaleDataset(ArrayList<DataPoint> data){
        // for each numerical feature
        for(int i = 0; i < data.get(0).sizeNumerical(); i++){
            double minFeatureValue = data.get(0).getNumerical(i);
            double maxFeatureValue = data.get(0).getNumerical(i);
            double averageValue = 0;

            // for each data point
            for(int j = 0; j < data.size(); j++){
                averageValue += data.get(j).getNumerical(i);

                if(data.get(j).getNumerical(i) < minFeatureValue){
                    minFeatureValue = data.get(j).getNumerical(i);
                }
                if (data.get(j).getNumerical(i) > maxFeatureValue){
                    maxFeatureValue = data.get(j).getNumerical(i);
                }
            }
            //calculate mean
            averageValue = averageValue / data.size();

            // normalise feature for every datapoint
            for(int j = 0; j < data.size(); j++){
                double x = data.get(j).getNumerical(i);

                x = (x - averageValue) / (maxFeatureValue - minFeatureValue);
                data.get(j).setNumerical(i, x);
            }
        }
        return data;
    }
}
