package converter.instruction;

import GUI.TabInput;
import converter.MeasureCollection;
import converter.MeasureGroup;
import converter.ScoreComponent;
import converter.measure.Measure;
import utility.Patterns;
import utility.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Repeat extends Instruction {
    public static String PATTERN = getPattern();
    public static int MAX_REPEATS = 20;

    private int repeatCount;
    private boolean startApplied = false;
    private boolean endApplied = false;
    Repeat(String content, int position, RelativePosition relativePosition) {
        super(content, position, relativePosition);
        Matcher matcher = Pattern.compile("[0-9]+").matcher(content);
        if (matcher.find())
            this.repeatCount = Integer.parseInt(matcher.group());
    }

    public <E extends ScoreComponent> void applyTo(E scoreComponent) {
        if (!validateSelf().isEmpty() || this.getHasBeenApplied() || this.repeatCount==0) {
            this.setHasBeenApplied(true);
            return;
        }

        if (scoreComponent instanceof MeasureCollection) {
            MeasureCollection measureCollection = (MeasureCollection) scoreComponent;
            Measure firstMeasure = null;
            Measure lastMeasure = null;
            for (MeasureGroup measureGroup : measureCollection.getMeasureGroupList()) {
                Range measureGroupRange = measureGroup.getRelativeRange();
                if (measureGroupRange==null) continue;
                if (!this.getRelativeRange().overlaps(measureGroupRange)) continue;
                for (Measure measure : measureGroup.getMeasureList()) {
                    Range measureRange = measure.getRelativeRange();
                    if (measureRange==null || !this.getRelativeRange().overlaps(measureRange)) continue;
                    if (firstMeasure==null && !this.startApplied)
                        firstMeasure = measure;
                    if (!this.endApplied)
                        lastMeasure = measure;
                }
            }
            if (firstMeasure!=null)
                this.startApplied = firstMeasure.setRepeat(this.repeatCount, RepeatType.START);
            if (lastMeasure!=null)
                this.endApplied = lastMeasure.setRepeat(this.repeatCount, RepeatType.END);
        }
        this.setHasBeenApplied(this.startApplied && this.endApplied);
    }

    public List<HashMap<String, String>> validate() {
        List<HashMap<String, String>> result = new ArrayList<>(super.validate());
        result.addAll(validateSelf());
        if ((!this.startApplied && this.endApplied)) {
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "This repeat was only partially applied for some reason.");
            response.put("positions", "["+this.getPosition()+","+(this.getPosition()+this.getContent().length())+"]");
            int priority = 1;
            response.put("priority", ""+priority);
            if (TabInput.ERROR_SENSITIVITY>=priority)
                result.add(response);
        }
        return result;
    }

    private List<HashMap<String, String>> validateSelf() {
        List<HashMap<String, String>> result = new ArrayList<>();
        if (!(this.getRelativeRange() instanceof Top)) {
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Repeats should only be applied to the top of measures.");
            response.put("positions", "["+this.getPosition()+","+(this.getPosition()+this.getContent().length())+"]");
            response.put("priority", "3");
            result.add(response);
        }
        if (this.repeatCount>MAX_REPEATS) {
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "only up to "+MAX_REPEATS+" repeats recommended.");
            response.put("positions", "["+this.getPosition()+","+(this.getPosition()+this.getContent().length())+"]");
            response.put("priority", "3");
            result.add(response);
        }
        return result;
    }

    private static String getPattern() {
        String times = "[xX]";
        String timesLong = "[Tt][Ii][Mm][Ee][Ss]";
        String count = "[0-9]{1,2}";
        String repeatTextPattern = "[Rr][Ee][Pp][Ee][Aa][Tt]" + "([ -]{0,7}|[ \t]{0,2})"  +  "(" +"("+times+count+")|("+ count+times +")|("+ count + "([ -]{0,7}|[ \t]{0,3})"  + timesLong + ")" + ")";
        //     | or sol or whitespace   optional space or -                     optional space or -     | or eol or whitespace
        return "("+"(((?<=\\|)|\\||^|"+ Patterns.WHITESPACE+")|(?<=\n))"  +        "[ -]*"       +   repeatTextPattern   +   "[ -]*"     +     "(($|\s)|\\|)" + ")";
    }
}
