/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Sydney Tsai
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        i = input;
        c1 = colName1;
        c2 = colName2;
    }

    @Override
    protected boolean keep() {
        if (_next.getValue(i.colNameToIndex(c1)).equals(_next.getValue(i.colNameToIndex(c2)))) {
            return true;
        }
        return false;
    }

    Table i;
    String c1;
    String c2;
}
