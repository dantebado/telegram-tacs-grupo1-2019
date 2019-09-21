package tacs.frba.utn.telegram.bot.layouts;

import java.util.ArrayList;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class MenuLayout {

	public static void setUserLayout(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        
        ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

        keyboard.add(LayoutUtils.buildRowWithSingleButton("Ver detalles de un Repositorio"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Administrar mis Favoritos"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Realizar una Búsqueda Avanzada"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Logout"));
        
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
	
	public static void setAdminLayout(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        
        ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Ver detalles de un Usuario"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Comparar Favoritos de dos Usuarios"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Ver popularidad de un Repositorio"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Ver Repositorios registrados"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Logout"));
        
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
	
}
