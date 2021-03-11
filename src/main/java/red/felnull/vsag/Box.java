package red.felnull.vsag;

import com.sun.javafx.geom.Vec3d;

import java.util.Arrays;
import java.util.Optional;

public class Box {
    private final Vec3d fromVec3d;
    private final Vec3d toVec3d;
    private final double angle;
    private final RotationAxis axis;
    private final Vec3d originVec3d;

    public Box(double fromX, double fromY, double fromZ, double toX, double toY, double toZ, double angle, RotationAxis axis, double originX, double originY, double originZ) {
        this.angle = angle;
        this.fromVec3d = new Vec3d(fromX, fromY, fromZ);
        this.toVec3d = new Vec3d(toX, toY, toZ);
        this.axis = axis;
        this.originVec3d = new Vec3d(originX, originY, originZ);
    }

    public RotationAxis getAxis() {
        return axis;
    }

    public Vec3d getFromVec3d() {
        return fromVec3d;
    }

    public Vec3d getToVec3d() {
        return toVec3d;
    }

    public Vec3d getOriginVec3d() {
        return originVec3d;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public String toString() {
        return "Box{" +
                "fromVec3d=" + fromVec3d +
                ", toVec3d=" + toVec3d +
                ", angle=" + angle +
                ", axis=" + axis.getName() +
                ", originVec3d=" + originVec3d +
                '}';
    }

    public static enum RotationAxis {
        X("x"), Y("y"), Z("z");

        private final String name;

        private RotationAxis(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static RotationAxis getByString(String str) {
            Optional<RotationAxis> ax = Arrays.stream(values()).filter(n -> n.getName().equals(str)).findAny();
            return ax.orElse(X);
        }
    }
}
