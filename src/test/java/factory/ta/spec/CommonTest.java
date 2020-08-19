package factory.ta.spec;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import factory.ta.model.Api;
import factory.ta.utils.RequestBuilder;
import org.testng.annotations.BeforeMethod;

public class CommonTest {
    static Path currentDir = Paths.get("./src/test/java/factory.ta/expected");

    @BeforeMethod
    public void clearDatabase() throws IOException, InterruptedException {
        RequestBuilder.execute(Api.clearDatabase());
    }
}
