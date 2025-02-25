package org.example;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class Main {
    public static void main(String[] args) {
        UserMessage userMessage = UserMessage.from("""
                给我输出一段Apache Camel XML-Io-DSL定义的XML文件。
                需要以Timer作为开始节点，周期为1天。
                后续通过设置请求体，将Body设置为12345，并将内容输出为Log。
                最终输出内容仅需要XML文件内容，屏蔽其他内容，不需要Markdown格式。
                """);

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(userMessage)
                .build();

        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey("sk-xxxx")
                .modelName("qwen-plus")
                .logRequests(true)
                .logResponses(true)
                .build();

        ChatResponse chatResponse = chatModel.chat(chatRequest);

        String output = chatResponse.aiMessage().text();
        System.out.println(output);
    }
}
