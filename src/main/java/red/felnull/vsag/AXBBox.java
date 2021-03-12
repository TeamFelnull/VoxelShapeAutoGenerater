package red.felnull.vsag;

import com.sun.javafx.geom.Vec3d;

public class AXBBox {
    private final Vec3d fromVec3d;
    private final Vec3d toVec3d;

    public AXBBox(Vec3d from, Vec3d to) {
        this.fromVec3d = from;
        this.toVec3d = to;
    }

    public AXBBox(double fromX, double fromY, double fromZ, double toX, double toY, double toZ) {
        this.fromVec3d = new Vec3d(fromX, fromY, fromZ);
        this.toVec3d = new Vec3d(toX, toY, toZ);
    }

    public Vec3d getFromVec3d() {
        return fromVec3d;
    }

    public Vec3d getToVec3d() {
        return toVec3d;
    }
}
