package testing;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.security.AllPermission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;

public class OverrideSecurity extends ExternalResource {

    private static SecurityManager originalManager;

    private static Policy originalPolicy;

    private static Policy setByUs;

    @Override
    protected void before() throws Throwable {
        super.before();

        originalManager = System.getSecurityManager();
        originalPolicy = Policy.getPolicy();

        resetManager();
        resetPolicy();
    }

    @Override
    protected void after() {
        System.setSecurityManager(null);
        Policy.setPolicy(originalPolicy);
        System.setSecurityManager(originalManager);
    }

    public void resetPolicy() {
        setByUs = new Policy() {

            @Override
            public PermissionCollection getPermissions(ProtectionDomain protectionDomain) {
                // Make policy liberal, this is required for Gradle tests task to not crash on AccessExceptions
                final Permissions permissions = new Permissions();
                permissions.add(new AllPermission());
                return permissions;
                /*
                if (protectionDomain.getClassLoader() == ClassLoader.getSystemClassLoader()) {
                    final Permissions permissions = new Permissions();
                    permissions.add(new AllPermission());
                    return permissions;
                } else if (protectionDomain...) {
                    // Detect gradle and give AllPermission?
                } else {
                        return super.getPermissions(protectionDomain);
                }
                */
            }
        };
        Policy.setPolicy(setByUs);
    }

    public void resetManager() {
        System.setSecurityManager(null);
    }

    public void assertPolicyReferencesAreInIntialState(final Policy compare) {
        assert(compare == setByUs);
    }
}
