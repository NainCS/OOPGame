/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section2_Lance;
import Section3_Edgar.GameObject;
import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author Lance Wilde
 */

public class CollisionDetector {

    public boolean checkCollision(Object obj1, Object obj2) {
        // try to get bounding rectangles from the objects
        Rectangle r1 = boundsFrom(obj1);
        Rectangle r2 = boundsFrom(obj2);
        if (r1 == null || r2 == null) return false;
        return r1.intersects(r2);
    }

    public boolean checkPlayerFireCollision(Object player, List<?> fires) {
        Rectangle pBounds = boundsFrom(player);
        if (pBounds == null) return false;
        for (Object f : fires) {
            Rectangle fb = boundsFrom(f);
            if (fb != null && pBounds.intersects(fb)) return true;
        }
        return false;
    }

    public boolean checkWaterFireCollision(Object water, List<?> fires) {
        Rectangle wBounds = boundsFrom(water);
        if (wBounds == null) return false;
        for (Object f : fires) {
            Rectangle fb = boundsFrom(f);
            if (fb != null && wBounds.intersects(fb)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPlayerTreeCollision(Object player, List<?> trees) {
        Rectangle pBounds = boundsFrom(player);
        if (pBounds == null) return false;
        for (Object t : trees) {
            Rectangle tb = boundsFrom(t);
            if (tb != null && pBounds.intersects(tb)) return true;
        }
        return false;
    }

    public boolean isWithinRange(Object obj1, Object obj2, int range) {
        Rectangle r1 = boundsFrom(obj1);
        Rectangle r2 = boundsFrom(obj2);
        if (r1 == null || r2 == null) return false;
        int dx = r1.x - r2.x;
        int dy = r1.y - r2.y;
        double distance = Math.sqrt(dx*dx + dy*dy);
        return distance <= range;
    }

    /**
     * Helper to extract bounds. Modify to match your GameObject/Player classes.
     */
    private Rectangle boundsFrom(Object o) {
        // WaterSpray uses its AOE range, not its small rectangle
        if (o instanceof Section3_Edgar.WaterSpray ws) {
            return ws.getSprayArea(); 
        }

        // All other objects use GameObject standard bounds
        if (o instanceof GameObject go) {
            return go.getBounds();
        }
        return null;
    }
}
