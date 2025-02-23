package org.verizon.hffv.bdp.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WithJsonData {

    /**
     * Refer website <a href="https://goessner.net/articles/JsonPath/">JSON Path Matching</a>
     * <p>
     * .+ in regex means one or more of any character, ensures that the values for "id" and "name" are non-empty
     * <p/>
     *
     * @return Current Instance
     */
    public WithJsonData withJsonDataAsBody() {

        // Stub for valid requests
        stubFor(
                post(urlEqualTo("/api/orders"))
                        .withRequestBody(matchingJsonPath("$.products[?(@.id)]"))
                        .withRequestBody(matchingJsonPath("$.customer.name"))
                        .willReturn(
                                aResponse()
                                        .withStatus(201)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("""
                                                    {
                                                        "orderId": "{{randomValue type='UUID'}}",
                                                        "timestamp": "{{now format='yyyy-MM-dd\\'T\\'HH:mm:ss'}}Z",
                                                        "customer": {
                                                            "name": "{{jsonPath request.body '$.customer.name'}}",
                                                            "rewardPoints": "{{randomValue min=100 max=1000}}"
                                                        },
                                                        "items": [
                                                            {{#each (jsonPath request.body '$.products') as |product|}}
                                                    			{
                                                    				"itemId": "{{randomValue type='UUID'}}",
                                                    				"productId": "{{product.id}}",
                                                    				"price": {{randomValue length=5 type='NUMERIC'}},
                                                    				"discount": "{{randomInt lower=5 upper=20}}"
                                                    			}{{#unless @last}},{{/unless}}
                                                            {{/each}}
                                                        ],
                                                        "metadata": {
                                                            "processedBy": "{{randomValue type='ALPHANUMERIC' length=8}}",
                                                            "priority": "{{#if (contains (jsonPath request.body '$.customer.name') 'VIP')}}HIGH{{else}}NORMAL{{/if}}"
                                                        }
                                                    }
                                                """)
                        )
        );

        // Stub for invalid requests
        stubFor(
                post(urlEqualTo("/api/orders"))
                        .withRequestBody(notMatching("\\{\\s*\"products\"\\s*:\\s*\\[\\s*\\{\\s*\"id\"\\s*:\\s*\".+\"\\s*\\}\\s*(,\\s*\\{\\s*\"id\"\\s*:\\s*\".+\"\\s*\\}\\s*)*\\]\\s*,\\s*\"customer\"\\s*:\\s*\\{\\s*\"name\"\\s*:\\s*\".+\"\\s*\\}\\s*\\}"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withBody("{\"error\":\"Invalid request: Must include products array and customer name\"}")
                        )
        );
        return this;
    }

}
