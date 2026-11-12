package market.rest;

import market.entity.Category;
import market.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryRest {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> save(@RequestBody Category c){
        Category categoryResponse = categoryService.saveCategory(c);
        try {
            return ResponseEntity.created(new URI("/category/"+categoryResponse.getId())).body(categoryResponse);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable long id, @RequestBody Category updatedCategory) {
        Optional<Category> existing = categoryService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        existing.get().setName(updatedCategory.getName());
        return ResponseEntity.ok(categoryService.saveCategory(existing.get()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteById(id);
        Optional<Category> c = categoryService.findById(id);
        if(c.isEmpty()){
            return ResponseEntity.ok().body("Categoria eliminada exitosamente");
        }else{
            return ResponseEntity.internalServerError().body("Problemas al eliminar");
        }
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAllCategory(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> findCategoryById(@PathVariable long id){
        Optional<Category> c = categoryService.findById(id);
        if(c.isPresent()){
            return ResponseEntity.ok(c.get());
        }
        return ResponseEntity.notFound().build();
    }

}
