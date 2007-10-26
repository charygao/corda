package java.lang;

import java.io.PrintStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileDescriptor;

public abstract class System {
  private static Property properties;
  
//   static {
//     loadLibrary("natives");
//   }

  public static final PrintStream out = new PrintStream
    (new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)), true);

  public static final PrintStream err = new PrintStream
    (new BufferedOutputStream(new FileOutputStream(FileDescriptor.err)), true);

  public static final InputStream in
    = new BufferedInputStream(new FileInputStream(FileDescriptor.in));

  public static native void arraycopy(Object src, int srcOffset, Object dst,
                                      int dstOffset, int length);

  public static String getProperty(String name) {
    for (Property p = properties; p != null; p = p.next) {
      if (p.name.equals(name)) {
        return p.value;
      }
    }

    boolean[] found = new boolean[1];
    String value = getProperty(name, found);
    if (found[0]) return value;

    value = getVMProperty(name, found);
    if (found[0]) return value;

    return null;
  }

  public static String setProperty(String name, String value) {
    for (Property p = properties; p != null; p = p.next) {
      if (p.name.equals(name)) {
        String oldValue = p.value;
        p.value = value;
        return oldValue;
      }
    }

    properties = new Property(name, value, properties);
    return null;
  }

  private static native String getProperty(String name, boolean[] found);

  private static native String getVMProperty(String name, boolean[] found);

  public static native long currentTimeMillis();

  public static native int identityHashCode(Object o);

  public static String mapLibraryName(String name) {
    if (name != null) {
      return doMapLibraryName(name);
    } else {
      throw new NullPointerException();
    }
  }

  private static native String doMapLibraryName(String name);

  public static void load(String path) {
    Runtime.getRuntime().load(path);
  }

  public static void loadLibrary(String name) {
    Runtime.getRuntime().loadLibrary(name);
  }

  public static void gc() {
    Runtime.getRuntime().gc();
  }

  public static void exit(int code) {
    Runtime.getRuntime().exit(code);
  }

  private static class Property {
    public final String name;
    public String value;
    public final Property next;

    public Property(String name, String value, Property next) {
      this.name = name;
      this.value = value;
      this.next = next;
    }
  }
}
