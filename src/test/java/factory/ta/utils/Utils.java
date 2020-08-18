package factory.ta.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import factory.ta.model.ResponseJson;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.internal.support.FileReader;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.io.File;
import java.io.IOException;


public class Utils {

    public static String readJsonToString(String filepath){
        File file = new File(filepath);
        return FileReader.readToString(file, "UTF-8");
    }

    public static ResponseJson httpResponseToJson(HttpResponse response_1) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Utils.toString(response_1), new TypeReference<ResponseJson>(){});
    }

    @Step("Reading response body")
    public static String toString(HttpResponse response) throws IOException {
        String responseBody = EntityUtils.toString(response.getEntity());
        Allure.addAttachment("Response body: ", "application/json", responseBody);
        return responseBody;
    }

    @Step("Asserting results")
    public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher){
        Allure.addAttachment("Actual result: ", "application/json", actual.toString());
        Allure.addAttachment("Expected result: ", "application/json", matcher.toString());
        MatcherAssert.assertThat(reason, actual, matcher);
    }
}
