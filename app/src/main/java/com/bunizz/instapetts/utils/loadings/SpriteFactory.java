package com.bunizz.instapetts.utils.loadings;

import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.utils.loadings.style.ChasingDots;
import com.bunizz.instapetts.utils.loadings.style.Circle;
import com.bunizz.instapetts.utils.loadings.style.CubeGrid;
import com.bunizz.instapetts.utils.loadings.style.DoubleBounce;
import com.bunizz.instapetts.utils.loadings.style.FadingCircle;
import com.bunizz.instapetts.utils.loadings.style.FoldingCube;
import com.bunizz.instapetts.utils.loadings.style.MultiplePulse;
import com.bunizz.instapetts.utils.loadings.style.MultiplePulseRing;
import com.bunizz.instapetts.utils.loadings.style.Pulse;
import com.bunizz.instapetts.utils.loadings.style.PulseRing;
import com.bunizz.instapetts.utils.loadings.style.RotatingCircle;
import com.bunizz.instapetts.utils.loadings.style.RotatingPlane;
import com.bunizz.instapetts.utils.loadings.style.ThreeBounce;
import com.bunizz.instapetts.utils.loadings.style.WanderingCubes;
import com.bunizz.instapetts.utils.loadings.style.Wave;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
