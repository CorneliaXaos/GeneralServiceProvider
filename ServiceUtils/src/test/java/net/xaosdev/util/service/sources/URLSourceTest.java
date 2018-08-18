package net.xaosdev.util.service.sources;

import net.xaosdev.util.service.Service;
import org.junit.Test;
import testing.producer.spi.TestService;

import java.io.File;

public class URLSourceTest {

    private static final String PATH_TO_EXPORTED = "../Test Artifacts/build";
    private static final String PATH_TO_JAR = PATH_TO_EXPORTED + "/exported/Test Impl 1.jar";
    private static final String PATH_TO_DIR = PATH_TO_EXPORTED + "/classes/";

    @Test
    public void jarFileURLSourceTest() throws Exception {
        testSource(PATH_TO_JAR);
    }

    @Test
    public void directoryURLSourceTest() throws Exception {
        testSource(PATH_TO_DIR);
    }

    private void testSource(final String path) throws Exception {
        // Arrange
        final File file = new File(path);
        final URLSource source = new URLSource(file.toURI().toURL());
        final Service<TestService> service = new Service<>(TestService.class);
        service.addSource(source);

        // Act / Assert
        assert(service.getServiceStream().count() == 1);
        service.getServiceStream().forEach((impl) -> {
            assert(impl.returnTrue());
        });
    }
}