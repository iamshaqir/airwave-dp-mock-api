package org.verizon.hffv.bdp;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.verizon.hffv.bdp.stubs.FaultResponseStubs;
import org.verizon.hffv.bdp.stubs.WithJsonDataAsFile;
import org.verizon.hffv.bdp.stubs.WithJsonDataFromFile;
import org.verizon.hffv.bdp.utils.SchemaValidationRequestMatcher;
import org.verizon.hffv.bdp.utils.SchemaValidationTransformer;


import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;

public class WireMockServerAWDP {

    private static final Logger logger = LoggerFactory.getLogger(WireMockServerAWDP.class);
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;
    private static final String RESOURCES_DIR = "src/main/resources";

    public static void main(String[] args) {
        logger.info("creating wiremock server on port:{}", PORT);
        WireMockServer wireMockServer = new WireMockServer(
                options()
                        .port(PORT)
                        .usingFilesUnderDirectory(RESOURCES_DIR)
                        .extensions(new SchemaValidationRequestMatcher(), new SchemaValidationTransformer())
                        .globalTemplating(true)
                        .notifier(new ConsoleNotifier(true))
        );
        logger.info("starting wiremock server on port:{}", PORT);
        wireMockServer.start();
        logger.info("wiremock server up & running...");
        configureFor(HOSTNAME, PORT);
        WithJsonDataFromFile stubWithJsonDataFromFile = new WithJsonDataFromFile().createStub(wireMockServer).schemaErrorStub();
    }
}
