package com.example.mysklad1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private List<Product> products = new ArrayList<>();



    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Optional<Product> product = products.stream().filter(p -> p.getName().equals(name)).findFirst();

        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Product addProduct(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) Integer price,
                              @RequestParam(required = false) Boolean inStock) {
        if (!getProductByName(name).equals(ResponseEntity.notFound().build()) || name.isBlank() || price < 0 || description.length() > 4096 || name.length() > 255){
            return null;
        }
        Product product = new Product(name, description, price, inStock);
        products.add(product);
        return product;
    }

    @PutMapping("/{name}")
    public ResponseEntity<Product> updateProduct(@PathVariable String name, @RequestBody @Valid Product product) {
        if (getProductByName(name).equals(ResponseEntity.notFound().build()) || product.getPrice() < 0 || product.getDescription().length() > 4096 || product.getName().length() > 255){
            return null;
        }
        for (Product p : products) {
            if (p.getName().equals(name)) {
                p.setDescription(product.getDescription());
                p.setPrice(product.getPrice());
                p.setInStock(product.isInStock());
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String name) {
        products.removeIf(p -> p.getName().equals(name));
        return ResponseEntity.noContent().build();
    }
}
