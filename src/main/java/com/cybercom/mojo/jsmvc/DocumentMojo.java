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

import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal document
 * @phase package
 * @description Documents application using the underlaying DocumentJS
 * 
 * @author Ivar Grimstad (ivar.grimstad@cybercom.com)
 */
public class DocumentMojo extends AbstractJavaScriptMVCMojo {

   private static final String DOCUMENT_SCRIPT_LINUX = "compress.sh";
   private static final String DOCUMENT_SCRIPT_WINDOWS = "compress.bat";

   @Override
   protected void executeLinux() throws MojoExecutionException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   protected void executeWindows() throws MojoExecutionException {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
