package org.apache.maven.it.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import io.tesla.shell.maven.pom.Pom;
import io.tesla.shell.maven.pom.Pominator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.maven.model.Dependency;
import org.apache.maven.shared.utils.io.FileUtils;
import org.apache.maven.shared.utils.io.IOUtil;

/* @todo this can be replaced with plexus-archiver */
public class ResourceExtractor {

  private static File junitTests = new File("/Users/jvanzyl/js/tesla/maven-integration-testing/core-it-suite/src/test/java/org/apache/maven/it");
  private static File baseDirectory = new File("/tmp/maven-its/");
  
  public static File XsimpleExtractResources(Class cl, String resourcePath) throws IOException {

    File itsDirectory = ResourceExtractor.extractResourcePath(cl, resourcePath, baseDirectory, true);
    // Copy the unit test over
    String javaFileName = cl.getSimpleName() + ".java";
    File javaFile = new File( junitTests, javaFileName);
    String javaFileContent = FileUtils.fileRead(javaFile);
    javaFileContent = javaFileContent.replaceAll(".*File testDir.*", "");
    javaFileContent = javaFileContent.replaceAll("import org.apache.maven.it.*", "");
    javaFileContent = javaFileContent.replaceAll("testDir.getAbsolutePath\\(\\)", "System.getProperty(\"basedir\")");
    System.out.println(javaFileContent);

    File unitTest = new File(itsDirectory, "src/test/java/org/apache/maven/it/" + javaFileName);
    unitTest.getParentFile().mkdirs();
    Writer w = new FileWriter(unitTest);
    IOUtil.copy(javaFileContent, w);
    w.close();
    //FileUtils.copyFile(javaFile, unitTest);
    //
    // Add dependencies to the POM. We need  the IT helper for the abstract test class, and the verifier
    //
    Pom p = new Pom(new File(itsDirectory, "pom.xml"));
    p.addDependency("org.apache.maven.its:maven-it-helper:2.1-SNAPSHOT");
    
    Dependency d = new Dependency();
    d.setGroupId("io.tesla.maven");
    d.setArtifactId("maven-verifier");
    d.setVersion("0.0.2-SNAPSHOT");
    d.setScope("test");
    p.addDependency(d);
    
    p.write();
    return itsDirectory;
  }

  public static File simpleExtractResources(Class cl, String resourcePath) throws IOException {
    String tempDirPath = System.getProperty("maven.test.tmpdir", System.getProperty("java.io.tmpdir"));
    File tempDir = new File(tempDirPath);

    File testDir = new File(tempDir, resourcePath);

    FileUtils.deleteDirectory(testDir);

    testDir = ResourceExtractor.extractResourcePath(cl, resourcePath, tempDir, false);
    return testDir;
  }

  public static File extractResourcePath(String resourcePath, File dest) throws IOException {
    return extractResourcePath(ResourceExtractor.class, resourcePath, dest);
  }

  public static File extractResourcePath(Class cl, String resourcePath, File dest) throws IOException {
    return extractResourcePath(cl, resourcePath, dest, false);
  }

  public static File extractResourcePath(Class cl, String resourcePath, File tempDir, boolean alwaysExtract) throws IOException {
    File dest = new File(tempDir, resourcePath);
    return extractResourceToDestination(cl, resourcePath, dest, alwaysExtract);
  }

  public static File extractResourceToDestination(Class cl, String resourcePath, File destination, boolean alwaysExtract) throws IOException {
    URL url = cl.getResource(resourcePath);
    if (url == null) {
      throw new IllegalArgumentException("Resource not found: " + resourcePath);
    }
    if ("jar".equalsIgnoreCase(url.getProtocol())) {
      File jarFile = getJarFileFromUrl(url);
      extractResourcePathFromJar(cl, jarFile, resourcePath, destination);
    } else {
      try {
        File resourceFile = new File(new URI(url.toExternalForm()));
        if (!alwaysExtract) {
          return resourceFile;
        }
        if (resourceFile.isDirectory()) {
          FileUtils.copyDirectoryStructure(resourceFile, destination);
        } else {
          FileUtils.copyFile(resourceFile, destination);
        }
      } catch (URISyntaxException e) {
        throw new RuntimeException("Couldn't convert URL to File:" + url, e);
      }
    }
    return destination;
  }

  private static void extractResourcePathFromJar(Class cl, File jarFile, String resourcePath, File dest) throws IOException {
    ZipFile z = new ZipFile(jarFile, ZipFile.OPEN_READ);
    String zipStyleResourcePath = resourcePath.substring(1) + "/";
    ZipEntry ze = z.getEntry(zipStyleResourcePath);
    if (ze != null) {
      // DGF If it's a directory, then we need to look at all the entries
      for (Enumeration entries = z.entries(); entries.hasMoreElements();) {
        ze = (ZipEntry) entries.nextElement();
        if (ze.getName().startsWith(zipStyleResourcePath)) {
          String relativePath = ze.getName().substring(zipStyleResourcePath.length());
          File destFile = new File(dest, relativePath);
          if (ze.isDirectory()) {
            destFile.mkdirs();
          } else {
            FileOutputStream fos = new FileOutputStream(destFile);
            try {
              IOUtil.copy(z.getInputStream(ze), fos);
            } finally {
              IOUtil.close(fos);
            }
          }
        }
      }
    } else {
      FileOutputStream fos = new FileOutputStream(dest);
      try {
        IOUtil.copy(cl.getResourceAsStream(resourcePath), fos);
      } finally {
        IOUtil.close(fos);
      }
    }
  }

  private static File getJarFileFromUrl(URL url) {
    if (!"jar".equalsIgnoreCase(url.getProtocol())) {
      throw new IllegalArgumentException("This is not a Jar URL:" + url.toString());
    }
    String resourceFilePath = url.getFile();
    int index = resourceFilePath.indexOf("!");
    if (index == -1) {
      throw new RuntimeException("Bug! " + url.toExternalForm() + " does not have a '!'");
    }
    String jarFileURI = resourceFilePath.substring(0, index);
    try {
      File jarFile = new File(new URI(jarFileURI));
      return jarFile;
    } catch (URISyntaxException e) {
      throw new RuntimeException("Bug! URI failed to parse: " + jarFileURI, e);
    }

  }
}
