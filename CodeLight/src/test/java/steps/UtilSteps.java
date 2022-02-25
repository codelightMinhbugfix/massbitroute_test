package steps;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import utilities.Log;
import utilities.SerenityUtil;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilSteps {

    private EnvironmentVariables environmentVariables;

    public String getAPIURL(){
        String webserviceEndpoint =  EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.massbitroute.url");
        return webserviceEndpoint;
//        return SerenityUtil.getEnv("api.massbitroute.url");
    }

    public static void runCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        printResults(process);
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            Log.info(line);
        }
    }

        public static void writeToFile(String filename, String content) {
            try {
                FileWriter myWriter = new FileWriter(filename);
                myWriter.write(content);
                myWriter.close();
                Log.info("Successfully wrote to the file.");
            } catch (IOException e) {
                Log.highlight("An error occurred.");
                e.printStackTrace();
            }
        }

        public static List<List<String>> convertCSVFormatToList(String csvFormmat){
            List<String> list = Arrays.asList(csvFormmat.split("\n"));
            List<List<String>> list_item = new ArrayList<>();

            for(String data_line : list){
                String[] arr = data_line.split(" ");
                ArrayList<String> lst = new ArrayList<>();
                for(String str : arr){
                    lst.add(str);
                }
                list_item.add(lst);
            }
            Log.info("Convert done");
            return list_item;
        }

}
