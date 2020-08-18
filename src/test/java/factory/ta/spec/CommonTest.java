package factory.ta.spec;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import factory.ta.model.ApiSchema;
import factory.ta.utils.RequestBuilder;
import org.testng.annotations.BeforeMethod;

public class CommonTest {
    static Path currentDir = Paths.get("./src/test/java/factory.ta/expected");
    public static final String EXPECTED_FILES_PATH = currentDir.toAbsolutePath().toString() + "\\";
    ObjectMapper mapper = new ObjectMapper();

    @BeforeMethod
    public void clearDatabase() throws IOException, InterruptedException {
        RequestBuilder.execute(ApiSchema.clearDatabase());
    }
}
