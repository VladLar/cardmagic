package org.vlad;


import java.io.IOException;
import java.util.Map;


public class MagicValidator
{
    private MagicValidator() {}

    public static final int MAX_PAN_LENGTH = 16;

    public static String validate(Transaction transaction)
    {
        StringBuilder errorMessage = new StringBuilder();

        try
        {
            validatePANLength(transaction, errorMessage);
            validateType(transaction, errorMessage);
            validatePANRange(transaction, errorMessage);
            validateAmount(transaction, errorMessage);
            validateCurrency(transaction, errorMessage);
        }
        catch (IOException e)
        {
            errorMessage.append(e.getMessage());
        }

        return errorMessage.toString();
    }

    private static void validatePANLength(Transaction transaction, StringBuilder errorMessage)
    {
        String panString = transaction.getPan().toString();
        int panLength = panString.length();
        if (panLength != MAX_PAN_LENGTH)
        {
            errorMessage.append(
                String.format(
                    "PAN must be %d digits long. Actual: %d. ",
                    MAX_PAN_LENGTH,
                    panLength
                )
            );
        }
    }

    private static void validateType(Transaction transaction, StringBuilder errorMessage) throws IOException
    {

        Map<String, Object> requirementData = Utils.getConfigData(Utils.TRANSACTION_TYPES_REQ_FILENAME);

        String type = transaction.getType();

        if (requirementData.get(type) == null)
        {
            errorMessage.append(
                String.format(
                    "Transaction type not supported: %s. ",
                    type
                )
            );
            throw new IOException("");
        }
    }

    private static void validateAmount(Transaction transaction, StringBuilder errorMessage) throws IOException
    {
        Map<String, Object> requirementData = Utils.getConfigData(Utils.TRANSACTION_TYPES_REQ_FILENAME);

        Map<String, Object> reqForType = (Map<String, Object>) requirementData.get(transaction.getType());

        Integer maxSubUnits = (Integer) reqForType.get("maxAmount");

        Integer amountSubUnits = transaction.getAmount();
        if (amountSubUnits == null || amountSubUnits <= 0)
        {
            errorMessage.append("Amount must be above 0.");
            throw new IOException("");
        }

        if (amountSubUnits > maxSubUnits)
        {
            errorMessage.append(
                String.format(
                    "Transactions with type %s must not exceed amount of %.2f. Actual: %.2f. ",
                    reqForType.get("name"),
                    Utils.getUnitsFromSubunits(maxSubUnits),
                    Utils.getUnitsFromSubunits(amountSubUnits)
                )
            );
        }
    }

    private static void validatePANRange(Transaction transaction, StringBuilder errorMessage) throws IOException
    {
        Map<String, Object> requirementData = Utils.getConfigData(Utils.TRANSACTION_TYPES_REQ_FILENAME);
        Map<String, Object> reqForType = (Map<String, Object>) requirementData.get(transaction.getType());

        String panString = transaction.getPan().toString();
        if (!panString.startsWith((String) reqForType.get("firstDigit")))
        {
            errorMessage.append(
                String.format(
                    "Transactions with type %s must have PAN starting with %s. ",
                    reqForType.get("name"),
                    reqForType.get("firstDigit")
                )
            );
        }

        if (transaction.getPan() < (Long) reqForType.get("minPANRange") ||
            transaction.getPan() > (Long) reqForType.get("maxPANRange"))
        {
            errorMessage.append(
                String.format(
                    "Transactions with type %s must have PAN within range [%s - %s]. ",
                    reqForType.get("name"),
                    reqForType.get("minPANRange"),
                    reqForType.get("maxPANRange")
                )
            );
        }
    }

    private static void validateCurrency(Transaction transaction, StringBuilder errorMessage) throws IOException
    {
        Map<String, Object> currencies = Utils.getConfigData(Utils.CURRENCY_CODES_FILENAME);
        String currencyCode = transaction.getCurrencyCode();

        if (currencies.get(currencyCode) == null)
        {
            errorMessage.append(
                    String.format(
                            "Currency code not supported: %s. ",
                            currencyCode
                    )
            );
            throw new IOException("");
        }
    }
}
