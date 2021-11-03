package converter.instruction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import converter.ScoreComponent;
import converter.TabRow;
import converter.TabSection;
import converter.measure.TabMeasure;
import utility.Range;
import utility.Settings;
import utility.ValidationError;

public class TimeSignature extends Instruction {
    public static String PATTERN = "(?<=\\s|\n|\r|^)[0-9][0-9]?\\/[0-9][0-9]?(?=\\s|\n|\r|$)";
    private int beatType;
    private int beatCount;
    TimeSignature(String content, int position, RelativePosition relativePosition) {
        super(content, position, relativePosition);
        Matcher beatCountMatcher = Pattern.compile("[0-9]+(?=[/\\\\])").matcher(content);
        Matcher beatTypeMatcher = Pattern.compile("(?<=[/\\\\])[0-9]+").matcher(content);
        if (beatCountMatcher.find())
            this.beatCount = Integer.parseInt(beatCountMatcher.group());
        if (beatTypeMatcher.find())
            this.beatType = Integer.parseInt(beatTypeMatcher.group());
    }

    @Override
    public <E extends ScoreComponent> void applyTo(E scoreComponent) {
        if (!validate().isEmpty() || this.getHasBeenApplied()) {
            this.setHasBeenApplied(true);
            return;
        }

        if (scoreComponent instanceof TabSection) {
            TabSection measureCollection = (TabSection) scoreComponent;
            for (TabRow measureGroup : measureCollection.getTabRowList()) {
                Range measureGroupRange = measureGroup.getRelativeRange();
                if (measureGroupRange==null) continue;
                if (!measureGroupRange.contains(this.getRelativeRange())) continue;
                for (TabMeasure measure : measureGroup.getMeasureList()) {
                    Range measureRange = measure.getRelativeRange();
                    if (measureRange==null || !measureRange.contains(this.getRelativeRange())) continue;
                    boolean itWorked = measure.setTimeSignature(this.beatCount, this.beatType);
                    this.setHasBeenApplied(itWorked);
                    if (itWorked) measure.changesTimeSignature = true;
                }
            }
        }
    }

    public List<ValidationError> validate() {
        if (!(this.getRelativeRange() instanceof Top)) {
            addError(
                    "Time signatures should only be applied to the top of measures.",
                    3,
                    getRanges());
        }
        else
        if (beatCount<=0 || beatType<=0) {
            addError(
                    "Invalid beat " + (this.beatCount<=0?"count" : "type") + " value.",
                    2,
                    getRanges());
        }else if (!isValid(this.beatCount, this.beatType)) {
            addError(
                    "Unsupported time signature.",
                    2,
                    getRanges());
        }
        return errors;
    }

    public static boolean isValid(int beatCount, int beatType) {
        return switch (beatCount + "/" + beatType) {
            case "2/4", "2/2", "3/8", "3/4", "3/2", "4/8", "4/4", "4/2", "6/8", "6/4", "9/8", "9/4", "12/8", "12/4" -> true;
            default -> false;
        };
    }
}
