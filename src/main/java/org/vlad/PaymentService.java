package org.vlad;


import java.io.IOException;

public class PaymentService
{
    private PaymentService() {}
    public static void process(Transaction transaction)
    {
        String errorMsg = MagicValidator.validate(transaction);

        if (errorMsg.isEmpty())
        {
            transaction.setStatus("01");

            String logMessage;
            try
            {
                logMessage = MagicLogger.prepareLogMessage(transaction);
            }
            catch (IOException e)
            {
                logMessage = e.getMessage();
            }

            MagicLogger.getLogger().info(logMessage);
        }
        else
        {
            transaction.setStatus("02");
            MagicLogger.getLogger().severe(errorMsg);
        }
    }
}
