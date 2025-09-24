package bullet;


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
    }
}