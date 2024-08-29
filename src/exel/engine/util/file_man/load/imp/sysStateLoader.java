package exel.engine.util.file_man.load.imp;

import exel.engine.spreadsheet.api.Sheet;
import java.io.*;

public class sysStateLoader {
    public static Sheet loadSheet(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Sheet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
