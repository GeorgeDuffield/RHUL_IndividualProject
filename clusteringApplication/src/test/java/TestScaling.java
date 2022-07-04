import kernel.DataPoint;
import kernel.MeanNormalization;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestScaling {
    MeanNormalization scaler = new MeanNormalization();
    // empty string provided for original tests where only numerical data was used
    String[] catData = new String[0];

    // test 1
    @Test
    void scaleSingleFeature(){
        Double[] input1 = {2.0};
        Double[] input2 = {5.0};
        Double[] input3 = {3.0};
        Double[] input4 = {10.0};
        Double[] input5 = {7.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);
        DataPoint dp3 = new DataPoint("", catData, input3);
        DataPoint dp4 = new DataPoint("", catData, input4);
        DataPoint dp5 = new DataPoint("", catData, input5);

        ArrayList<DataPoint> data = new ArrayList<>();
        data.add(dp1);
        data.add(dp2);
        data.add(dp3);
        data.add(dp4);
        data.add(dp5);

        data = scaler.scaleDataset(data);
        DecimalFormat df = new DecimalFormat("##.###");
        Double scaledValue = Double.valueOf(df.format(data.get(0).getNumerical(0)));
        assertEquals(-0.425, scaledValue, "average is 5.4, thus (2 - 5.4) / 10 - 2 = -0.425");
    }

    // test 2
    @Test
    void scaleNegativeFeatures(){
        Double[] input1 = {-2.0};
        Double[] input2 = {-5.0};
        Double[] input3 = {-3.0};
        Double[] input4 = {-10.0};
        Double[] input5 = {-7.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);
        DataPoint dp3 = new DataPoint("", catData, input3);
        DataPoint dp4 = new DataPoint("", catData, input4);
        DataPoint dp5 = new DataPoint("", catData, input5);

        ArrayList<DataPoint> data = new ArrayList<>();
        data.add(dp1);
        data.add(dp2);
        data.add(dp3);
        data.add(dp4);
        data.add(dp5);

        data = scaler.scaleDataset(data);
        DecimalFormat df = new DecimalFormat("##.###");
        Double scaledValue = Double.valueOf(df.format(data.get(0).getNumerical(0)));
        assertEquals(0.425, scaledValue, "(-2 - (-5.4)) / ((-2) - (-10)) = 0.425");
    }

    @Test
    void scaleTwoFeatures(){
        Double[] input1 = {2.0, -2.0};
        Double[] input2 = {5.0, -5.0};
        Double[] input3 = {3.0, -3.0};
        Double[] input4 = {10.0, -10.0};
        Double[] input5 = {7.0, -7.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);
        DataPoint dp3 = new DataPoint("", catData, input3);
        DataPoint dp4 = new DataPoint("", catData, input4);
        DataPoint dp5 = new DataPoint("", catData, input5);

        ArrayList<DataPoint> data = new ArrayList<>();
        data.add(dp1);
        data.add(dp2);
        data.add(dp3);
        data.add(dp4);
        data.add(dp5);

        data = scaler.scaleDataset(data);
        DecimalFormat df = new DecimalFormat("##.###");
        Double scaledValue1 = Double.valueOf(df.format(data.get(0).getNumerical(0)));
        Double scaledValue2 = Double.valueOf(df.format(data.get(0).getNumerical(1)));
        assertTrue(scaledValue1 == -0.425 && scaledValue2 == 0.425, "Scaling one feature should not have an effect on other features and thus" +
                " results should match that of previous independent tests");
    }
}
