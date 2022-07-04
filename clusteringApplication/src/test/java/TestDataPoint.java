import kernel.DataPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDataPoint {
    @Test
    public void testDataPointCreation(){
        DataPoint dp = new DataPoint();
    }

    @Test
    public void testDataPointCreationWithData(){
        String[] catData = new String[]{"cat"};
        Double[] numData = new Double[]{3.0};
        DataPoint dp = new DataPoint("3.0", catData, numData);
        assertTrue(dp.toString().equals("3.0") && dp.getCategorical(0).equals("cat") && dp.getNumerical(0).equals(3.0),
                "Data within dataPoint should match data provided at creation");
    }

    @Test
    public void testEmptyData(){
        String[] catData = new String[0];
        Double[] numData = new Double[0];
        DataPoint dp = new DataPoint("", catData, numData);
    }

    @Test
    public void testSize(){
        String[] catData = new String[]{"cat"};
        Double[] numData = new Double[]{3.0};
        DataPoint dp = new DataPoint("3.0", catData, numData);
        assertEquals(dp.sizeTotal(), 2, "There is one numerical and one categorical feature thus total size should be 2");
    }
}
