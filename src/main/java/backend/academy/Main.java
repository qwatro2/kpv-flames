package backend.academy;

import backend.academy.fractals.app.App;
import backend.academy.fractals.app.FractalsApp;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        App app = new FractalsApp(System.out, System.err);
        app.run(args);
    }
}
