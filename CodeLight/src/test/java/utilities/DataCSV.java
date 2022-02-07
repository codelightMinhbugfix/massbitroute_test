package utilities;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataCSV {

    public static Object[][] getAllDataCSV(String fileName) {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertListToArray(records);
    }

    public static Object[][] convertListToArray(List<List<String>> data) {
        Object[][] retObjArr = new Object[data.size()-1][data.get(0).size()];
        for (int i = 1; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).size(); j++) {
                retObjArr[i-1][j] = data.get(i).get(j);
            }
        }
        return retObjArr;
    }

}
