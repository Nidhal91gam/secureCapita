package io.techno.securecapita.dto;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String adress;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private Boolean notLocked;
    private Boolean usingMfa;

}
