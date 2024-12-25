package com.tpi.forexapi;

import com.tpi.forexapi.cron.ForexJob;
import com.tpi.forexapi.model.ForexRate;
import com.tpi.forexapi.service.ForexService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class JobTest {

    @MockitoBean
    private ForexService forexService;

    @MockitoBean
    private ForexJob forexJob;

    @Test
    public void testForexJob() {
        List<ForexRate> forexRateList = new ArrayList<>();
        forexRateList.add(
                ForexRate.builder().date(LocalDateTime.of(2999, 12, 31, 23, 59)).usd2ntd(BigDecimal.ONE).build());
        forexRateList.add(
                ForexRate.builder().date(LocalDateTime.of(2999, 12, 30, 23, 59)).usd2ntd(BigDecimal.TEN).build());

        List<ForexRate> mockData = doReturn(forexRateList)
                .when(forexService)
                .fetchForexData();
        doNothing()
                .when(forexService)
                .updateForexDB(mockData);
        forexJob.fetchForexJob();
        verify(forexJob, times(1)).fetchForexJob();
    }

}
