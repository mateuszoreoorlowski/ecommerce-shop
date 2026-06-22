package pl.edu.ecommerceshop.catalog.assets;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageStorageService {

    String uploadProductImage(MultipartFile file);
}
