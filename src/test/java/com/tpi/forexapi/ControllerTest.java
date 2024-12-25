package com.tpi.forexapi;

import com.tpi.forexapi.constant.OkCodes;
import com.tpi.forexapi.dto.ForexSearchResponseDTO;
import com.tpi.forexapi.service.ForexService;
import com.tpi.forexapi.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @MockitoBean
    private ForexService forexService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testForexSearch() throws Exception {
        ForexSearchResponseDTO responseDTO = ForexSearchResponseDTO.builder()
                                                                   .error(ForexSearchResponseDTO.Error.builder()
                                                                                                      .message(
                                                                                                              OkCodes.OK_MSG)
                                                                                                      .code(OkCodes.OK_CODE)
                                                                                                      .build())
                                                                   .currency(
                                                                           List.of(ForexSearchResponseDTO.Currency.builder()
                                                                                                                  .usd("10")
                                                                                                                  .date("20991231")
                                                                                                                  .build()))
                                                                   .build();

        doReturn(responseDTO)
                .when(forexService)
                .search("2024/12/10", "2024/12/22", "usd");

        String requestBody = """
                {
                    "startDate": "2024/12/10",
                    "endDate": "2024/12/22",
                    "currency": "usd"
                }
                """;
        mockMvc.perform(
                       MockMvcRequestBuilders.post("/forex").contentType(MediaType.APPLICATION_JSON).content(requestBody))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().json(JsonUtils.toJsonString(responseDTO)));
    }

}
