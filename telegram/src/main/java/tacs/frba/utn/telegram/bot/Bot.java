package tacs.frba.utn.telegram.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import tacs.frba.utn.telegram.bot.messages.MessageProcessor;
import tacs.frba.utn.telegram.user.SessionsManager;
import tacs.frba.utn.telegram.user.User;
import tacs.frba.utn.telegram.user.UserSession;

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
		
		Long chatId = update.getMessage().getChatId();
		UserSession session = SessionsManager.getManager().findSession(chatId);
		if(session == null) {
			//User is not known
			session = SessionsManager.getManager().registerSession(chatId, new UserSession(chatId, null));
		}
		
		try {
			execute(MessageProcessor.processUpdate(session, update));
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
