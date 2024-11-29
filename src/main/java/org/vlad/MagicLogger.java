package org.vlad;

import java.io.IOException;
import java.util.logging.*;


public class MagicLogger
{
    private static final String FILEDATE_FORMAT = "yyMMdd";
    static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String LOG_FORMAT = "%1$tH:%1$tM:%1$tS.%1$tL %2$s: %3$s %n";
    private static Logger logger;

    private MagicLogger() {}

    public static Logger getLogger()
    {
        if (logger == null)
        {
            logger = Logger.getLogger(MagicLogger.class.getName());
            initLogger();
        }

        return logger;
    }

    private static void initLogger()
    {
        try
        {
            logger.addHandler(getFileHandler());
        }
        catch (java.io.IOException e)
        {
            System.err.println("Failed to initialize log file handler: " + e.getMessage());
        }
    }

    private static FileHandler getFileHandler() throws IOException
    {
        StringBuilder sb = new StringBuilder();

        sb.append("output/cardmagic_");
        sb.append(Utils.getCurrentDateTimeFormatted(FILEDATE_FORMAT));
        sb.append(".log");

        FileHandler fileHandler = new FileHandler(sb.toString(),true);

        fileHandler.setLevel(Level.ALL);

        fileHandler.setFormatter(
            new Formatter()
                {
                    @Override
                    public String format(LogRecord record)
                    {
                        return String.format(LOG_FORMAT, record.getMillis(), record.getLevel(), record.getMessage());
                    }
                }
        );
        return fileHandler;
    }

    public static String prepareLogMessage(Transaction transaction) throws IOException {
        StringBuilder message = new StringBuilder();

        message.append("Processed transaction with card number ");

        message.append(Utils.sanitizePAN(transaction.getPan()));

        message.append(" on ");

        message.append(Utils.getCurrentDateTimeFormatted(DATETIME_FORMAT));

        message.append(", amount ");

        message.append(String.format("%.2f ", Utils.getUnitsFromSubunits(transaction.getAmount())));

        message.append(Utils.getConfigData(Utils.CURRENCY_CODES_FILENAME).get(transaction.getCurrencyCode()));

        message.append(", status ");

        message.append(Utils.getConfigData(Utils.TRANSACTION_STATUSES_FILENAME).get(transaction.getStatus()));

        message.append(". Return transaction details: ");

        message.append(transaction);


        return message.toString();
    }

}
