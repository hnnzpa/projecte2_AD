package ivha.jpa.project2.DTO;

public class ErrorDTO {
    private int status;
    private String message;

    public ErrorDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
