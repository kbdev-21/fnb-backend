package com.example.fnb.image.domain;

import com.example.fnb.image.ImageService;
import com.example.fnb.image.domain.entity.Image;
import com.example.fnb.image.domain.repository.ImageRepository;
import com.example.fnb.image.dto.UploadImageResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class SupabaseStorageService implements ImageService {

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_KEY}")
    private String supabaseKey;

    @Value("${SUPABASE_BUCKET}")
    private String bucketName;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ImageRepository imageRepository;

    public SupabaseStorageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public UploadImageResponseDto uploadFile(MultipartFile file) throws IOException {
        String encodedFileName = URLEncoder.encode(Objects.requireNonNull(file.getOriginalFilename()), StandardCharsets.UTF_8);
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + encodedFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uploadUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + encodedFileName;
            imageRepository.save(new Image(
                    UUID.randomUUID(),
                    imgUrl,
                    encodedFileName
            ));
            return new UploadImageResponseDto(
                    imgUrl,
                    encodedFileName
            );
        } else {
            throw new RuntimeException("Upload failed: " + response.getBody());
        }
    }

//    public String getPublicUrl(String fileName){
//        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
//        return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + encodedFileName;
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Map<String, Object>> listFiles(){
//        String listUrl = supabaseUrl + "/storage/v1/object/list" + bucketName;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + supabaseKey);
//        headers.set("apikey", supabaseKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        ResponseEntity<List> response = restTemplate.exchange(listUrl, HttpMethod.POST, request, List.class);
//
//        return response.getBody();
//    }

//    public String updateFile(String oldFileName, MultipartFile newFile) throws IOException {
//        deleteFile(oldFileName);
//        return uploadFile(newFile);
//    }
//
//    public void deleteFile(String fileName) {
//        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
//        String deleteUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + encodedFileName;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + supabaseKey);
//        headers.set("apikey", supabaseKey);
//
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, requestEntity, String.class);
//    }
}