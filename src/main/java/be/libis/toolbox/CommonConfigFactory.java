package be.libis.toolbox;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class CommonConfigFactory extends ConfigurationFactory {

    private final String logFilename;
    private final Level logLevel;
    private final Level consoleLevel;

    CommonConfigFactory(String logFilename, Level logLevel, Level consoleLevel) {
        super();
        this.logFilename = logFilename;
        this.logLevel = logLevel;
        this.consoleLevel = consoleLevel;
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{"*"};
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {

        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();

        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        builder.add(console);

        LayoutComponentBuilder standard = builder.newLayout("PatternLayout");
        standard.addAttribute("pattern", "%highlight{%-5level} - %msg%n%xThrowable{3}");
        console.add(standard);

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(consoleLevel == null ? Level.INFO : consoleLevel);
        rootLogger.add(builder.newAppenderRef("stdout"));

        if (logFilename != null) {
            AppenderComponentBuilder file = builder.newAppender("log", "File");
            file.addAttribute("fileName", logFilename);
            builder.add(file);

            if (logLevel != null) {
                FilterComponentBuilder levelFilter =
                        builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
                levelFilter.addAttribute("level", logLevel);
                file.add(levelFilter);
            }

            LayoutComponentBuilder logfile = builder.newLayout("PatternLayout");
            logfile.addAttribute("pattern", "%level{length=1},%sn,[%d],%C,%M,%msg - %msg%n%xThrowable{3}");
            file.add(logfile);

            rootLogger.add(builder.newAppenderRef("log"));
        }

        builder.add(rootLogger);

        return builder.build();
    }
}
