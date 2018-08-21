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

import java.security.AccessController;
import java.security.AllPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.SecurityPermission;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple Policy that allows filtering ProtectionDomains based on Service sources.
 *
 * This policy provides for associating with particular Source objects various permissions.  Additionally, the system
 * permissions may be set for the default ClassLoader as well as default permissions for when an unregistered Source
 * attempts to access secured operations.
 *
 * NOTE: because ProtectionDomains contain only a reference to a ClassLoader, sources that have identical ClassLoaders
 * will cause unusual behaviour within the system.  Therefore, it is recommended that all sources have DIFFERENT
 * ClassLoaders.  The one exception to this rule is that the System Permissions are applied prior to locating any
 * any Sources matching the System ClassLoader.  This means that services loaded from the classpath will have the same
 * permissions as the application itself!
 */
public class SourceFilteringPolicy extends Policy {

    //region Fields (Private)

    /**
     * The permissions granted by default to unknown sources.
     */
    private Permissions defaultPermissions;

    /**
     * The map used to correlate a source with granted permissions.
     */
    private Map<ClassLoader, Permissions> permissionMap;

    //endregion

    //region Constructors (Public)

    /**
     * Creates a new SourceFilteringPolicy.
     */
    public SourceFilteringPolicy() {
        final Permissions systemPermissions = new Permissions();
        systemPermissions.add(new AllPermission());
        systemPermissions.setReadOnly();

        defaultPermissions = new Permissions();
        defaultPermissions.setReadOnly();

        permissionMap = new HashMap<>();
        permissionMap.put(ClassLoader.getSystemClassLoader(), systemPermissions);
    }

    //endregion

    //region Interface (Policy)

    /**
     * @inheritDoc
     */
    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        return permissionMap.getOrDefault(domain.getClassLoader(), defaultPermissions);
    }

    //endregion

    //region Interface (Public)

    /**
     * Sets the Permissions granted to the system ClassLoader.
     *
     * Care should be taken with this as one can lock the application out of the entire system if certain Permissions
     * are revoked, including the setter methods within this class.  By default, all permissions are granted.
     * @param permissions the new Permissions to grant to the system ClassLoader.
     */
    public void setSystemPermissions(final Permissions permissions) {
        doPolicySetCheck();
        permissionMap.put(ClassLoader.getSystemClassLoader(), rectifyPermissions(permissions));
    }

    /**
     * Gets a read-only view of the system Permissions.
     * @return the Permissions granted to the System.
     */
    public Permissions getSystemPermissions() {
        doPolicyGetCheck();
        return permissionMap.get(ClassLoader.getSystemClassLoader());
    }

    /**
     * Sets the Permissions granted by default to unknown sources.
     *
     * Any ClassLoaders that are unregistered with this Policy will be granted only these permissions.  By default,
     * no permissions are granted.
     * @param permissions the new Permissions to grant to the system ClassLoader.
     */
    public void setDefaultPermissions(final Permissions permissions) {
        doPolicySetCheck();
        defaultPermissions = rectifyPermissions(permissions);
    }

    /**
     * Gets a read-only view of the default Permissions.
     * @return the Permissions granted to unknown sources.
     */
    public Permissions getDefaultPermissions() {
        doPolicyGetCheck();
        return defaultPermissions;
    }

    /**
     * Sets the Permissions granted to a particular source.
     *
     * Note that the source is only used to identify a ClassLoader.
     * @param source the Source to grant Permissions to.
     * @param permissions the Permissions to grant to the Source.
     */
    public void setPermissions(final Source source, final Permissions permissions) {
        doPolicySetCheck();
        if (permissions == null) {
            permissionMap.remove(source.getClassLoader());
        } else {
            permissionMap.put(source.getClassLoader(), rectifyPermissions(permissions));
        }
    }

    /**
     * Gets a read-only view of the Permissions granted to the provided source.
     *
     * If the source hasn't had any permissions explicitly granted, the default permissions are returned.
     * @return the Permissions granted.
     */
    public Permissions getPermissions(final Source source) {
        doPolicyGetCheck();
        return permissionMap.getOrDefault(source.getClassLoader(), defaultPermissions);
    }

    //endregion

    //region Interface (Private)

    /**
     * Used to make sure Permissions aren't null and creates a copy of incoming Permissions object.
     * @param permissions the permissions to check.
     * @return a non-null Permissions object.
     */
    private Permissions rectifyPermissions(final Permissions permissions) {
        final Permissions copy = new Permissions();
        if (permissions != null) {
            Enumeration<Permission> enumeration = permissions.elements();
            while (enumeration.hasMoreElements()) {
                copy.add(enumeration.nextElement());
            }
        }
        copy.setReadOnly();
        return copy;
    }

    /**
     * Checks if a caller can get a policy or policy information.
     */
    private void doPolicyGetCheck() {
        final Policy current = AccessController.doPrivileged((PrivilegedAction<Policy>) Policy::getPolicy);

        if (current == this) {
            final Permission permission = new SecurityPermission("getPolicy");
            AccessController.checkPermission(permission);
        }
    }

    /**
     * Checks if a caller can set a policy or plicy information.
     */
    private void doPolicySetCheck() {
        final Policy current = AccessController.doPrivileged((PrivilegedAction<Policy>) Policy::getPolicy);

        if (current == this) {
            final Permission permission = new SecurityPermission("setPolicy");
            AccessController.checkPermission(permission);
        }
    }

    //endregion
}
