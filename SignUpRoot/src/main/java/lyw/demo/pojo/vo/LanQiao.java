package lyw.demo.pojo.vo;

import lombok.Data;
import org.apache.poi.ss.formula.eval.*;
import org.apache.poi.ss.formula.functions.Fixed1ArgFunction;

@Data
public class LanQiao extends Fixed1ArgFunction {

    private String name;

    private String str;

    private String str1;

    private String str2;

    private String str3;

    private String str4;

    @Override
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        ValueEval arg = arg0;
        if (arg instanceof RefEval) {
            // always use the first sheet
            RefEval re = (RefEval)arg;
            arg = re.getInnerValueEval(re.getFirstSheetIndex());
        } else if (arg instanceof AreaEval) {
            // when the arg is an area, choose the top left cell
            arg = ((AreaEval) arg).getRelativeValue(0, 0);
        }

        if (arg instanceof StringEval) {
            // Text values are returned unmodified
            return arg;
        }

        if (arg instanceof ErrorEval) {
            // Error values also returned unmodified
            return arg;
        }
        // for all other argument types the result is empty string
        return StringEval.EMPTY_INSTANCE;
    }
}
