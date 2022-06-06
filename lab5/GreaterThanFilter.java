/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Sydney Tsai
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        i = input;
        c = colName;
        r = ref;
    }

    @Override
    protected boolean keep() {
        if (_next.getValue(i.colNameToIndex(c)).length() > r.length()) {
            return true;
        }
        return false;
    }

    Table i;
    String c;
    String r;
}
