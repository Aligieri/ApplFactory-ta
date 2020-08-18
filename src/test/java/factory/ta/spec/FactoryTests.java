package factory.ta.spec;

import factory.ta.model.ApiSchema;
import factory.ta.model.ResponseJson;
import factory.ta.utils.RequestBuilder;
import org.testng.annotations.Test;
import org.apache.http.HttpResponse;
import java.io.IOException;
import factory.ta.utils.Utils;
import static factory.ta.utils.Utils.assertThat;
import static org.hamcrest.Matchers.*;


public class FactoryTests extends CommonTest {
    @Test(description = "Verify gap compensation")
    public void debugGapCompensationTest() throws IOException, InterruptedException {
        //Given
        HttpResponse response_1 = RequestBuilder.execute(ApiSchema.getNewSerialIdForFactory("123"));
        HttpResponse response_2 = RequestBuilder.execute(ApiSchema.getGapIdForFactory("123"));
        HttpResponse response_3 = RequestBuilder.execute(ApiSchema.getNewSerialIdForFactory("123"));

        //When

        String token_1 = Utils.httpResponseToJson(response_1).getToken();
        HttpResponse appId_1 = RequestBuilder.execute(ApiSchema.getAppIdAndStatus(token_1));
        String token_2 = Utils.httpResponseToJson(response_2).getToken();
        HttpResponse appId_2 = RequestBuilder.execute(ApiSchema.getAppIdAndStatus(token_2));
        String token_3 = Utils.httpResponseToJson(response_3).getToken();
        HttpResponse appId_3 = RequestBuilder.execute(ApiSchema.getAppIdAndStatus(token_3));

        ResponseJson bodyAppId_1 = Utils.httpResponseToJson(appId_1);
        ResponseJson bodyAppId_2 = Utils.httpResponseToJson(appId_2);
        ResponseJson bodyAppId_3 = Utils.httpResponseToJson(appId_3);

        //Then
        assertThat("First request valid ID is appl123_1",
                bodyAppId_1.getId(),
                equalTo("appl123_1"));
        assertThat("First request valid status is provisioned",
                bodyAppId_1.getStatus(),
                equalTo("provisioned"));

        assertThat("First request valid ID is appl123_2",
                bodyAppId_2.getId(),
                equalTo("appl123_2"));
        assertThat("First request valid status is timeout",
                bodyAppId_2.getStatus(),
                equalTo("timeout"));

        assertThat("First request valid ID is appl123_2",
                bodyAppId_3.getId(),
                equalTo("appl123_2"));
        assertThat("First request valid status is provisioned",
                bodyAppId_3.getStatus(),
                equalTo("provisioned"));
    }
}
