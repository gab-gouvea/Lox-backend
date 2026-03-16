package br.com.lox.domain.upload;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CloudinaryService {

    @Value("${cloudinary.url}")
    private String cloudinaryUrl;

    private String cloudName;
    private String apiKey;
    private String apiSecret;

    @PostConstruct
    public void init() {
        // Parse cloudinary://API_KEY:API_SECRET@CLOUD_NAME
        Matcher m = Pattern.compile("cloudinary://([^:]+):([^@]+)@(.+)").matcher(cloudinaryUrl);
        if (!m.matches()) {
            throw new IllegalArgumentException("CLOUDINARY_URL inválida. Formato: cloudinary://key:secret@cloud_name");
        }
        apiKey = m.group(1);
        apiSecret = m.group(2);
        cloudName = m.group(3);
    }

    public String upload(MultipartFile file) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String publicId = "lox/" + UUID.randomUUID();
        String toSign = "public_id=" + publicId + "&timestamp=" + timestamp + apiSecret;
        String signature = sha1(toSign);

        String boundary = "----FormBoundary" + UUID.randomUUID().toString().replace("-", "");
        URI uri = URI.create("https://api.cloudinary.com/v1_1/" + cloudName + "/image/upload");

        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream os = conn.getOutputStream()) {
            writeField(os, boundary, "api_key", apiKey);
            writeField(os, boundary, "timestamp", timestamp);
            writeField(os, boundary, "signature", signature);
            writeField(os, boundary, "public_id", publicId);
            writeFile(os, boundary, "file", file);
            os.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
        String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        is.close();

        if (status < 200 || status >= 300) {
            throw new IOException("Cloudinary upload failed: " + response);
        }

        Matcher matcher = Pattern.compile("\"secure_url\"\\s*:\\s*\"([^\"]+)\"").matcher(response);
        if (matcher.find()) {
            return matcher.group(1).replace("\\/", "/");
        }
        throw new IOException("Could not parse Cloudinary response");
    }

    private void writeField(OutputStream os, String boundary, String name, String value) throws IOException {
        os.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        os.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        os.write((value + "\r\n").getBytes(StandardCharsets.UTF_8));
    }

    private void writeFile(OutputStream os, String boundary, String name, MultipartFile file) throws IOException {
        os.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        os.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getOriginalFilename() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        os.write(("Content-Type: " + file.getContentType() + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        os.write(file.getBytes());
        os.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    private String sha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
