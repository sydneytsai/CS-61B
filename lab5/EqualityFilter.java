/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Sydney Tsai
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        i = input;
        c = colName;
        m = match;
    }

    @Override
    protected boolean keep() {
        if (_next.getValue(i.colNameToIndex(c)).equals(m)) {
            return true;
        }
        return false;
    }

    Table i;
    String c;
    String m;
}
