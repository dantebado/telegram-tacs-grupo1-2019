package tacs.frba.utn.telegram.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
	
	public static final String TELEGRAM_TOKEN = "854487790:AAEgBU3a_4QUDL8Z184uq-bUf70Sx2Kq8jk";
	public static final String TELEGRAM_NAME= "TACS";

	public String getBotUsername() {
		return TELEGRAM_NAME;
	}

	@Override
	public String getBotToken() {
		return TELEGRAM_TOKEN;
	}

	public void onUpdateReceived(Update update) {
		System.out.println("[" + update.getMessage().getChatId() + "]:::" + update.getMessage().getText());
		sendMsg(update.getMessage().getChatId().toString(), "Este es el bot de TACS. Pronto estará disponible.");
	}
	
	public synchronized void sendMsg(String chatId, String responseMessage) {		
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(responseMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

}
