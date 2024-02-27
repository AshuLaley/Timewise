package com.watcher.service;

import java.util.List;

import com.watcher.dto.ApiResponse;
import com.watcher.dto.ProductDto;
import com.watcher.entity.Category;
import com.watcher.entity.Product;

public interface ProductService {
	
	ApiResponse addProduct(ProductDto product);
	
	List<ProductDto> getAllProduct();
	
	ProductDto getProductById(int productId);
	
	ProductDto updateProduct(int productId, ProductDto productDto);
	
	ApiResponse deleteProductById(int productId);

	List<ProductDto> getProductsByCategory(Category category);
	
	List<ProductDto> getProductsByColor(String color);

   Product updateProductQuantity(int productId, int quantity) ;

   long getNumberOfProducts();
}
