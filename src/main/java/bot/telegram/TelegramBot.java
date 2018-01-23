package bot.telegram;

import com.vdurmont.emoji.EmojiParser;
import entity.AnswerEntity;
import entity.ChatEntity;
import entity.MessageEntity;
import entity.UserEntity;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import parser.TimetableParser;
import persistence.Factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Math.toIntExact;

public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(TelegramBot.class);
    private static final String myScheduleMenuText = "\uD83D\uDCCB Мій розклад";
    private static final String settingsMenuText = "\u2699 Налаштування";
    private static final String addGroupMenuText = "\uD83D\uDCBE Додати групу";
    private static final String editGroupMenuText = "\uD83D\uDCDD Змінити групу";
    private static final String backButtonText = "\u2B05 Назад";
    private static final String infoMenuText = "\u2139 Інформація";
    private static final String helpMenuText = "\u2B50 Допомога";

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        UserEntity user = null;
        String answer = null;

        if (message != null && message.hasText()) {
            try {
                user = checkUser(message.getFrom());
            } catch (SQLException e) {
                log.warn(e.getMessage());
            }

            Properties properties = new Properties();
            try (FileInputStream in = new FileInputStream("src/main/resources/answers.xml")){
                properties.loadFromXML(in);
            } catch (IOException e) {
                log.warn(e.getMessage());
            }

            switch (message.getText()) {
                case myScheduleMenuText:
                    if (user.getIsRemembered() == 1) {
                        sendMsgWithDaysInlineKeyb(message);
                    }
                    else {
                        answer = properties.getProperty("noGroup");
                        sendMsg(message, answer);
                    }
                    break;
                case addGroupMenuText:
                    answer = properties.getProperty("addGroupText");
                    sendMsgWithoutKeyboard(message, answer);
                    break;
                case "/settings":
                case settingsMenuText:
                    sendMsgSettings(message, message.getText(), user);
                    break;
                case editGroupMenuText:
                    answer = "За тобою закріплено групу " + user.getGroupTitle() + " " + user.getInstituteTitle()
                            + ". Щоб змінити свою групу, введи назву інституту та групи через пробіл(Наприклад, ібід бд-21), або кнопку "
                            + backButtonText + " щоб повернутись назад.";
                    sendMsgWithoutKeyboard(message, answer);
                    break;
                case backButtonText:
                    sendMsg(message, message.getText());
                    break;
                case helpMenuText:
                case "/help":
                    answer = properties.getProperty("help");
                    sendMsg(message, answer);
                    break;
                case "/start":
                    answer = properties.getProperty("start");
                    sendMsg(message, answer);
                    break;
                case infoMenuText:
                case "/info":
                    answer = properties.getProperty("info");
                    sendMsg(message, answer);
                    break;
                default: {
                    String[] arrMessage = message.getText().split(" ");
                    if (arrMessage.length == 2) {
                        if (arrMessage[0].length() == 4 && arrMessage[1].contains("-")) { // назва інституту завжди
                            // складається із чотирьох літер, у назві групи завжди міститься знак -
                            try {
                                Factory.getInstance().getUserDAO().updateUser(user,
                                        message.getFrom().getFirstName() + "\u00a0" + message.getFrom().getLastName(),
                                        message.getFrom().getUserName(), arrMessage[0], arrMessage[1], (byte) 1);
                                sendMsg(message, "Групу " + user.getGroupTitle() + " " + user.getInstituteTitle() +
                                        " успішно додано.");
                            } catch (SQLException e) {
                                log.warn(e.getMessage());
                                sendMsg(message, "*Помилка! Перевір введені дані. \n/help для допомоги");
                            }
                        } else sendMsg(message, "Помилка! Перевір введені дані. \n/help для допомоги");
                    }
                    else sendMsg(message, message.getText());

                }
            }
        }
        else
            if (update.hasCallbackQuery()) {
                String callData = update.getCallbackQuery().getData();
                long messageId = update.getCallbackQuery().getMessage().getMessageId();
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                TimetableParser parser = new TimetableParser();
                try {
                    user = checkUser(update.getCallbackQuery().getFrom());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String institute = user.getInstituteTitle();
                String group = user.getGroupTitle();
                String checkMarkEmoji = EmojiParser.parseToUnicode("\n:calendar: ");
                switch (callData) {
                    case "mon":
                        answer = checkMarkEmoji + "*Понеділок:*\n" + parser.parseDLesson("Пн", institute, group);
                        break;
                    case "tue":
                        answer = checkMarkEmoji + "*Вівторок:*\n" + parser.parseDLesson("Вт", institute, group);
                        break;
                    case "wed":
                        answer = checkMarkEmoji + "*Середа:*\n" + parser.parseDLesson("Ср", institute, group);
                        break;
                    case "thu":
                        answer = checkMarkEmoji + "*Четвер:*\n" + parser.parseDLesson("Чт", institute, group);
                        break;
                    case "fri":
                        answer = checkMarkEmoji + "*П\'ятниця:*\n" + parser.parseDLesson("Пт", institute, group);
                        break;
                    case "all":
                        group = user.getGroupTitle();
                        institute = user.getInstituteTitle();
                        StringBuffer answerBuf = new StringBuffer();
                        answerBuf.append("Розклад для групи " + group + ", " + institute + "\n");
                        answerBuf.append(checkMarkEmoji + "*Понеділок:* " + new TimetableParser().parseDLesson("Пн", institute, group));
                        answerBuf.append(checkMarkEmoji + "*Вівторок:* " + new TimetableParser().parseDLesson("Вт", institute, group));
                        answerBuf.append(checkMarkEmoji + "*Середа:* " + new TimetableParser().parseDLesson("Ср", institute, group));
                        answerBuf.append(checkMarkEmoji + "*Четвер:* " + new TimetableParser().parseDLesson("Чт", institute, group));
                        answerBuf.append(checkMarkEmoji + "*П\'ятниця:* " + new TimetableParser().parseDLesson("Пт", institute, group));
                        answer = answerBuf.toString();
                        break;
                }

                EditMessageText newMessage = new EditMessageText()
                        .setChatId(chatId)
                        .setMessageId(toIntExact(messageId))
                        .setParseMode("Markdown")
                        .setText(answer);
                try {
                    execute(newMessage);
                    //logger(update.getCallbackQuery().getMessage(), answer);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public String getBotUsername() {
        return "timetableLPNUbot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_TOKEN");
    }

    private void sendMsg(Message message, String answer) {
        long chatID = message.getChatId();

        SendMessage sendMessage = new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatID)
                .setParseMode("Markdown")
                .setText(answer);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup()
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        List<KeyboardRow> keyboardRow = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(myScheduleMenuText);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(settingsMenuText);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(infoMenuText);
        keyboardThirdRow.add(helpMenuText);

        keyboardRow.add(keyboardFirstRow);
        keyboardRow.add(keyboardSecondRow);
        keyboardRow.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboardRow);

         try {
             execute(sendMessage);
             logger(message, answer);
        } catch (TelegramApiException e) {
            log.warn(e.getMessage());
            sendMsg(message, "Неправильний формат повідомлення.\n/help - для інформації.");
        }
    }

    private void sendMsgSettings(Message message, String answer, UserEntity user) {
        long chatID = message.getChatId();

        SendMessage sendMessage = new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatID)
                .setParseMode("Markdown")
                .setText(answer);

        // створити клавіатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        // спосок рядків клавіатури
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        if (!user.getIsRemembered().equals((byte) 1)) {
            keyboardFirstRow.add(addGroupMenuText);
        }
        keyboardFirstRow.add(editGroupMenuText);
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(backButtonText);
        // додати всі рядки клавіатури в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // встановити цей список клавіатурі
        replyKeyboardMarkup.setKeyboard(keyboard);

        try {
            execute(sendMessage);
            logger(message, answer);
        } catch (TelegramApiException e) {
            log.warn(e.getMessage());
            sendMsg(message, "Неправильний формат повідомлення.\n/help - для інформації.");
        }
    }

    private void sendMsgWithoutKeyboard(Message message, String answer) {
        long chatID = message.getChatId();
        SendMessage sendMessage = new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatID)
                .setParseMode("Markdown")
                .setText(answer);
        try {
            execute(sendMessage);
            logger(message, answer);
        } catch (TelegramApiException e) {
            log.warn(e.getMessage());
            sendMsg(message, "Неправильний формат повідомлення.\n/help - для інформації.");
        }
    }

    private void sendMsgWithDaysInlineKeyb(Message message) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setText("Вибери день зі списку:\n");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonsList = new ArrayList<>();
        List<InlineKeyboardButton> buttonsFirstRow = new ArrayList<>();
        List<InlineKeyboardButton> buttonsSecondRow = new ArrayList<>();

        buttonsFirstRow.add(new InlineKeyboardButton().setText("Пн").setCallbackData("mon"));
        buttonsFirstRow.add(new InlineKeyboardButton().setText("Вт").setCallbackData("tue"));
        buttonsFirstRow.add(new InlineKeyboardButton().setText("Ср").setCallbackData("wed"));
        buttonsFirstRow.add(new InlineKeyboardButton().setText("Чт").setCallbackData("thu"));
        buttonsFirstRow.add(new InlineKeyboardButton().setText("Пт").setCallbackData("fri"));
        buttonsSecondRow.add(new InlineKeyboardButton().setText("Весь тиждень").setCallbackData("all"));

        buttonsList.add(buttonsFirstRow);
        buttonsList.add(buttonsSecondRow);
        keyboard.setKeyboard(buttonsList);
        sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
            logger(message, "Вибери день зі списку");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функція перевіряє чи є юзер у БД, якщо ні - то додає його у БД
     * @param tlgrUser
     * @return Користувача із БД
     * @throws SQLException
     */
    private UserEntity checkUser(User tlgrUser) throws SQLException {
        Collection dbUsers = Factory.getInstance().getUserDAO().getAllUsers();
        for (Object dbUser : dbUsers) {
            UserEntity userEntity = (UserEntity) dbUser;
            if (userEntity.getUserId().equals(tlgrUser.getId())) {
                return userEntity;
            }
        }

        UserEntity newUser = new UserEntity(tlgrUser.getId(), tlgrUser.getFirstName() + "\u00a0" + tlgrUser.getLastName(),
                tlgrUser.getUserName(), "", "", (byte) 0, new Date());
        Factory.getInstance().getUserDAO().addUser(newUser);
        return Factory.getInstance().getUserDAO().getUserById(tlgrUser.getId());
    }

    /**
     *
     * @param tlgrChat
     * @return
     * @throws SQLException
     */
    private ChatEntity checkChat(Chat tlgrChat, UserEntity userEntity) throws SQLException {
        Collection dbChats = Factory.getInstance().getChatDAO().getAllChats();
        for (Object dbChat : dbChats) {
            ChatEntity chatEntity = (ChatEntity) dbChat;
            if (chatEntity.getChatId().equals(tlgrChat.getId())) {
                return chatEntity;
            }
        }

        ChatEntity newChatEntity = new ChatEntity();
        newChatEntity.setChatId(tlgrChat.getId());
        newChatEntity.setUser(userEntity);

        if (tlgrChat.isGroupChat()) {
            newChatEntity.setIsGroupChat((byte) 1);
            newChatEntity.setIsUserChat((byte) 0);
        } else {
            newChatEntity.setIsGroupChat((byte) 0);
            newChatEntity.setIsUserChat((byte) 1);
        }

        Factory.getInstance().getChatDAO().addChat(newChatEntity);
        return Factory.getInstance().getChatDAO().getChatById(tlgrChat.getId());
    }

    private void logger(Message message, String answer) {
        UserEntity userEntity = null;
        ChatEntity chatEntity = null;
        MessageEntity messageEntity = new MessageEntity();
        AnswerEntity answerEntity = new AnswerEntity();

        try {
            userEntity = Factory.getInstance().getUserDAO().getUserById(message.getFrom().getId());
            chatEntity = checkChat(message.getChat(), userEntity);
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        messageEntity.setMessageId(message.getMessageId());
        messageEntity.setChat(chatEntity);
        messageEntity.setMessageText(message.getText());

        answerEntity.setAnswerText(answer);
        answerEntity.setMessage(messageEntity);

        try {
            Factory.getInstance().getMessageDAO().addMessage(messageEntity);
            Factory.getInstance().getAnswerDAO().addAnswer(answerEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        log.info("Message from " + userEntity.getName()
                + ". (id = " + userEntity.getUserId()
                + ", username =  @" + userEntity.getUsername()
                + ") \n Text - " + message.getText()
                + "\n Bot answer: \n Text - " + answer);
    }
}
