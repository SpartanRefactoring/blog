package il.org.spartan.classfiles;

import static il.org.spartan.utils.___.*;

import java.io.*;

import il.org.spartan.files.visitors.*;
import il.org.spartan.files.visitors.FileSystemVisitor.Action.*;
import org.jetbrains.annotations.NotNull;

public class AllClasses {
  public static void main(final String[] args) throws IOException, StopTraversal {
    for (final String root : CLASSPATH.asArray())
      new ClassFilesVisitor(root, new FileSystemVisitor.FileOnlyAction() {
        @Override public void visitFile(@NotNull final File ¢) {
          System.out.println(Filename.path2class(¢.getAbsolutePath(), root));
        }

        @Override public void visitZipEntry(@NotNull final String entryName, final InputStream __) {
          unused(__);
          System.out.println(Filename.path2class(entryName, root));
        }
      }).go();
  }
}
