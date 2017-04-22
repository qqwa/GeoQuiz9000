package no.ntnu.tdt4240.geoquiz9000.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import no.ntnu.tdt4240.geoquiz9000.R;

/**
 * From https://github.com/davemorrissey/subsampling-scale-image-view/blob/master/sample/src/com/davemorrissey/labs/subscaleview/sample/extension/views/PinView.java
 */
public class PinView extends SubsamplingScaleImageView {

    public enum Colors {
        GREEN, RED
    }

    private Map<PointF, Colors> sPoints = new HashMap<>();
    private Map<PointF, Bitmap> pins = new HashMap<>();
    private PointF sPin;
    private Bitmap clearPin;

    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    /**
     * Set one pin on the map.
     *
     * @param sPin point of the pin
     */
    public void setPin(PointF sPin) {
        this.sPin = sPin;
        if (sPoints == null) sPoints = new HashMap<>();
        if (pins == null) pins = new HashMap<>();

        sPoints.put(sPin, Colors.RED);
        initialise();
        invalidate();
    }

    /**
     * Set one solution pin on the map.
     *
     * @param sPin point of the pin
     */
    public void setSolutionPin(PointF sPin) {
        this.sPin = sPin;
        if (sPoints == null) sPoints = new HashMap<>();
        if (pins == null) pins = new HashMap<>();

        sPoints.put(sPin, Colors.GREEN);
        initialise();
        invalidate();
    }

    /**
     * Set a map of pins on the map.
     *
     * @param sPoints Map with location and color information
     */
    public void setPins(Map<PointF, Colors> sPoints) {
        if (sPoints == null) sPoints = new HashMap<>();
        if (pins == null) pins = new HashMap<>();

        this.sPoints = sPoints;
        initialise();
        invalidate();
    }

    public PointF getPin() {
        return sPin;
    }

    /**
     * Reset the map from pins.
     */
    public void clear() {
        clearPin = null;
        sPin = null;
        sPoints = null;
        pins = null;

        initialise();
        invalidate();
    }

    private void initialise() {
        if (sPoints != null && sPoints.size() != 0) {
            Set<PointF> pointFs = sPoints.keySet();

            for (PointF p : pointFs) {
                Bitmap pin;
                float density = getResources().getDisplayMetrics().densityDpi;

                Colors c = sPoints.get(p);

                switch (c) {
                    case RED:
                        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_place_red);
                        break;
                    case GREEN:
                        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_name);
                        break;
                    default:
                        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_place_red);
                }

                float w = (density / 420f) * pin.getWidth();
                float h = (density / 420f) * pin.getHeight();
                pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);

                pins.put(p, pin);
            }
        }
        else if (sPin == null) {
            float density = getResources().getDisplayMetrics().densityDpi;
            clearPin = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_place_red);
            float w = (density / 420f) * clearPin.getWidth();
            float h = (density / 420f) * clearPin.getHeight();
            clearPin = Bitmap.createScaledBitmap(clearPin, (int)w, (int)h, true);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (pins != null) {
            Set<PointF> pointFs = sPoints.keySet();

            for (PointF p : pointFs) {
                Bitmap pin = pins.get(p);

                PointF vPin = sourceToViewCoord(p);
                float vX = vPin.x - (pin.getWidth() / 2);
                float vY = vPin.y - pin.getHeight();
                canvas.drawBitmap(pin, vX, vY, paint);
            }

        }
    }

}
