package market.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import market.entity.Product;
import market.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ProductRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired //CAMBIO POR MOCK
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void setUp() {
        Product p1 = createProduct();
        Product p2 = createProduct();
        //AÑADIRLOS A LA BASE DE DATOS
        product = productService.saveProduct(p1);
        productService.saveProduct(p2);
    }


    @AfterEach
    void endTest(){
        List<Product> lista = productService.findAll();
        for(Product p: lista){
            productService.deleteById(p.getId());
        }
    }

    private Product createProduct(){
        Product p = new Product();
        p.setName("Test");
        p.setPrice(12);
        return p;
    }

    @Test
    void save() throws Exception {
        Product p = createProduct();
        p.setName("Prueba");
        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Prueba"));
    }

    @Test
    void updateProduct() throws Exception {
        Product p = createProduct();
        p.setName("Prueba");
        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Prueba"));
    }

    @Test
    void deleteProductById() throws Exception{
        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findAllProduct() throws Exception{
        mockMvc.perform(get("/product"))
                .andExpect(status().isOk());
    }

    @Test
    void findProductById() throws Exception {
        mockMvc.perform(get("/product/"+product.getId()))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("Test"));
    }
}