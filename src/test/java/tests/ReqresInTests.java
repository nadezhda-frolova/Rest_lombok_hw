package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import lombok.Data;
import model.createUserTest.CreateUserBodyModel;
import model.createUserTest.CreateUserResponseModel;
import model.usersNameUpdate.UsersNameUpdateModel;
import model.usersNameUpdate.UsersNameUpdateResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.CreateUserSpec.createUserSpecRequest;
import static specs.CreateUserSpec.createUserSpecResponse;
import static specs.UsersNameUpdateSpec.usersNameUpdateRequestSpec;
import static specs.UsersNameUpdateSpec.usersNameUpdateResponseSpec;

@Data
public class ReqresInTests {

    @Test
    @DisplayName("Successful user creation")
    void createUserTest() {
        //String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }"; было
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
        // String request_body = "{ \"name\": \"john\", \"job\": \"leader\" }";
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

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    @DisplayName("Total count of users")
    void getUsers() {
        given()
                .filter(withCustomTemplates())
                .log().uri()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12));

    }


    @Test
    @DisplayName("Single user not found")
    void singleUserNotFoundTest() {
        given()
                .filter(withCustomTemplates())
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }
}
