package md2html;

public class Md2Html {
    public static void main(String args[]) {
        if (args.length < 2) {
            System.out.println("Not enough arguments");
        } else {
            new Md2HtmlParser(new FileMd2Html(args[0], args[1])).parse();
        }
    }
}
