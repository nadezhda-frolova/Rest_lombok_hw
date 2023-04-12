package tests;

import lombok.Data;
import model.createusertest.CreateUserBodyModel;
import model.createusertest.CreateUserResponseModel;
import model.usersnameupdate.UsersNameUpdateModel;
import model.usersnameupdate.UsersNameUpdateResponseModel;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.CreateUserSpec.createUserSpecRequest;
import static specs.CreateUserSpec.createUserSpecResponse;
import static specs.GetUsersSpec.getUsersRequestSpec;
import static specs.GetUsersSpec.getUsersResponseSpec;
import static specs.LoginSpecs.loginRequestSpec;
import static specs.LoginSpecs.loginResponseSpec;
import static specs.SingleUserNotFoundSpec.singleUserNotFoundRequestSpec;
import static specs.SingleUserNotFoundSpec.singleUserNotFoundResponseSpec;
import static specs.UsersNameUpdateSpec.usersNameUpdateRequestSpec;
import static specs.UsersNameUpdateSpec.usersNameUpdateResponseSpec;
import static specs.BadRequestCreateUserSpec.badRequestCreateUserSpec;
import static specs.BadRequestCreateUserSpec.badRequestResponseSpec;

@Data
public class ReqresInTests {
    @Test
    @DisplayName("GroovyTest")
    void checkColorNameWithGroovyTest() {

        step("Check color's name more than 2002 year", () -> {
            given(loginRequestSpec)
                    .when()
                    .get("/unknown")
                    .then()
                    .spec(loginResponseSpec)
                    .body("data.findAll{it.year>2002}.name.flatten()",
                            CoreMatchers.hasItems("aqua sky", "tigerlily", "blue turquoise"));
        });
    }

    @Test
    @DisplayName("Successful user creation")
    void createUserTest() {
        CreateUserBodyModel createUserBody = new CreateUserBodyModel();
        createUserBody.setName("morpheus");
        createUserBody.setJob("leader");


        CreateUserResponseModel createUserResponse =
                step("set User's data", () -> {
                    return given(createUserSpecRequest)
                            .body(createUserBody)
                            .when()
                            .post("/users")
                            .then()
                            .spec(createUserSpecResponse)
                            .extract().as(CreateUserResponseModel.class);
                });

        step("Verify create User response", () -> {
            assertThat(createUserResponse.getName()).isEqualTo("morpheus");
            assertThat(createUserResponse.getJob()).isEqualTo("leader");
            assertThat(createUserResponse.getId()).isNotNull();
            assertThat(createUserResponse.getCreatedAt()).isNotNull();
        });
    }

    @Test
    @DisplayName("User's name update")
    void usersNameUpdate() {
        UsersNameUpdateModel usersNameBody = new UsersNameUpdateModel();
        usersNameBody.setName("john");
        usersNameBody.setJob("leader");

        UsersNameUpdateResponseModel usersNameUpdateResponse =
                step("update User's data", () -> {
                    return given(usersNameUpdateRequestSpec)
                            .body(usersNameBody)
                            .when()
                            .put("/users/2")
                            .then()
                            .spec(usersNameUpdateResponseSpec)
                            .extract().as(UsersNameUpdateResponseModel.class);
                });
        step("Verify update User response", () -> {
            assertThat(usersNameUpdateResponse.getName()).isEqualTo("john");
            assertThat(usersNameUpdateResponse.getJob()).isEqualTo("leader");
            assertThat(usersNameUpdateResponse.getUpdatedAt()).isNotNull();
        });
    }

    @Test
    @DisplayName("400 status code when name is omitted")
    void badRequestCreateUserTest() {
        String data = "{ \"name\": , \"job\": \"leader\" }";

        given(badRequestCreateUserSpec)
                .body(data)
                .when()
                .post("/users")
                .then()
                .spec(badRequestResponseSpec);
    }

    @Test
    @DisplayName("Total count of users")
    void getUsers() {
        given(getUsersRequestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(getUsersResponseSpec);
    }


    @Test
    @DisplayName("Single user not found")
    void singleUserNotFoundTest() {
        given(singleUserNotFoundRequestSpec)
                .when()
                .get("/users/23")
                .then()
                .spec(singleUserNotFoundResponseSpec);
    }
}
