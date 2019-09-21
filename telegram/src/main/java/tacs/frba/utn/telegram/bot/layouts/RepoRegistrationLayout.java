package tacs.frba.utn.telegram.bot.layouts;

import java.util.ArrayList;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class RepoRegistrationLayout {
	
	public static void setDatesLayout(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        
        ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        keyboard.add(LayoutUtils.buildRowWithSingleButton("El día de hoy"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Los últimos tres días"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("La última semana"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("El último mes"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("El inicio de los tiempos"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Volver"));
        
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

}
