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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal qunit
 * @phase test
 * @description Runs qunit tests.
 * 
 * @author Ivar Grimstad (ivar.grimstad@cybercom.com)
 */
public class TestMojo extends AbstractJavaScriptMVCMojo {

   private static final String QUNIT_SCRIPT_LINUX = "compress.sh";
   private static final String QUNIT_SCRIPT_WINDOWS = "compress.bat";

   /**
    * {@inheritDoc}}
    */
   @Override
   protected void executeLinux() throws MojoExecutionException {
      try {

         File compressFile = createQUnitScriptLinux();
         executeCommand("chmod", "+x", compressFile.getAbsolutePath());
         executeCommand("chmod", "+x", outputDirectory + "/" + finalName + "/js");
         getLog().info("Executing script " + compressFile.getAbsolutePath().replaceAll(" ", "\\ "));
         executeCommand("./" + QUNIT_SCRIPT_LINUX);

      } catch (IOException e) {
         throw new MojoExecutionException(e.getMessage());
      }
   }

   /**
    * {@inheritDoc}}
    */
   @Override
   protected void executeWindows() throws MojoExecutionException {
      try {

         File compressFile = createQUnitScriptWindows();
         getLog().info("Executing script " + compressFile.getAbsolutePath());
         executeCommand("cmd", "/c", QUNIT_SCRIPT_WINDOWS);

      } catch (IOException e) {
         throw new MojoExecutionException(e.getMessage());
      }
   }
   
      private File createQUnitScriptLinux() throws IOException {

      File targetDir = new File(outputDirectory);
      File file = new File(targetDir, QUNIT_SCRIPT_LINUX);
      file.setExecutable(true);

      PrintWriter writer = new PrintWriter(new FileWriter(file));
      writer.println("#!/bin/bash");
      writer.print("cd ");
      writer.print(outputDirectory);
      writer.print(File.separator);
      writer.println(finalName);
      writer.print("./js ");
//      writer.print(moduleName);
//      writer.print(File.separator);
//      writer.println(buildScript);

      writer.flush();
      writer.close();
      return file;
   }

   private File createQUnitScriptWindows() throws IOException {

      File targetDir = new File(outputDirectory);
      File file = new File(targetDir, QUNIT_SCRIPT_WINDOWS);
      file.setExecutable(true);

      PrintWriter writer = new PrintWriter(new FileWriter(file));
      writer.print("cd ");
      writer.print(outputDirectory);
      writer.print(File.separator);
      writer.println(finalName);
      writer.print("js.bat ");
//      writer.print(moduleName);
//      writer.print(File.separator);
//      writer.println(buildScript);

      writer.flush();
      writer.close();
      return file;
   }

}
