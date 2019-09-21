package tacs.frba.utn.telegram.bot.layouts;

import java.util.ArrayList;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class FavouriteLayout {
	
	public static void setInitialLayout(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        
        ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

        keyboard.add(LayoutUtils.buildRowWithSingleButton("Ver Favoritos"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Añadir un Favorito"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Eliminar un Favorito"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Vaciar Lista"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Volver"));
        
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
	
	public static void setClearLayout(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        
        ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

        keyboard.add(LayoutUtils.buildRowWithSingleButton("Sí"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("No"));
        
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

}
