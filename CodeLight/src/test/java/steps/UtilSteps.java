package steps;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import utilities.Log;
import utilities.SerenityUtil;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

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

}
