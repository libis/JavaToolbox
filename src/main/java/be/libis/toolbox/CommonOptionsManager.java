package be.libis.toolbox;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

/**
 * This class deals with the default actions required to parse the options.
 * <p>
 * <p>
 * It also processes the common options specified in CommonOptions.
 * <p>
 * See processOptions() for details on usage.
 *
 * @param <T> Specify an interface that inherits from CommonOptions and which
 *            contains the application specific options.
 */
@SuppressWarnings("unused")
public class CommonOptionsManager<T> {

    /**
     * The method that parses the command line options and deals with the common options.
     *
     * <p>
     * In your application's main, call this method like this (with <i>T</i> your derived {@link CommonOptions} interface) :
     * <p>
     * <code>
     * <i>T</i> options = new CommonOptionsManager<<i>T</i>>().processOptions(<i>T</i>.class, args);<br>
     * if (options == null) return; // error messages are printed by the CommonOptionsManager
     * </code>
     * <p>
     * You can then query the resulting option object for your command line options.
     *
     * Important note: You should not create your log4j2 logger in your application before making this call.
     *
     * @param c
     *          Class of the derived interface. This is typically <i>T.class</i>
     * @param args
     *          Pass the command line arguments here.
     * @return a <i>T</i> object or null if parsing failed.
     */
    public T processOptions(Class<T> c, String[] args) {

        T options;

        try {

            options = CliFactory.parseArguments(c, args);

//            if (options == null || !processLoggingOptions((CommonOptions) options)) {
//                System.err.println();
//                System.err.println(CliFactory.createCli(c).getHelpMessage());
//                System.err.println();
//                return null;
//            }

        } catch (ArgumentValidationException ex) {
            System.err.println();
            System.err.println(ex.getMessage());
            if (!ex.getMessage().startsWith("Usage:")) {
                System.err.println();
                System.err.println(CliFactory.createCli(c).getHelpMessage());
            }
            System.err.println();
            return null;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + " : " + e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(" - " + el.toString());
            }
            return null;
        }

        return options;
    }

    /**
     * Processes all the command line options that deal with configuring the
     * logger.
     *
     * The options processed are:
     * <ul>
     * <li>Logging file
     * <li>File logging level
     * <li>Console logging level
     * </ul>
     *
     * @param options
     *          The object containing the parsed command line options
     * @return true if successful , false otherwise.
     */
    private boolean processLoggingOptions(CommonOptions options) {

        String logFilename = options.isLogFile() ? options.getLogFile() : null;
        Level logLevel = options.isLogLevel() ? intToLevel(options.getLogLevel()) : null;
        Level consoleLevel = options.isConsoleLogLevel() ? intToLevel(options.getConsoleLogLevel()) : null;

        if (logFilename != null || logLevel != null || consoleLevel != null) {
            ConfigurationFactory custom = new CommonConfigFactory(logFilename, logLevel, consoleLevel);
            ConfigurationFactory.setConfigurationFactory(custom);
        }

        return true;
    }

    /**
     * Converts an integer to a {@link Level} object.
     *
     * <ul>
     * <li>0 = OFF
     * <li>1 = ERROR
     * <li>2 = WARN
     * <li>3 = INFO
     * <li>4 = ALL
     * </ul>
     *
     * @param i
     *          Logging level number.
     * @return The generated Level object or null if the number was out of range.
     */
    public static Level intToLevel(int i) {
        Level level = null;
        switch (i) {
            case 0:
                level = Level.OFF;
                break;
            case 1:
                level = Level.ERROR;
                break;
            case 2:
                level = Level.WARN;
                break;
            case 3:
                level = Level.INFO;
                break;
            case 4:
                level = Level.ALL;
                break;
        }
        return level;
    }
}
