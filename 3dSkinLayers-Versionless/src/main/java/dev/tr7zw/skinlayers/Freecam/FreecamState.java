package dev.tr7zw.skinlayers.freecam;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public final class FreecamState {
    public static boolean active;
    public static Vec3d pos;
    public static float yaw, pitch;
    private static Vec3d vel = Vec3d.ZERO;
    public static double speed = 0.45;
    public static double friction = 0.9;

    public static void toggle(MinecraftClient mc) {
        active = !active;
        if (active) {
            var cam = mc.gameRenderer.getCamera();
            pos = cam.getPos();
            yaw = cam.getYaw();
            pitch = cam.getPitch();
            vel = Vec3d.ZERO;
        }
    }

    public static void tick(MinecraftClient mc) {
        if (!active || mc.isPaused() || mc.currentScreen != null) return;
        var in = mc.options;
        double ax = 0, ay = 0, az = 0;
        var rad = Math.toRadians(yaw);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        if (in.forwardKey.isPressed()) { ax += -sin; az += cos; }
        if (in.backKey.isPressed())    { ax += sin;  az += -cos; }
        if (in.leftKey.isPressed())    { ax += -cos; az += -sin; }
        if (in.rightKey.isPressed())   { ax += cos;  az += sin; }
        if (in.jumpKey.isPressed()) ay += 1;
        if (in.sneakKey.isPressed()) ay -= 1;

        var cam = mc.gameRenderer.getCamera();
        yaw = cam.getYaw();
        pitch = cam.getPitch();

        double len = Math.sqrt(ax*ax + ay*ay + az*az);
        if (len > 0) {
            ax /= len; ay /= len; az /= len;
            vel = vel.add(ax*speed, ay*speed, az*speed);
        }
        vel = vel.multiply(friction);
        pos = pos.add(vel);
    }
}
