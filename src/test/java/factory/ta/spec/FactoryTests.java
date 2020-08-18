package factory.ta.spec;

import com.fasterxml.jackson.databind.JsonNode;
import factory.ta.model.Api;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.apache.http.HttpResponse;
import java.io.IOException;
import factory.ta.utils.Utils;
import static factory.ta.utils.Utils.assertThat;
import static factory.ta.utils.RequestBuilder.execute;
import static org.hamcrest.Matchers.equalTo;


public class FactoryTests extends CommonTest {

    @Test(description = "Verify gap compensation")
    public void debugGapCompensationTest() throws IOException {
        //Given
        HttpResponse response_1 = execute(Api.getNewSerialIdForFactory("123")); //appl123_1
        HttpResponse response_2 = execute(Api.getGapIdForFactory("123")); //appl123_2(gap)
        HttpResponse response_3 = execute(Api.getNewSerialIdForFactory("123"));//appl123_2
        HttpResponse response_4 = execute(Api.getNewSerialIdForFactory("123"));//appl123_3

        execute(Api.processRequests());

        String token_1 = Utils.httpResponseToJson(response_1).get("token").asText();
        String token_2 = Utils.httpResponseToJson(response_2).get("token").asText();
        String token_3 = Utils.httpResponseToJson(response_3).get("token").asText();
        String token_4 = Utils.httpResponseToJson(response_4).get("token").asText();

        //When
        HttpResponse appId_1 = execute(Api.getAppIdAndStatus(token_1));
        HttpResponse appId_2 = execute(Api.getAppIdAndStatus(token_2));
        HttpResponse appId_3 = execute(Api.getAppIdAndStatus(token_3));
        HttpResponse appId_4 = execute(Api.getAppIdAndStatus(token_4));

        execute(Api.processRequests());

        JsonNode bodyAppId_1 = Utils.httpResponseToJson(appId_1);
        JsonNode bodyAppId_2 = Utils.httpResponseToJson(appId_2);
        JsonNode bodyAppId_3 = Utils.httpResponseToJson(appId_3);
        JsonNode bodyAppId_4 = Utils.httpResponseToJson(appId_4);

        //Then
        assertThat("First request valid ID is appl123_1",
                bodyAppId_1.get("id").asText(),
                equalTo("appl123_1"));
        assertThat("First request valid status is provisioned",
                bodyAppId_1.get("status").asText(),
                equalTo("provisioned"));

        assertThat("Second request valid ID is appl123_2",
                bodyAppId_2.get("id").asText(),
                equalTo("appl123_2"));
        assertThat("Second request valid status is timeout",
                bodyAppId_2.get("status").asText(),
                equalTo("timeout"));

        assertThat("Third request valid ID is appl123_2 (closing gap)",
                bodyAppId_3.get("id").asText(),
                equalTo("appl123_2"));
        assertThat("Third request valid status is provisioned",
                bodyAppId_3.get("status").asText(),
                equalTo("provisioned"));

        assertThat("Fourth request valid ID is appl123_3 (continue sequence)",
                bodyAppId_4.get("id").asText(),
                equalTo("appl123_3"));
        assertThat("Third request valid status is provisioned",
                bodyAppId_4.get("status").asText(),
                equalTo("provisioned"));
    }

    @Test(description = "Create new sequence for each factory")
    public void newSerialForNewFactory() throws IOException {
        //Given
        HttpResponse response_1 = execute(Api.getNewSerialIdForFactory("1"));
        HttpResponse response_2 = execute(Api.getNewSerialIdForFactory("1"));
        HttpResponse response_3 = execute(Api.getNewSerialIdForFactory("2"));
        HttpResponse response_4 = execute(Api.getNewSerialIdForFactory("2"));

        execute(Api.processRequests());

        String token_1 = Utils.httpResponseToJson(response_1).get("token").asText();
        String token_2 = Utils.httpResponseToJson(response_2).get("token").asText();
        String token_3 = Utils.httpResponseToJson(response_3).get("token").asText();
        String token_4 = Utils.httpResponseToJson(response_4).get("token").asText();

        //When
        HttpResponse appId_1 = execute(Api.getAppIdAndStatus(token_1));
        HttpResponse appId_2 = execute(Api.getAppIdAndStatus(token_2));
        HttpResponse appId_3 = execute(Api.getAppIdAndStatus(token_3));
        HttpResponse appId_4 = execute(Api.getAppIdAndStatus(token_4));

        execute(Api.processRequests());

        JsonNode bodyAppId_1 = Utils.httpResponseToJson(appId_1);
        JsonNode bodyAppId_2 = Utils.httpResponseToJson(appId_2);
        JsonNode bodyAppId_3 = Utils.httpResponseToJson(appId_3);
        JsonNode bodyAppId_4 = Utils.httpResponseToJson(appId_4);

        //Then
        assertThat("First valid request for factory_1 ID is appl1_1",
                bodyAppId_1.get("id").asText(),
                equalTo("appl1_1"));
        assertThat("First valid request for factory_1 status is provisioned",
                bodyAppId_1.get("status").asText(),
                equalTo("provisioned"));

        assertThat("Second valid request for factory_1 ID is appl1_2",
                bodyAppId_2.get("id").asText(),
                equalTo("appl1_2"));
        assertThat("Second valid request for factory_1 status is provisioned",
                bodyAppId_2.get("status").asText(),
                equalTo("provisioned"));

        assertThat("First valid request for factory_2 ID is appl2_1",
                bodyAppId_3.get("id").asText(),
                equalTo("appl2_1"));
        assertThat("First valid request for factory_2 status is provisioned",
                bodyAppId_3.get("status").asText(),
                equalTo("provisioned"));

        assertThat("Second valid request for factory_2 ID is appl2_2",
                bodyAppId_4.get("id").asText(),
                equalTo("appl2_2"));
        assertThat("Second valid request for factory_2 status is provisioned",
                bodyAppId_4.get("status").asText(),
                equalTo("provisioned"));
    }

    @Test(description = "Factory response verification")
    public void validFactoryResponse() throws IOException {
        //Given
        HttpResponse response_1 = execute(Api.getNewSerialIdForFactory("100"));
        execute(Api.processRequests());
        String token_1 = Utils.httpResponseToJson(response_1).get("token").asText();

        //When
        HttpResponse appId_1 = execute(Api.getAppIdAndStatus(token_1));
        execute(Api.processRequests());
        JsonNode bodyAppId_1 = Utils.httpResponseToJson(appId_1);

        //Then
        assertThat("First valid request for factory_1 ID is appl100_1",
                appId_1.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
        assertThat("First valid request for factory_1 ID is appl100_1",
                bodyAppId_1.get("id").asText(),
                equalTo("appl100_1"));
        assertThat("First valid request for factory_1 status is provisioned",
                bodyAppId_1.get("status").asText(),
                equalTo("provisioned"));
    }

    @Test(description = "Factory receive sequential ID's")
    public void idIsSequentialForOneFactory() throws IOException {
        //Given
        HttpResponse response_1 = execute(Api.getNewSerialIdForFactory("123"));
        HttpResponse response_2 = execute(Api.getNewSerialIdForFactory("123"));
        HttpResponse response_3 = execute(Api.getNewSerialIdForFactory("123"));

        execute(Api.processRequests());

        String token_1 = Utils.httpResponseToJson(response_1).get("token").asText();
        String token_2 = Utils.httpResponseToJson(response_2).get("token").asText();
        String token_3 = Utils.httpResponseToJson(response_3).get("token").asText();

        //When
        HttpResponse appId_1 = execute(Api.getAppIdAndStatus(token_1));
        HttpResponse appId_2 = execute(Api.getAppIdAndStatus(token_2));
        HttpResponse appId_3 = execute(Api.getAppIdAndStatus(token_3));

        execute(Api.processRequests());

        JsonNode bodyAppId_1 = Utils.httpResponseToJson(appId_1);
        JsonNode bodyAppId_2 = Utils.httpResponseToJson(appId_2);
        JsonNode bodyAppId_3 = Utils.httpResponseToJson(appId_3);

        int id_1 = Integer.parseInt(bodyAppId_1.get("id").asText().replace("appl123_", ""));
        int id_2 = Integer.parseInt(bodyAppId_2.get("id").asText().replace("appl123_", ""));
        int id_3 = Integer.parseInt(bodyAppId_3.get("id").asText().replace("appl123_", ""));

        int[] ids = {id_1, id_2, id_3};

        //Then
        assertThat("ID numbers are sequential",
                Utils.isSequence(ids),
                equalTo(true));
    }
}
