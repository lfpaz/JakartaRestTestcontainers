package br.com.lucaspaz.test.integration;

import io.restassured.RestAssured;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.nio.file.Paths;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class AppIT {

    MountableFile warFile = MountableFile.forHostPath(Paths.get("target/TestRest-0.01.war").toAbsolutePath(), 0777);

    @Container
    GenericContainer microContainer = new GenericContainer("payara/micro:6.2023.10-jdk17")
            .withExposedPorts(8080)
            .withCopyFileToContainer(warFile, "/opt/payara/deployments/TestRest-0.01.war")
            .withCommand("--deploy /opt/payara/deployments/TestRest-0.01.war --contextRoot /")
            .waitingFor(Wait.forLogMessage(".* Payara Micro .* ready in .*\\s", 1));

    @Test
    public void checkContainerIsRunning() {
        Assert.assertTrue(microContainer.isRunning());
    }

    @Test
    public void testHello() {

        RestAssured.get(getUrl("app/hello"))
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testNome() {

        JsonObject json = Json.createObjectBuilder().add("name", "João").build();

        RestAssured.given()
                .body(json.toString())
                .post(getUrl("app/to-upper"))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", is("JOÃO"));
    }

    @Test
    public void testErroSemNome() {

        JsonObject json = Json.createObjectBuilder().add("name", "").build();

        RestAssured.given()
                .body(json.toString())
                .post(getUrl("app/to-upper"))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("error", is("Name not found"));
    }

    public String getUrl(String url) {
        return String.format("http://%s:%d/%s", microContainer.getHost(), microContainer.getMappedPort(8080), url);
    }

}
