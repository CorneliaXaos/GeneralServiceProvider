/*
 * Copyright 2018 Cornelia Ada Schultz
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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