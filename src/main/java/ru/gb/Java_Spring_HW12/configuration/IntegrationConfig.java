package ru.gb.Java_Spring_HW12.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import java.io.File;
import java.io.FileReader;
import java.util.Locale;

@Configuration
public class IntegrationConfig {
    @Bean
    public MessageChannel TextInputChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageChannel FileReaderChannel() {
        return new DirectChannel();
    }
    @Bean
    @Transformer(inputChannel = "TextInputChannel", outputChannel = "FileReaderChannel")
    public GenericTransformer<String, String> myTransformer() {
        return text -> text.toUpperCase(Locale.ROOT).trim();
    }
    @Bean
    @ServiceActivator(inputChannel = "FileReaderChannel")
    public FileWritingMessageHandler myMessageHandler() {
        FileWritingMessageHandler handler =
                new FileWritingMessageHandler
                        (new File("C:\\Users\\user\\IdeaProjects\\Java_Spring_HW12"));
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        return handler;
    }
}

