package org.vlad;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args)
    {
        File inputFile = Utils.getFileFromArgs(args);

        if (inputFile != null)
        {
            try
            {
                Transaction transaction = MagicParser.parseTransaction(inputFile);

                PaymentService.process(transaction);
            }
            catch (IOException e)
            {
                MagicLogger.getLogger().severe(e.getMessage());
            }
        }
    }
}