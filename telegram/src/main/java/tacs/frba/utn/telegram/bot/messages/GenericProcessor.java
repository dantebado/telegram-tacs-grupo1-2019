package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.user.UserSession;

public class GenericProcessor {
	
	public static void processUpdate(UserSession session, Update update, SendMessage message) {
		message.setText("Inicializando la sesion");
	}

}
