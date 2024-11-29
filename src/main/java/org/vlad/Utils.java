package org.vlad;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;

public class Utils
{
    private Utils() {}

    static final String CURRENCY_CODES_FILENAME = "currency_codes.json";
    static final String TRANSACTION_STATUSES_FILENAME = "transaction_statuses.json";
    static final String TRANSACTION_TYPES_REQ_FILENAME = "transaction_types_requirements.json";


    public static File getFileFromArgs(String[] args)
    {
        Path filepath = Paths.get("input", "input.json");

        if (args.length > 1)
        {
            MagicLogger.getLogger().severe("More than one argument given.");
            return null;
        }

        if (args.length == 1)
        {
            filepath = Paths.get(args[0]);
        }
        return checkFile(filepath);
    }

    public static File checkFile(Path filepath)
    {
        File file = filepath.toFile();

        if (!file.exists())
        {
            MagicLogger.getLogger().log(Level.SEVERE, "File does not exist: {0}", filepath);
            return null;
        }

        if (!file.isFile())
        {
            MagicLogger.getLogger().log(Level.SEVERE, "The specified path does not point to a file: {0}", filepath);
            return null;
        }
        return file;
    }

    public static String getCurrentDateTimeFormatted(String format)
    {
        LocalDateTime now = LocalDateTime.now();

        return now.format(DateTimeFormatter.ofPattern(format));
    }

    public static float getUnitsFromSubunits(int subUnits)
    {
        return (float) subUnits / 100;
    }

    public static String sanitizePAN(Long pan)
    {
        String panPartOne = pan.toString().substring(0, 6);
        String panPartTwo = "*".repeat(6);
        String panPartThree = pan.toString().substring(12);

        return panPartOne + panPartTwo + panPartThree;
    }

    public static Map<String, Object> getConfigData(String confFileName) throws IOException {
        Path confFilePath = Paths.get("config", confFileName);

        File file = checkFile(confFilePath);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, Map.class);

    }

}
