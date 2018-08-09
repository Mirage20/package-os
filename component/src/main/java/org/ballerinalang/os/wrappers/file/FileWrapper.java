package org.ballerinalang.os.wrappers.file;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.WorkerExecutionContext;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.util.exceptions.BallerinaException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;

/**
 * Wraps the Java File API for Ballerina operations.
 */
public class FileWrapper {
    private static final String WORKING_DIR = "WORKING_DIR";


    private static File getWorkingDirFromContext(Context context) {
        WorkerExecutionContext workerContext = context.getParentWorkerExecutionContext();
        if (workerContext.localProps == null) {
            workerContext.localProps = new HashMap<>();
        }
        return (File) workerContext.localProps.computeIfAbsent(WORKING_DIR,
                k -> new File(System.getProperty("user.dir"))
        );
    }

    private static void setWorkingDir(Context context, File newWorkingDir) {
        WorkerExecutionContext workerContext = context.getParentWorkerExecutionContext();
        if (workerContext.localProps == null) {
            workerContext.localProps = new HashMap<>();
        }
        workerContext.localProps.put(WORKING_DIR, newWorkingDir);
    }

    public static BString getWorkingDir(Context context) {
        try {
            return new BString(getWorkingDirFromContext(context).getCanonicalPath());
        } catch (IOException e) {
            return new BString(getWorkingDirFromContext(context).getAbsolutePath());
        }
    }

    public static void setWorkingDir(Context context, String dir) {
        File newWorkingDir = new File(dir);
        if (!newWorkingDir.isAbsolute()) {
            try {
                newWorkingDir = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), dir).toFile()
                        .getCanonicalFile();
            } catch (IOException e) {
                newWorkingDir = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), dir).toFile();
            }
        }
        if (!newWorkingDir.isDirectory()) {
            throw new BallerinaException("\'" + newWorkingDir.getAbsolutePath() + "\' is not a directory.");
        }
        setWorkingDir(context, newWorkingDir);
    }


    public static BStringArray listFiles(Context context) {
        String[] fileList = getWorkingDirFromContext(context).list();
        return new BStringArray(fileList == null ? new String[0] : fileList);
    }

    public static void remove(Context context, String file, boolean recursive) throws IOException {
        File removeFile = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), file).toFile();
        if (getWorkingDirFromContext(context).getCanonicalPath().equals(removeFile.getCanonicalPath())) {
            throw new BallerinaException("cannot delete the current working directory '"
                    + getWorkingDirFromContext(context).getCanonicalPath() + "'");
        }
        if (recursive) {
            Path directory = Paths.get(removeFile.getCanonicalPath());
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            if (!removeFile.delete()) {
                throw new BallerinaException("error while deleting '" + removeFile.getCanonicalPath() + "'");
            }
        }
    }

    public static void move(Context context, String src, String dest) throws IOException {
        Path srcPath = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), src);
        Path destPath = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), dest);
        Files.move(srcPath, destPath);
    }

    public static void copy(Context context, String src, String dest) throws IOException {
        Path srcPath = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), src);
        Path destPath = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), dest);
        Files.copy(srcPath, destPath);
    }

    public static void createDir(Context context, String dir, boolean createParentDirs) throws IOException {
        File dirFile = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), dir).toFile();
        boolean created = createParentDirs ? dirFile.mkdirs() : dirFile.mkdir();
        if (!created) {
            throw new BallerinaException("error while creating directory '" + dirFile.getCanonicalPath() + "'");
        }
    }

    public static void changePermission(Context context, String file, String permissions) throws IOException {
        Path filePath = Paths.get(getWorkingDirFromContext(context).getAbsolutePath(), file);
        Files.setPosixFilePermissions(filePath, PosixFilePermissions.fromString(permissions));
    }
}
