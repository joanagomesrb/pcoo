package src;

import static java.lang.System.*;
import java.awt.Color;
import java.awt.Point;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.*;
import pt.ua.concurrent.*;

public class Controller{
    static Point actualThiefPosition;
    static Point lastThiefPosition;

    public Controller() {
        this.actualThiefPosition = new Point();
        this.lastThiefPosition = new Point();
    }

}
