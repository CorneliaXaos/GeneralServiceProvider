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

import net.xaosdev.util.service.Source;
import net.xaosdev.util.service.sources.ExtensionsSource;
import net.xaosdev.util.service.sources.SystemSource;
import org.junit.Before;
import org.junit.Test;

import java.io.FilePermission;
import java.security.Permission;
import java.security.Permissions;
import java.util.Enumeration;

import static org.junit.Assert.*;

public class SourceFilteringPolicyTest {

    private SourceFilteringPolicy policy;
    private Permissions testingPermissions;

    @Before
    public void setUp() {
        policy = new SourceFilteringPolicy();
        testingPermissions = new Permissions();
        testingPermissions.add(new FilePermission("file", "read"));
    }

    @Test
    public void systemPermissions() {
        // Arrange - not needed
        // Act
        policy.setSystemPermissions(testingPermissions);

        // Assert
        final Permissions system = policy.getSystemPermissions();
        enumerateAndTest(testingPermissions, system);
        enumerateAndTest(system, testingPermissions);
    }

    @Test
    public void defaultPermissions() {
        // Arrange - not needed
        // Act
        policy.setDefaultPermissions(testingPermissions);

        // Assert
        final Permissions defaultPerms = policy.getDefaultPermissions();
        enumerateAndTest(testingPermissions, defaultPerms);
        enumerateAndTest(defaultPerms, testingPermissions);
    }

    @Test
    public void bySourcePermissions() {
        // Arrange
        final Source source = SystemSource.getSource();

        // Act
        policy.setPermissions(source, testingPermissions);

        // Assert
        final Permissions permissions = policy.getPermissions(source);
        enumerateAndTest(testingPermissions, permissions);
        enumerateAndTest(permissions, testingPermissions);
    }

    @Test
    public void unknownSourcePermissions() {
        // Arrange
        final Source source = ExtensionsSource.getSource();

        // Act
        policy.setDefaultPermissions(testingPermissions);

        // Assert
        final Permissions permissions = policy.getPermissions(source);
        enumerateAndTest(testingPermissions, permissions);
        enumerateAndTest(permissions, testingPermissions);
    }

    private void enumerateAndTest(final Permissions a, final Permissions b) {
        final Enumeration<Permission> enumeration = a.elements();
        while (enumeration.hasMoreElements()) {
            assert(b.implies(enumeration.nextElement()));
        }
    }
}