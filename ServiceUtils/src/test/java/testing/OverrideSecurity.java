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
