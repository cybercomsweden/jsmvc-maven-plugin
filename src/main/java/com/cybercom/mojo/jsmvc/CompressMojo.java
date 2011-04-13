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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Locale;
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

   private static final String COMPRESS_SCRIPT_LINUX = "compress.sh";
   private static final String COMPRESS_SCRIPT_WINDOWS = "compress.bat";
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
    * @parameter expression="${buildScript}" default-value="scripts${file.separator}build.js"
    * @required
    */
   private String buildScript;

   @Override
   public void execute() throws MojoExecutionException, MojoFailureException {

      File targetFolder = new File(outputDirectory);
      if (targetFolder == null || !targetFolder.exists()) {
         targetFolder.mkdirs();
      }

      if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("windows")) {
         executeWindows();
      } else {
         executeLinux();
      }
   }

   private void executeLinux() throws MojoExecutionException {

      try {

         File compressFile = createCompressScriptLinux();
         executeCommand(false, false, "chmod +x " + compressFile.getAbsolutePath());
         executeCommand(false, false, "chmod +x " + outputDirectory + "/" + finalName + "/js");
         getLog().info("Executing script " + compressFile.getAbsolutePath().replaceAll(" ", "\\ "));
//         executeCommand(outputDirectory + File.separator + COMPRESS_SCRIPT_LINUX, false);
         executeCommand(false, true, "./" + COMPRESS_SCRIPT_LINUX);

      } catch (IOException e) {
         throw new MojoExecutionException(e.getMessage());
      }
   }

   private void executeWindows() throws MojoExecutionException {

      try {

         File compressFile = createCompressScriptWindows();
         getLog().info("Executing script " + compressFile.getAbsolutePath());
         executeCommand(true, false, "cmd", "/c", COMPRESS_SCRIPT_WINDOWS);

      } catch (IOException e) {
         throw new MojoExecutionException(e.getMessage());
      }
   }

   private void executeCommand(boolean windows, boolean test, String... cmd) {
      try {

         Runtime run = Runtime.getRuntime();
         Process pr;

         if (test) {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(new File(outputDirectory));
            pr = pb.start();
         } else {
            if (windows) {
               ProcessBuilder pb = new ProcessBuilder(cmd);
               pb.directory(new File(outputDirectory));
               pr = pb.start();
            } else {
               pr = run.exec(cmd[0]);
            }
         }
         InputStream is = pr.getInputStream();
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader br = new BufferedReader(isr);
         String line;
         while ((line = br.readLine()) != null) {
            getLog().info(line);
         }

      } catch (IOException e) {
         getLog().error("IO: " + e.getMessage());
      }
   }

   public File createCompressScriptLinux() throws IOException {

      File targetDir = new File(outputDirectory);
      File file = new File(targetDir, COMPRESS_SCRIPT_LINUX);
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

   public File createCompressScriptWindows() throws IOException {

      File targetDir = new File(outputDirectory);
      File file = new File(targetDir, COMPRESS_SCRIPT_WINDOWS);
      file.setExecutable(true);

      PrintWriter writer = new PrintWriter(new FileWriter(file));
      writer.println("echo Here we go");
      writer.print("cd ");
      writer.print(outputDirectory);
      writer.print(File.separator);
      writer.println(finalName);
      writer.println("echo moved to my directory");
      writer.print("js.bat ");
      writer.print(moduleName);
      writer.print(File.separator);
      writer.println(buildScript);

      writer.flush();
      writer.close();
      return file;
   }
}
