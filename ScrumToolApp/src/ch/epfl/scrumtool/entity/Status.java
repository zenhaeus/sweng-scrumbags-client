package ch.epfl.scrumtool.entity;

import java.io.Serializable;

import ch.epfl.scrumtool.R;

/**
 * @author ketsio
 */
public enum Status implements Serializable {

    READY_FOR_SPRINT(R.color.Chartreuse, "Ready for sprint"), IN_SPRINT(
            R.color.Salmon, "In sprint"), READY_FOR_ESTIMATION(R.color.Lime,
            "Ready for estimation"), FINISHED(R.color.Cyan, "Finished");

    private int colorRef;
    private String value;

    Status(int colorRef, String stringValue) {
        this.colorRef = colorRef;
        this.value = stringValue;
    }

    /**Check if the status is valid
     * @return
     */
    public boolean isAValidStatus() {
        return this.equals(READY_FOR_SPRINT) || this.equals(IN_SPRINT)
                || this.equals(READY_FOR_ESTIMATION) || this.equals(FINISHED);
    }

    /**
     * @return the color. It is <b>NOT</b> a color, but rather a R reference to
     *         one!
     */
    public int getColorRef() {
        return colorRef;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
