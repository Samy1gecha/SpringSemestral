package com.example.SpringSemestral;
import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Repository.ProductRepository;
import com.example.SpringSemestral.Service.ProductService;
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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc

    public class ProductTest{
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProductRepository productRepository;

    @MockitoBean
    ProductService productService;

    @Test
    void findAllTest() {
        List<Product> products = productRepository.findAll();
        assertNotNull(products);
        assertEquals(1,products.size());
    }
    @Test
    void chekNameProduct() {
            Product products = productRepository.findById(1).get();
            assertNotNull(products);
            assertEquals("Perfume Lara Clein", products.getNombre());
    }
    @Test
    void getAllProductsControllerTest()    {
        //when
        Mockito.when(productService.getAllProducts()).thenReturn(productRepository.findAll());
        try {
            //Intentar ejecutar un codigo
            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("ListaCompleta"));
        } catch (Exception e) {
            //Capturar la excepcion
            System.out.println("Error: " + e.getMessage());
            fail();
        }
    }
}
