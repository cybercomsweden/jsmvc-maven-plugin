# Introduction #

This is a short getting started guide.

**NOTE! Version 0.1 of jsmvc-maven-plugin will only work on Linux!**

## Setup ##

```
   <pluginRepositories>
      <pluginRepository>
         <releases>
            <updatePolicy>always</updatePolicy>
            <checksumPolicy>fail</checksumPolicy>
         </releases>
         <snapshots>
            <enabled>false</enabled>
            <updatePolicy>never</updatePolicy>
            <checksumPolicy>warn</checksumPolicy>
         </snapshots>
         <id>cybercom</id>
         <url>http://nexus.pot.cybercom.com/content/groups/public</url>
         <layout>default</layout>
      </pluginRepository>
   </pluginRepositories>
```