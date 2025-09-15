package com.ecommernce.service;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommernce.exceptions.APIExcpetion;
import com.ecommernce.exceptions.ResourceNotFoundException;
import com.ecommernce.model.Category;
import com.ecommernce.model.Product;
import com.ecommernce.payload.ProductDTO;
import com.ecommernce.payload.ProductResponse;
import com.ecommernce.repository.CategoryRepository;
import com.ecommernce.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String filePath;
	
	@Autowired
	private ModelMapper modelMapper;
	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		
		Sort sortedByOrder=sortOrder.equalsIgnoreCase("asc")
				?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
		
		Pageable pageDetails=PageRequest.of(pageNumber, pageSize,sortedByOrder);
		Page<Product> page=productRepository.findAll(pageDetails);
		List<Product> findAllProducts=page.getContent();
		
		if(findAllProducts.isEmpty()) {
			throw new APIExcpetion("Products Not Existing!!!");
		}
		List<ProductDTO> productDTOs=findAllProducts.stream()
				.map(product->modelMapper.map(product, ProductDTO.class)).toList();
		ProductResponse productResponse=new ProductResponse();
		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(page.getNumber());
		productResponse.setPageSize(page.getSize());
		productResponse.setTotalElemts(page.getTotalElements());
		productResponse.setTotalPages(page.getTotalPages());
		productResponse.setLastPage(page.isLast());
		return productResponse;
	}

	@Override
	public ProductDTO addProducts(ProductDTO productDTO,Long categoryId) {
		Category category=categoryRepository
				.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));
		
		List<Product> products=category.getProducts();
		Boolean isfindpathproducts=true;
		for(Product product:products) {
			
			  if(product.getProductName().equalsIgnoreCase(productDTO.getProductName())) {
				  
				    isfindpathproducts=false;
				    break;
			  }
		}
		if(isfindpathproducts) {
			Product product=modelMapper.map(productDTO, Product.class);
			product.setImage("default.png");
			product.setCategory(category);
			Double specialPrice=productDTO.getPrice()-((productDTO.getDiscount()*0.01)*productDTO.getPrice());
			product.setSpecialPrice(specialPrice);
			productRepository.save(product);
			
			return modelMapper.map(product, ProductDTO.class);
			
		}else {
			throw new APIExcpetion("Product Name Already Existing!!!");
		}
		
	}

	@Override
	public ProductDTO deleteProduct(Long productId) {
		
		Product findByProductId=productRepository.findById(productId)
				.orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));
	
		productRepository.deleteById(productId);
		return modelMapper.map(findByProductId, ProductDTO.class);
	}

	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
		Product findByProductId=productRepository.findById(productId)
				.orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));
		
		findByProductId.setDescription(productDTO.getDescription());
		findByProductId.setDiscount(productDTO.getDiscount());
		findByProductId.setPrice(productDTO.getPrice());
		Double specialPrice=productDTO.getPrice()-((productDTO.getDiscount()*0.01)*productDTO.getPrice());
		findByProductId.setSpecialPrice(specialPrice);
		
		Product updateProduct=productRepository.save(findByProductId);
		
		return modelMapper.map(updateProduct, ProductDTO.class);
	}

	@Override
	public ProductDTO updateImage(Long productId, MultipartFile file) throws IOException {
		
		Product findByProducts=productRepository.findById(productId)
				.orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));
		String fileName=fileService.uploadFile(filePath, file);
		findByProducts.setImage(fileName);
		Product updateProduct=productRepository.save(findByProducts);
		return modelMapper.map(updateProduct, ProductDTO.class);
	}

	@Override
	public ProductResponse getAllProductByProductId(Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
	
		 Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));
		 Sort sortedByOrder=sortOrder.equalsIgnoreCase("asc")
					?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
		 Pageable pageDetails=PageRequest.of(pageNumber, pageSize,sortedByOrder);
		 Page<Product> page=productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
		 List<Product> products=page.getContent();
		 
		 if(products.isEmpty()) {
			 throw new APIExcpetion("Product Not Existing!!");
		 }
		  List<ProductDTO> productDTOs=products.stream()
				  .map(product->modelMapper.map(product, ProductDTO.class)).toList();
		  ProductResponse productResponse=new ProductResponse();
		  productResponse.setContent(productDTOs);
		  productResponse.setPageNumber(page.getNumber());
			productResponse.setPageSize(page.getSize());
			productResponse.setTotalElemts(page.getTotalElements());
			productResponse.setTotalPages(page.getTotalPages());
			productResponse.setLastPage(page.isLast());
		return productResponse;
	}

	@Override
	public ProductResponse getAllProductsByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,String keyword) {
		Sort sortedByOrder=sortOrder.equalsIgnoreCase("asc")
				?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
		Pageable pageDetails=PageRequest.of(pageNumber, pageSize,sortedByOrder);
		Page<Product> page=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);
		
		List<Product> findByProductName=page.getContent();
		
		if(findByProductName.isEmpty()) {
			throw new APIExcpetion("Product Not Existing!!!");
		}
		
	    List<ProductDTO> productDTOs=findByProductName.stream()
	    		.map(product->modelMapper.map(product, ProductDTO.class)).toList();
	    ProductResponse productResponse=new ProductResponse();
	    productResponse.setContent(productDTOs);
		  productResponse.setPageNumber(page.getNumber());
			productResponse.setPageSize(page.getSize());
			productResponse.setTotalElemts(page.getTotalElements());
			productResponse.setTotalPages(page.getTotalPages());
			productResponse.setLastPage(page.isLast());
		return productResponse;
	}

}
