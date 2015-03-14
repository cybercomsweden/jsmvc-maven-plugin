## Goals ##
| **Goal** | **Description** |
|:---------|:----------------|
| compress | Compresses the application to production.js and production.css |

### Parameters ###
| **Name** | **Type** | **Since** | **Description** |
|:---------|:---------|:----------|:----------------|
| moduleName | String | 0.1 | The name of the JavaScriptMVC module to compress.  |
| outputDirectory | String | 0.1 | The target directory. Default value is **${project.build.directory}** |
| finalName | String | 0.1 | The project final name. Default value is **${project.finalName}** |
| buildScript | String | 0.1 | Reference to the build script generated by JavaScriptMVC. Default value is **${moduleName}/scripts/build.js** |

## Usage ##

The following configuration works if you set up a standard maven war project with the JavaScriptMVC cookbook example.

### Layout ###
```
src/main/webapp/WEB-INF/...
               /cookbook/...
               /documentjs/...
               /funcunit/...
               /jquery/...
               /steal/...
               /js
               /js.bat

```

### pom.xml ###

```
   <build>
      <plugins>
         ...
         <plugin>
            <groupId>com.cybercom.mojo</groupId>
            <artifactId>jsmvc-maven-plugin</artifactId>
            <version>0.5</version>
            <executions>
               <execution>
                  <phase>package</phase>
                  <goals>
                     <goal>compress</goal>
                  </goals>
                  <configuration>
                     <moduleName>cookbook</moduleName>
                  </configuration>
               </execution>
            </executions>
         </plugin>
         ...
      </plugins>
   </build>
```