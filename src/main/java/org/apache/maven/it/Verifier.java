package org.apache.maven.it;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface Verifier {
  public void setLocalRepo(String localRepo);

  public void resetStreams();

  public void displayStreamBuffers();

  public void verify(boolean chokeOnErrorOutput) throws VerificationException;

  public void verifyErrorFreeLog() throws VerificationException;

  public void verifyTextInLog(String text) throws VerificationException;

  public Properties loadProperties(String filename) throws VerificationException;

  public List<String> loadLines(String filename, String encoding) throws IOException;

  public List<String> loadFile(String basedir, String filename, boolean hasCommand) throws VerificationException;

  public List<String> loadFile(File file, boolean hasCommand) throws VerificationException;

  public String getArtifactPath(String org, String name, String version, String ext);

  public String getArtifactPath(String gid, String aid, String version, String ext, String classifier);

  public List<String> getArtifactFileNameList(String org, String name, String version, String ext);

  public String getArtifactMetadataPath(String gid, String aid, String version);

  public String getArtifactMetadataPath(String gid, String aid, String version, String filename);

  public String getArtifactMetadataPath(String gid, String aid);

  public void executeHook(String filename) throws VerificationException;

  public void deleteArtifact(String org, String name, String version, String ext) throws IOException;

  public void deleteArtifacts(String gid) throws IOException;

  public void deleteArtifacts(String gid, String aid, String version) throws IOException;

  public void deleteDirectory(String path) throws IOException;

  public void writeFile(String path, String contents) throws IOException;

  public File filterFile(String srcPath, String dstPath, String fileEncoding, Map filterProperties) throws IOException;

  public Properties newDefaultFilterProperties();

  public void assertFilePresent(String file);

  public void assertFileMatches(String file, String regex);

  public void assertFileNotPresent(String file);

  public void assertArtifactPresent(String org, String name, String version, String ext);

  public void assertArtifactNotPresent(String org, String name, String version, String ext);

  public void executeGoal(String goal) throws VerificationException;

  public void executeGoal(String goal, Map envVars) throws VerificationException;

  public void executeGoals(List<String> goals) throws VerificationException;

  public String getExecutable();

  public void executeGoals(List<String> goals, Map envVars) throws VerificationException;

  public String getMavenVersion() throws VerificationException;

  public void assertArtifactContents(String org, String artifact, String version, String type, String contents) throws IOException;

  public List getCliOptions();

  public void setCliOptions(List<String> cliOptions);

  public void addCliOption(String option);

  public Properties getSystemProperties();

  public void setSystemProperties(Properties systemProperties);

  public void setSystemProperty(String key, String value);

  public Properties getEnvironmentVariables();

  public void setEnvironmentVariables(Properties environmentVariables);

  public void setEnvironmentVariable(String key, String value);

  public Properties getVerifierProperties();

  public void setVerifierProperties(Properties verifierProperties);

  public boolean isAutoclean();

  public void setAutoclean(boolean autoclean);

  public String getBasedir();

  public String getLogFileName();

  public void setLogFileName(String logFileName);

  public void setDebug(boolean debug);

  public boolean isMavenDebug();

  public void setMavenDebug(boolean mavenDebug);

  public void setForkJvm(boolean forkJvm);

  public boolean isDebugJvm();

  public void setDebugJvm(boolean debugJvm);

  public String getLocalRepoLayout();

  public void setLocalRepoLayout(String localRepoLayout);

  public void displayLogFile();

  public void findLocalRepo(String settingsFile) throws VerificationException;

  public String getLocalRepo();
}
