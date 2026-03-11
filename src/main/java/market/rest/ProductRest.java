package market.rest;

import market.entity.Product;
import market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductRest {

    @Autowired
    ProductService productService;

    public ResponseEntity<Product> save(@RequestBody Product p){
        Product productResponse = productService.saveProduct(p);
        try {
            return ResponseEntity.created(new URI("/product/"+productResponse.getId())).body(productResponse);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestBody Product updatedProduct) {
        Optional<Product> existing = productService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }else {
            existing.get().setName(updatedProduct.getName());
            existing.get().setDescription(updatedProduct.getDescription());
            existing.get().setPrice(updatedProduct.getPrice());
            existing.get().setStock(updatedProduct.getStock());
            return ResponseEntity.ok(productService.saveProduct(existing.get()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id){
        productService.deleteById(id);
        Optional<Product> p = productService.findById(id);
        if(p.isEmpty()){
            return ResponseEntity.ok().body("Producto eliminado exitosamente");
        }else{
            return ResponseEntity.internalServerError().body("Problemas al eliminar");
        }

    }


    @GetMapping
    public ResponseEntity<List<Product>> findAllProduct(){

        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findProductById(@PathVariable long id){
        Optional<Product> p = productService.findById(id);
        if(p.isPresent()){
            return ResponseEntity.ok(p.get());
        }
        return ResponseEntity.notFound().build();
    }

}
