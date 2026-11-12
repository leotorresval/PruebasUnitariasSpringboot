package market.service;

import market.entity.Category;
import market.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category createCategory(Long id, String name) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        return c;
    }

    @Test
    void saveCategory() {
        Category c = createCategory(null, "Bebidas");
        Category saved = createCategory(1L, "Bebidas");
        when(categoryRepository.save(c)).thenReturn(saved);

        Category result = categoryService.saveCategory(c);

        assertEquals(1L, result.getId());
        assertEquals("Bebidas", result.getName());
        verify(categoryRepository).save(c);
    }

    @Test
    void findAll() {
        when(categoryRepository.findAll()).thenReturn(List.of(
                createCategory(1L, "Bebidas"),
                createCategory(2L, "Lacteos")));

        List<Category> result = categoryService.findAll();

        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void findByIdExisting() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(createCategory(1L, "Bebidas")));

        Optional<Category> result = categoryService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Bebidas", result.get().getName());
    }

    @Test
    void findByIdNotExisting() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(categoryService.findById(99L).isEmpty());
    }

    @Test
    void deleteById() {
        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
    }
}
