package com.example.SpringSemestral;
import com.example.SpringSemestral.Model.Cupon;
import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Repository.CuponRepository;
import com.example.SpringSemestral.Service.CuponService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc


public class CuponTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    CuponRepository cuponRepository;

    @MockitoBean
    CuponService cuponService;
    @Test
    void findAllTest() {
        List<Cupon> cupons = cuponRepository.findAll();
        assertNotNull(cupons);
        assertEquals(1,cupons.size());
    }
    @Test
    void chekNameCupon() {
        Cupon cupons = cuponRepository.findById(1).get();
        assertNotNull(cupons);
        assertEquals("PRIMAVERA01", cupons.getCodigo());
    }
    @Test
    void getAllCuponsControllerTest()    {
        //when
        Mockito.when(cuponService.getClass().getSimpleName()).thenReturn("CuponService");
        try {
            //Intentar ejecutar un codigo
            mockMvc.perform(get("/cupons"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("ListaCompleta"));
        } catch (Exception e) {
            //Capturar la excepcion
            System.out.println("Error: " + e.getMessage());
            fail();
        }
    }
}
