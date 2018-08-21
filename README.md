# ServiceUtils
ServiceUtils is a small library used to simplify working with the Java ServiceLoader.  It's primary features include:

    * Declaring various sources that providers may be loaded from
    * Centralizing those providers into a single Service object
    * Security classes to assist in protecting an execution environment from potentially malicious providers
    * An extensible "Source" architecture for user-defined provider sources
    
[![Release](https://jitpack.io/v/net.xaosdev/ServiceUtils.svg)](https://jitpack.io/#net.xaosdev/ServiceUtils)

## Acquiring

ServiceUtils is shipped with Jitpack and can be declared as a dependency using any maven-compatible tool:

|-----------|------------------|
|Repository |https://jitpack.io|
|Group ID   |net.xaosdev       |
|Artifact ID|ServiceUtils      |
|Version    |1.0.0             |

(Note that Version maps to various identifiers such as branches, tags, and short commit hashes.)

For example, to import ServiceUtils using gradle:

```groovy

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'net.xaosdev:ServiceUtils:1.0.0'
}

```

## Usage

To use ServiceUtils:

    1. Create a Service<SPI> object.
    2. Add Sources to your service.
    3. Access the service stream.
    
```java

Service<MyService> myService = new Service<>(MyService.class);
myService.addSource(SystemSource.getSource()); // Load providers from the classpath
myService.getServiceStream().forEach(provider -> {
   provider.doMyThing(); 
});

```

Additionally, there are other sources for loading providers from JVM extensions Jar Files, URLs, and arbitrary
ClassLoaders:

```java

Source extension = ExtensionsSource.getSource();
Source jarFile = JarFileSourceCreator.tryCreateSourceFromFile(myJarFile);
Source pluginDirectorySource = JarFileSourceCreator.tryCreateSourceFromFilesInDirectory(pluginDirectory);
Source urlSource = new URLSource(urlToJar);
Source classLoaderSource = new ClassLoaderSource(myCustomClassLoader);

```

Note that you should follow good practices when defining your SPIs.  Ideally, they should be interfaces, but classes
are permitted (but require a no-argument constructor due to how java.util.ServiceLoader functions).  To get around this,
you can define an interface to instantiate your classes for you:

```java

MyService selected = myService.getServicesStream().findFirst().get();
MyObject object = selected.createNewObject(1, 2, 3);

```

## Security

Finally, there are some classes within the security package that can be used to protect your application environment
from potentially malicious providers.

(Note that this package may be transitioned to its own artifact in the future.)

A helper class, simply denoted "Security" contains some methods that help establish a secure environment.  Doing so
is as simple as calling one of the following methods:

```java

Security.install();

// or

Security.installWithPolicy(myPolicyFile);

```

To aid in actually securing providers the "SourceFilteringPolicy" class was created.  This class allows one to define
groups of permissions that are applied to:

    * The Application Classpath / System ClassLoader (i.e. trusted code)
    * Individual Sources (each can be configured separately)
    * Unknown Sources (i.e. default permissions)
    
Note that you can revoke permissions for your own, trusted code by clearing the System Permissions and using this
policy.  The interface for this object is well documented within the javadoc of the library.