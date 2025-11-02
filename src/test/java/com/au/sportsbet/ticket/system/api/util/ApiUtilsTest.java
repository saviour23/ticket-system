package com.au.sportsbet.ticket.system.api.util;

import com.au.sportsbet.ticket.system.api.constants.ApiConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ApiUtilsTest {

    @Test
    public void getCorrelationIdTest_withNoDefault() {

        String correlationId = ApiUtils.getCorrelationId();
        Assertions.assertNotNull(correlationId);

        String correlationId1 = MDC.get(ApiConstant.CORRELATION_ID);
        Assertions.assertEquals(correlationId, correlationId1);

    }


    @Test
    public void getCorrelationIdTest_withEmptyDefault() {

        MDC.put(ApiConstant.CORRELATION_ID, "");
        String correlationId = ApiUtils.getCorrelationId();
        Assertions.assertNotNull(correlationId);

        String correlationId1 = MDC.get(ApiConstant.CORRELATION_ID);
        Assertions.assertEquals(correlationId, correlationId1);

    }


    @Test
    public void getCorrelationIdTest_withCorrelationPresent() {

        String correlationId1 = UUID.randomUUID().toString();
        MDC.put(ApiConstant.CORRELATION_ID, correlationId1);
        String correlationId2 = ApiUtils.getCorrelationId();
        Assertions.assertEquals(correlationId1, correlationId2);

    }

}
