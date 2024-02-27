package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.exception.ProductException;
import com.shopkart.ecommerce.model.Category;
import com.shopkart.ecommerce.model.Product;
import com.shopkart.ecommerce.repository.CategoryRepository;
import com.shopkart.ecommerce.repository.ProductRepository;
import com.shopkart.ecommerce.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(CreateProductRequest req){

        Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());

        if(topLevel == null){
            Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getTopLevelCategory());
            topLevelCategory.setLevel(1);

            topLevel = categoryRepository.save(topLevelCategory);

        }

        Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLevelCategory(),topLevel.getName());

        if(secondLevel == null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepository.save(secondLevelCategory);

        }

        Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(), secondLevel.getName());
        if(thirdLevel == null){
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);

            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();

        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscounted_price(req.getDiscountedPrice());
        product.setDiscountPercent(req.getDiscountPercent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSize());
        product.setCategory(thirdLevel);
        product.setQuantity(req.getQuantity());
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException{

        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product Deleted Successfully!!";

    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException{

        Product product = findProductById(productId);

        if(req.getQuantity() != 0){
            product.setQuantity(req.getQuantity());
        }

        return productRepository.save(product);

    }

    @Override
    public Product findProductById(Long productId) throws ProductException{

        Optional<Product> optional = productRepository.findById(productId);

        if(optional.isPresent()){
            return optional.get();
        }

        throw new ProductException("Product Not Find with Id : " + productId);

    }

    @Override
    public List<Product> findProductByCategory(String category){

        return null;

    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize){

        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        // Get all the products
        List<Product> products = productRepository.filterProducts(category,minPrice,maxPrice,minDiscount,sort);

        // filter according to the colours
        // if the colour is not empty then check if products colour matches
        if(!colors.isEmpty()){
            products = products.stream().filter(p->colors.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }


        // Filter for stock
        if(stock != null){

            if(stock.equals("in_stock")){
                products = products.stream().filter(p->p.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p->p.getQuantity() < 1).collect(Collectors.toList());
            }

        }

       // Pagination

        // The pageable will give the number of pages to skip
       int startIndex = (int) pageable.getOffset();
        // get end index
       int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

       List<Product> pageContent = products.subList(startIndex,endIndex);

       Page<Product> filteredProducts = new PageImpl<>(pageContent,pageable, products.size());

       return filteredProducts;

    }
}
