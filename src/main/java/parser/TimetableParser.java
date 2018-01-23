package parser;

import com.vdurmont.emoji.EmojiParser;
import doubleLesson.DLesson;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;


public class TimetableParser {
    public static final String URL = "http://www.lp.edu.ua/rozklad-dlya-studentiv"; // Посилання на стор. розкладу

    private Logger log = Logger.getLogger(getClass().getName());

    private DLesson dLesson = null;
    private ArrayList<DLesson> pairsArr = new ArrayList<>();

    private final String semestr = "1"; // параметр get запиту. 0 для першого семестру, 1 - для другого
    private final String semest_part = "1"; // параметр get запиту. 1 для першої половини семестру, 2 - для другої
    // TODO присвоєння значень змінних semestr та semest_part залежно від місяця(або іншого параметру?????, за яким вони мають змінюватись)

    /**
     * Функція завантажує сторінку із розкладом для групи із назвою instTitle, парсить її,
     * та повертає розклад на день
     * @param day день тижня
     * @param instTitle назва інституту
     * @param groupTitle назва групи
     * @return Список пар для групи у даний дань
     */
    public String parseDLesson(String day, String instTitle, String groupTitle) {
        Document doc = null;

        try {
            doc = getPage(instTitle, groupTitle);
        }catch (IOException e) { log.warn(e.getMessage()); }

        DayOfWeek dayOfWeek = getDay(day);  // форматувати текстове представлення дня тижня у java.time.DayOfWeek;
        Elements rows = doc.select("table").get(0).select("tr"); //вибрати кожен рядок із першої таблиці
        rows.remove(0); // видалити рядок із заголовками таблиці

        String dLessons = getDayLessons(dayOfWeek, rows); // отримати список пар у вибраний день
        if (dLessons.isEmpty()) return "\nСхоже, пар немає";
        return dLessons;
    }

    /**
     * Вибирає день тижня, залежно від параметра day
     * @param day назва дня, отримана від користувача
     * @return день тижня
     */
    private DayOfWeek getDay(String day){
        day = day.toLowerCase();
        if (day.length() != 0) {
            if (day.equals("пн")) return DayOfWeek.MONDAY;
            if (day.equals("вт")) return DayOfWeek.TUESDAY;
            if (day.equals("ср")) return DayOfWeek.WEDNESDAY;
            if (day.equals("чт")) return DayOfWeek.THURSDAY;
            if (day.equals("пт")) return DayOfWeek.FRIDAY;
        }
        return LocalDate.now().getDayOfWeek();
    }

    /**
     * Функція повертає ідентифікатор інституту із списку форми на сторінці розкладу.
     * Ідентифікатор використовується для get запиту сторінки розкладу
     * @param instTitle назва інституту
     * @return отриманий із значення форми ідентифікатор інституту
     */
    private String getInstituteID(String instTitle) throws NullPointerException{
        String instituteID = "";
        Document document = null;
        try {
            document = Jsoup.connect(URL)
                    .data("inst", "")
                    .data("group", "")
                    .data("semestr",semestr)
                    .data("semest_part",semest_part)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get();
        } catch (IOException e) { log.warn(e.getMessage()); }

        Elements institutes = document.select("form.rozklad select[name=inst] option");  // список інститутів

        for (Element inst: institutes){
            if (inst.text().equals(instTitle.toUpperCase())){
                return inst.val();
            }
        }
        return instituteID;
    }

    /**
     * Функція повертає ідентифікатор групи із списку форми на сторінці розкладу
     * із завантаженого інституту.
     * Ідентифікатор використовується для get запиту сторінки розкладу
     * @param groupTitle назва групи
     * @param instID ID інституту
     * @return отриманий із значення форми ідентифікатор групи
     */
    private String getGroupID(String groupTitle, String instID) throws  NullPointerException {
        String groupID = "";
        Document document = null;
        try {
            document = Jsoup.connect(URL)
                    .data("inst", instID)
                    .data("group", "")
                    .data("semestr",semestr)
                    .data("semest_part",semest_part)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get();
        } catch (IOException e) { log.warn(e.getMessage()); }

        Elements groups = document.select("form.rozklad select[name=group] option");  // список груп

        for (Element group: groups){
            if (group.text().equals(groupTitle.toUpperCase())){
                return group.val();
            }
        }
        return groupID;
    }

    /**
     *
     * @param instTitle назва інституту
     * @param groupTitle назва групи
     * @return запитану html сторінку у типі Document
     * @throws IOException
     */
    private Document getPage(String instTitle, String groupTitle) throws IOException{
        String instID = getInstituteID(instTitle);
        String groupID = getGroupID(groupTitle, instID);
        return Jsoup.connect(URL)
                .data("inst", instID)
                .data("group", groupID)
                .data("semestr",semestr)
                .data("semest_part",semest_part)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com").get();
    }

    private String getDayLessons(DayOfWeek day, Elements rows) {
        boolean flag = false;   // прапорець для поточного дня

        for (Element currentRow: rows) {
            if (currentRow.text().contains("Пн") && day == DayOfWeek.MONDAY) {
                flag = true;
            }

            if (currentRow.text().contains("Вт") || currentRow.text().contains("Ср") ||
                    currentRow.text().contains("Чт") || currentRow.text().contains("Пт")) {
                flag = false;
            }

            if (currentRow.text().contains("Вт") && day == DayOfWeek.TUESDAY) {
                flag = true;
            }

            if (currentRow.text().contains("Ср") || currentRow.text().contains("Чт") || currentRow.text().contains("Пт")) {
                flag = false;
            }

            if (currentRow.text().contains("Ср") && day == DayOfWeek.WEDNESDAY) {
                flag = true;
            }

            if (currentRow.text().contains("Чт") || currentRow.text().contains("Пт")) {
                flag = false;
            }

            if (currentRow.text().contains("Чт") && day == DayOfWeek.THURSDAY) {
                flag = true;
            }

            if (currentRow.text().contains("Пт")) {
                flag = false;
            }

            if (currentRow.text().contains("Пт" ) && day == DayOfWeek.FRIDAY) {
                flag = true;
            }

            if (flag) {
                Elements sTable = currentRow.select("table");
                String pairNumber = currentRow.select("td.leftcell").text();

                // якщо пару поділено на підгрупи
                if (currentRow.getElementsByAttributeValue("width", "245px").hasText()) {
                    Elements subj1 = sTable.select("tr.color > td:nth-child(1)");   // перша підгрупа
                    Elements subj2 = sTable.select("tr.color > td:nth-child(2)");   // друга підгрупа

                    String title1 = subj1.select("b").text();
                    String title2 = subj2.select("b").text();

                    String lecturer1 = subj1.select("i").text();
                    String lecturer2 = subj2.select("i").text();

                    String location1 = getLocation(subj1.select("div").html());
                    String location2 = getLocation(subj2.select("div").html());

                    if (title1.length() != 0 && !pairNumber.isEmpty()) {
                        // костиль, щоб не показувати підгрупи у випадках,
                        // коли одна пара є для цілої групи, а інша пара -  тільки для однієї половини,
                        // і які чергуються по чисельнику/знаменнику
                        if (!location1.contains("лекція") && !location1.contains("прак"))
                            title1 = title1 + EmojiParser.parseToUnicode("\n:school_satchel: 1-ша підгрупа");

                        addToDLessonArray(title1, pairNumber, lecturer1, location1);
                    }

                    if (title2.length() != 0 && !pairNumber.isEmpty()) {
                        if (!location2.contains("лекція") && !location2.contains("прак"))  // той самий костиль
                            title2 = title2 + EmojiParser.parseToUnicode("\n:school_satchel: 2-га підгрупа");

                        addToDLessonArray(title2, pairNumber, lecturer2, location2);
                    }

                }
                else {  // пара не розділена на підгрупи
                    String title = currentRow.select(".color b").text();
                    String lecturer = currentRow.select(".color i").text();
                    String location = getLocation(currentRow.select(".color div").html());

                    if (title.length() != 0 && !pairNumber.isEmpty()) {
                        addToDLessonArray(title, pairNumber, lecturer, location);
                    }
                }
            }
        }

        StringBuffer ttDay = new StringBuffer();
        for (DLesson p : pairsArr) {
            ttDay.append(p);
        }
        return ttDay.toString();
    }

    /**
     * Шукає у частині html рядок з інформацією про лакацію та тип пари
     * @param tr html рядока таблиці певної пари
     * @return локацію та тип пари(третій рядочок у комірці на сайті)
     */
    private String getLocation(String tr) {
        if (tr.length() != 0) {
            String l;
            for (int j = 0; j < tr.length(); j++) {
                l = tr.substring(j, j + 4);
                if (l.contains("</i>")) {
                    tr = tr.substring(j + 9);
                    tr = tr.replace("&nbsp;", " ");
                    break;
                }
            }
            return tr;
        }
        return null;
    }

    /**
     * Функція додає у список пар ще одну
     * @param title
     * @param pairNumber
     * @param lecturer
     * @param location
     */
    private void addToDLessonArray(String title, String pairNumber, String lecturer, String location) {
        int count = 0;
        if (pairNumber.length() > 1) {
            pairNumber = pairNumber.substring(pairNumber.length() - 1);
        }
        try {
            count = Integer.parseInt(pairNumber);
        } catch (Exception e) {
            log.warn(e.getMessage());
            count = 0;
        }

        dLesson = new DLesson(count, title, lecturer, location);
        pairsArr.add(dLesson);
    }
}
