//This is an error response DTO
package in.abhi.votezy.exception;

public class ErrorResponse {
    private int statusCode;
    private String messageString;

    public ErrorResponse() {
    }

    public ErrorResponse(int statusCode, String messageString) {
        this.statusCode = statusCode;
        this.messageString = messageString;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }
}
