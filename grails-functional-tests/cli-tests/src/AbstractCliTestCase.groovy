import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.*

/**
 * The linchpin of the CLI functional tests. This abstract test case
 * makes it easy to run a Grails command and query its output. It's
 * currently configured via a set of system properties:
 * <ul>
 * <li><tt>grails.home</tt> - location of Grails distribution to test</li>
 * <li><tt>grails.version</tt> - version of Grails we're testing</li>
 * <li><tt>cli.test.dir</tt> - location of the base CLI test directory</li>
 * <li><tt>grails.work.dir</tt> - location of the Grails global working directory</li>
 * </ul>
 */
abstract class AbstractCliTestCase extends GroovyTestCase {
    final Lock lock = new ReentrantLock()
    final Condition condition = lock.newCondition() 
    final Condition waiting = lock.newCondition()

    private String commandOutput
    private String grailsHome = new File(System.getProperty("grails.home") ?: "../grails").absolutePath
    private String grailsVersion = System.getProperty("grails.version") ?: "1.2-SNAPSHOT"

    private File baseWorkDir = new File(System.getProperty("cli.test.dir") ?: "cli-tests", "tmp")
    private File workDir = baseWorkDir
    private File outputDir = new File(System.getProperty("cli.test.dir") ?: "cli-tests", "output")
    private Process process
    private boolean streamsProcessed

    /**
     * Executes a Grails command. The path to the Grails script is
     * inserted at the front, so the first element of <tt>command</tt>
     * should be the name of the Grails command you want to start,
     * e.g. "help" or "run-app".
     * @param a list of command arguments (minus the Grails script/executable).
     */
    protected void execute(List<String> command) {
        // Make sure the working and output directories exist before
        // running the command.
        baseWorkDir.mkdirs()
        outputDir.mkdirs()

        // Add the path to the Grails script as the first element of
        // the command. Note that we use an absolute path.
        def cmd = new ArrayList<String>(command.size() + 2)
        cmd.add "${grailsHome}/bin/grails".toString()
        cmd.add "-Dgrails.work.dir=${System.getProperty('grails.work.dir')}".toString()
        cmd.addAll command

        // Prepare to execute Grails as a separate process in the
        // configured working directory.
        def pb = new ProcessBuilder(cmd)
        pb.redirectErrorStream(true)
        pb.directory(workDir)
        pb.environment()["GRAILS_HOME"] = grailsHome
        
        process = pb.start()

        // Read the process output on a separate thread. This is
        // necessary to deal with output that overflows the buffer
        // and when a command requires user input at some stage.
        final currProcess = process
        Thread.startDaemon {
            output = currProcess.in.text

            // Once we've finished reading the process output, signal
            // the main thread.
            signalDone()
        }
    }

    /**
     * Returns the process output as a string.
     */
    String getOutput() {
        return commandOutput
    }

    void setOutput(String output) {
        this.commandOutput = output
    }

    /**
     * Returns the working directory for the current command. This
     * may be the base working directory or a project.
     */
    File getWorkDir() {
        return workDir
    }

    void setWorkDir(File dir) {
        this.workDir = dir
    }

    /**
     * Returns the base working directory, which is where the test
     * projects are created.
     */
    File getBaseWorkDir() {
        return baseWorkDir
    }

    /**
     * Allows you to provide user input for any commands that require
     * it. In other words, you can run commands in interactive mode.
     * For example, you could pass "app1" as the <tt>input</tt> parameter
     * when running the "create-app" command.
     */
    void enterInput(String input) {
        process << input << "\r"
    }

    /**
     * Waits for the current command to finish executing. It returns
     * the exit code from the external process. It also dumps the
     * process output into the "cli-tests/output" directory to aid
     * debugging.
     */
    int waitForProcess() {
        // First wait for the process to finish.
        int code = process.waitFor()

        // Now wait for the stream reader threads to finish.
        lock.lock()
        try {
            while (!streamsProcessed) condition.await(2, TimeUnit.MINUTES)
        }
        finally {
            lock.unlock()
        }

        // DEBUG - Dump the process output to a file.
        int i = 1
        def outFile = new File(outputDir, "${getClass().simpleName}-out-${i}.txt")
        while (outFile.exists()) {
            i++
            outFile = new File(outputDir, "${getClass().simpleName}-out-${i}.txt")
        }
        outFile << commandOutput
        // END DEBUG

        return code
    }

    /**
     * Signals any threads waiting on <tt>condition</tt> to inform them
     * that the process output stream has been read. Should only be used
     * by this class (not sub-classes). It's protected so that it can be
     * called from the reader thread closure (some strange Groovy behaviour).
     */
    protected void signalDone() {
        // Signal waiting threads that we're done.
        lock.lock()
        try {
            streamsProcessed = true
            condition.signalAll()
        }
        finally {
            lock.unlock()
        }
    }

    /**
     * Checks that the output of the current command starts with the
     * expected header, which includes the Grails version and the
     * location of GRAILS_HOME.
     */
    protected final void verifyHeader() {
        assertTrue output.startsWith("""Welcome to Grails ${grailsVersion} - http://grails.org/
Licensed under Apache Standard License 2.0
Grails home is set to: ${grailsHome}
""")
    }
}
