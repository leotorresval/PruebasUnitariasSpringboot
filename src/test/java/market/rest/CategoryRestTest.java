package market.rest;

import market.entity.Category;
import market.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRestTest {

    /** Stub: el borrado no elimina nada, así la categoría sigue existiendo. */
    static class FailingDeleteCategoryService extends CategoryService {
        Long deletedId;

        @Override
        public void deleteById(Long id) {
            deletedId = id;
        }

        @Override
        public Optional<Category> findById(Long id) {
            Category c = new Category();
            c.setId(id);
            c.setName("Bebidas");
            return Optional.of(c);
        }
    }

    @Test
    void deleteCategoryByIdFails() {
        FailingDeleteCategoryService service = new FailingDeleteCategoryService();
        CategoryRest categoryRest = new CategoryRest();
        categoryRest.categoryService = service;

        ResponseEntity<String> response = categoryRest.deleteCategoryById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Problemas al eliminar", response.getBody());
        assertEquals(1L, service.deletedId);
    }
}
