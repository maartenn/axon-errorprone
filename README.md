[![](https://jitpack.io/v/maartenn/axon-errorprone.svg)](https://jitpack.io/#maartenn/axon-errorprone)

# axon-errorprone
Compile-time checks for [Axon Framework](https://github.com/AxonFramework/AxonFramework) created as extension for [Google Error Prone](https://github.com/google/error-prone).

Running this plugin (together with error-prone) will help you preventing common errors while using Axon Framework by giving errors at compile time.


# Error Checks
## `InitialResultNotCalledOnSubscriptionQuery`
Method initialResult() is not called but updates() is called. For more info see; [wiki](https://github.com/maartenn/axon-errorprone/wiki/InitialResultNotCalledOnSubscriptionQuery)


# Usage 
## Gradle
To use gradle with error prone you need the [Gradle ErrorProne Plugin](https://github.com/tbroyer/gradle-errorprone-plugin/)
```
plugins {
    id "net.ltgt.errorprone" version "2.0.2"
}

...

repositories {
    maven {
        url 'https://jitpack.io'
        content {              
            includeGroup 'com.github.maartenn'
        }
    }
}
    
...

dependencies {
    errorprone 'com.google.errorprone:error_prone_core:2.10.0'
    errorprone 'com.github.maartenn:axon-errorprone:0.0.5'
}
```


## Maven
```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
</repositories>
    
...

<dependency>
	    <groupId>com.github.maartenn</groupId>
	    <artifactId>axon-errorprone</artifactId>
	    <version>0.0.5</version>
</dependency>
```
