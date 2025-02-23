package org.verizon.hffv.bdp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.time.LocalDateTime;
import java.util.List;

public class SchemaValidationTransformer implements ResponseDefinitionTransformerV2 {
    public record ValidationError(String error, String message, List<?> details, LocalDateTime localDateTime) {
    }


    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        List<String> errorDetails = SchemaValidationRequestMatcher.getValidationErrors();

        if (errorDetails != null && !errorDetails.isEmpty()) {
            SchemaValidationRequestMatcher.clearValidationErrors();
            ValidationError errorResponse = new ValidationError("INVALID_REQUEST",
                    "Request body does not match schema",
                    errorDetails,
                    LocalDateTime.now());
            return new ResponseDefinitionBuilder()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(400)
                    .withBody(JSONUtil.toJson(errorResponse))
                    .build();
        }
        return serveEvent.getResponseDefinition();
    }

    @Override
    public String getName() {
        return "schema-validation-transformer";
    }
}
