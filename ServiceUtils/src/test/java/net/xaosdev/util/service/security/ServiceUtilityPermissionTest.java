package net.xaosdev.util.service.security;

import net.xaosdev.util.service.Service;
import net.xaosdev.util.service.Source;
import net.xaosdev.util.service.sources.JarFileSourceCreator;
import net.xaosdev.util.service.sources.SystemSource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import testing.OverrideSecurity;
import testing.producer.spi.TestPermissions;
import testing.producer.spi.TestService;

import java.io.File;
import java.security.Permissions;

/**
 * This class actually tests that this permission is required where it should be.
 */
public class ServiceUtilityPermissionTest {

    private static final String TO_ISOLATED_DIR = "../Test Artifacts/build/isolated";
    private static final String IMPL = TO_ISOLATED_DIR + "/Test Permissions.jar";

    @Rule
    public OverrideSecurity overrideSecurity = new OverrideSecurity();

    private final Service<TestPermissions> testService;

    private final Source source;

    private final SourceFilteringPolicy policy;

    public ServiceUtilityPermissionTest() {
        source = JarFileSourceCreator.tryCreateSourceFromFile(new File(IMPL));
        testService = new Service<>(TestPermissions.class);
        testService.addSource(source);

        policy = new SourceFilteringPolicy();
        policy.setDefaultPermissions(policy.getSystemPermissions());
    }

    @Before
    public void setUp() {
        Security.installWithPolicy(policy);
    }

    @Test
    public void permissionsCreate() {
        // Arrange
        policy.setPermissions(source, getUpdatePermissions());

        // Act
        testService.getServiceStream().forEach(TestPermissions::testCreatingService);

        // Assert
        assert(testService.getServiceStream().count() > 0);
    }

    @Test (expected = SecurityException.class)
    public void permissionsCreateFails() {
        // Arrange
        policy.setPermissions(source, new Permissions()); // no permissions granted

        // Act
        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        //testService.getServiceStream().forEach(TestPermissions::testCreatingService);
        testService.getServiceStream().forEach(impl -> {
            impl.testCreatingService();
        });
    }

    @Test
    public void permissionsAdd() {
        // Arrange
        policy.setPermissions(source, getUpdatePermissions());
        final Service<TestService> service = new Service<>(TestService.class);

        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        testService.getServiceStream().forEach(impl -> {
            impl.testAddingSources(service);
        });
    }

    @Test (expected = SecurityException.class)
    public void permissionsAddFails() {
        // Arrange
        policy.setPermissions(source, new Permissions()); // no permissions granted
        final Service<TestService> service = new Service<>(TestService.class);

        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        testService.getServiceStream().forEach(impl -> {
            impl.testAddingSources(service);
        });
    }

    @Test
    public void permissionsGetSources() {
        // Arrange
        policy.setPermissions(source, getUpdatePermissions());
        final Service<TestService> service = new Service<>(TestService.class);

        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        testService.getServiceStream().forEach(impl -> {
            impl.testGettingSources(service);
        });
    }

    @Test (expected = SecurityException.class)
    public void permissionsGetSourcesFails() {
        // Arrange
        policy.setPermissions(source, new Permissions()); // no permissions granted
        final Service<TestService> service = new Service<>(TestService.class);

        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        testService.getServiceStream().forEach(impl -> {
            impl.testGettingSources(service);
        });
    }

    @Test
    public void permissionsRemoveSource() {
        // Arrange
        policy.setPermissions(source, getUpdatePermissions());
        final Service<TestService> service = new Service<>(TestService.class);

        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        testService.getServiceStream().forEach(impl -> {
            impl.testRemovingSources(service);
        });
    }

    @Test (expected = SecurityException.class)
    public void permissionsRemoveSourceFails() {
        // Arrange
        policy.setPermissions(source, new Permissions()); // no permissions granted
        final Service<TestService> service = new Service<>(TestService.class);

        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        testService.getServiceStream().forEach(impl -> {
            impl.testRemovingSources(service);
        });
    }

    @Test
    public void permissionsGetServices() {
        // Arrange
        policy.setPermissions(source, getAccessPermissions());
        final Service<TestService> service = new Service<>(TestService.class);

        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        testService.getServiceStream().forEach(impl -> {
            impl.testGettingServiceStream(service);
        });
    }

    @Test (expected = SecurityException.class)
    public void permissionsGetServicesFails() {
        // Arrange
        policy.setPermissions(source, new Permissions()); // no permissions granted
        final Service<TestService> service = new Service<>(TestService.class);

        // Act / Assert
        assert(testService.getServiceStream().count() > 0);
        testService.getServiceStream().forEach(impl -> {
            impl.testGettingServiceStream(service);
        });
    }

    private Permissions getAccessPermissions() {
        final Permissions permissions = new Permissions();
        permissions.add(new ServiceUtilityPermission(ServiceUtilityPermission.Type.ACCESS));
        return permissions;
    }

    private Permissions getUpdatePermissions() {
        final Permissions permissions = new Permissions();
        permissions.add(new ServiceUtilityPermission(ServiceUtilityPermission.Type.UPDATE));
        return permissions;
    }
}