package net.xaosdev.util.service.security;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import testing.OverrideSecurity;

import java.security.Policy;

import static org.junit.Assert.*;

public class SecurityTest {

    @Rule
    public OverrideSecurity overrideSecurity = new OverrideSecurity();

    @Before
    public void setUp() {
        overrideSecurity.resetManager();
        overrideSecurity.resetPolicy();
    }

    @Test
    public void install() {
        // Arrange
        SecurityManager first = System.getSecurityManager();

        // Act
        final Policy originalPolicy = Policy.getPolicy();
        Security.install();

        // Assert
        assertNull(first);
        assertNotNull(System.getSecurityManager());
        overrideSecurity.assertPolicyReferencesAreInIntialState(originalPolicy);
    }

    @Test (expected = IllegalStateException.class)
    public void installFails() {
        // Arrange
        System.setSecurityManager(new SecurityManager());

        // Act
        Security.install();

        // Assert - not needed
    }

    @Test
    public void installWithPolicy() {
        // Arrange
        final SourceFilteringPolicy policy = new SourceFilteringPolicy();
        policy.setDefaultPermissions(policy.getSystemPermissions()); // make policy liberal, required for Gradle tests

        // Act
        Security.installWithPolicy(policy);

        // Assert
        assert(policy == Policy.getPolicy());
    }

    @Test
    public void getSourceFilteringPolicy() {
        // Arrange
        final SourceFilteringPolicy policy = new SourceFilteringPolicy();
        policy.setDefaultPermissions(policy.getSystemPermissions()); // make policy liberal, required for Gradle tests

        // Act
        Security.installWithPolicy(policy);

        // Assert
        assert(policy == Security.getSourceFilteringPolicy());
    }

    @Test
    public void getSourceFilteringPolicyFails() {
        // Arrange - not needed
        // Act
        Security.install();

        // Assert
        assertNull(Security.getSourceFilteringPolicy());
    }
}