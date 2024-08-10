package org.discord.backend.socketIO;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.backend.dto.TempDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketIoService {
    private final SocketIOServer socketIOServer;

    public String getChannelNewMessageEventName(String channelId){
        return "chat:"+channelId+":messages";
    }
    public String getChannelUpdateOrDeleteMessageEventName(String channelId){
        return "chat:"+channelId+":messages:update";
    }
    public ConnectListener connectListener(){
        return new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                log.info("New User Connected :" + client.getSessionId());
            }
        };
    }

    public DisconnectListener disconnectListener(){
        return new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                log.info("User Disconnected :" + client.getSessionId());
            }
        };
    }


    public DataListener<TempDto> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            log.info("Getting data from client");
            senderClient.sendEvent("received",data);

        };
    }

    public void sendNewMessageToChannel(String channelId, Object object){
        socketIOServer.getBroadcastOperations().sendEvent(getChannelNewMessageEventName(channelId),object);
    }
    public void sendUpdateOrDeleteMessageToChannel(String channelId, Object object){
        socketIOServer.getBroadcastOperations().sendEvent(getChannelUpdateOrDeleteMessageEventName(channelId),object);
    }
    public void sendNewMessageToConversation(String conversationId, Object object){
        socketIOServer.getBroadcastOperations().sendEvent(getChannelNewMessageEventName(conversationId),object);
    }
    public void sendUpdateOrDeleteMessageToConversation(String channelId, Object object){
        socketIOServer.getBroadcastOperations().sendEvent(getChannelUpdateOrDeleteMessageEventName(channelId),object);
    }
}
