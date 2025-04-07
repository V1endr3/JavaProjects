package org.example;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.example.asset.StructuredPromptTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.data.message.UserMessage.userMessage;

@Slf4j
public class LangchainTest {
    private static final String BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";
    private static final String API_KEY = "sk-xxx";

    private OpenAiChatModel.OpenAiChatModelBuilder modelBuilder;

    @BeforeEach
    public void init() {
        modelBuilder = OpenAiChatModel.builder()
                .baseUrl(BASE_URL)
                .apiKey(API_KEY)
                .modelName("qwen-plus");
    }

    @Test
    public void testHelloWorld() {
        ChatLanguageModel model = modelBuilder.build();

        String response = model.chat("Say `Hello, world!`");
        log.info(response);
    }

    @Test
    public void testModelParameters() {
        ChatLanguageModel model = modelBuilder.temperature(0.3)
                .timeout(Duration.ofSeconds(60))
                .logRequests(true)
                .logResponses(true)
                .build();

        String prompt = "Explain how to compile gcc in linux-like bear-metal machines";
        String response = model.chat(prompt);
        log.info(response);
    }

    @Test
    public void testPromptTemplate() {
        ChatLanguageModel model = modelBuilder.build();

        String template = "Create a recipe for a {{dishType}} with the following ingredients: {{ingredients}}";
        PromptTemplate promptTemplate = PromptTemplate.from(template);

        Map<String, Object> variables = new HashMap<>();
        variables.put("dishType", "oven dish");
        variables.put("ingredients", "potato, tomato, feta, olive oil");
        Prompt prompt = promptTemplate.apply(variables);

        String response = model.chat(prompt.text());
        log.info(response);
    }

    @Test
    public void testPromptTemplateUsingClasses() {
        ChatLanguageModel model = modelBuilder.build();

        StructuredPromptTemplate.CreateRecipePrompt createRecipePrompt = new StructuredPromptTemplate.CreateRecipePrompt(
                "salad",
                Arrays.asList("cucumber", "tomato", "feta", "onion", "olives")
        );

        Prompt prompt = StructuredPromptProcessor.toPrompt(createRecipePrompt);

        String response = model.chat(prompt.text());
        log.info(response);
    }

    @Test
    public void testStreamingModel() throws Exception {
        // TODO: 流式输出有问题
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .baseUrl(BASE_URL)
                .apiKey(API_KEY)
                .modelName("qwen-plus")
                .build();

        String prompt = "Write a short funny poem about developers and null-pointers, 10 lines maximum";
        log.info("Nr of chars: {}", prompt.length());
        log.info("Nr of tokens: {}", model.estimateTokenCount(prompt));

        model.chat(prompt, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                log.info("partial consumed: {}", partialResponse);
                System.out.print(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                log.info("completed, {}", completeResponse.aiMessage());
            }

            @Override
            public void onError(Throwable error) {
                log.error("prompt went wrong, ", error);
            }
        });
        Thread.sleep(1000000000);
    }

    @Test
    public void testChatMemory() throws Exception {
        ChatLanguageModel model = modelBuilder.build();

        ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(1000, new OpenAiTokenizer());

        SystemMessage systemMessage = SystemMessage.from(
                "You are a senior developer explaining to another senior developer, "
                        + "the project you are working on is an e-commerce platform with Java back-end, " +
                        "Oracle database,and Spring Data JPA");
        chatMemory.add(systemMessage);


        UserMessage userMessage1 = userMessage(
                "How do I optimize database queries for a large-scale e-commerce platform? "
                        + "Answer short in three to five lines maximum.");
        chatMemory.add(userMessage1);

        log.info("[User]: {}", userMessage1.text());
        log.info("[LLM]: ");

        CompletableFuture<AiMessage> futureAiMessage = new CompletableFuture<>();

        ChatResponse response = model.chat(chatMemory.messages());
        log.info(response.aiMessage().text());
        futureAiMessage.complete(response.aiMessage());
        chatMemory.add(futureAiMessage.get());

        UserMessage userMessage2 = userMessage(
                "Give a concrete example implementation of the first point? " +
                        "Be short, 10 lines of code maximum.");
        chatMemory.add(userMessage2);

        log.info("\n\n[User]: {}", userMessage2.text());
        log.info("[LLM]: ");

        ChatResponse response2 = model.chat(chatMemory.messages());
        log.info(response2.aiMessage().text());
    }
}
