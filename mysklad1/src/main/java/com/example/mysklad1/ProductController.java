package com.example.mysklad1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    ConnectionUtility connectionUtility= new ConnectionUtility();
    Connection connection;


    @GetMapping
    public ResultSet getAllProducts() throws SQLException {
        connection = connectionUtility.getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM romashka_table;");
        connection.close();
        resultSet.close();
        return resultSet;
    }

    @GetMapping("/{name}")
    public Product getProductByName(@PathVariable String name) throws SQLException {
        connection = connectionUtility.getConnection();
        ResultSet productSet = connection.createStatement().executeQuery("SELECT * FROM romashka_table WHERE name = '" + name + "';");
        connection.close();
        return new Product(name, productSet.getString("description"), productSet.getInt("price"), productSet.getBoolean("in_stock"));
    }

    @PostMapping("/add")
    public Product addProduct(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) Integer price,
                              @RequestParam(required = false) Boolean inStock) throws SQLException {
        connection = connectionUtility.getConnection();
        ResultSet productSet = connection.createStatement().executeQuery("SELECT * FROM romashka_table WHERE name = '" + name + "';");
        productSet.next();
        if (productSet.isAfterLast() || name.isBlank() || price < 0 || description.length() > 4096 || name.length() > 255){
            return null;
        }
        connection.createStatement().executeQuery("INSERT IGNORE INTO romashka_table (name, description, price, inStock) VALUES " + name + ", " + description + ", " + price + ", " + inStock + ";");
        connection.commit();
        connection.close();
        return new Product(name, description, price, inStock);
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> updateProduct(@PathVariable String name, @RequestBody @Valid Product product) throws SQLException {
        if (getProductByName(name) == null || product.getPrice() < 0 || product.getDescription().length() > 4096 || product.getName().length() > 255){
            return null;
        }
        connection = connectionUtility.getConnection();
        ResultSet productSet = connection.createStatement().executeQuery("SELECT * FROM romashka_table WHERE name = '" + name + "';");
        productSet.next();
        if (productSet.isFirst()){
            connection.createStatement().executeQuery("UPDATE romashka_table SET price = " + product.getPrice() + " WHERE name = '" + name + "';");
            connection.createStatement().executeQuery("UPDATE romashka_table SET in_stock = " + product.isInStock() + " WHERE name = '" + name + "';");
            if (!product.getDescription().isBlank()) {
                connection.createStatement().executeQuery("UPDATE romashka_table SET description = " + product.getDescription() + " WHERE name = '" + name + "';");
            }
            return (ResponseEntity<?>) ResponseEntity.ok();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String name) throws SQLException {
        connection = connectionUtility.getConnection();
        connection.createStatement().executeQuery("DELETE FROM romashka_table WHERE name = '" + name + "';");
        return ResponseEntity.noContent().build();
    }
}
