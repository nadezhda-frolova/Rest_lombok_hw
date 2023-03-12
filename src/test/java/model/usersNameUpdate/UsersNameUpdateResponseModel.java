package model.usersNameUpdate;
import lombok.Data;

@Data
public class UsersNameUpdateResponseModel {
    //.body("name", is("john"));
    String name, job, updatedAt;
}
