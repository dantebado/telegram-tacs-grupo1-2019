package tacs.frba.utn.telegram.bot.layouts;

import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class LayoutUtils {
	
	public static KeyboardRow buildRowWithSingleButton(String buttonText) {
		KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(buttonText));
        return keyboardRow;
	}

}
