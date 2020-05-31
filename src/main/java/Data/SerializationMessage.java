package Data;


import Data.Root;
import com.google.gson.Gson;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.*;

public class SerializationMessage {

    public static boolean Serialization(Root File, String path) throws IOException {
        String json = new Gson().toJson(File);
        FileWriter fw = new FileWriter(Paths.get(path).toString());
        fw.write(json);
        fw.close();
        return true;
    }

    public static Root Deserialization (String path) throws IOException {
        String json = String.valueOf(Files.readAllLines(Paths.get(path)));
        Root File = new Gson().fromJson(json, Root.class);
        return File;
    }
}
