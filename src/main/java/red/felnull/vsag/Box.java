package red.felnull.vsag;

import com.sun.javafx.geom.Vec3d;

import java.util.Arrays;
import java.util.Optional;

public class Box extends AXBBox {
    private final double angle;
    private final RotationAxis axis;
    private final Vec3d originVec3d;

    public Box(double fromX, double fromY, double fromZ, double toX, double toY, double toZ, double angle, RotationAxis axis, double originX, double originY, double originZ) {
        super(fromX, fromY, fromZ, toX, toY, toZ);
        this.angle = angle;
        this.axis = axis;
        this.originVec3d = new Vec3d(originX, originY, originZ);
    }

    public RotationAxis getAxis() {
        return axis;
    }

    public Vec3d getOriginVec3d() {
        return originVec3d;
    }

    public double getAngle() {
        return angle;
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

        public double getV3DValue(Vec3d vec3d) {
            switch (this) {
                case X:
                    return vec3d.x;
                case Y:
                    return vec3d.y;
                case Z:
                    return vec3d.z;
            }
            return 0;
        }

        public RotationAxis rote() {
            switch (this) {
                case X:
                    return Y;
                case Y:
                    return Z;
                case Z:
                    return X;
            }
            return X;
        }

        public Vec3d setVD3(Vec3d v3d, double value) {
            switch (this) {
                case X:
                    v3d.x = value;
                    break;
                case Y:
                    v3d.y = value;
                    break;
                case Z:
                    v3d.z = value;
                    break;
            }
            return v3d;
        }
    }
}
