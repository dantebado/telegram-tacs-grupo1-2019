package tacs.frba.utn.telegram.bot.layouts;

import java.awt.List;
import java.util.ArrayList;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class PreInitLayout {
	
	public static void setLayout(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        
        ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Login"));
        keyboard.add(LayoutUtils.buildRowWithSingleButton("Registrarse"));
        
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

}
