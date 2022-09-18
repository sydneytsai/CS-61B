package gitlet;
import java.io.File;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Leo Huang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    static final File GITLET = new File("gitletObj");

    /**
     *
     * @param args args
     */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
            return;
        }
        if (!args[0].equals("init")) {
            if (!GITLET.exists()) {
                System.out.println("Not in an initialized Gitlet directory.");
                System.exit(0);
                return;
            }
        }
        if (args[0].equals("init")) {
            init();
        } else if (args[0].equals("add")) {
            add(args[1]);
        } else if (args[0].equals("commit")) {
            commit(args[1]);
        } else if (args[0].equals("rm")) {
            rm(args[1]);
        } else if (args[0].equals("log")) {
            log();
        } else if (args[0].equals("global-log")) {
            globalLog();
        } else if (args[0].equals("find")) {
            find(args[1]);
        } else if (args[0].equals("status")) {
            status();
        } else if (args[0].equals("checkout")) {
            Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
            if (args.length == 2) {
                repo.branchCheckOut(args[1]);
            } else if (args[1].equals("--")) {
                repo.checkOut(args[2]);
            } else if (args[2].equals("--")) {
                repo.checkOut(args[1], args[3]);
            } else {
                checkOutErr();
                return;
            }
            Utils.writeObject(GITLET, repo);
        } else if (args[0].equals("branch")) {
            branch(args[1]);
        } else if (args[0].equals("rm-branch")) {
            rmBranch(args[1]);
        } else if (args[0].equals("reset")) {
            reset(args[1]);
        } else if (args[0].equals("merge")) {
            merge(args[1]);
        } else {
            defaultComm();
        }
    }

    /**
     *
     */
    public static void init() {
        Gitlet repo = new Gitlet();
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     * @param arg1 arg1
     */
    public static void add(String arg1) {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.add(arg1);
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     * @param arg1 arg1
     */
    public static void commit(String arg1) {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.commit(arg1);
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     * @param arg1 arg1
     */
    public static void rm(String arg1) {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.rm(arg1);
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     */
    public static void log() {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.log();
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     */
    public static void globalLog() {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.globalLog();
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     * @param arg1 arg1
     */
    public static void find(String arg1) {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.find(arg1);
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     */
    public static void status() {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.status();
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     * @param arg1 arg1
     */
    public static void branch(String arg1) {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.branch(arg1);
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     * @param arg1 arg1
     */
    public static void rmBranch(String arg1) {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.rmBranch(arg1);
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     * @param arg1 arg1
     */
    public static void reset(String arg1) {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.reset(arg1);
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     * @param arg1 arg1
     */
    public static void merge(String arg1) {
        Gitlet repo = Utils.readObject(GITLET, Gitlet.class);
        repo.merge(arg1);
        Utils.writeObject(GITLET, repo);
    }

    /**
     *
     */
    public static void defaultComm() {
        System.out.println("No command with that name exists.");
        System.exit(0);
    }

    /**
     *
     */
    public static void checkOutErr() {
        System.out.println("Incorrect operands.");
        System.exit(0);
    }

}
