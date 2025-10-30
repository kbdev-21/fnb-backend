package com.example.fnb.image.domain;

import com.example.fnb.image.StorageService;
import com.example.fnb.image.domain.entity.FileData;
import com.example.fnb.image.domain.repository.FileDataRepository;
import com.example.fnb.image.dto.FileDataDto;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SupabaseStorageService implements StorageService {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private final FileDataRepository fileDataRepository;
    private final ModelMapper modelMapper;

    public SupabaseStorageService(FileDataRepository fileDataRepository, ModelMapper modelMapper) {
        this.fileDataRepository = fileDataRepository;
        this.modelMapper = modelMapper;
    }

    private final ConnectionProvider provider = ConnectionProvider.builder("storage-pool")
        .maxConnections(100)
        .maxIdleTime(Duration.ofSeconds(30))
        .maxLifeTime(Duration.ofMinutes(5))
        .pendingAcquireTimeout(Duration.ofSeconds(5))
        .build();

    private final WebClient webClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create(provider)))
        .build();

    @Override
    public FileDataDto uploadFile(MultipartFile file) {
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes", e);
        }

        String supabaseStorageUrl = supabaseUrl + "/storage/v1";
        UUID fileId = UUID.randomUUID();
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileKey = bucketName + "/" + fileId + fileExtension;

        String uploadUrl = supabaseStorageUrl + "/object/" + fileKey;

        String response = webClient.post()
            .uri(uploadUrl)
            .header("Authorization", "Bearer " + supabaseKey)
            .header("apikey", supabaseKey)
            .contentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())))
            .bodyValue(fileBytes)
            .retrieve()
            .onStatus(HttpStatusCode::isError, r ->
                r.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException(
                    "Upload failed: " + r.statusCode() + " | " + body
                )))
            )
            .bodyToMono(String.class)
            .block();

        JSONObject json = new JSONObject(response);
        String responseKey = json.getString("Key");

        String publicUrl = supabaseUrl + "/storage/v1/object/public/" + fileKey;
        FileData savedFileData = fileDataRepository.save(new FileData(
            fileId,
            publicUrl,
            responseKey
        ));

        return modelMapper.map(savedFileData, FileDataDto.class);
    }

    @Override
    public List<FileDataDto> getAllFileData() {
        var fileDataList = fileDataRepository.findAll();
        return fileDataList.stream()
            .map(f -> modelMapper.map(f, FileDataDto.class))
            .toList();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "";
        }
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1 || lastDot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDot).toLowerCase();
    }

}