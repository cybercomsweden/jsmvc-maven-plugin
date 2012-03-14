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
import java.io.FileWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;
import static org.powermock.reflect.Whitebox.*;

/**
 *
 * @author Ivar Grimstad (ivar.grimstad@cybercomgroup.com)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CompressMojo.class)
public class CompressMojoTest {

   private File dirMock = createMock(File.class);
   private File fileMock = createMock(File.class);
   private FileWriter fwMock = createMock(FileWriter.class);
   private PrintWriter pwMock = createMock(PrintWriter.class);
   private ProcessBuilder pbMock = createMock(ProcessBuilder.class);
   private Process pMock = createMock(Process.class);
   private InputStream isMock = createMock(InputStream.class);
   private InputStreamReader isrMock = createMock(InputStreamReader.class);
   private BufferedReader brMock = createMock(BufferedReader.class);
   private CompressMojo mojo = new CompressMojo();

   @Before
   public void setup() {
      setInternalState(mojo, "outputDirectory", "jall");
      setInternalState(mojo, "finalName", "final");
      setInternalState(mojo, "moduleName", "module");
      setInternalState(mojo, "buildScript", "script");
   }

   /**
    * Test of executeLinux method, of class CompressMojo.
    */
   @Test
   public void testExecuteLinux() throws Exception {

      expectNew(File.class, "jall").andReturn(dirMock);
      expectNew(File.class, dirMock, "compress.sh").andReturn(fileMock);
      expect(fileMock.setExecutable(Boolean.TRUE)).andReturn(Boolean.TRUE);
      expectNew(FileWriter.class, fileMock).andReturn(fwMock);
      expectNew(PrintWriter.class, fwMock).andReturn(pwMock);
      pwMock.println("#!/bin/bash");
      pwMock.print("cd ");
      pwMock.print("jall");
      pwMock.print(File.separator);
      pwMock.println("final");
      pwMock.print("./js ");
      pwMock.print("module");
      pwMock.print(File.separator);
      pwMock.println("script");
      pwMock.flush();
      pwMock.close();
      expect(fileMock.getAbsolutePath()).andReturn("absolute").times(2);

      // chmod on script
      expectNew(ProcessBuilder.class, "chmod", "+x", "absolute").andReturn(pbMock);
      expectNew(File.class, "jall").andReturn(fileMock);
      expect(pbMock.directory(fileMock)).andReturn(pbMock);
      expect(pbMock.start()).andReturn(pMock);
      expect(pMock.getInputStream()).andReturn(isMock);
      expectNew(InputStreamReader.class, isMock).andReturn(isrMock);
      expectNew(BufferedReader.class, isrMock).andReturn(brMock);
      expect(brMock.readLine()).andReturn(null);

      // chmod on js
      expectNew(ProcessBuilder.class, "chmod", "+x", "jall/final/js").andReturn(pbMock);
      expectNew(File.class, "jall").andReturn(fileMock);
      expect(pbMock.directory(fileMock)).andReturn(pbMock);
      expect(pbMock.start()).andReturn(pMock);
      expect(pMock.getInputStream()).andReturn(isMock);
      expectNew(InputStreamReader.class, isMock).andReturn(isrMock);
      expectNew(BufferedReader.class, isrMock).andReturn(brMock);
      expect(brMock.readLine()).andReturn(null);

      // compress
      expectNew(ProcessBuilder.class, "./compress.sh").andReturn(pbMock);
      expectNew(File.class, "jall").andReturn(fileMock);
      expect(pbMock.directory(fileMock)).andReturn(pbMock);
      expect(pbMock.start()).andReturn(pMock);
      expect(pMock.getInputStream()).andReturn(isMock);
      expectNew(InputStreamReader.class, isMock).andReturn(isrMock);
      expectNew(BufferedReader.class, isrMock).andReturn(brMock);
      expect(brMock.readLine()).andReturn(null);
      isrMock.close();
      isrMock.close();
      isrMock.close();

      replayAll();
      mojo.executeLinux();
      verifyAll();
   }

   /**
    * Test of executeWindows method, of class CompressMojo.
    */
   @Test
   public void testExecuteWindows() throws Exception {

      expectNew(File.class, "jall").andReturn(dirMock);
      expectNew(File.class, dirMock, "compress.bat").andReturn(fileMock);
      expect(fileMock.setExecutable(Boolean.TRUE)).andReturn(Boolean.TRUE);
      expectNew(FileWriter.class, fileMock).andReturn(fwMock);
      expectNew(PrintWriter.class, fwMock).andReturn(pwMock);
      pwMock.print("cd ");
      pwMock.print("jall");
      pwMock.print(File.separator);
      pwMock.println("final");
      pwMock.print("js.bat ");
      pwMock.print("module");
      pwMock.print(File.separator);
      pwMock.println("script");
      pwMock.flush();
      pwMock.close();
      expect(fileMock.getAbsolutePath()).andReturn("absolute");

      // compress
      expectNew(ProcessBuilder.class, "cmd", "/c", "compress.bat").andReturn(pbMock);
      expectNew(File.class, "jall").andReturn(fileMock);
      expect(pbMock.directory(fileMock)).andReturn(pbMock);
      expect(pbMock.start()).andReturn(pMock);
      expect(pMock.getInputStream()).andReturn(isMock);
      expectNew(InputStreamReader.class, isMock).andReturn(isrMock);
      expectNew(BufferedReader.class, isrMock).andReturn(brMock);
      expect(brMock.readLine()).andReturn(null);
      isrMock.close();

      replayAll();
      mojo.executeWindows();
      verifyAll();
   }
}
