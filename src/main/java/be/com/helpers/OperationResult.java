package be.com.helpers;

public class OperationResult
{
    public static OperationResult ok()
    {
        return new OperationResult(true, "");
    }

    public static OperationResult error(String ErrorMessage)
    {
        return new OperationResult(false, ErrorMessage);
    }

    private Boolean result;
    private String ErrorMessage;

    private OperationResult(Boolean result, String ErrorMessage)
    {
        this.result = result;
        this.ErrorMessage = ErrorMessage;
    }

    public Boolean getResult()
    {
        return result;
    }

    public String getErrorMessage()
    {
        return ErrorMessage;
    }

    public Boolean isOk()
    {
        return result;
    }

    public Boolean isError()
    {
        return !result;
    }
}
