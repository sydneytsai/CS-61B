# Gitlet Design Document

**Name**: Leo Huang

## Classes and Data Structures

### Gitlet
This class represents gitlet version-control system.

**Fields** 
1. ArrayList<String> addStage
2. ArrayList<String> rmStage
3. ArrayList<String> commitIds

### Commit
This class represents a commit to gitlet system.

### Package
This class represents a package contained in a Commit.

**Fields**
1. Content: contents of the package.
2. int hashValue: fixed value that corresponds to the package.

## Algorithms

### Gitlet Class
1. Gitlet(): The class constructor; automatically starts with one default commit that contains no files and has the commit message "initial commit". Since initial commit in all repositories created by gitlet will have the exact same content, all repositories will automatically share this commit and all commits in all repositories will trace back to it.
2. stageRemove(): stage a file for removal; if file tracked in current commit, stage for removal and remove the file from the working directory if user has not already done so.
3. globalLog(): similar to log() but for every commit ever made.
4. find(String message): prints id of all commands that have commitMessage = message.
5. status(): displays branches, staged files, removed files, modifications not staged for commit, untracked files.
6. reset(): checks out all files tracked by given commit, removes tracked files that are not present in that commit, moves current branch’s head to that commit node (remember intro for what happens to head pointer), [commit id] may be abbreviated for checkout, staging area cleared, essentially checkout of an arbitrary commit that also changes the current branch head.
7. merge(String name): merges files from given branch into current branch.

### Commit Class
1. Commit(...): The class constructor; takes in a Package as a parameter. Saves a snapshot of files in this Commit, "tracks" saved files. By default, each commits snapshot of files will be exactly the same as its parents commits snapshot of files, will keep versions of files exactly as they are, not update them.
2. hashValue(): returns a unique hash value for this commit (SHA-1, which produces a 160-bit integer hash from any sequence of bytes).
2. log(): display info about the commit (commit id, date, commit message), displays backwards along the commit tree.

### Package Class
1. Blob(...): The class constructor; takes in a file as a parameter.
2. hashValue(): returns a unique hash value for this commit (SHA-1, which produces a 160-bit integer hash from any sequence of bytes).

## Persistence

When a Package or Commit is created, it will be assigned a unique hashValue which cannot be changed, therefore, the hashValue will only map back to that specific object.
Package/Commits that contain the same contents will have the same hashValues. 