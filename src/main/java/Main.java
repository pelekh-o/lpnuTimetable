import bot.telegram.TelegramBot;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        Logger log = Logger.getLogger(Main.class);
        BasicConfigurator.configure();
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new TelegramBot());
            log.info("The bot has started.");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
