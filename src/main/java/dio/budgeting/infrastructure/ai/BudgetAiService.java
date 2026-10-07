package dio.budgeting.infrastructure.ai;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
public class BudgetAiService {
    private static final String SYSTEM_PROMPT = """
            Você é um assistente financeiro de orçamento pessoal.
            Interprete pedidos em português e use as ferramentas disponíveis quando o usuário quiser
            criar uma receita/despesa, consultar gastos por categoria ou consultar o saldo.
            Nunca invente transações. Para operações financeiras, prefira chamar uma ferramenta.
            Responda de forma curta, clara e em português do Brasil.
            """;

    private final ChatClient chatClient;
    private final FinanceTools tools;
    private final TranscriptionModel transcriptionModel;
    private final TextToSpeechModel textToSpeechModel;

    public BudgetAiService(ChatClient.Builder builder, FinanceTools tools,
                           TranscriptionModel transcriptionModel,
                           TextToSpeechModel textToSpeechModel) {
        this.chatClient = builder.build();
        this.tools = tools;
        this.transcriptionModel = transcriptionModel;
        this.textToSpeechModel = textToSpeechModel;
    }

    public String processText(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia.");
        }

        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(message)
                .tools(tools)
                .call()
                .content();
    }

    public VoiceResult processVoice(byte[] audio, String filename) {
        if (audio == null || audio.length == 0) {
            throw new IllegalArgumentException("O arquivo de áudio está vazio.");
        }

        ByteArrayResource resource = new ByteArrayResource(audio) {
            @Override
            public String getFilename() {
                return filename == null || filename.isBlank() ? "audio.webm" : filename;
            }
        };

        String transcript = transcriptionModel.transcribe(resource);
        String answer = processText(transcript);
        byte[] mp3 = textToSpeechModel.call(answer);
        return new VoiceResult(transcript, answer, mp3);
    }

    public record VoiceResult(String transcript, String answer, byte[] audio) {}
}
