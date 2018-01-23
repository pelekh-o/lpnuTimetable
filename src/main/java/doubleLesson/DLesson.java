package doubleLesson;

import com.vdurmont.emoji.EmojiParser;

public class DLesson {
    private int count;              // номер за порядком
    private String title;           // назва предмету
    private String lecturer;        // викладач
    private String location;        // локація

    public DLesson() {
        count = 0;
        title = "";
        String time = "";
        String lecturer = "";
        String location = "";
    }

    public DLesson(int count, String title, String lecturer, String location) {
        this.count = count;
        this.title = title;
        this.lecturer = lecturer;
        this.location = location;
    }

    @Override
    public String toString() {
        return  "\n" + this.getCount() + "\t\t" + EmojiParser.parseToUnicode(":clock10: ") + this.getTime() +
                "\n" + EmojiParser.parseToUnicode(":blue_book: ") + this.getTitle() +
                "\n" + EmojiParser.parseToUnicode(":mortar_board: ")+ this.getLecturer() +
                "\n" + EmojiParser.parseToUnicode(":round_pushpin: ")+this.getLocation() + "\n";
    }

    public String getCount() {
        String countEmoji = "";

        switch (count) {
            case 1: countEmoji = ":one:";   break;
            case 2: countEmoji = ":two:";   break;
            case 3: countEmoji = ":three:"; break;
            case 4: countEmoji = ":four:";  break;
            case 5: countEmoji = ":five:";  break;
            case 6: countEmoji = ":six:";  break;
            case 7: countEmoji = ":seven:";  break;
            case 8: countEmoji = ":eight:";  break;
            default: countEmoji = ":hash:";
        }
        countEmoji = EmojiParser.parseToUnicode(countEmoji);
        return countEmoji;
    }

    public String getTime() {
        switch (count) {
            case 1: return Time.FIRST;
            case 2: return Time.SECOND;
            case 3: return Time.THIRD;
            case 4: return Time.FOURTH;
            case 5: return Time.FIFTH;
            case 6: return Time.SIXTH;
            case 7: return Time.SEVENTH;
            case 8: return Time.EIGHTH;
            default: return "";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getLecturer() {
        return lecturer;
    }

    public String getLocation() {
        return location;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
