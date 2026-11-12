package market.rest;

import market.entity.Product;
import market.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductRestTest {

    /** Stub: el borrado no elimina nada, así el producto sigue existiendo. */
    static class FailingDeleteProductService extends ProductService {
        Long deletedId;

        @Override
        public void deleteById(Long id) {
            deletedId = id;
        }

        @Override
        public Optional<Product> findById(Long id) {
            Product p = new Product();
            p.setId(id);
            p.setName("Leche");
            return Optional.of(p);
        }
    }

    @Test
    void deleteProductByIdFails() {
        FailingDeleteProductService service = new FailingDeleteProductService();
        ProductRest productRest = new ProductRest();
        productRest.productService = service;

        ResponseEntity<String> response = productRest.deleteProductById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Problemas al eliminar", response.getBody());
        assertEquals(1L, service.deletedId);
    }
}
