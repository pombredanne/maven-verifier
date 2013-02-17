package org.apache.maven.it;

import java.io.File;

import junit.framework.TestCase;

public class Embedded3xLauncherTest
    extends TestCase
{
    private String testProjectDirectory;

    private File logFileEmbedded0;

    private File logFileEmbedded1;

    private File logFileVerifier0;

    private File logFileVerifier1;

    private File mavenHome;

    protected void setUp()
        throws Exception
    {
        String basedir = System.getProperty( "user.dir" );
        testProjectDirectory = new File( basedir, "src/test/projects/p0" ).getAbsolutePath();
         mavenHome = new File("/Users/jvanzyl/apache-maven-3.1-SNAPSHOT-logback");
        //mavenHome = new File( "/Users/jvanzyl/apache-maven-3.0.4" );
        System.setProperty( "maven.home", mavenHome.getAbsolutePath() );

    }

    public void testEmbeddedLauncherDirectlyAgainstMavenInstallation()
        throws Exception
    {
        logFileEmbedded0 = new File( testProjectDirectory, "log-embedder0.txt" );
        logFileEmbedded0.delete();

        Embedded3xLauncher launcher0 = new Embedded3xLauncher( mavenHome.getAbsolutePath() );
        launcher0.run( new String[] { "clean", "-l", logFileEmbedded0.getName() }, testProjectDirectory, logFileEmbedded0 );

        logFileEmbedded1 = new File( testProjectDirectory, "log-embedder1.txt" );
        logFileEmbedded1.delete();

        Embedded3xLauncher launcher1 = new Embedded3xLauncher( mavenHome.getAbsolutePath() );
        launcher1.run( new String[] { "clean", "-l", logFileEmbedded1.getName() }, testProjectDirectory, logFileEmbedded1 );
    }

    public void testEmbeddedLauncherWrappedByTheVerifier()
        throws Exception
    {
        logFileVerifier0 = new File( testProjectDirectory, "log-verifier0.txt" );
        logFileVerifier0.delete();

        Verifier v0 = newVerifier( testProjectDirectory, null, false );
        // Log file is relative to the working directory
        v0.setLogFileName( logFileVerifier0.getName() );
        v0.setForkJvm( false );
        v0.setAutoclean( false );
        v0.deleteDirectory( "target" );
        v0.executeGoal( "clean" );
        v0.resetStreams();

        logFileVerifier1 = new File( testProjectDirectory, "log-verifier1.txt" );
        logFileVerifier1.delete();

        Verifier v1 = newVerifier( testProjectDirectory, null, false );
        // Log file is relative to the working directory
        v1.setLogFileName( logFileVerifier1.getName() );
        v1.setForkJvm( false );
        v1.setAutoclean( false );
        v1.deleteDirectory( "target" );
        v1.executeGoal( "clean" );
        v1.resetStreams();

    }

    protected Verifier newVerifier( String basedir, String settings, boolean debug )
        throws VerificationException
    {
        Verifier verifier = new Verifier( basedir, debug );

        verifier.setAutoclean( false );

        if ( settings != null )
        {
            File settingsFile;
            if ( settings.length() > 0 )
            {
                settingsFile = new File( "settings-" + settings + ".xml" );
            }
            else
            {
                settingsFile = new File( "settings.xml" );
            }

            if ( !settingsFile.isAbsolute() )
            {
                String settingsDir = System.getProperty( "maven.it.global-settings.dir", "" );
                if ( settingsDir.length() > 0 )
                {
                    settingsFile = new File( settingsDir, settingsFile.getPath() );
                }
            }

            String path = settingsFile.getAbsolutePath();

            // dedicated CLI option only available since MNG-3914
            // if ( matchesVersionRange( "[2.1.0,)" ) )
            // {
            // verifier.getCliOptions().add( "--global-settings" );
            // if ( path.indexOf( ' ' ) < 0 )
            // {
            // verifier.getCliOptions().add( path );
            // }
            // else
            // {
            // verifier.getCliOptions().add( '"' + path + '"' );
            // }
            // }
            // else
            // {
            // verifier.getSystemProperties().put( "org.apache.maven.global-settings", path );
            // }
        }

        return verifier;
    }

}
