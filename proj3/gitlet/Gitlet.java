package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Gitlet class.
 * @author Leo Huang
 */
public class Gitlet implements Serializable {

    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** Directory. */
    static final File REPO = new File(".gitlet");
    /** Staging directory (for adding), contains file names.*/
    static final File STAGE = Utils.join(".gitlet", "stage");
    /** Packages directory.*/
    static final File PACKAGES = Utils.join(".gitlet", "Packages");
    /** Commits directory.*/
    static final File COMMITS = Utils.join(".gitlet", "commits");
    /** Branches directory.*/
    static final File BRANCHES = Utils.join(".gitlet", "branches");
    /** Staging list (for removing)).*/
    private ArrayList<String> rmStage = new ArrayList<String>();
    /** Head. */
    private String head;
    /** Current branch. */
    private String currBranch;
    /** Initial commit ID. */
    private String initialID;


    /**
     * Gitlet constructor.
     */
    public Gitlet() {
        if (REPO.exists()) {
            System.out.println("A Gitlet version-control system already "
                    + "exists in the current directory.");
            return;
        }
        REPO.mkdir();
        STAGE.mkdir();
        PACKAGES.mkdir();
        COMMITS.mkdir();
        BRANCHES.mkdir();
        Commit initial = new Commit("initial commit", null,
                null, new HashMap<>());
        head = initial.getID();
        initialID = initial.getID();
        File f0 = new File(COMMITS, initial.getID());
        Utils.writeObject(f0, initial);
        File f1 = new File(BRANCHES, "master");
        Utils.writeContents(f1, initial.getID());
        currBranch = "master";
    }


    /**
     *
     * @param name file name
     */
    public void add(String name) {
        File f0 = new File(CWD, name);
        if (!f0.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
            return;
        }
        if (rmStage.contains(name)) {
            rmStage.remove(name);
        }
        String hash = Utils.sha1(name, Utils.readContents(f0));
        Commit curr = getHead();
        if (curr.containsID(hash)) {
            File f = new File(STAGE, name);
            if (f.exists()) {
                f.delete();
            }
            return;
        }
        File f1 = new File(STAGE, name);
        Utils.writeContents(f1, Utils.readContents(f0));
    }

    /**
     *
     * @param name file name
     */
    public void rm(String name) {
        boolean rem = false;
        File f0 = new File(STAGE, name);
        if (f0.exists()) {
            rem = true;
            f0.delete();
        }
        Commit curr = getHead();
        if (curr.containsFile(name)) {
            rem = true;
            rmStage.add(name);
            File f = new File(name);
            if (f.exists()) {
                f.delete();
            }
        }
        if (!rem) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
            return;
        }
    }

    /**
     *
     * @param message message
     */
    public void commit(String message) {
        List<String> addStage = Utils.plainFilenamesIn(STAGE);
        if ((addStage.size() == 0) && rmStage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
            return;
        }
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
            return;
        }
        Commit h = getHead();
        HashMap<String, String> fileToLoc = h.getFileLocations();
        for (String fileName : addStage) {
            File f0 = new File(STAGE, fileName);
            Package b = new Package(fileName, Utils.readContents(f0));
            File f1 = new File(PACKAGES, b.getID());
            if (!f1.exists()) {
                Utils.writeObject(f1, b);
            }
            if (fileToLoc.containsKey(fileName)) {
                fileToLoc.replace(fileName, b.getID());
            } else {
                fileToLoc.put(fileName, b.getID());
            }
            f0.delete();
        }
        for (String fileName : rmStage) {
            fileToLoc.remove(fileName);
        }
        rmStage.clear();
        Commit c = new Commit(message, head, null, fileToLoc);
        File f = new File(COMMITS, c.getID());
        Utils.writeObject(f, c);
        head = c.getID();
        File b = new File(BRANCHES, currBranch);
        Utils.writeContents(b, head);
    }

    /**
     *
     */
    public void log() {
        Commit c = getHead();
        c.log();
        while (c.getParent() != null) {
            File f = new File(COMMITS, c.getParent());
            c = Utils.readObject(f, Commit.class);
            c.log();
        }
    }

    /**
     *
     */
    public void globalLog() {
        List<String> commitList = Utils.plainFilenamesIn(COMMITS);
        for (String name : commitList) {
            if (!name.equals(initialID)) {
                Commit c = getCommit(name);
                c.log();
            }
        }
        Commit c = getCommit(initialID);
        c.log();
    }

    /**
     *
     * @param message message
     */
    public void find(String message) {
        int found = 0;
        for (String name : Utils.plainFilenamesIn(COMMITS)) {
            Commit c = getCommit(name);
            if (c.getMessage().equals(message)) {
                System.out.println(c.getID());
                found += 1;
            }
        }
        if (found == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
            return;
        }
    }

    /**
     *
     */
    public void status() {
        String ret = "=== Branches ===\n";
        Commit h = getHead();
        for (String name : Utils.plainFilenamesIn(BRANCHES)) {
            File f = new File(BRANCHES, name);
            String brH = Utils.readContentsAsString(f);
            if (brH.equals(head)) {
                ret += "*";
            }
            ret += name + "\n";
        }
        ret += "\n=== Staged Files ===\n";
        for (String name : Utils.plainFilenamesIn(STAGE)) {
            ret += name + "\n";
        }
        ret += "\n=== Removed Files ===\n";
        for (String name : rmStage) {
            ret += name + "\n";
        }
        ret += "\n=== Modifications Not Staged For Commit ===\n";
        ret += "\n=== Untracked Files ===";
        System.out.println(ret);
    }

    /**
     *
     * @param name name
     */
    public void checkOut(String name) {
        Commit c = getHead();
        String packageID = c.getLocation(name);
        if (packageID == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
            return;
        }
        File to = new File(name);
        Package b = getPackage(packageID);
        Utils.writeContents(to, b.getContents());
    }

    /**
     *
     * @param id id
     * @param name name
     */
    public void checkOut(String id, String name) {
        int len = id.length();
        for (String commitID : Utils.plainFilenamesIn(COMMITS)) {
            if (commitID.substring(0, len).equals(id)) {
                Commit c = getCommit(commitID);
                String packageID = c.getLocation(name);
                if (packageID == null) {
                    System.out.println("File does not exist in that commit.");
                    System.exit(0);
                    return;
                }
                File to = new File(name);
                Package b = getPackage(packageID);
                Utils.writeContents(to, b.getContents());
                return;
            }
        }
        System.out.println("No commit with that id exists.");
        System.exit(0);
    }

    /**
     *
     * @param branchName branch name
     */
    public void branchCheckOut(String branchName) {
        File br = new File(BRANCHES, branchName);
        if (!br.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
            return;
        }
        String brHead = Utils.readContentsAsString(br);
        if (branchName.equals(currBranch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
            return;
        }
        Commit h = getHead();
        Commit bH = getCommit(brHead);
        Set<String> files = bH.getFiles();
        for (String name : files) {
            File f = new File(name);
            if (f.exists() && !h.containsFile(name)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
                return;
            }
        }
        for (String name : files) {
            File f = new File(name);
            String packageID = bH.getLocation(name);
            Package b = getPackage(packageID);
            Utils.writeContents(f, b.getContents());
        }
        for (String name : h.getFiles()) {
            if (!bH.containsFile(name)) {
                File f = new File(name);
                f.delete();
            }
        }
        for (String name : Utils.plainFilenamesIn(STAGE)) {
            File f = new File(STAGE, name);
            f.delete();
        }
        head = brHead;
        currBranch = branchName;
    }

    /**
     *
     * @param branch branch
     */
    public void branch(String branch) {
        File f = new File(BRANCHES, branch);
        if (f.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
            return;
        }
        Utils.writeContents(f, head);
    }

    /**
     *
     * @param branch branch
     */
    public void rmBranch(String branch) {
        File f = new File(BRANCHES, branch);
        if (!f.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
            return;
        }
        if (currBranch.equals(branch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
            return;
        }
        f.delete();
    }

    /**
     *
     * @param id id
     */
    public void reset(String id) {
        int len = id.length();
        for (String commitID : Utils.plainFilenamesIn(COMMITS)) {
            if (commitID.substring(0, len).equals(id)) {
                Commit c = getCommit(commitID);
                Commit h = getHead();
                for (String name : c.getFiles()) {
                    File f = new File(name);
                    if (!h.containsFile(name) && f.exists()) {
                        System.out.println("There is an untracked "
                                + "file in the way;"
                                + " delete it, or add and commit it first.");
                        System.exit(0);
                        return;
                    }
                }
                for (String name : c.getFiles()) {
                    File f = new File(name);
                    Package b = getPackage(c.getLocation(name));
                    Utils.writeContents(f, b.getContents());
                }
                for (String name : h.getFiles()) {
                    if (!c.containsFile(name)) {
                        File f = new File(name);
                        f.delete();
                    }
                }
                File br = new File(BRANCHES, currBranch);
                Utils.writeContents(br, commitID);
                head = commitID;
                for (String name : Utils.plainFilenamesIn(STAGE)) {
                    File f = new File(STAGE, name);
                    f.delete();
                }
                return;
            }
        }
        System.out.println("No commit with that id exists.");
        System.exit(0);
    }

    /**
     *
     * @param branch branch
     */
    public void merge(String branch) {
        Commit h = getHead();
        File br = new File(BRANCHES, branch);
        if (!br.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
            return;
        }
        List<String> addStage = Utils.plainFilenamesIn(STAGE);
        if ((addStage.size() > 0) || !rmStage.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
            return;
        }
        String brHead = Utils.readContentsAsString(br);
        Commit given = getCommit(brHead);
        for (String name : given.getFiles()) {
            File f = new File(name);
            if (f.exists() && !h.containsFile(name)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
                return;
            }
        }
        if (branch.equals(currBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
            return;
        }
        String split = findCommonAncestor(branch);
        if (split.equals(brHead)) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            System.exit(0);
            return;
        }
        if (split.equals(head)) {
            branchCheckOut(branch);
            System.out.println("Current branch"
                    + " fast-forwarded.");
            System.exit(0);
            return;
        }
        mergeHelp(branch);
    }

    /**
     *
     * @param branch branch
     */
    public void mergeHelp(String branch) {
        Commit h = getHead();
        File br = new File(BRANCHES, branch);
        String brHead = Utils.readContentsAsString(br);
        Commit given = getCommit(brHead); boolean con = false;
        Commit s = getCommit(findCommonAncestor(branch));
        for (String name : s.getFiles()) {
            String sB = s.getLocation(name);
            String currB = h.getLocation(name);
            String gB = given.getLocation(name);
            if (given.containsFile(name)) {
                if (!gB.equals(sB)) {
                    if (sB.equals(currB)) {
                        File to = new File(name);
                        Package b = getPackage(gB);
                        Utils.writeContents(to, b.getContents());
                        add(name);
                    } else if (!gB.equals(currB)) {
                        conflict(currB, gB, name);
                        con = true;
                        add(name);
                    } else if (!h.containsFile(name)) {
                        conflict(currB, gB, name);
                        con = true; add(name);
                    }
                }
            } else {
                if (sB.equals(currB)) {
                    File f0 = new File(name);
                    f0.delete();
                    rm(name);
                } else if (h.containsFile(name)) {
                    conflict(currB, gB, name);
                    con = true;
                    add(name);
                }
            }
        }
        for (String name : given.getFiles()) {
            if ((!s.containsFile(name)) && (!h.containsFile(name))) {
                File to = new File(name);
                Package b = getPackage(given.getLocation(name));
                Utils.writeContents(to, b.getContents());
                add(name);
            } else if ((!s.containsFile(name)) && h.containsFile(name)) {
                String gB = given.getLocation(name);
                String currB = h.getLocation(name);
                if (!gB.equals(currB)) {
                    conflict(currB, gB, name);
                    con = true;
                    add(name);
                }
            }
        }
        String message = "Merged " + branch + " into " + currBranch + ".";
        mergeCommit(message, brHead);
        if (con) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /**
     *
     * @param message message
     * @param mergeParent mergeParent
     */
    public void mergeCommit(String message, String mergeParent) {
        List<String> addStage = Utils.plainFilenamesIn(STAGE);
        Commit h = getHead();
        HashMap<String, String> fileToLoc = h.getFileLocations();
        for (String fileName : addStage) {
            File f0 = new File(STAGE, fileName);
            Package b = new Package(fileName, Utils.readContents(f0));
            File f1 = new File(PACKAGES, b.getID());
            if (!f1.exists()) {
                Utils.writeObject(f1, b);
            }
            if (fileToLoc.containsKey(fileName)) {
                fileToLoc.replace(fileName, b.getID());
            } else {
                fileToLoc.put(fileName, b.getID());
            }
            f0.delete();
        }
        for (String fileName : rmStage) {
            fileToLoc.remove(fileName);
        }
        rmStage.clear();
        Commit c = new Commit(message, head, mergeParent, fileToLoc);
        File f = new File(COMMITS, c.getID());
        Utils.writeObject(f, c);
        head = c.getID();
        File b = new File(BRANCHES, currBranch);
        Utils.writeContents(b, head);
    }

    /**
     *
     * @param curr curr
     * @param given given
     * @param name name
     */
    public void conflict(String curr, String given, String name) {
        File f0 = new File(name);
        String newCont = "<<<<<<< HEAD\n";
        if (curr != null) {
            Package currB = getPackage(curr);
            Utils.writeContents(f0, currB.getContents());
            newCont += Utils.readContentsAsString(f0);
        }
        newCont += "=======\n";
        if (given != null) {
            Package gB = getPackage(given);
            Utils.writeContents(f0, gB.getContents());
            newCont += Utils.readContentsAsString(f0);
        }
        newCont += ">>>>>>>\n";
        Utils.writeContents(f0, newCont);
    }

    /**
     *
     * @param branch branch
     * @return common ancestor
     */
    public String findCommonAncestor(String branch) {
        String splitPoint = null;
        File b = new File(BRANCHES, branch);
        String brHead = Utils.readContentsAsString(b);
        HashMap<Integer, ArrayList<String>> distances = stepsTo();
        for (int dist : distances.keySet()) {
            boolean found = false;
            ArrayList<String> commits = distances.get(dist);
            for (String toFind : commits) {
                if (found(toFind, brHead)) {
                    splitPoint = toFind;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        return splitPoint;
    }

    /**
     *
     * @param toFind to find
     * @param c commit
     * @return found
     */
    public boolean found(String toFind, String c) {
        if (toFind.equals(c)) {
            return true;
        }
        Commit c0 = getCommit(c);
        boolean p1 = false;
        boolean p2 = false;
        if (c0.getParent() != null) {
            p1 = found(toFind, c0.getParent());
        }
        if (!p1 && (c0.getMergeParent() != null)) {
            p2 = found(toFind, c0.getMergeParent());
        }
        return p1 || p2;
    }

    /**
     *
     * @return hash map of distances
     */
    public HashMap<Integer, ArrayList<String>> stepsTo() {
        HashMap<Integer, ArrayList<String>> steps = new HashMap<>();
        stepsHelper(0, head, steps);
        return steps;
    }

    /**
     *
     * @param dist distance
     * @param cID commit id
     * @param h hashmpa
     */
    public void stepsHelper(int dist, String cID,
                            HashMap<Integer, ArrayList<String>> h) {
        Commit c = getCommit(cID);
        if (!h.containsKey(dist)) {
            h.put(dist, new ArrayList<String>());
        }
        h.get(dist).add(cID);
        if (c.getParent() != null) {
            stepsHelper(dist + 1, c.getParent(), h);
        }
        if (c.getMergeParent() != null) {
            stepsHelper(dist + 1, c.getMergeParent(), h);
        }
    }

    /**
     *
     * @param id id
     * @return commit
     */
    public Commit getCommit(String id) {
        File f = new File(COMMITS, id);
        return Utils.readObject(f, Commit.class);
    }

    /**
     *
     * @return head commit
     */
    public Commit getHead() {
        return getCommit(head);
    }

    /**
     *
     * @param id id
     * @return package
     */
    public Package getPackage(String id) {
        File f = new File(PACKAGES, id);
        return Utils.readObject(f, Package.class);
    }
}
