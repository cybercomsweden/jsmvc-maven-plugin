/**
 * Copyright (C) 2011 by Cybercom Sweden AB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cybercom.mojo.jsmvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal compress
 * @phase package
 * @description Compresses application to one javascript file and one css file
 * 
 * @author Ivar Grimstad (ivar.grimstad@cybercom.com)
 */
public class CompressMojo extends AbstractMojo {

   private static final String COMPRESS_SCRIPT = "compress.sh";
   /**
    * @parameter expression="${moduleName}"
    * @required
    */
   private String moduleName;
   /**
    * Location of the file.
    *
    * @parameter expression="${project.build.directory}"
    * @required
    */
   private String outputDirectory;
   /**
    * @parameter expression="${project.build.finalName}"
    * @required
    */
   private String finalName;
   /**
    * @parameter expression="${buildScript}" default-value="scripts/build.js"
    * @required
    */
   private String buildScript;

   @Override
   public void execute() throws MojoExecutionException, MojoFailureException {

      File targetFolder = new File(outputDirectory);
      if (targetFolder == null || !targetFolder.exists()) {
         targetFolder.mkdirs();
      }

      try {
         File compressFile = createCompressScript();
         executeCommand("chmod +x " + compressFile.getAbsolutePath());
         executeCommand("chmod +x " + outputDirectory + "/" + finalName + "/js");
         getLog().info("Executing script " + compressFile.getAbsolutePath().replaceAll(" ", "\\ "));
         executeCommand(outputDirectory + File.separator + COMPRESS_SCRIPT);
      } catch (IOException e) {
         throw new MojoExecutionException(e.getMessage());
      }
   }

   private void executeCommand(String cmd) {
      try {
         Runtime run = Runtime.getRuntime();
         Process pr = run.exec(cmd);
         pr.waitFor();
         BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
         String line = "";
         while ((line = buf.readLine()) != null) {
            getLog().info(line);
         }

      } catch (IOException e) {
         getLog().error("IO: " + e.getMessage());
      } catch (InterruptedException e) {
         getLog().error("Interrupt: " + e.getMessage());
      }
   }

   public File createCompressScript() throws IOException {

      File targetDir = new File(outputDirectory);
      File file = new File(targetDir, COMPRESS_SCRIPT);
      file.setExecutable(true);

      PrintWriter writer = new PrintWriter(new FileWriter(file));
      writer.println("#!/bin/bash");
      writer.print("cd ");
      writer.print(outputDirectory);
      writer.print(File.separator);
      writer.println(finalName);
      writer.print("./js ");
      writer.print(moduleName);
      writer.print(File.separator);
      writer.println(buildScript);

      writer.flush();
      writer.close();
      return file;
   }
}
