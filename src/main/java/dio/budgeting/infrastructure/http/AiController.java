package dio.budgeting.infrastructure.http;

import dio.budgeting.infrastructure.ai.BudgetAiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final BudgetAiService service;

    public AiController(BudgetAiService service) {
        this.service = service;
    }

    @PostMapping("/text")
    public Map<String, String> text(@Valid @RequestBody TextRequest request) {
        return Map.of("answer", service.processText(request.message()));
    }

    @PostMapping(value = "/voice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> voice(@RequestPart("audio") MultipartFile audio) throws IOException {
        var result = service.processVoice(audio.getBytes(), audio.getOriginalFilename());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=answer.mp3")
                .header("X-Transcription", sanitizeHeader(result.transcript()))
                .header("X-AI-Answer", sanitizeHeader(result.answer()))
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(result.audio());
    }

    private String sanitizeHeader(String value) {
        return value.replace("\r", " ").replace("\n", " ");
    }

    public record TextRequest(@NotBlank String message) {}
}
