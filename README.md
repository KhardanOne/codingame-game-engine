# Goal

This is a fork of [CodinGame/coding-game-engine](https://github.com/CodinGame/codingame-game-engine). The original engine is used by CodinGame games. This fork removes the time limitations, so that player agents can be debugged when running those games locally.

# Disclaimer
Use it on your own risk. There are absolutely no guaranties for anything. I created this experiment as a total beginner to Java and Maven. If Github allowed private forks, then this repository would not be public.

# Requirements
- [OpenJDK 8](https://www.openlogic.com/openjdk-downloads) (newer is not good)
    - install hints:
        - download a zip version and extract
        - add JDK8/bin folder to path
- [Maven](https://maven.apache.org/download.cgi)
    - install hints:
        - download a zip verison and extract
        - add Maven/bin folder to path

# Installation
This will install the engine to `c:\users\user\.m2\repository` folder.

```
git clone https://github.com/KhardanOne/codingame-game-engine engine
cd engine
mvn install
```
The engine folder can be deleted if installed successfully or kept for further tampering.


# Referencing from games
Inside the game's `pom.xml` file's `dependencies` section replace all `com.codingame.gameengine` groupIds and versions to `khardan.dev.gameengine` and `master-SNAPSHOT`, respectively. The result should look something similar:

```xml
<dependencies>
    <dependency>
        <groupId>khardan.dev.gameengine</groupId>
        <artifactId>core</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>

    <dependency>
        <groupId>khardan.dev.gameengine</groupId>
        <artifactId>module-entities</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>

    <dependency>
        <groupId>khardan.dev.gameengine</groupId>
        <artifactId>runner</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>
</dependencies>
```

For an example see [my other repository about how to modify a CG game to debug agents](https://github.com/KhardanOne/SpringChallenge2022).
