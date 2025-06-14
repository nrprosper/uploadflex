# UploadFlex

## Multi-Provider Files Upload API – Spring Boot

A flexible and extensible files upload solution for Spring Boot applications. Easily switch between providers like MinIO, AWS S3, and Cloudinary without modifying your business logic.

### Features

- Modular design using the Strategy Pattern
- Easily configurable via application properties
- Switch providers at runtime via request parameter or default setting
- Simple to extend with custom providers

---

### How It Works

1. A common `FileService` interface defines the contract.
2. Each provider implements this interface:
    - `CloudinaryService`
    - `S3Service`
    - `MinioService`
3. A strategy resolver maps provider keys to implementations.
4. You can:
    - Set the default provider in `application.yml`
    - Override it per request using a `provider` parameter