package org.verizon.hffv.bdp.stubs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.verizon.hffv.bdp.temp.FileUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WithJsonDataAsFile {

    private static final Logger log = LoggerFactory.getLogger(WithJsonDataAsFile.class);
    private static final String STUBS_DIR = "stubs";
    private static final String ERROR_TEMPLATES_DIR = "src/main/resources/error-templates";
    private static final String SCHEMAS = "src/main/resources/schemas";

    public WithJsonDataAsFile createStub() {
        String jsonSchema = FileUtils.readFile(SCHEMAS + "/create-ticket-schema.json");
        stubFor(post(urlEqualTo("/api/tickets"))
                .withRequestBody(matchingJsonSchema(jsonSchema))
                .willReturn(
                        aResponse()
                                .withStatus(201)
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile(STUBS_DIR + "/create-ticket/response.hbs")
                                .withTransformers("response-template")
                )
        );
        log.info("created stub endpoint for ticket creation and response as Json file..");
        return this;
    }
}
