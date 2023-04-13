package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static org.hamcrest.Matchers.equalTo;

public class GetUsersSpec {
    public static RequestSpecification getUsersRequestSpec = with()
            .filter(withCustomTemplates())
            .log().uri()
            .log().headers()
            .baseUri("https://reqres.in")
            .basePath("/api");

    public static ResponseSpecification getUsersResponseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200)
            .expectBody("total", equalTo(12))
            .build();

}
