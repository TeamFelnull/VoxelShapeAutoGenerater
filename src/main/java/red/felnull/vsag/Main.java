package red.felnull.vsag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.javafx.geom.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final Gson gson = new GsonBuilder().create();
    private static String preFix = "";
    private static double onePixsel = 1d;

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
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

        List<AXBBox> axboxs = new ArrayList<>();

        boxs.forEach(n -> {
            if (n.getAngle() == 0d) {
                axboxs.add(n);
                return;
            }

            double nagasa = n.getAxis().getV3DValue(n.getToVec3d());
            double kakudo = n.getAngle();
            double teihen = nagasa * Math.cos(Math.toRadians(kakudo));
            int op = (int) (teihen / onePixsel);
            double finalOP = teihen / op;
            for (int i = 0; i < op; i++) {
                double minH = (finalOP * i) * Math.tan(Math.toRadians(kakudo));
                double maxH = (finalOP * (i + 1)) * Math.tan(Math.toRadians(kakudo));
                double averageH = (minH + maxH) / 2;
/*
                Vec3d fromv3 = new Vec3d(0, 0, 0);
                fromv3 = n.getAxis().setVD3(fromv3, finalOP * i);
                fromv3 = n.getAxis().rote().setVD3(fromv3, 0);
                fromv3 = n.getAxis().rote().rote().setVD3(fromv3, 0);

                Vec3d tov3 = new Vec3d(0, 0, 0);
                tov3 = n.getAxis().setVD3(tov3, finalOP * (i + 1));
                tov3 = n.getAxis().rote().setVD3(tov3, averageH);
                tov3 = n.getAxis().rote().rote().setVD3(tov3, 2);
                */
                axboxs.add(new AXBBox(finalOP * i, 0, 0d, finalOP * (i + 1), averageH, 2));
            }
        });

        String code = createCode(axboxs);
        System.out.println(code);
        copyClipBoard(code);
        LOGGER.info("Finish! " + (System.currentTimeMillis() - startTime) + "ms");
    }


    public static String createCode(List<AXBBox> boxs) {
        StringBuilder builder = new StringBuilder();
        AtomicInteger ct = new AtomicInteger();
        boxs.forEach(n -> {
            addLine(builder, ct.getAndIncrement(), n.getFromVec3d().x, n.getFromVec3d().y, n.getFromVec3d().z, n.getToVec3d().x, n.getToVec3d().y, n.getToVec3d().z);
        });
        builder.append("\n");

        StringBuilder sbNa = new StringBuilder();

        for (int i = 1; i < ct.get() - 1; i++) {
            sbNa.append(String.format("NORTH%s_PART", preFix) + i + ", ");
        }

        sbNa.append(String.format("NORTH%s_PART", preFix) + (ct.get() - 1) + "");

        builder.append(String.format("public static final VoxelShape NORTH%s_AXIS_AABB = Shapes.or(%s);\n", preFix, sbNa.toString()));
        builder.append(String.format("public static final VoxelShape SOUTH%s_AXIS_AABB = IKSGVoxelShapeUtil.rotateBoxDirection(NORTH%s_AXIS_AABB, Direction.SOUTH);\n", preFix, preFix));
        builder.append(String.format("public static final VoxelShape EAST%s_AXIS_AABB = IKSGVoxelShapeUtil.rotateBoxDirection(NORTH%s_AXIS_AABB, Direction.EAST);\n", preFix, preFix));
        builder.append(String.format("public static final VoxelShape WEST%s_AXIS_AABB = IKSGVoxelShapeUtil.rotateBoxDirection(NORTH%s_AXIS_AABB, Direction.WEST);\n", preFix, preFix));

        return builder.toString();
    }

    private static void addLine(StringBuilder sb, int cont, double x1, double y1, double z1, double x2, double y2, double z2) {
        sb.append(String.format("private static final VoxelShape NORTH%s_PART%s = IKSGVoxelShapeUtil.makeBox(%sd, %sd, %sd, %sd, %sd, %sd);\n", preFix, cont, x1, y1, z1, x2, y2, z2));
    }

    public static void copyClipBoard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, selection);
    }
}
