package org.vlad;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class MagicParser
{
    private MagicParser() {}
    public static Transaction parseTransaction(File file) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(file, Transaction.class);
    }
}
