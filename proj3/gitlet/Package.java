package gitlet;

import java.io.Serializable;

/**
 * Package class.
 * @author Leo Huang
 */
public class Package implements Serializable {
    /** id. */
    private String _id;
    /** file name. */
    private String _name;
    /** file contents. */
    private byte[] _contents;

    /**
     * Package constructor.
     * @param fileName file name
     * @param fileContents file contents
     */
    public Package(String fileName, byte[] fileContents) {
        _name = fileName;
        _contents = fileContents;
        _id = Utils.sha1(_name, _contents);
    }

    /**
     * get the id.
     * @return id.
     */
    public String getID() {
        return _id;
    }

    /**
     * get the name.
     * @return file name.
     */
    public String getName() {
        return _name;
    }

    /**
     * get the contents.
     * @return contents
     */
    public byte[] getContents() {
        return _contents;
    }
}
