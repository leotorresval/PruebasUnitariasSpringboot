package market.service;

import market.entity.Product;
import market.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product createProduct(Long id, String name) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setDescription("Descripcion");
        p.setPrice(10.5);
        p.setStock(3);
        return p;
    }

    @Test
    void saveProduct() {
        Product p = createProduct(null, "Leche");
        Product saved = createProduct(1L, "Leche");
        when(productRepository.save(p)).thenReturn(saved);

        Product result = productService.saveProduct(p);

        assertEquals(1L, result.getId());
        assertEquals("Leche", result.getName());
        assertEquals("Descripcion", result.getDescription());
        assertEquals(10.5, result.getPrice());
        assertEquals(3, result.getStock());
        verify(productRepository).save(p);
    }

    @Test
    void findAll() {
        when(productRepository.findAll()).thenReturn(List.of(
                createProduct(1L, "Leche"),
                createProduct(2L, "Pan")));

        assertEquals(2, productService.findAll().size());
        verify(productRepository).findAll();
    }

    @Test
    void findByIdExisting() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(createProduct(1L, "Leche")));

        Optional<Product> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Leche", result.get().getName());
    }

    @Test
    void findByIdNotExisting() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(productService.findById(99L).isEmpty());
    }

    @Test
    void deleteById() {
        productService.deleteById(1L);

        verify(productRepository).deleteById(1L);
    }
}
