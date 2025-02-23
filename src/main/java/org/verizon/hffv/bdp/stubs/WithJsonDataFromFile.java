package org.verizon.hffv.bdp.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.verizon.hffv.bdp.temp.FileUtils;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WithJsonDataFromFile {

    private static final Logger log = LoggerFactory.getLogger(WithJsonDataFromFile.class);
    private static final String STUBS_DIR = "src/main/resources/stubs";
    private static final String SCHEMAS = "src/main/resources/schemas";

    public WithJsonDataFromFile createStub(WireMockServer wireMockServer) {
        String jsonSchema = FileUtils.readFile(SCHEMAS + "/create-ticket-schema.json");
        String responseTemplate = FileUtils.readFile(STUBS_DIR + "/create-ticket/response.hbs");
        stubFor(post(urlEqualTo("/api/tickets"))
                .atPriority(1)
                .andMatching("schema-validation-matcher", Parameters.one("schema", jsonSchema))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseTemplate)
                        .withTransformers("response-template", "schema-validation-transformer")
                )
        );
        log.info("created stub endpoint for ticket creation...");
        return this;
    }

    public WithJsonDataFromFile schemaErrorStub() {
        String schemaErrorTemplate = FileUtils.readFile(STUBS_DIR + "/error-templates/simple-error-template.hbs");

        stubFor(post(urlEqualTo("/api/tickets"))
                .atPriority(10)
                .willReturn(
                        aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody(schemaErrorTemplate)
                                .withTransformers("response-template")
                                .withTransformerParameter("schemaValidationErrorTemplate",
                                        Map.of(
                                                "status", 400,
                                                "jsonBody", Map.of(
                                                        "error", "Schema validation failed",
                                                        "invalidProperties", "{{jsonPath request.body '$..[@]' | invalidProps}}"
                                                )
                                        )
                                )

                )
        );
        return this;
    }
}
