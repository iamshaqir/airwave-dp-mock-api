package org.verizon.hffv.bdp.temp;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import java.util.List;

public class TelecomStubServer {
    private static final String STUBS_DIR = "src/main/resources/stubs";
    private static final String ERROR_TEMPLATES_DIR = "src/main/resources/error-templates";

    public static void main(String[] args) {
        WireMockServer wireMockServer = new WireMockServer(
                WireMockConfiguration.options()
                        .port(8080)
                        .globalTemplating(true)
        );

        wireMockServer.start();
        loadStubs(wireMockServer);
        loadErrorStubs(wireMockServer);

        Runtime.getRuntime().addShutdownHook(new Thread(wireMockServer::stop));
    }

    private static void loadStubs(WireMockServer wireMockServer) {
        List<StubConfig> stubs = List.of(
                new StubConfig("create-ticket", "POST", "/api/tickets", 201),
                new StubConfig("get-ticket", "GET", "/api/tickets/[0-9a-f-]+", 200),
                new StubConfig("list-tickets", "GET", "/api/tickets", 200)
        );

        stubs.forEach(stub -> {
            String requestBody = FileUtils.readFile(STUBS_DIR + "/" + stub.getName() + "/request.json");
            String responseTemplate = FileUtils.readFile(STUBS_DIR + "/" + stub.getName() + "/response.hbs");

            wireMockServer.stubFor(
                    WireMock.request(stub.getMethod(), WireMock.urlPathMatching(stub.getUrl()))
                            .withRequestBody(stub.getMethod().equals("POST") ?
                                    WireMock.equalToJson(requestBody) : WireMock.absent())
                            .willReturn(
                                    WireMock.aResponse()
                                            .withStatus(stub.getStatusCode())
                                            .withHeader("Content-Type", "application/json")
                                            .withBody(responseTemplate)
                                            .withTransformers("response-template")
                            ));
        });
    }

    private static void loadErrorStubs(WireMockServer wireMockServer) {
        String errorTemplate = FileUtils.readFile(ERROR_TEMPLATES_DIR + "/generic-error.hbs");

        wireMockServer.stubFor(
                WireMock.any(WireMock.anyUrl())
                        .atPriority(10)
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(400)
                                        .withBody(errorTemplate)
                                        .withTransformers("response-template")
                        ));
    }
}