package bullet;

import bullet.linearmath.btTransform;
import bullet.linearmath.btVector3;
import com.github.xpenatan.jparser.loader.JParserLibraryLoader;

/**
 * @author xpenatan
 */
/*[-IDL_SKIP]*/
public class Bullet {

    /* [-C++;-NATIVE]
         return CustomCode::GetBTVersion();
    */
    /* [-teaVM;-NATIVE]
        return bullet.CustomCode.prototype.GetBTVersion();
    */
    public static native int btGetVersion();

    /**
     * Only call this dispose when app is closing
     */
    public static void dispose() {
        btVector3.TMP_1.dispose();
        btVector3.TMP_2.dispose();
        btVector3.TMP_3.dispose();
        btTransform.TMP_1.dispose();
        btTransform.TMP_2.dispose();
        btTransform.TMP_3.dispose();
    }
}