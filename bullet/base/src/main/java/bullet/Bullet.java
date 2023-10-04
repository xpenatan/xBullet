package bullet;

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
}
