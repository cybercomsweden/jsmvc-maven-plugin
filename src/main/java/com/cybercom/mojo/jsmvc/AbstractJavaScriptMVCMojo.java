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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Abstract base class for JavaScriptMVC maven plugins.
 * 
 * @author Ivar Grimstad (ivar.grimstad@cybercom.com)
 */
public abstract class AbstractJavaScriptMVCMojo extends AbstractMojo {

   /**
    * Location of the file.
    *
    * @parameter expression="${project.build.directory}"
    * @required
    */
   protected String outputDirectory;

   /**
    * @parameter expression="${project.build.finalName}"
    * @required
    */
   protected String finalName;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void execute() throws MojoExecutionException, MojoFailureException {

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
   
   /**
    * Executes os specific command.
    * 
    * @param cmd the command(s)
    */
   protected void executeCommand(String... cmd) {
      try {
         ProcessBuilder pb = new ProcessBuilder(cmd);
         pb.directory(new File(outputDirectory));
         Process pr = pb.start();
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
   
   /**
    * Executes on Linux.
    * @throws MojoExecutionException 
    */
   protected abstract void executeLinux() throws MojoExecutionException;
   
   /**
    * Executes on Windows.
    * @throws MojoExecutionException 
    */
   protected abstract void executeWindows() throws MojoExecutionException;
}
