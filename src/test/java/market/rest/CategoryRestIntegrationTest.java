package market.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import market.entity.Category;
import market.service.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CategoryRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category;

    @BeforeEach
    void setUp() {
        category = categoryService.saveCategory(createCategory("Test"));
        categoryService.saveCategory(createCategory("Test2"));
    }

    @AfterEach
    void endTest() {
        List<Category> lista = categoryService.findAll();
        for (Category c : lista) {
            categoryService.deleteById(c.getId());
        }
    }

    private Category createCategory(String name) {
        Category c = new Category();
        c.setName(name);
        return c;
    }

    @Test
    void save() throws Exception {
        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategory("Prueba"))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Prueba"));
    }

    @Test
    void updateCategory() throws Exception {
        mockMvc.perform(put("/category/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategory("Prueba"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value("Prueba"));

        assertEquals("Prueba", categoryService.findById(category.getId()).orElseThrow().getName());
    }

    @Test
    void updateCategoryNotFound() throws Exception {
        mockMvc.perform(put("/category/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategory("Prueba"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategoryById() throws Exception {
        mockMvc.perform(delete("/category/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Categoria eliminada exitosamente"));

        assertTrue(categoryService.findById(category.getId()).isEmpty());
    }

    @Test
    void findAllCategory() throws Exception {
        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findCategoryById() throws Exception {
        mockMvc.perform(get("/category/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void findCategoryByIdNotFound() throws Exception {
        mockMvc.perform(get("/category/999999"))
                .andExpect(status().isNotFound());
    }
}
