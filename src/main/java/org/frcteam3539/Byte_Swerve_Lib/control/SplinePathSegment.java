package org.frcteam3539.Byte_Swerve_Lib.control;

import org.frcteam3539.Byte_Swerve_Lib.math.spline.Spline;

public final class SplinePathSegment extends PathSegment {
    private static final double LENGTH_SAMPLE_STEP = 1.0e-4;

    private final Spline spline;

    private transient double length = Double.NaN;

    

    public SplinePathSegment(Spline spline) {
        this.spline = spline;
    }

    @Override
    public State calculate(double distance) {
        double t = distance / getLength();

        return new State(
                spline.getPoint(t),
                spline.getHeading(t),
                spline.getCurvature(t)
        );
    }

    @Override
    public double getRadius()
    {
        return 0.0;
    }

    @Override
    public double getLength() {
        if (!Double.isFinite(length)) {
            length = 0.0;
            var p0 = spline.getPoint(0.0);
            for (double t = LENGTH_SAMPLE_STEP; t <= 1.0; t += LENGTH_SAMPLE_STEP) {
                var  p1 = spline.getPoint(t);
                length += p1.minus(p0).getNorm();

                p0 = p1;
            }
        }

        return length;
    }

    public Spline getSpline() {
        return spline;
    }
}
