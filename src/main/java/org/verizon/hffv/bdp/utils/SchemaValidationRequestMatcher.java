package org.verizon.hffv.bdp.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcher;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import javax.xml.validation.Schema;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaValidationRequestMatcher extends RequestMatcherExtension {

    private static final ThreadLocal<List<String>> validationErrors = new ThreadLocal<>();

    @Override
    public String getName() {
        return "schema-validation-matcher";
    }

    @Override
    public MatchResult match(Request request, Parameters parameters) {

        String schema = parameters.getString("schema");
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonSchema jsonSchema = jsonSchemaFactory.getSchema(schema);


        String requestBody = request.getBodyAsString();
        JsonNode requestNode = Json.read(requestBody, JsonNode.class);

        Set<ValidationMessage> validationMessages = jsonSchema.validate(requestNode);
        if (validationMessages.isEmpty()) return MatchResult.of(true);
        List<String> errorDetails = validationMessages.stream()
                .map(ValidationMessage::getMessage)
                .toList();
        validationErrors.set(errorDetails);
        return MatchResult.of(false);
    }

    public static List<String> getValidationErrors() {
        return validationErrors.get();
    }

    public static void clearValidationErrors() {
        validationErrors.remove();
    }
}
