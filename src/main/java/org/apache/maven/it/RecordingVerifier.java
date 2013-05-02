package org.apache.maven.it;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class RecordingVerifier implements Verifier {

  private List<String> cliOptions = new ArrayList<String>();
  
  public RecordingVerifier(String basedir, boolean debug) {
    // TODO Auto-generated constructor stub
  }

  public void setLocalRepo(String localRepo) {
  }

  public void resetStreams() {
  }

  public void displayStreamBuffers() {
  }

  public void verify(boolean chokeOnErrorOutput) throws VerificationException {
  }

  public void verifyErrorFreeLog() throws VerificationException {
  }

  public void verifyTextInLog(String text) throws VerificationException {
  }

  public Properties loadProperties(String filename) throws VerificationException {
    return null;
  }

  public List<String> loadLines(String filename, String encoding) throws IOException {
    return null;
  }

  public List<String> loadFile(String basedir, String filename, boolean hasCommand) throws VerificationException {
    return null;
  }

  public List<String> loadFile(File file, boolean hasCommand) throws VerificationException {
    return null;
  }

  public String getArtifactPath(String org, String name, String version, String ext) {
    return null;
  }

  public String getArtifactPath(String gid, String aid, String version, String ext, String classifier) {
    return null;
  }

  public List<String> getArtifactFileNameList(String org, String name, String version, String ext) {
    return null;
  }

  public String getArtifactMetadataPath(String gid, String aid, String version) {
    return null;
  }

  public String getArtifactMetadataPath(String gid, String aid, String version, String filename) {
    return null;
  }

  public String getArtifactMetadataPath(String gid, String aid) {
    return null;
  }

  public void executeHook(String filename) throws VerificationException {
  }

  public void deleteArtifact(String org, String name, String version, String ext) throws IOException {
  }

  public void deleteArtifacts(String gid) throws IOException {
  }

  public void deleteArtifacts(String gid, String aid, String version) throws IOException {
  }

  public void deleteDirectory(String path) throws IOException {
  }

  public void writeFile(String path, String contents) throws IOException {
  }

  public File filterFile(String srcPath, String dstPath, String fileEncoding, Map filterProperties) throws IOException {
    return null;
  }

  public Properties newDefaultFilterProperties() {
    Properties filterProperties = new Properties();
    return filterProperties;
  }

  public void assertFilePresent(String file) {
  }

  public void assertFileMatches(String file, String regex) {
  }

  public void assertFileNotPresent(String file) {
  }

  public void assertArtifactPresent(String org, String name, String version, String ext) {
  }

  public void assertArtifactNotPresent(String org, String name, String version, String ext) {
  }

  public void executeGoal(String goal) throws VerificationException {
  }

  public void executeGoal(String goal, Map envVars) throws VerificationException {
  }

  public void executeGoals(List<String> goals) throws VerificationException {
  }

  public String getExecutable() {
    return null;
  }

  public void executeGoals(List<String> goals, Map envVars) throws VerificationException {
  }

  public String getMavenVersion() throws VerificationException {
    return null;
  }

  public void assertArtifactContents(String org, String artifact, String version, String type, String contents) throws IOException {
  }

  public List getCliOptions() {
    return cliOptions;
  }

  public void setCliOptions(List<String> cliOptions) {
  }

  public void addCliOption(String option) {
  }

  public Properties getSystemProperties() {
    return null;
  }

  public void setSystemProperties(Properties systemProperties) {
  }

  public void setSystemProperty(String key, String value) {
  }

  public Properties getEnvironmentVariables() {
    return null;
  }

  public void setEnvironmentVariables(Properties environmentVariables) {
  }

  public void setEnvironmentVariable(String key, String value) {
  }

  public Properties getVerifierProperties() {
    return null;
  }

  public void setVerifierProperties(Properties verifierProperties) {
  }

  public boolean isAutoclean() {
    return false;
  }

  public void setAutoclean(boolean autoclean) {
  }

  public String getBasedir() {
    return null;
  }

  public String getLogFileName() {
    return null;
  }

  public void setLogFileName(String logFileName) {
  }

  public void setDebug(boolean debug) {
  }

  public boolean isMavenDebug() {
    return false;
  }

  public void setMavenDebug(boolean mavenDebug) {
  }

  public void setForkJvm(boolean forkJvm) {
  }

  public boolean isDebugJvm() {
    return false;
  }

  public void setDebugJvm(boolean debugJvm) {
  }

  public String getLocalRepoLayout() {
    return null;
  }

  public void setLocalRepoLayout(String localRepoLayout) {
  }

  public void displayLogFile() {
  }

  public void findLocalRepo(String settingsFile) throws VerificationException {
  }

  public String getLocalRepo() {
    return null;
  }

}
