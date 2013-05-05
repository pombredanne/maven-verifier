package org.apache.maven.it.launcher;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.shared.utils.cli.CommandLineException;
import org.apache.maven.shared.utils.cli.CommandLineUtils;
import org.apache.maven.shared.utils.cli.Commandline;
import org.apache.maven.shared.utils.cli.StreamConsumer;
import org.apache.maven.shared.utils.cli.WriterStreamConsumer;
import org.apache.maven.shared.utils.io.FileUtils;

/**
 * @author Benjamin Bentmann
 */
public class ForkedLauncher implements MavenLauncher {

  private final String mavenHome;

  private final Map<String, String> envVars;

  private final String executable;

  public ForkedLauncher(String mavenHome) {
    this(mavenHome, Collections.<String, String> emptyMap(), false);
  }

  public ForkedLauncher(String mavenHome, Map<String, String> envVars, boolean debugJvm) {
    this.mavenHome = mavenHome;
    this.envVars = Collections.unmodifiableMap(new LinkedHashMap<String, String>(envVars));

    String script = debugJvm ? "mvnDebug" : "mvn";

    if (mavenHome != null) {
      executable = new File(mavenHome, "bin/" + script).getPath();
    } else {
      executable = script;
    }
  }

  public int run(String[] cliArgs, Map<String, String> envVars, String workingDirectory, File logFile) throws IOException, LauncherException {
    Commandline cmd = new Commandline();

    cmd.setExecutable(executable);

    if (mavenHome != null) {
      cmd.addEnvironment("M2_HOME", mavenHome);
    }

    if (envVars != null) {
      for (Object o : envVars.keySet()) {
        String key = (String) o;

        cmd.addEnvironment(key, (String) envVars.get(key));
      }
    }

    if (envVars == null || envVars.get("JAVA_HOME") == null) {
      cmd.addEnvironment("JAVA_HOME", System.getProperty("java.home"));
    }

    cmd.addEnvironment("MAVEN_TERMINATE_CMD", "on");

    cmd.setWorkingDirectory(workingDirectory);

    for (String cliArg : cliArgs) {
      cmd.createArg().setValue(cliArg);
    }

    Writer logWriter = new FileWriter(logFile);

    StreamConsumer out = new WriterStreamConsumer(logWriter);

    StreamConsumer err = new WriterStreamConsumer(logWriter);

    try {
      return CommandLineUtils.executeCommandLine(cmd, out, err);
    } catch (CommandLineException e) {
      throw new LauncherException("Failed to run Maven: " + e.getMessage() + "\n" + cmd, e);
    } finally {
      logWriter.close();
    }
  }

  public int run(String[] cliArgs, String workingDirectory, File logFile) throws IOException, LauncherException {
    return run(cliArgs, envVars, workingDirectory, logFile);
  }

  public String getMavenVersion() throws IOException, LauncherException {
    File logFile;
    try {
      logFile = File.createTempFile("maven", "log");
    } catch (IOException e) {
      throw new LauncherException("Error creating temp file", e);
    }

    try {
      // disable EMMA runtime controller port allocation, should be harmless if EMMA is not used
      Map<String, String> envVars = Collections.singletonMap("MAVEN_OPTS", "-Demma.rt.control=false");
      run(new String[] {
        "--version"
      }, envVars, null, logFile);
    } catch (IOException e) {
      throw new LauncherException("IO Error communicating with commandline " + e.toString(), e);
    }

    List<String> logLines = FileUtils.loadFile(logFile);
    //noinspection ResultOfMethodCallIgnored
    logFile.delete();

    String version = extractMavenVersion(logLines);

    if (version == null) {
      version = extractTeslaVersion(logLines);
    }

    if (version == null) {
      throw new LauncherException("Illegal maven output: String 'Maven version: ' not found in the following output:\n" + StringUtils.join(logLines.iterator(), "\n"));
    } else {
      return version;
    }
  }

  private static final Pattern MAVEN_VERSION = Pattern.compile("(?i).*Maven [^0-9]*([0-9]\\S*).*");

  static String extractMavenVersion(List<String> logLines) {
    String version = null;

    for (Iterator<String> it = logLines.iterator(); version == null && it.hasNext();) {
      String line = (String) it.next();

      Matcher m = MAVEN_VERSION.matcher(line);
      if (m.matches()) {
        version = m.group(1);
      }
    }

    return version;
  }

  static String extractTeslaVersion(List<String> logLines) {
    String version = null;

    final Pattern MAVEN_VERSION = Pattern.compile("(?i).*Tesla [^0-9]*([0-9]\\S*).*");

    for (Iterator<String> it = logLines.iterator(); version == null && it.hasNext();) {
      String line = (String) it.next();

      Matcher m = MAVEN_VERSION.matcher(line);
      if (m.matches()) {
        version = m.group(1);
      }
    }

    return version;
  }
}
