import plugins.DefaultParser;
import kernel.DataPoint;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDefaultParser {
    DefaultParser DBparser = new DefaultParser();
    File iris = new File("src/main/resources/iris.data");
    File ecoli = new File("src/main/resources/ecoli.data");
    File yeast = new File("src/main/resources/yeast.data");

    // test 1
    @Test
    void testReadLine() throws IOException {
        DBparser.setDatabase(iris);
        String s = DBparser.readLine();
        assertEquals(s, "5.1,3.5,1.4,0.2",
                "First line read from the file should match that in the DB");
    }

    // test 2
    @Test
    void testSplitLine() throws IOException {
        DBparser.setDatabase(iris);
        String s = DBparser.readLine();
        DataPoint splitData = DBparser.seperateData(s);
        assertEquals(splitData.sizeTotal(), 4, "First line consists of 4 numbers so length should be 4");
    }

    // test 3
    @Test
    void testConnectToNewDB() throws IOException {
        DBparser.setDatabase(iris);
        DBparser.setDatabase(ecoli);
        String s = DBparser.readLine();
        assertEquals(s, "0.49  0.29  0.48  0.50  0.56  0.24  0.35", "The line read should match the first line in the new DB");
    }

    // test 4
    @Test
    void readWholeDB() throws IOException {
        DBparser.setDatabase(iris);
        ArrayList<DataPoint> database = DBparser.readDB();
        assertTrue(database.size() == 150, "the iris database has 150 samples and thus the size of the array should also be 150.");
    }

    //test 5
    @Test
    void testDoubleWhiteSpaceSplit() throws IOException {
        DBparser.setDatabase(ecoli);
        String s = DBparser.readLine();
        DataPoint split = DBparser.seperateData(s);
        assertTrue(split.sizeTotal() == 7, "Data point should only contain the 7 numerical features found in the ecoli dataset.");
    }

    //Test 6
    @Test
    void testMultiTypedData() throws IOException {
        DBparser.setDatabase(yeast);
        DataPoint dp = DBparser.seperateData(DBparser.readLine());
        assertTrue(dp.toString().equals("ADT1_YEAST  0.58  0.61  0.47  0.13  0.50  0.00  0.48  0.22  MIT") && dp.sizeCategorical() == 2 && dp.sizeNumerical() == 8,
                "FileParser should be able to read DB and created a data point which reflects the multi data type data");
    }

    //Test 7
    @Test
    void testGetName() throws IOException {
        DBparser.setDatabase(yeast);
        assertEquals("yeast.data", DBparser.getFileName(), "File name should match the name/type of the file given to the parser.");
    }

    //Test 8
    @Test
    void testLineNumber() throws IOException {
        DBparser.setDatabase(iris);
        ArrayList<DataPoint> database = DBparser.readDB();
        assertEquals(150, DBparser.getNumberOfLines(), "Total number of lines in the database is 150, thus line number variable should be 150");
    }

    //Test 8
    @Test
    void testLineNumberWithEmptyLines() throws IOException {
        File file = new File("src/main/resources/iris-whitespace.data");
        DBparser.setDatabase(file);
        ArrayList<DataPoint> database = DBparser.readDB();
        assertEquals(150, DBparser.getNumberOfLines(), "File contains additional whitespace which should not be read, thus number of lines should still be 150.");
    }

    // Test 9
    @Test
    void testNumberOfFeatures() throws IOException {
        DBparser.setDatabase(iris);
        ArrayList<DataPoint> database = DBparser.readDB();
        assertEquals(4, DBparser.getFeatureNumber(), "Iris samples have 4 features, thus feature number should be 4");
    }

    // Test 10
    @Test
    void testWrongNumberOfFeatures() throws IOException {
        File file = new File("src/main/resources/oddFeatureSize.data");
        DBparser.setDatabase(file);
        ArrayList<DataPoint> database = DBparser.readDB();
        assertEquals(1, DBparser.getNumberOfLines(), "There are two samples in this file, " +
                "the second has less features than the first and thus should not be added to the DB.");
    }

    // Test 11
    @Test
    void testSequanceRead() throws IOException{
        File file = new File("src/main/resources/promoters.data");
        DBparser.setDatabase(file);
        ArrayList<DataPoint> database = DBparser.readDB();
        assertTrue(database.size() == 53 && database.get(0).sizeTotal() == 3, "There are a total of 53 lines, the parser should be able to read all of them");
    }
}
