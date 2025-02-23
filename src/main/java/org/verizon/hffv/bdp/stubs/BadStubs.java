package org.verizon.hffv.bdp.stubs;

import com.github.tomakehurst.wiremock.http.Fault;
import org.json.JSONObject;
import org.verizon.hffv.bdp.utils.JSONUtil;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BadStubs {

    public BadStubs badStubsMappings() {
        stubFor(get(urlEqualTo("/fault/1")).willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));
        stubFor(get(urlEqualTo("/fault/2")).willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        stubFor(get(urlEqualTo("/fault/3")).willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
        stubFor(get(urlEqualTo("/fault/4")).willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));
        return this;
    }

    public BadStubs customFaultStub() {
        JSONObject faultRequest = JSONUtil.parseFile("fault-stub-req");
        stubFor(post(urlEqualTo("/api/register")).atPriority(3)
                .withRequestBody(equalToJson(faultRequest.toString())).willReturn(aResponse()
                        .withBodyFile("reg-success-response")
                        .withHeader("Content-Type", "application/json")));

        stubFor(post(urlEqualTo("/api/register")).willReturn(aResponse()
                .withBodyFile("fault-response").withStatus(400)
                .withHeader("Content-Type", "application/json")));
        return this;
    }
}
