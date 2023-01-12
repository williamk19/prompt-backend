package com.restful.promptbackend.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductRepository productRepository;

  @GetMapping("/list")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_SELLER", "ROLE_CLIENT"})
  public List<Product> listAll() {
    return productRepository.findAll();
  }

  @GetMapping("/{id}")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_SELLER", "ROLE_CLIENT"})
  public ResponseEntity<?> getProductById(@PathVariable Integer id) {
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      return ResponseEntity.ok().body(product);
    }
    return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
  }

  @PostMapping("/create")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_SELLER"})
  public ResponseEntity<?> createProduct(@RequestBody @Valid Product p) {
    Product savedProduct = productRepository.save(p);
    URI newProductUri = URI.create("/product/" + savedProduct.getId());

    Map<String, Object> map = new HashMap<>();
    map.put("id", savedProduct.getId());
    map.put("productName", savedProduct.getProductName());
    map.put("price", savedProduct.getPrice());
    map.put("productUrl", newProductUri);

    return ResponseEntity.created(newProductUri).body(map);
  }
}