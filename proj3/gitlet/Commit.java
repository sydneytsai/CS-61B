package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Commit class.
 * @author Leo Huang
*/
public class Commit implements Serializable {
    /** id. */
    private String _id;
    /** message. */
    private String _message;
    /** date. */
    private String _date;
    /** parent id. */
    private String _parentID;
    /** merge parent id. */
    private String _mergeParent;
    /** file to package ids. */
    private HashMap<String, String> fileToID;

    /**
     * Commit constructor.
     * @param message message
     * @param parent1 parent 1
     * @param parent2 merged parent
     * @param packs hashmap of packages
     */
    public Commit(String message, String parent1, String parent2,
                  HashMap<String, String> packs) {
        _message = message;
        _parentID = parent1;
        _mergeParent = parent2;
        Date d;
        if (message.equals("initial commit")) {
            d = new Date(0);
        } else {
            d = new Date(System.currentTimeMillis());
        }
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat dateForm = new SimpleDateFormat(pattern);
        _date = dateForm.format(d);
        fileToID = packs;
        _id = Utils.sha1((Object) Utils.serialize(this));
    }

    /**
     *
     * @return id
     */
    public String getID() {
        return _id;
    }

    /**
     *
     * @return message
     */
    public String getMessage() {
        return _message;
    }

    /**
     *
     * @return date
     */
    public String getTimestamp() {
        return _date;
    }

    /**
     *
     * @param fileName file name
     * @return location
     */
    public String getLocation(String fileName) {
        return fileToID.get(fileName);
    }

    /**
     *
     * @return files
     */
    public Set<String> getFiles() {
        if (fileToID != null) {
            return fileToID.keySet();
        }
        return null;
    }

    /**
     *
     * @return parent 1
     */
    public String getParent() {
        return _parentID;
    }

    /**
     *
     * @return parent 2
     */
    public String getMergeParent() {
        return _mergeParent;
    }

    /**
     *
     * @return file locations
     */
    public HashMap<String, String> getFileLocations() {
        return fileToID;
    }

    /**
     *
     * @param fileName file name
     * @return if contains file
     */
    public boolean containsFile(String fileName) {
        return (fileToID != null) && fileToID.containsKey(fileName);
    }

    /**
     *
     * @param packID package id
     * @return if contains id
     */
    public boolean containsID(String packID) {
        return (fileToID != null) && fileToID.containsValue(packID);
    }

    /**
     * log.
     */
    public void log() {
        String ret = "===\n";
        ret += "commit " + _id + "\n";
        if (_mergeParent != null) {
            ret += "Merge: ";
            ret += _parentID.substring(0, 7) + " ";
            ret += _mergeParent.substring(0, 7) + "\n";
        }
        ret += "Date: " + _date + "\n";
        ret += _message;
        if (_parentID != null) {
            ret += "\n";
        }
        System.out.println(ret);
    }
}
