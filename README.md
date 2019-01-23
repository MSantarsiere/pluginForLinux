# pluginForLinux

Per utilizzare il plug-in per la statementCoverage inserire nel pom

            <plugin>
                <groupId>io.github.msantarsiere</groupId>
                <artifactId>rift</artifactId>
                <version>0.13</version>
                 <executions>	                
                    <execution>                	
                        <phase>install</phase>
                        <goals>
                            <goal>statementCoverage</goal> 
                           
                       </goals>
                        <configuration> 
                            <msg>
                                ${basedir}
                            </msg>
                        </configuration>	
                    </execution>	
                </executions>
            </plugin>
            
            Per utilizzare il plug-in per la BranchCoverage inserire nel pom
   <plugin>
                <groupId>io.github.msantarsiere</groupId>
                <artifactId>dado</artifactId>
                <version>0.13</version>
                 <executions>	                
                    <execution>                	
                        <phase>install</phase>
                        <goals>
                           
                            <goal>branchCoverage</goal> 
                       </goals>
                        <configuration> 
                            <msg>
                                ${basedir}
                            </msg>
                        </configuration>	
                    </execution>	
                </executions>
            </plugin>
