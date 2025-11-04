package com.au.sb.ticket.system.api.util;

import com.au.sb.ticket.system.api.constants.ApiConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

/**
 * Utility class for any static data response.
 */
@Slf4j
public final class ApiUtils {
    public static String getCorrelationId() {

        String correlationId = MDC.get(ApiConstant.CORRELATION_ID);

        if (ObjectUtils.isEmpty(correlationId)) {

            correlationId = UUID.randomUUID().toString();
            MDC.put(ApiConstant.CORRELATION_ID, correlationId);
        }

        return correlationId;

    }
}
