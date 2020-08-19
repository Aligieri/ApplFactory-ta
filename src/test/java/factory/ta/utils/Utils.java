package factory.ta.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.internal.support.FileReader;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import java.io.*;
import java.util.Arrays;


public class Utils {

    public static String readJsonToString(String filepath){
        File file = new File(filepath);
        return FileReader.readToString(file, "UTF-8");
    }

    public static JsonNode httpResponseToJson(HttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readTree(Utils.convertStreamToString(response.getEntity().getContent()));
    }

    @Step("Reading response body")
    public static String toString(HttpResponse response) throws IOException {
        String responseBody = EntityUtils.toString(response.getEntity());
        Allure.addAttachment("Response body: ", "application/json", responseBody);
        return responseBody;
    }

    @Step("Asserting results")
    public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher){
        try {
            Allure.addAttachment("Actual result: ", "application/json", actual.toString());
            Allure.addAttachment("Expected result: ", "application/json", matcher.toString());
            MatcherAssert.assertThat(reason, actual, matcher);
        }catch (NullPointerException e){
            Allure.addAttachment("Exception: ", "application/json", e.toString());
        }
    }

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Step("Sequence verification")
    public static boolean isSequence(int[] set) {
        int[] arr = Arrays.copyOf(set, set.length);
        Arrays.sort(arr);
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] + 1 != arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
