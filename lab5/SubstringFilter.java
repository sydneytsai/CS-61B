/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _input = input;
        _colName = colName;
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        if (_next.getValue(_input.colNameToIndex(_colName)).contains(_subStr)) {
            return true;
        }
        return false;
    }

    Table _input;
    String _colName;
    String _subStr;
}
