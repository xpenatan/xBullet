package bullet;

import com.github.xpenatan.jParser.loader.JParserLibraryLoader;
import com.github.xpenatan.jParser.loader.JParserLibraryLoaderListener;

/**
 * @author xpenatan
 */
public class BulletLoader {

    /*[-JNI;-NATIVE]
       #include "BulletCustom.h"
    */

    public static void init(JParserLibraryLoaderListener listener) {
        JParserLibraryLoader.load("bullet", listener);
    }

    /**
     * Not compatible with the web
     */
    public static void initSync(JParserLibraryLoaderListener listener) {
        JParserLibraryLoader.loadSync("bullet", listener);
    }
}