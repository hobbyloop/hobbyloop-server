package com.example.ticketservice.common.swagger;

import com.example.ticketservice.common.exception.ExceptionEnum;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class ApiExceptionResponseCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiExceptionResponse annotation = handlerMethod.getMethodAnnotation(ApiExceptionResponse.class);
        if (annotation != null) {
            for (ExceptionEnum exceptionEnum : annotation.value()) {
                Schema<?> schema = new Schema<>()
                        .type("object")
                        .addProperties("errorCode", new StringSchema().example(exceptionEnum.getCode()))
                        .addProperties("errorMessage", new StringSchema().example(exceptionEnum.getMessage()));

                ApiResponse apiResponse = new ApiResponse()
                        .description(exceptionEnum.getMessage())
                        .content(new Content().addMediaType("application/json",
                                new MediaType().schema(schema)));

                operation.getResponses().addApiResponse(String.valueOf(exceptionEnum.getStatus().value()), apiResponse);
            }
        }
        return operation;
    }
}
