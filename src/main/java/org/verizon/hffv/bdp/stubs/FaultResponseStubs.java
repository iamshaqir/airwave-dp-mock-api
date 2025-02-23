package org.verizon.hffv.bdp.stubs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.verizon.hffv.bdp.temp.FileUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class FaultResponseStubs {

    private static final Logger log = LoggerFactory.getLogger(WithJsonDataFromFile.class);
    private static final String STUBS_DIR = "src/main/resources/stubs";

    public FaultResponseStubs errorStub() {
        String errorTemplate = FileUtils.readFile(STUBS_DIR + "/error-templates/generic-error.hbs");
        stubFor(any(anyUrl())
                .atPriority(10)
                .willReturn(
                        aResponse()
                                .withStatus(400)
                                .withBody(errorTemplate)
                                .withTransformers("response-template")
                )
        );
        log.info("created stub endpoint for error response...");
        return this;
    }

    public FaultResponseStubs schemaErrorStub() {
        String schemaErrorTemplate = FileUtils.readFile(STUBS_DIR + "/error-templates/generic-error-schema.hbs");

        stubFor(post(urlEqualTo("/api/tickets"))
                .atPriority(9)
                .willReturn(
                        aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody(schemaErrorTemplate)
                                .withTransformers("response-template")
                )
        );
        return this;
    }


}
