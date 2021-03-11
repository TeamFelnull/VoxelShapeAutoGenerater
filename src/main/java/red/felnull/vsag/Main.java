package red.felnull.vsag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final Gson gson = new GsonBuilder().create();

    public static void main(String[] args) throws FileNotFoundException {
        LOGGER.info("start generate");
        String targetPath = "D:\\minecraft\\testmodel\\TESTSANKAKU.json";
        if (targetPath.isEmpty()) {
            LOGGER.error("target json path is empty");
            return;
        }
        File targetFile = new File(targetPath);

        if (!targetFile.exists()) {
            LOGGER.error("target json is not fond");
            return;
        }

        JsonObject tjo = gson.fromJson(new FileReader(targetFile), JsonObject.class);
        JsonArray tja = tjo.getAsJsonArray("elements");

        List<Box> boxs = new ArrayList<>();
        tja.forEach(n -> {
            JsonObject jo = n.getAsJsonObject();
            JsonArray from = jo.getAsJsonArray("from");
            JsonArray to = jo.getAsJsonArray("to");
            JsonObject rotation = jo.getAsJsonObject("rotation");
            double angle = 0;
            Box.RotationAxis axis = Box.RotationAxis.X;
            double originX = 0;
            double originY = 0;
            double originZ = 0;
            if (rotation != null) {
                angle = rotation.get("angle").getAsDouble();
                axis = Box.RotationAxis.getByString(rotation.get("axis").getAsString());
                JsonArray origin = rotation.getAsJsonArray("origin");
                originX = origin.get(0).getAsDouble();
                originY = origin.get(1).getAsDouble();
                originZ = origin.get(2).getAsDouble();
            }
            boxs.add(new Box(from.get(0).getAsDouble(), from.get(1).getAsDouble(), from.get(2).getAsDouble(), to.get(0).getAsDouble(), to.get(1).getAsDouble(), to.get(2).getAsDouble(), angle, axis, originX, originY, originZ));
        });
        System.out.println(boxs);
    }
}
