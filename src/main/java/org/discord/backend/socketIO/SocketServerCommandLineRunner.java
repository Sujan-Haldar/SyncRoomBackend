package org.discord.backend.socketIO;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.backend.dto.TempDto;
import org.discord.backend.repository.MessageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketServerCommandLineRunner implements CommandLineRunner {
    private final SocketIOServer socketIOServer;
    private  final SocketIoService socketIoService;
    private  final MessageRepository messageRepository;
    @Override
    public void run(String... args) throws Exception {
        socketIOServer.start();
        socketIOServer.addConnectListener(socketIoService.connectListener());
        socketIOServer.addDisconnectListener(socketIoService.disconnectListener());
        socketIOServer.addEventListener("send", TempDto.class,socketIoService.onChatReceived());
    }
    @PreDestroy
    private void stopServer() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            log.info("Socket.IO server stopped...");
        }
    }
}
