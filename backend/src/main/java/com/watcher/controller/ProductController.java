package com.watcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watcher.custom_exception.ResourceNotFoundException;
import com.watcher.dto.ProductDto;
import com.watcher.entity.Category;
import com.watcher.entity.Product;
import com.watcher.service.ProductService;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")

public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/admin/add")
	public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto){
		
		return ResponseEntity.status(HttpStatus.OK).body(productService.addProduct(productDto));
	}
	
	@GetMapping("/public/products")
	public ResponseEntity<?> getAllProduct(){
		
		return ResponseEntity.ok(productService.getAllProduct());
	}
	
	@GetMapping("/public/{productId}")
	public ResponseEntity<?> getProductByProductId(@PathVariable int productId){
		
		return ResponseEntity.ok(productService.getProductById(productId));
	}
	
	@GetMapping("/public/category/{category}")
	public ResponseEntity<?> getProductByCategory(@PathVariable Category category){
		
		return ResponseEntity.ok(productService.getProductsByCategory(category));
	}
	
	@GetMapping("/public/color/{color}")
	public ResponseEntity<?> getProductByColor(@PathVariable String color){
		
		return ResponseEntity.ok(productService.getProductsByColor(color));
	}
	
	@PutMapping("/admin/product/{productId}")
	public ResponseEntity<?> updateProductById(@PathVariable int productId, @RequestBody ProductDto productDto){
		
		return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productId, productDto));
		
	}
	@DeleteMapping("/admin/product/{productId}")
	public ResponseEntity<?> deleteProductByProductId(@PathVariable int productId){
		
		return ResponseEntity.ok(productService.deleteProductById(productId));
	
	}
	
	@PutMapping("/admin/product/{productId}/quantity/{quantity}")
	public ResponseEntity<?> updateProductQuantity(@PathVariable int productId, @PathVariable int quantity) {
	    try {
	        Product updatedProduct = productService.updateProductQuantity(productId, quantity);
	        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
	    } catch (ResourceNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
	    }
	}
	
	@GetMapping("/admin/NumberOfProducts")
	public ResponseEntity<?> getTotalUser() throws IOException {
		System.out.println("-----------------------------get users" );
		return ResponseEntity.ok(productService.getNumberOfProducts());
	}
}
