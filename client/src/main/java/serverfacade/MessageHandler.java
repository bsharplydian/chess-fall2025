package serverfacade;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface MessageHandler {
    void handleMessage(NotificationMessage notificationMessage);
    void handleMessage(LoadGameMessage loadGameMessage);
    void handleMessage(ErrorMessage errorMessage);
}
