package pl.edu.ecommerceshop.catalog.assets;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.config.CloudflareR2Properties;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudflareR2ProductImageStorageService implements ProductImageStorageService {

    private final S3Client r2S3Client;
    private final CloudflareR2Properties properties;

    @Override
    public String uploadProductImage(MultipartFile file) {
        validateImage(file);

        String contentType = file.getContentType();
        String extension = extensionForContentType(Objects.requireNonNull(contentType));
        String key = buildObjectKey(extension);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(key)
                .contentType(contentType)
                .contentLength(file.getSize())
                .cacheControl("public, max-age=31536000, immutable")
                .build();

        try {
            r2S3Client.putObject(
                    request,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException exception) {
            throw new BusinessException("Could not read uploaded product image.");
        } catch (RuntimeException exception) {
            throw new BusinessException("Could not upload product image to Cloudflare R2.");
        }

        return "/" + key;
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Product image is required.");
        }

        if (file.getSize() > properties.getMaxImageSizeBytes()) {
            throw new BusinessException(
                    "Product image is too large. Maximum size is %d bytes."
                            .formatted(properties.getMaxImageSizeBytes())
            );
        }

        String contentType = file.getContentType();

        if (contentType == null || !properties.getAllowedContentTypes().contains(contentType)) {
            throw new BusinessException(
                    "Unsupported product image type. Allowed types: %s."
                            .formatted(String.join(", ", properties.getAllowedContentTypes()))
            );
        }
    }

    private String extensionForContentType(String contentType) {
        return switch (contentType.toLowerCase(Locale.ROOT)) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new BusinessException("Unsupported product image type.");
        };
    }

    private String buildObjectKey(String extension) {
        String prefix = properties.getProductsPrefix();

        if (prefix == null || prefix.isBlank()) {
            prefix = "products";
        }

        prefix = prefix.trim();

        while (prefix.startsWith("/")) {
            prefix = prefix.substring(1);
        }

        while (prefix.endsWith("/")) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }

        return prefix + "/" + UUID.randomUUID() + extension;
    }
}
