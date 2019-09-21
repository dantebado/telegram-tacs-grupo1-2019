package tacs.frba.utn.telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import tacs.frba.utn.telegram.bot.Bot;

public class App 
{
    public static void main( String[] args )
    {
    	ApiContextInitializer.init();
        TelegramBotsApi telegramBotApi = new TelegramBotsApi();
        try {
            telegramBotApi.registerBot(Bot.getBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
